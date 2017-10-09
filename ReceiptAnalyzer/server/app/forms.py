from flask_wtf import Form
from wtforms import StringField
from wtforms import validators

class LoginForm(Form):
    username = StringField('Login:', [validators.DataRequired()])
    password = StringField('Password:',
                        [validators.DataRequired()])

    def __init__(self, *args, **kwargs):
        kwargs['csrf_enabled'] = False
        Form.__init__(self, *args, **kwargs)
