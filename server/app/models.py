from werkzeug.security import generate_password_hash, check_password_hash
from itsdangerous import (TimedJSONWebSignatureSerializer as Serializer, BadSignature, SignatureExpired)
from app import app, db


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(64), index=True, unique=True)
    password = db.Column(db.String(128))
    phone = db.Column(db.String(12))
    fts_key = db.Column(db.Integer, default=0)

    current_table = db.relationship('Table', secondary='user_table')
    current_products = db.relationship('UserProduct')

    def hash_password(self, password):
        """
        Hash given password for store in database

        :param password: User password
        :type  password: str
        """
        self.password = generate_password_hash(password)

    def verify_password(self, password):
        """
        Match given password with hash saved in database

        :param password: User password
        :type  password: str
        :return: True if the password matched, False otherwise
        :rtype:  bool
        """
        return check_password_hash(self.password, password)

    def generate_auth_token(self, expiration=3600):
        """
        Generate authentification bearer token with expiration by time
        from application secret key

        :param expiration: TTL of token in seconds
        :type  expiration: int
        """
        s = Serializer(app.config['SECRET_KEY'], expires_in=expiration)
        # Insert id in token
        return s.dumps({'id': self.id})

    @staticmethod
    def verify_auth_token(token):
        """
        Match authentification bearer token

        :param token: Bearer token
        :type  token: str
        :return: User with id inserted in token, None otherwise
        :rtype:  app.models.User/None
        """
        s = Serializer(app.config['SECRET_KEY'])
        try:
            data = s.loads(token)
        # Valid expired token
        except SignatureExpired:
            return None
        # Invalid token
        except BadSignature:
            return None
        # Check id in token
        user = User.query.get(data['id'])
        return user

    def __repr__(self):
        return '<User %r>' % (self.username)


class Table(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    table_key = db.Column(db.String(120), index=True, unique=True)
    table_date = db.Column(db.DateTime)

    users = db.relationship('User', secondary='user_table')
    products = db.relationship('Products', cascade="all, delete-orphan")
    user_products = db.relationship('UserProduct', cascade="all, delete-orphan")

    def getProducts(self):
        """
        Get dict of all products in table

        :return: Dict of all products in table
        :rtype:  dict/json
        """
        result = {}
        result['items'] = [{
            'id': item.id,
            'name': item.product_name,
            'quantity': item.count,
            'price': int(item.price * 100)
        } for item in self.products]
        return result

    def getUserProducts(self):
        """
        Get dict of products chosen by every user in table

        :return: Dict of users in table with their products
        :rtype:  dict/json
        """
        result = {}
        result['users'] = [{
            'id': item.id,
            'username': item.username,
            'items': [{
                'id': cart.products.id,
                'name': cart.products.product_name,
                'quantity': cart.count,
                'price': int(cart.products.price * 100)
            } for cart in item.current_products]
        } for item in self.users]
        return result

    def getFreeProducts(self):
        """
        Get dict of products no chosen by anyone

        :return: Dict of products with count of free pie
        :rtype:  dict/json
        """
        userProducts = self.getUserProducts()
        result = self.getProducts()
        for users in userProducts['users']:
            for items in users['items']:
                for index in range(len(result['items'])):
                    if result['items'][index]['id'] == items['id']:
                        result['items'][index].update(
                            {'quantity': result['items'][index]['quantity'] - items['quantity']})
                        break
        return result

    def __repr__(self):
        return '<Table %r>' % (self.table_key)


class Products(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    table_id = db.Column(db.Integer, db.ForeignKey(Table.id), nullable=False)
    product_name = db.Column(db.String(120))
    count = db.Column(db.Integer)
    price = db.Column(db.Float)

    table = db.relationship('Table')
    user_products = db.relationship('UserProduct', cascade="all, delete-orphan")

    __table_args__ = (
        db.UniqueConstraint('product_name', 'table_id', 'price', name='_unique_name_for_table_uc'),
    )

    def __repr__(self):
        return '<Product %r>' % (self.product_name)


class UserTable(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey(User.id), nullable=False, unique=True, index=True)
    table_id = db.Column(db.Integer, db.ForeignKey(Table.id), nullable=False, index=True)
    closed = db.Column(db.Boolean, default=False)

    user = db.relationship('User', backref=db.backref('user_tables', cascade="all, delete-orphan"))
    table = db.relationship('Table', backref=db.backref('user_tables', cascade="all, delete-orphan"))


class UserTableArchive(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey(User.id), nullable=False, index=True)
    table_id = db.Column(db.Integer, db.ForeignKey(Table.id), nullable=False, index=True)


class UserProduct(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey(User.id), nullable=False, index=True)
    product_id = db.Column(db.Integer, db.ForeignKey(Products.id), nullable=False, index=True)
    table_id = db.Column(db.Integer, db.ForeignKey(Table.id), index=True)
    count = db.Column(db.Integer)

    user = db.relationship('User')
    table = db.relationship('Table')
    products = db.relationship('Products')
