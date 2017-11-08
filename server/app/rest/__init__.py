from flask import Blueprint
from flask_restful import Api

# Receipt-Analyzer v1.0 blueprint instance
RAv1 = Blueprint('RAv1', __name__)
# Blueprint REST API
api = Api(RAv1)

# Import views (must be after the blueprint object is created)
from app.rest import Auth, Token, Users, Fts

# Add classes to REST API
api.add_resource(Token.Token, '/token', endpoint = 'token')
api.add_resource(Users.UserList, '/users', endpoint = 'users')
api.add_resource(Users.Users, '/users/<int:id>', endpoint = 'user')
api.add_resource(Users.UserInfo, '/users/me', endpoint = 'userme')
api.add_resource(Fts.FtsSignUp, '/fts/users', endpoint = 'fts_users')
api.add_resource(Fts.FtsReceiptRequest, '/fts/receipts', endpoint = 'fts_receipts')