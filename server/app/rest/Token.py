from flask import g
from flask_restful import Resource
from app import api
from app.models import User
from app.views import APIv1
from app.rest.Auth import Auth

class Token(Resource):
    method_decorators = [Auth.basic_auth.login_required]

    def get(self):
        user = User.query.filter_by(username = g.user.username).first()
        token = user.generate_auth_token()
        return { 'token': token.decode('ascii') }

api.add_resource(Token, APIv1 + '/token', endpoint = 'token')