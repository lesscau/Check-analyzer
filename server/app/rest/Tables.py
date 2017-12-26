from flask import g
from flask_restplus import Namespace, Resource, fields, abort
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm.exc import UnmappedInstanceError
from app import db
from app.ReceiptPartition import ReceiptPartition
from app.models import User, Table, UserTable, UserProduct, Products
from app.rest.Auth import Auth
from app.RandomPhrases import randomPhrase
from datetime import datetime

# Define namespace
api = Namespace('Tables', description='Operations with tables', path='/tables')

# JSON Parsers #

# Find table request JSON fields
table_request = api.parser()
table_request.add_argument('table_key', type=str, required=True,
    help='No table_key provided', location='json')
table_sync_request = api.parser()
table_sync_request.add_argument('sync_data', type=dict, required=True, help='Users pick', location='json')
# JSON Models #

# Creating new table response JSON fields
table_response_fields = api.model('Table response',
{
    'table_key': fields.String(description='Keyword'),
})

# Find table request JSON fields (all fields required)
find_table_request_fields = api.model('UserTable request',
{
    'table_key': fields.String(description='Keyword', required=True),
})

# TablesUsers response JSON template
user_table_response_fields = api.model('UserTable response',
{
    'table_id': fields.Integer(description='Table id', required=True),
    'user_id': fields.Integer(description='User id')
})

# TablesUsers list response item
tables_users_item_fields = api.model('Item TablesUsers response',
{
    'id': fields.Integer(description='User id', required=True),
    'name': fields.String(description='User name', required=True)
})

# TablesUsers list response JSON template
user_table_list_response_fields = api.model('UserTable list request',
{
    'users': fields.List(fields.Nested(tables_users_item_fields)),
})


# UserItems response JSON template
user_items_fields = api.model('User items response',
{
    'id': fields.Integer(description='Item id'),
    'name': fields.String(description='Item name'),
    'temp_username': fields.String(description='Item temp_username'),
    'quantity': fields.Integer(description='Item quantity'),
    'price': fields.Integer(description='Item price')
})
# UsersTotal response JSON template
users_total_fields = api.model('Users total response',
{
    'temp_username': fields.String(description='Users temp_username'),
    'total': fields.Integer(description='User Total')
})
# UserProducts response JSON template
user_products_response_fields = api.model('User Products response',
{
    'username': fields.String(description='User name', required=True),
    'total': fields.List(fields.Nested(users_total_fields)),
})

# UsersSum list response JSON template
users_sum_response_fields = api.model('UsersSum list response',
{
    'users': fields.List(fields.Nested(user_products_response_fields)),
})

# Free UserItems response JSON template
free_user_items_fields = api.model('Free User items response',
{
    'id': fields.Integer(description='Item id'),
    'name': fields.String(description='Item name'),
    'quantity': fields.Integer(description='Item quantity'),
    'price': fields.Integer(description='Item price')
})
# FreeItems list response JSON template
free_items_response_fields = api.model('FreeItems list response',
{
    'items': fields.List(fields.Nested(free_user_items_fields)),
})

# Temp Users sync request JSON fields (all fields required)
users_sync_request_fields = api.model('UsersSync request',
{
    'temp_username': fields.String(description='Users temp_username', required=True, nullable=True),
    'product_name': fields.String(description='Users product_name', required=True),
    'count': fields.Integer(description='Count of product', required=True)
})

# Find table sync request JSON fields
find_table_sync_request_fields = api.model('TablesSync request',
{
    'sync_data': fields.Nested(users_sync_request_fields),
})


@api.route('', endpoint='tables')
class Tables(Resource):
    """
    Operations with tables

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.marshal_with(table_response_fields, code=201)
    @api.doc(responses={
        401: 'Unauthorized access',
        406: 'User already connected with table',
        409: 'Table already exists',
    })
    def post(self):
        """
        Create new table

        :return: New table key phrase
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        # Create table and add to database
        key = self.newPhraseIfAlreadyExists()
        table = Table(
            table_key=key,
            table_date=datetime.utcnow())

        # TODO: Delete try-except or stop recursive key generation after X tries
        try:
            db.session.add(table)
            db.session.flush()
        except IntegrityError:
            db.session.rollback()
            abort(409, message="Table '{}' already exist".format(key))

        # Create user-table dependency
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
            abort(406, message="Username '{}' already connected with table".format(user.username))

    def newPhraseIfAlreadyExists(self):
        """
        Set the first keyword generated does not exist in database

        :return: New key phrase
        :rtype:  str
        """
        keyword = randomPhrase()
        if Table.query.filter_by(table_key=keyword).first() is not None:
            return self.newPhraseIfAlreadyExists()
        else:
            return keyword


@api.route('/users', endpoint='tables_users')
class TablesUsers(Resource):
    """
    Operations with users in table

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.marshal_with(user_table_list_response_fields)
    @api.doc(responses={
        401: 'Unauthorized access',
        404: 'Username does not connected to any table'
    })
    def get(self):
        """
        Get list of users related to the same table as auth user

        :return: list of users id's and names
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        try:
            result = {}
            result['users'] = [{
                'id': item.user_id,
                'name': item.user.username
            } for item in user.current_table[0].user_tables]
            return result
        except IndexError:
            abort(404, message="Username '{}' does not connected to any table".format(user.username))

    @api.expect(find_table_request_fields)
    @api.marshal_with(user_table_response_fields, code=201)
    @api.doc(responses={
        400: 'No table_key provided\n\n'
             'Input payload validation failed',
        401: 'Unauthorized access',
        404: 'Table keyword does not exist',
        406: 'Username already connected with table',
    })
    def post(self):
        """
        Add new user to the table

        :return: table and user id's
        :rtype:  dict/json
        """

        # Parsing request JSON fields
        args = table_request.parse_args()

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        if args['table_key'] is None:
            abort(400, message="Input payload validation failed")

        table = Table.query.filter_by(table_key=args['table_key']).first()

        if table is None:
            abort(404, message="Table keyword '{}' does not exist".format(args['table_key']))

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
            abort(406, message="Username '{}' already connected with table".format(user.username))

    @api.marshal_with(user_table_response_fields)
    @api.doc(responses={
        400: 'Table does not exist',
        401: 'Unauthorized access',
        404: 'Username does not connected to any table',
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
            for item in user.current_products():
                db.session.delete(item)
            db.session.delete(user_table)
        except UnmappedInstanceError:
            db.session.rollback()
            abort(404, message="Username '{}' does not connected to any table".format(user.username))

        user_table_list = UserTable.query.filter_by(table_id=user_table.table_id).first()
        try:
            if user_table_list is None:
                db.session.delete(user_table.table)
            db.session.commit()
            # Return JSON using template
            return user_table
        except (IntegrityError, UnmappedInstanceError):
            db.session.rollback()
            abort(400, message="Table '{}' does not exist".format(user_table.table.table_key))

@api.route('/me', endpoint='tables_me')
class TablesInfo(Resource):
    """
    Information about user current table

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.marshal_with(table_response_fields)
    @api.doc(responses={
        401: 'Unauthorized access',
        404: 'Username does not connected to any table'
    })
    def get(self):
        """
        Get table key of user current table

        :return: Table key phrase
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        try:
            return user.current_table[0]
        except IndexError:
            abort(404, message="Username '{}' does not connected to any table".format(user.username))

@api.route('/checkout', endpoint='tables_checkout')
class TablesCheckout(Resource):
    """
    Information about total sum of users

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.marshal_with(users_sum_response_fields)
    @api.doc(responses={
        401: 'Unauthorized access',
        404: 'Username does not connected to any table'
    })
    def get(self):
        """
        Get users products and total sum

        :return: List of table users with total sum
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        try:
            return ReceiptPartition(user.current_table[0].id)
        except IndexError:
            abort(404, message="Username '{}' does not connected to any table".format(user.username))

@api.route('/ack', endpoint='tables_ack')
class TablesAck(Resource):
    """
    Information about count of free items

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.marshal_with(free_items_response_fields)
    @api.doc(responses={
        401: 'Unauthorized access',
        404: 'Username does not connected to any table'
    })
    def get(self):
        """
        Get products free count

        :return: List of products with free count
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        try:
            table = Table.query.filter_by(id=user.current_table[0].id).first()
            return table.getFreeProducts()
        except IndexError:
            abort(404, message="Username '{}' does not connected to any table".format(user.username))

@api.route('/sync', endpoint='tables_sync')
class TablesSync(Resource):
    """
    Synchronize client and server

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.expect(find_table_sync_request_fields)
    @api.doc(responses={
        400: 'Nothing to synchronize data',
        401: 'Unauthorized access',
        404: 'Username does not connected to any table',
        406: 'Problems with database transactions',
    })
    def post(self):
        """
        Synchronize database with client

        :return: Code http response
        :rtype:  Integer
        """

        # Parsing request JSON fields
        args = table_sync_request.parse_args()
        if args['sync_data'] is None:
            abort(400, message="Nothing to synchronize data")

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        try:
            table = Table.query.filter_by(id=user.current_table[0].id).first()
            product_id = Products.query.filter_by(table_id=user.current_table[0].id, product_name=args['sync_data']['product_name']).first().id
            user_product = UserProduct.query.filter(UserProduct.table_id == user.current_table[0].id, UserProduct.user_id == user.id,
                                                    UserProduct.temp_username == args['sync_data']['temp_username'],
                                                    UserProduct.product_id == product_id).first()
            if args['sync_data']['count'] == 0:
                if user_product is not None:
                    db.session.delete(user_product)
            elif user_product is None:
                user_product = UserProduct(
                    user_id=user.id,
                    temp_username=args['sync_data']['temp_username'],
                    product_id=product_id,
                    table_id=user.current_table[0].id,
                    count=args['sync_data']['count']
                )
                db.session.add(user_product)
            else:
                user_product.count = args['sync_data']['count']
            db.session.commit()
            return 201
        except (IntegrityError, UnmappedInstanceError, AttributeError):
            db.session.rollback()
            abort(406, message="Problems with database transactions")
        except IndexError:
            abort(404, message="Username '{}' does not connected to any table".format(user.username))