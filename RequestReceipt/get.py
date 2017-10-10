#! /usr/bin/env python

import requests
import json, random, uuid

def getReceiptJson(fn, fd, fp, loginPhone, smsPass):
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

if __name__ == '__main__':
    fn = "8710000100331763"
    fd = "24423"
    fp = "1625615565"
    login = "+79312887716"
    passw  = "375159"

    try:
        json = getReceiptJson(fn, fd, fp, login, passw)
        print(json)
    except ValueError as e:
        print(e)