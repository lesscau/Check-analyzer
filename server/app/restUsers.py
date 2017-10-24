from flask_restful import Resource, reqparse, fields, marshal, abort
from app import api, db
from app.models import User
from app.views import APIv1

users_fields = {
    'id': fields.Integer,
    'username': fields.String,
    'url': fields.Url('user')
}

user_fields = {
    'id': fields.Integer,
    'username': fields.String,
    'phone': fields.String,
    'url': fields.Url('user')
}

class UserList(Resource):
    def __init__(self):
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

    def get(self):
        users = User.query.all()
        return { 'users': marshal(users, users_fields) }

    def post(self):
        args = self.reqparse.parse_args()
        self.abortIfUserAlreadyExist(args['username'], args['phone'])
        user = User(
            username = args['username'],
            password = args['password'],
            phone = args['phone'],
            fts_key = args['fts_key'])
        db.session.add(user)
        db.session.commit()
        return { 'user': marshal(user, user_fields) }, 201

    def abortIfUserAlreadyExist(self, username, phone):
        users = db.session.query(User).filter(
            (User.username == username) | (User.phone == phone)).all()
        if len(users) != 0:
            abort(409, message="Username '{}' and/or phone {} already exist".format(username, phone))

class Users(Resource):
    def __init__(self):
        self.reqparse = reqparse.RequestParser()
        self.reqparse.add_argument('username', type = str, location = 'json')
        self.reqparse.add_argument('password', type = str, location = 'json')
        self.reqparse.add_argument('phone', type = str, location = 'json')
        self.reqparse.add_argument('fts_key', type = int, location = 'json')
        super(Users, self).__init__()

    def get(self, id):
        user = User.query.get(id)
        self.abortIfUserDoesntExist(user, id)
        return { 'user': marshal(user, user_fields) }

    def put(self, id):
        args = self.reqparse.parse_args()
        user = User.query.get(id)
        self.abortIfUserDoesntExist(user, id)
        self.abortIfUserAlreadyExist(args['username'], args['phone'])
        for key, value in args.items():
            if value is not None:
                setattr(user, key, value)
        db.session.commit()
        return { 'user': marshal(user, user_fields) }

    def abortIfUserDoesntExist(self, user, id):
        if user is None:
            abort(404, message="User id {} doesn't exist".format(id))

    def abortIfUserAlreadyExist(self, username="", phone=""):
        users = db.session.query(User).filter(
            (User.username == username) | (User.phone == phone)).all()
        print(users)
        if len(users) != 0:
            abort(409, message="Username '{}' and/or phone {} already exist".format(username, phone))

class UserInfo(Resource):
    def get(self):
        pass

    def put(self):
        pass

api.add_resource(UserList, APIv1 + '/users', endpoint = 'users')
api.add_resource(Users, APIv1 + '/users/<int:id>', endpoint = 'user')
api.add_resource(UserInfo, APIv1 + '/users/me', endpoint = 'userme')