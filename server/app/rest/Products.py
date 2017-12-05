from flask import g
from flask_restplus import Namespace, Resource, fields, abort
from sqlalchemy import cast, String
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm.exc import UnmappedInstanceError
from app import db
from app.models import User
from app.models import Products as ProductsModel
from app.rest.Auth import Auth
from app.RandomPhrases import randomPhrase
from datetime import datetime

# Define namespace
api = Namespace('Products', description='Operations with products', path='/products')

# JSON Parsers #

# Products list request query fields
product_list_request = api.parser()
product_list_request.add_argument('divide', type=int, location='args')

# New product request JSON fields
product_request = api.parser()
product_request.add_argument('product_name', type=str, required=True,
    help='No product_name provided', location='json')
product_request.add_argument('count', type=int, required=True,
    help='No count provided', location='json')
product_request.add_argument('price', type=int, required=True,
    help='No price provided', location='json')

# Delete product request JSON fields
delete_product_request = api.parser()
delete_product_request.add_argument('product_name', type=str, required=True,
    help='No product_name provided', location='json')
delete_product_request.add_argument('price', type=int, required=True,
    help='No price provided', location='json')

# JSON Models #

# Creating new product request JSON fields  (all fields required)
product_request_fields = api.model('Product request',
{
    'product_name': fields.String(description='Name', required=True),
    'count': fields.Integer(description='Count', required=True),
    'price': fields.Integer(description='Price', required=True),
})

# Product response JSON fields
product_response_fields = api.model('Product response',
{
    'product_name': fields.String(description='Name'),
    'count': fields.Integer(description='Count'),
    'price': fields.Integer(description='Price'),
})

# Products list response JSON fields
product_list_response_fields = api.model('Products list response',
{
    'items': fields.List(fields.Nested(product_response_fields)),
})

# Delete product request JSON fields (name field required)
delete_product_request_fields = api.model('Delete product request',
{
    'product_name': fields.String(description='Name', required=True),
    'price': fields.Integer(description='Price', required=True),
})

# Delete product response JSON fields
delete_product_response_fields = api.model('Delete product response',
{
    'product_name': fields.String(description='Name', required=True),
})


@api.route('', endpoint='products')
class Products(Resource):
    """
    Operations with list of products

    :var     method_decorators: Decorators applied to methods
    :vartype method_decorators: list
    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.param('divide', 'Division by users if 1')
    @api.doc(responses={
        400: 'Input payload validation failed',
        401: 'Unauthorized access',
        404: 'Username does not connected to any table',
    })
    def get(self):
        """
        Get all products in table (with/without division by users)
        For division by users use divide=1 query parameter

        :return: Products in table
        :rtype:  dict/json
        """
        # Parsing request query fields
        args = product_list_request.parse_args()
        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()
        
        try:
            if args['divide'] is not None and args['divide'] == 1:
                response = user.current_table[0].getUserProducts()
            else:
                response = user.current_table[0].getProducts()
            
            # Return JSON using template
            return response
        except IndexError:
            abort(404, message="Username '{}' does not connected to any table".format(user.username))

    @api.expect(product_request_fields)
    @api.marshal_with(product_response_fields, code=201)
    @api.doc(responses={
        400: 'Input payload validation failed',
        401: 'Unauthorized access',
        404: 'Username does not connected to any table',
    })
    def post(self):
        """
        Add new product to the list of current user table

        :return: New product
        :rtype:  dict/json
        """

        # Parsing request JSON fields
        args = product_request.parse_args()

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        # Create JSON output with correct type of price
        output = {
            'product_name': args['product_name'],
            'count': args['count'],
            'price': args['price'],
        }

        try:
            exists_product = ProductsModel.query.filter(
                ProductsModel.product_name == args['product_name'],
                cast(ProductsModel.price, String()) == str(args['price'] / 100), 
                ProductsModel.table_id == user.current_table[0].id).first()

            if exists_product is None:
                # Create product and add to database
                new_product = ProductsModel(
                    table_id=user.current_table[0].id,
                    product_name=args['product_name'],
                    count=args['count'],
                    price=args['price'] / 100)
                db.session.add(new_product)
            else:
                exists_product.count += args['count']
                output['count'] = exists_product.count

            db.session.commit()

            # Return JSON using template
            return output, 201
        except (IntegrityError, IndexError):
            db.session.rollback()
            abort(404, message="Username '{}' does not connected to any table".format(user.username))
        

    @api.expect(delete_product_request_fields)
    @api.marshal_with(delete_product_response_fields)
    @api.doc(responses={
        400: 'Product with given price does not exist\n\n'
             'Input payload validation failed',
        401: 'Unauthorized access',
        404: 'Username does not connected to any table',
    })
    def delete(self):
        """
        Delete product from the table

        :return: Deleted product name
        :rtype:  dict/json
        """

        # Parsing request JSON fields
        args = delete_product_request.parse_args()

        # Login of authorized user stores in Flask g object
        user = User.query.filter_by(username=g.user.username).first()

        try:
            product = ProductsModel.query.filter(
                ProductsModel.product_name == args['product_name'],
                cast(ProductsModel.price, String()) == str(args['price'] / 100), 
                ProductsModel.table_id == user.current_table[0].id).first()

            if len(product.user_products) != 0:
                for item in product.user_products:
                    db.session.delete(item)
            db.session.delete(product)
            db.session.commit()
            # Return JSON using template
            return product
        except (IntegrityError, UnmappedInstanceError, AttributeError):
            db.session.rollback()
            abort(400, message="Product '{}' with given price does not exist".format(args['product_name']))
        except IndexError:
            abort(404, message="Username '{}' does not connected to any table".format(user.username))
