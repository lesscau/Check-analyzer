# -*- coding: utf-8 -*-
from flask import render_template, request, jsonify
import requests
import json, random, uuid
from app import app, models
from .forms import LoginForm

@app.route('/login')
def login():
    form = LoginForm()
    return render_template('login.json', form = form) 

# Password from json not used because it doesn't exist in current User model
@app.route('/receipt')
def getReceipt():
	# Get JSON from GET request
	json = request.get_json(force = True)
	# Parse QR-string
	qrArgs = json['qr'].split("&")
	fn = qrArgs[2][3:]
	fd = qrArgs[3][2:]
	fp = qrArgs[4][3:]
	# Get phone/pass from fts account
	# user = User.query.filter_by(username = json['login'], password = json['password']).first_or_404()
	user = models.User.query.filter_by(username = json['login']).first_or_404()

	receipt = getReceiptFromFts(fn, fd, fp, user.phone, user.fts_key)
	return jsonify(receipt)

def getReceiptFromFts(fn, fd, fp, loginPhone, smsPass):
    baseUrl   = "https://proverkacheka.nalog.ru:9999"
    userAgent = "okhttp/3.0.1"
    protocol  = "2"

    deviceId  = uuid.uuid4().hex
    deviceOs  = "Adnroid 4.4.4"
    clientVersion = "1.4.1.3"

    url = "{}/v1/inns/*/kkts/*/fss/{}/tickets/{}?fiscalSign={}&sendToEmail=no".format(baseUrl, fn, fd, fp)
    auth = (loginPhone, smsPass)
    headers = {'Device-Id':     deviceId,
               'Device-OS':     deviceOs,
               'Version':       protocol,
               'ClientVersion': clientVersion,
               'user-agent':    userAgent}

    response = requests.get(url, headers = headers, auth = auth)
    try:
        return json.loads(response.text)['document']['receipt']
    except ValueError:
        raise ValueError('Non-JSON response: "{}"'.format(response.text))