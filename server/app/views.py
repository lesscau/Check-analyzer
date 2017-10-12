# -*- coding: utf-8 -*-
from flask import render_template, flash, redirect
from app import app
from .forms import LoginForm

@app.route('/login')
def login():
    form = LoginForm()
    return render_template('login.json', form = form) 
