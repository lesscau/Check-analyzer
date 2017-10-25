from flask import g
from flask_restful import abort
from flask_httpauth import HTTPBasicAuth, HTTPTokenAuth, MultiAuth
from app.models import User

class Auth():
    basic_auth = HTTPBasicAuth()
    token_auth = HTTPTokenAuth('Bearer')
    multi_auth = MultiAuth(basic_auth, token_auth)

    @staticmethod
    @basic_auth.verify_password
    def verify_password(username, password):
        user = User.query.filter_by(username = username).first()
        if not user or not user.verify_password(password):
            return False
        g.user = user
        return True

    @staticmethod
    @token_auth.error_handler
    @basic_auth.error_handler
    def unauthorized():
        abort(401, message="Unauthorized access")

    @staticmethod
    @token_auth.verify_token
    def verify_token(token):
        user = User.verify_auth_token(token)
        if not user:
            return False
        g.user = user
        return True