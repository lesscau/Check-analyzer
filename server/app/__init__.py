from flask import Flask
from sqlalchemy import MetaData
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from config import basedir
import os

# Constraints naming convention when the name is not specified explicitly
naming_convention = {
    "ix": 'ix_%(column_0_label)s',
    "uq": "uq_%(table_name)s_%(column_0_name)s",
    "ck": "ck_%(table_name)s_%(column_0_name)s",
    "fk": "fk_%(table_name)s_%(column_0_name)s_%(referred_table_name)s",
    "pk": "pk_%(table_name)s"
}

# Application instance
app = Flask(__name__)
# Application configuration
app.config.from_object('config')
# Application database
db = SQLAlchemy(app, metadata=MetaData(naming_convention=naming_convention))
# Application database migrations
migrate = Migrate(app, db, render_as_batch=True, compare_type=True)


# Close the database session after each request or application context shutdown
@app.teardown_appcontext
def shutdown_session(exception=None):
    db.session.remove()


# Import views (must be after the application object is created)
from app import rest, models, FtsRequest, RandomPhrases

# Register blueprint Receipt-Analyzer v1.0
app.register_blueprint(rest.RAv1)
