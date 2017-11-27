from flask import g
from flask_restplus import Namespace, Resource, fields, abort
from sqlalchemy.exc import IntegrityError
from app import db
from app.models import User
from app.rest.Auth import Auth
from RandomPhrases import randomPhrase
from datetime import datetime

# Define namespace
api = Namespace('Users', description='Operations with users', path='/users')


# JSON Parsers #

# Find table request JSON fields
table_request = api.parser()
table_request.add_argument('table_key', type=str, required=True,
    help='No name provided', location='json')



# JSON Models #

# Creating new table response JSON fields
new_table_response_fields = api.model('Table list request',
{
    'table_key': fields.String(description='Keyword', required=True),
})



@api.route('/tables', endpoint='tables')
class Tables(Resource):
    """
    Operations with list of tables

    """
    method_decorators = [Auth.multi_auth.login_required]

    @api.doc(security=None)
    @api.marshal_with(new_table_response_fields)
    @api.doc(responses={
        409: 'User already relates to a table',
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
            table_key = key,
            table_date = datetime.utcnow())

	#Create user-table dependency 
	table = Table.query.filter_by(table_key = key).first()
	user_table = UserTable (
             user_id = user.id,
             table_id = table.id) 

         try:
            db.session.add(table)
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
	keyword = randomPhrase();
        if Table.query.filter_by(table_key = keyword).first() is not None:
            return self.newPhraseIfAlreadyExists()
	else
            return keyword


