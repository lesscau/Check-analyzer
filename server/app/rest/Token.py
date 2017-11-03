from flask import g
from flask_restful import Resource
from app import api
from app.models import User
from app.views import APIv1
from app.rest.Auth import Auth

class Token(Resource):
    """
    Obtaining bearer token for authorized user

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.basic_auth.login_required]

    def get(self):
        """
        Obtaining bearer token for authorized user

        :return: bearer token for authorized user
        :rtype:  dict/json
        """
        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username = g.user.username).first()
        # Generate token
        token = user.generate_auth_token()
        # Send token in ASCII format
        return { 'token': token.decode('ascii') }

# Add class to REST API
api.add_resource(Token, APIv1 + '/token', endpoint = 'token')