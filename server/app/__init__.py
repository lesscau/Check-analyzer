from flask import Flask
from flask_restful import Api
from flask_httpauth import HTTPBasicAuth, HTTPTokenAuth, MultiAuth
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from config import basedir
import os

app = Flask(__name__)
app.config.from_object('config')
db = SQLAlchemy(app)

api = Api(app)
basic_auth = HTTPBasicAuth()
token_auth = HTTPTokenAuth('Bearer')
multi_auth = MultiAuth(basic_auth, token_auth)

migrate = Migrate(app, db)

from app import views, models, FtsRequest, restUsers