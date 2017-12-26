from flask import g
from flask_restplus import Namespace, Resource, fields, abort
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm.exc import UnmappedInstanceError
from app import db
from app.ReceiptPartition import ReceiptPartition
from app.models import User, Table, UserTable, UserProduct, Products
from app.rest.Auth import Auth

# Define namespace
api = Namespace('Archive', description='Operations in tables history', path='/archive')


# JSON Parsers #

# Find table request JSON fields
archive_table_request = api.parser()
archive_table_request.add_argument('table_id', type=int, required=True,
    help='No table_id provided', location='json')


# JSON Models #
# Archive tables parameters JSON fields
archive_tables_list_fields = api.model('Table list response',
{
    'table_id': fields.Integer(description='Table_id'),
    'table_date': fields.DateTime(description='Table_date')
})

# Achive tables list list response JSON template
archive_tables_list_response_fields = api.model('ArchiveTable list response',
{
    'tables': fields.List(fields.Nested(archive_tables_list_fields)),
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
    'id': fields.Integer(description='User id', required=True),
    #'username': fields.String(description='Username', required=True),
    'total': fields.List(fields.Nested(users_total_fields)),
    #'items': fields.List(fields.Nested(user_items_fields))
})

# ArchiveUsersSum list response JSON template
archive_users_sum_response_fields = api.model('UsersSum list response',
{
    'users': fields.List(fields.Nested(user_products_response_fields)),
})

# Archive table request JSON fields
archive_table_request_fields = api.model('Table request',
{
    'table_id': fields.Integer(description='Table_id', required=True)
})


@api.route('/list', endpoint='archive_tables_list')
class ArchiveTablesList(Resource):
    """
    Operations with archive tables list

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.marshal_with(archive_tables_list_response_fields, code=201)
    @api.doc(responses={
        401: 'Unauthorized access'
    })
    def get(self):
        """
        Get all the tables user was connected with

        :return: List of pairs (table_id, date)
        :rtype:  dict/json
        """

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        try:
            result = {}
            result['tables'] = [{
                'table_id': user_table.id,
                'table_date': user_table.table[0].table_date
            } for user_table in UserTableArchive.query.filter_by(user_id=user.id)]
            return result
        except IndexError:
            abort(401, message="Unauthorized access")



@api.route('', endpoint='archive_table')
class ArchiveTables(Resource):
    """
    Operations with archive table

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.marshal_with(archive_users_sum_response_fields, code=201)
    @api.doc(responses={
        401: 'Unauthorized access',
        404: 'Table does not exists',
    })
    def get(self):
        """
        Get info about partition on archive table

        :return: List of user sums
        :rtype:  dict/json
        """

        # Parsing request JSON fields
        args = archive_table_request.parse_args()

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()
        if user is None:
            abort(401, message="Unauthorized access")

        try:
            return ReceiptPartition(args['table_id'])
        except IndexError:
            abort(404, message="Table '{}' does not exists".format(args['table_id']))


