import os
basedir = os.path.abspath(os.path.dirname(__file__))

SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.path.join(basedir, 'app.db')
SQLALCHEMY_MIGRATE_REPO = os.path.join(basedir, 'db_repository')
SQLALCHEMY_TRACK_MODIFICATIONS = False

JSON_AS_ASCII = False
ERROR_404_HELP = False

CSRF_ENABLED = True
SECRET_KEY = 'FU8vYvAvQB3q8XVrtPFcjcSbv'