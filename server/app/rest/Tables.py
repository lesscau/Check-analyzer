from flask import g
from flask_restplus import Namespace, Resource, fields, abort
from sqlalchemy.exc import IntegrityError
from app import db
from app.models import User, Table, UserTable
from app.rest.Auth import Auth
from app.RandomPhrases.main import randomPhrase
from datetime import datetime

# Define namespace
api = Namespace('Tables', description='Operations with tables', path='/tables')


# JSON Parsers #

# Find table request JSON fields
table_request = api.parser()
table_request.add_argument(
                    'table_key', type=str, required=True,
                    help='No key provided', location='json')


# JSON Models #

# Creating new table response JSON fields
table_response_fields = api.model(
    'Table response ',
    {
        'table_key': fields.String(description='Keyword'),
    })

# Find table request JSON fields (all fields required)
find_table_request_fields = api.model(
    'UserTable request',
    {
        'table_key': fields.String(description='Keyword', required=True),
    })

# TablesUsers response JSON template
user_table_response_fields = api.model(
    'UserTable request',
    {
        'table_id': fields.Integer(description='Table_id', required=True),
        'user_id': fields.Integer(description='User_id')
    })

# TablesUsers list response item
tables_users_item_fields = api.model(
    'Item TablesUsers response',
    {
        'id': fields.Integer(description='User id', required=True),
        'name': fields.String(description='User name', required=True)
    })

# TablesUsers list response JSON template
user_table_list_response_fields = api.model(
    'UserTable list request',
    {
        'items': fields.List(fields.Nested(tables_users_item_fields)),
    })


@api.route('', endpoint='tables')
class Tables(Resource):
    """
    Operations with list of tables

    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.doc(security=None)
    @api.marshal_with(table_response_fields)
    @api.doc(responses={
        409: 'Table already exists',
    })
    def post(self):
        """
        Create new table

        :return: New table
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        # Create table and add to database
        key = self.newPhraseIfAlreadyExists()
        table = Table(
                table_key=key,
                table_date=datetime.utcnow())

        try:
            db.session.add(table)
            db.session.flush()
            # Return JSON using template
        except IntegrityError:
            db.session.rollback()
            abort(409, message="Table '{}' already exist".format(key))

        # Create user-table dependency
        table = Table.query.filter_by(table_key=key).first()
        user_table = UserTable(
               user_id=user.id,
               table_id=table.id)

        try:
            db.session.add(user_table)
            db.session.commit()
            # Return JSON using template
            return table, 201
        except IntegrityError:
            db.session.rollback()
            abort(409, message="Username '{}' already exist".format(user.username))

    @staticmethod
    def newPhraseIfAlreadyExists():
        """
        Set the first keyword generated does not exist in database

        """
        keyword = randomPhrase()
        if Table.query.filter_by(table_key=keyword).first() is not None:
            return self.newPhraseIfAlreadyExists()
        else:
            return keyword


@api.route('/users', endpoint='tables/users')
class TablesUsers(Resource):
    """
    Operations with  tables - users dependencies

    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.doc(security=None)
    @api.expect(find_table_request_fields)
    @api.marshal_with(user_table_response_fields)
    @api.doc(responses={
        400: 'Table keyword does not exist',
        409: 'User already relates to a table',
    })
    def post(self):
        """
        Add new user at the table

        :return: table-user dependency
        :rtype:  dict/json
        """

        # Parsing request JSON fields
        args = user_request.parse_args()

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        table = Table.query.filter_by(table_key=args['table_key']).first()

        # Create user-table dependency
        user_table = UserTable(
             user_id=user.id,
             table_id=table.id)

        try:
            db.session.add(user_table)
            db.session.commit()
            # Return JSON using template
            return user_table, 201
        except IntegrityError:
            db.session.rollback()
            abort(400, message="Table keyword '{}' does not exist".format(table.table_key))

    @api.doc(security=None)
    @api.marshal_with(user_table_response_fields)
    @api.doc(responses={
        400: 'User does not exist at any tables'
    })
    def delete(self):
        """
        Delete user from the table

        :return: table-user dependency
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        user_table = UserTable.query.filter_by(user_id=user.id).first()

        try:
            db.session.delete(user_table)
            db.session.flush()
            # Return JSON using template
        except IntegrityError:
            db.session.rollback()
            abort(400, message="User '{}' does not exist".format(user.id))

        user_table_list = UserTable.query.filter_by(table_id=user_table.table_id).first()
        if user_table_list is None:
            table = Table.query.filter_by(id=user_table.table_id).first()
            try:
                db.session.delete(table)
            except IntegrityError:
                db.session.rollback()
                abort(400, message="Table '{}' does not exist".format(table.id))
        db.session.commit()
        return user_table, 201

    @api.doc(security=None)
    @api.marshal_with(user_table_response_fields)
    @api.doc(responses={
        400: 'User does not exist at any tables'
    })
    def get(self):
        """
        Get list of users related to the same table as auth user

        :return: list of users' ids and names
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        table = UserTable.query.filter_by(user_id=user.id).first()

        result = {}
        result['items'] = [{
            'id': item.user_id,
            'name': item.user.name
            } for item in UserTable.query.filter_by(table_id=table.id)]
        return result, 200
