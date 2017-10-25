from flask import Flask
from flask_restful import Api
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from config import basedir
import os

# Application instance
app = Flask(__name__)
# Application configuration
app.config.from_object('config')
# Application database
db = SQLAlchemy(app)
# Application database migrations
migrate = Migrate(app, db)
# Application REST API
api = Api(app)

from app import rest, views, models, FtsRequest