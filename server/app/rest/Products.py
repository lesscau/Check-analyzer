from flask import g
from flask_restplus import Namespace, Resource, fields, abort
from sqlalchemy.exc import IntegrityError
from app import db
from app.models import User, Products
from app.rest.Auth import Auth
from app.RandomPhrases.main import randomPhrase
from datetime import datetime

# Define namespace
api = Namespace('Products', description='Operations with products', path='/products')


# JSON Parsers #

# New product request JSON fields
product_request = api.parser()
product_request.add_argument(
                    'product_name', type=str, required=True,
                    help='No name provided', location='json')
product_request.add_argument(
                    'count', type=int, required=True,
                    help='No name provided', location='json')
product_request.add_argument(
                    'price', type=float, required=True,
                    help='No name provided', location='json')


# Delete product request JSON fields
delete_product_request = api.parser()
delete_product_request.add_argument(
                    'product_name', type=str, required=True,
                    help='No name provided', location='json')



# JSON Models #

# Creating new product request JSON fields  (all fields required)
product_request_fields = api.model(
    'Product request',
    {
        'product_name': fields.String(description='Name', required=True),
        'count': fields.Integer(description='Count', required=True),
        'price': fields.Float(description='Price', required=True),
    })

# Product response JSON fields
product_response_fields = api.model(
    'Product response ',
    {
        'product_name': fields.String(description='Name'),
        'count': fields.Integer(description='Count'),
        'price': fields.Float(description='Price'),
    })

# Products list response JSON fields
product_list_response_fields = api.model(
    'Products list response',
    {
        'items': fields.List(fields.Nested(product_response_fields)),
    })

# Delete product request JSON fields  (name field required)
delete_product_request_fields = api.model(
    'Delete product request',
    {
        'product_name': fields.String(description='Name', required=True),
    })

# Delete product response JSON fields 
delete_product_response_fields = api.model(
    'Delete product response',
    {
        'product_name': fields.String(description='Name', required=True),
    })



@api.route('', endpoint='products')
class Products(Resource):
    """
    Operations with list of products

    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.doc(security=None)
    @api.expect(product_request_fields)
    @api.marshal_with(product_response_fields)
    @api.doc(responses={
        400: 'User does not exist at any tables'
    })
    def post(self):
        """
        Add new product to the list of table current user sitting at

        :return: New product
        :rtype:  dict/json
        """

        # Parsing request JSON fields
        args = product_request.parse_args()

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        exists_product = Products.query.filter_by(product_name=args['product_name'], price=args['price']).first()

        if exists_product is None:
            # Create product and add to database 
            new_product = Products(
                      table_id=user.current_table.id,
                      product_name=args['product_name'],
                      count=args['count'],
                      price=args['price'])

            try:
                db.session.add(new_product)
                db.session.commit()
                # Return JSON using template
                return product, 201
            except IntegrityError:
                db.session.rollback()
                abort(400, message="User '{}' does not associated with any table".format(user.id))

        else:
            try:
                setattr(exists_poduct, 'count', args['count'] + exists_product.count)
                db.session.commit()
                # Return JSON using template
                return exists_product, 201
            except:
                db.session.rollback()
                abort(400, message="User '{}' does not associated with any table".format(user.id))

    @api.doc(security=None)
    @api.expect(delete_product_request_fields)
    @api.marshal_with(delete_product_response_fields)
    @api.doc(responses={
        400: 'Product does not exist'
    })
    def delete(self):
        """
        Delete product from the table

        :return: deleted products name
        :rtype:  dict/json
        """

        # Parsing request JSON fields
        args = product_request.parse_args()

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        product = Products.query.filter_by(product_name=args['product_name'], table_id=user.current_table).first()

        try:
            db.session.delete(product)
            # Return JSON using template
        except IntegrityError:
            db.session.rollback()
            abort(400, message="Product '{}' does not exist".format(product.product_name))

        db.session.commit()
        return product, 201
