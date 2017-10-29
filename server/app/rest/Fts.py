from flask_restful import Resource, reqparse, abort
from app import api
from app.views import APIv1
from app.FtsRequest import FtsRequest

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

# Add classes to REST API
api.add_resource(FtsSignUp, APIv1 + '/fts/users', endpoint = 'fts_users')