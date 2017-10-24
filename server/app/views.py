# -*- coding: utf-8 -*-
from flask import render_template, request, jsonify
from flask_restful import Resource, reqparse, fields, marshal
import json

from app import app, api, db, models
from .forms import LoginForm
from .FtsRequest import FtsRequest

APIv1 = "/api/v1.0"

@app.route(APIv1 + '/login')
def login():
	form = LoginForm()
	return render_template('login.json', form = form)

# Password from json not used because it doesn't exist in current User model
@app.route(APIv1 + '/receipt')
def getReceipt():
	# Get JSON from GET request
	json = request.get_json(force = True)
	# Parse QR-string
	qrArgs = json['qr'].split("&")
	fn = qrArgs[2][3:]
	fd = qrArgs[3][2:]
	fp = qrArgs[4][3:]
	# Get phone/pass from fts account
	# user = User.query.filter_by(username = json['login'], password = json['password']).first_or_404()
	user = models.User.query.filter_by(username = json['login']).first_or_404()

	receipt = FtsRequest().getReceipt(fn, fd, fp, user.phone, user.fts_key)
	return jsonify(receipt)

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
		users = models.User.query.all()
		return { 'users': marshal(users, users_fields) }

	def post(self):
		args = self.reqparse.parse_args()
		user = models.User(
			username = args['username'],
			password = args['password'],
			phone = args['phone'],
			fts_key = args['fts_key'])
		db.session.add(user)
		db.session.commit()
		return { 'user': marshal(user, user_fields) }

class Users(Resource):
	def __init__(self):
		self.reqparse = reqparse.RequestParser()
		self.reqparse.add_argument('username', type = str, location = 'json')
		self.reqparse.add_argument('password', type = str, location = 'json')
		self.reqparse.add_argument('phone', type = str, location = 'json')
		self.reqparse.add_argument('fts_key', type = int, location = 'json')
		super(Users, self).__init__()

	def get(self, id):
		user = models.User.query.get(id)
		return { 'user': marshal(user, user_fields) }

	def put(self, id):
		args = self.reqparse.parse_args()
		user = models.User.query.get(id)
		for key, value in args.items():
			if value is not None:
				setattr(user, key, value)
		print(user.fts_key)
		db.session.commit()
		return { 'user': marshal(user, user_fields) }

class UserInfo(Resource):
	def get(self):
		pass

	def put(self):
		pass

api.add_resource(UserList, APIv1 + '/users', endpoint = 'users')
api.add_resource(Users, APIv1 + '/users/<int:id>', endpoint = 'user')
api.add_resource(UserInfo, APIv1 + '/users/me', endpoint = 'userme')