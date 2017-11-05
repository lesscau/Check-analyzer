from flask import g, request
from flask_restful import Resource, reqparse, abort
from app import api
from app.views import APIv1
from app.models import User
from app.FtsRequest import FtsRequest
from app.rest.Auth import Auth

class FtsSignUp(Resource):
    """
    Register new user in Federal Tax Service

    :ivar    reqparse: Request parsing interface to provide simple and uniform access to any variable on the flask.request object in Flask
    :vartype reqparse: flask_restful.reqparse.RequestParser
    """
    def __init__(self):
        # Define request JSON fields
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('name', type = str, required = True,
            help = 'No name provided', location = 'json')
        self.reqparse.add_argument('email', type = str, required = True,
            help = 'No email provided', location = 'json')
        self.reqparse.add_argument('phone', type = str, required = True,
            help = 'No phone provided', location = 'json')
        super(FtsSignUp, self).__init__()

    def get(self):
        """
        Check if user exists in Federal Tax Service
        Phone and SMS key send as Basic Auth header

        :return: JSON with "check" field True if user exists, False otherwise
        :rtype:  dict/json
        """
        # Check if authorization is Basic Auth
        if request.authorization is None:
            abort(400, message="The resource requires the Basic authentication")
        # Get phone and SMS key from Basic Auth header
        phone = request.authorization.username
        fts_key = request.authorization.password
        # Send check auth request
        fts = FtsRequest()
        auth = fts.checkAuthData(phone, fts_key)
        # Return JSON
        result = { 'check': auth }
        return (result, 200) if auth else (result, 404)

    def post(self):
        """
        Create new user in Federal Tax Service and send password SMS

        :return: Response message
        :rtype:  dict/json
        """
        # Parsing request JSON fields
        args = self.reqparse.parse_args()
        # Send signup request
        fts = FtsRequest()
        request = fts.signUp(args['name'], args['email'], args['phone'])
        # Restore password if user exists
        if request['ftsRequestSuccess'] is False and request['error'] == "user exists":
            fts.restorePassword(args['phone'])
        # Send error JSON if bad request
        if request['ftsRequestSuccess'] is False and request['error'] != "user exists":
            abort(request['responseCode'], message = request['error'])
        # Return JSON
        return { 'message': 'SMS with password was sent to {}'.format(args['phone'])}, 200

class FtsReceiptRequest(Resource):
    """
    Operations with FTS receipts

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    def get(self, fn, fd, fp):
        """
        Get receipt with given ФН, ФД and ФП numbers

        :param fn: ФН number from receipt
        :type  fn: int
        :param fd: ФД number from receipt
        :type  fd: int
        :param fp: ФП number from receipt
        :type  fp: int
        :return: Products from receipt with name, quantity and price of 1 piece of product
        :rtype:  dict/json
        """
        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username = g.user.username).first()
        # Send request of receipt JSON
        fts = FtsRequest()
        request = fts.getReceipt(fn, fd, fp, user.phone, user.fts_key)
        # Send error JSON if bad request
        if request['ftsRequestSuccess'] is False:
            abort(request['responseCode'], message = request['error'])
        # Extract products info from JSON
        result = {}
        result['items'] = [ { 'name': item['name'], 'quantity': item['quantity'], 'price': item['price'] }
            for item in request['items'] ]
        # Return extracted part of JSON
        return result, 200

# Add classes to REST API
api.add_resource(FtsSignUp, APIv1 + '/fts/users', endpoint = 'fts_users')
api.add_resource(FtsReceiptRequest, APIv1 + '/fts/receipts/fns/<int:fn>/fds/<int:fd>/fps/<int:fp>', endpoint = 'fts_receipts')