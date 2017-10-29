from flask import g
from flask_restful import Resource, reqparse, fields, marshal, abort
from app import api, db
from app.models import User
from app.views import APIv1
from app.rest.Auth import Auth

# Response JSON template for /users requests
users_fields = {
    'id': fields.Integer,
    'username': fields.String,
    'url': fields.Url('user')
}

# Response JSON template for /users/{id} or /users/me requests
user_fields = {
    'id': fields.Integer,
    'username': fields.String,
    'phone': fields.String,
    'url': fields.Url('user')
}

class UserList(Resource):
    """
    Operations with list of users

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    :ivar    reqparse: Request parsing interface to provide simple and uniform access to any variable on the flask.request object in Flask
    :vartype reqparse: flask_restful.reqparse.RequestParser
    """
    # Applied only to get method
    method_decorators = {'get': [Auth.multi_auth.login_required]}

    def __init__(self):
        # Define request JSON fields
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', type = str, required = True,
            help = 'No username provided', location = 'json')
        self.reqparse.add_argument('password', type = str, required = True,
            help = 'No password provided', location = 'json')
        self.reqparse.add_argument('phone', type = str, required = True,
            help = 'No phone provided', location = 'json')
        self.reqparse.add_argument('fts_key', type = int, required = True,
            help = 'No ftskey provided', location = 'json')
        super(UserList, self).__init__()

    def post(self):
        """
        Create new user in database

        :return: New user with some data
        :rtype:  dict/json
        """
        # Parsing request JSON fields
        args = self.reqparse.parse_args()
        # Error checking
        self.abortIfUserAlreadyExist(args['username'], args['phone'])
        # Create user and add to database
        user = User(
            username = args['username'],
            phone = args['phone'],
            fts_key = args['fts_key'])
        user.hash_password(args['password'])
        db.session.add(user)
        db.session.commit()
        # Return JSON using template
        return { 'user': marshal(user, user_fields) }, 201

    @staticmethod
    def abortIfUserAlreadyExist(username, phone):
        """
        Return error JSON in 409 response if user or phone already exists in database

        :param username: User login
        :type  username: str
        :param phone: User phone number
        :type  phone: str
        """
        users = db.session.query(User).filter(
            (User.username == username) | (User.phone == phone)).all()
        if len(users) != 0:
            abort(409, message="Username '{}' and/or phone {} already exist".format(username, phone))

class Users(Resource):
    """
    Operations with user selected by id

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    :ivar    reqparse: Request parsing interface to provide simple and uniform access to any variable on the flask.request object in Flask
    :vartype reqparse: flask_restful.reqparse.RequestParser
    """
    method_decorators = [Auth.multi_auth.login_required]

    def __init__(self):
        # Define request JSON fields
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', type = str, location = 'json')
        self.reqparse.add_argument('password', type = str, location = 'json')
        self.reqparse.add_argument('phone', type = str, location = 'json')
        self.reqparse.add_argument('fts_key', type = int, location = 'json')
        super(Users, self).__init__()

    def get(self, id):
        """
        Get user partial info with provided id

        :return: User with some data
        :rtype:  dict/json
        """
        user = User.query.get(id)
        # Error checking
        self.abortIfUserDoesntExist(user, id)
        # Return JSON using template
        return { 'user': marshal(user, user_fields) }

    @staticmethod
    def abortIfUserDoesntExist(user, id):
        """
        Return error JSON in 404 response if user doesn't exists in database

        :param user: User from database
        :type  user: app.models.User
        :param id: User id
        :type  id: int
        """
        if user is None:
            abort(404, message="User id {} doesn't exist".format(id))

class UserInfo(Resource):
    """
    Operations with authorized user

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    :ivar    reqparse: Request parsing interface to provide simple and uniform access to any variable on the flask.request object in Flask
    :vartype reqparse: flask_restful.reqparse.RequestParser
    """
    method_decorators = [Auth.multi_auth.login_required]

    def __init__(self):
        # Define request JSON fields
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', type = str, location = 'json')
        self.reqparse.add_argument('password', type = str, location = 'json')
        self.reqparse.add_argument('phone', type = str, location = 'json')
        self.reqparse.add_argument('fts_key', type = int, location = 'json')
        super(UserInfo, self).__init__()

    def get(self):
        """
        Get authorized user partial info

        :return: User with some data
        :rtype:  dict/json
        """
        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username = g.user.username).first()
        # Return JSON using template
        return { 'user': marshal(user, user_fields) }

    def put(self):
        """
        Modify authorized user in database

        :return: Modified user with some data
        :rtype:  dict/json
        """
        # Parsing request JSON fields
        args = self.reqparse.parse_args()
        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username = g.user.username).first()
        # Error checking
        self.abortIfUserAlreadyExist(args['username'], args['phone'])
        # Modify user info according to JSON fields
        for key, value in args.items():
            if value is not None:
                if key == "password":
                    user.hash_password(value)
                    continue
                setattr(user, key, value)
        db.session.commit()
        # Return JSON using template
        return { 'user': marshal(user, user_fields) }

    @staticmethod
    def abortIfUserAlreadyExist(username="", phone=""):
        """
        Return error JSON in 409 response if user or phone already exists in database

        :param username: User login
        :type  username: str
        :param phone: User phone number
        :type  phone: str
        """
        users = db.session.query(User).filter(
            (User.username == username) | (User.phone == phone)).all()
        if len(users) != 0:
            abort(409, message="Username '{}' and/or phone {} already exist".format(username, phone))

# Add classes to REST API
api.add_resource(UserList, APIv1 + '/users', endpoint = 'users')
api.add_resource(Users, APIv1 + '/users/<int:id>', endpoint = 'user')
api.add_resource(UserInfo, APIv1 + '/users/me', endpoint = 'userme')