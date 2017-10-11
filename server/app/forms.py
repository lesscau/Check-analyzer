from flask_wtf import Form
from wtforms import StringField
from wtforms import validators

class LoginForm(Form):
    username = StringField('Login:', [validators.DataRequired()])
    password = StringField('Password:',  [validators.DataRequired()])

    def __init__(self, *args, **kwargs):
        kwargs['csrf_enabled'] = False
        Form.__init__(self, *args, **kwargs)


    def edit_login(request):
    Form = LoginForm(request.POST, username)

    if request.POST and Form.validate():
        Form.populate_obj(username)
        username.save()
        return redirect('/username')

    return render('edit.html', form=form, username=username)


class RegisterForm(Form):
      name = StringField('NickName', [validators.DataRequired()])





