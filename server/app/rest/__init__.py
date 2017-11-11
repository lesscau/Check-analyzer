from flask import Blueprint
from flask_restplus import Api

# Receipt-Analyzer v1.0 blueprint instance
RAv1 = Blueprint('RAv1', __name__, url_prefix='/api/v1.0')
# Blueprint REST API
api = Api(RAv1,
    title='Receipt Analyzer',
    version='1.0',
    description='Receipt Analyzer Android app REST service')

# Import views (must be after the blueprint object is created)
from app.rest import Auth, Token, Users, Fts

# Define namespaces in REST API (groups in documentation)
api.add_namespace(Users.api)
api.add_namespace(Fts.api)
api.add_namespace(Token.api)