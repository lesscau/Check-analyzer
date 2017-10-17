from requests import get, post
import json, uuid

# TODO: handle possible errors
# TODO: remove prints
# TODO: write docstrings
class FtsRequest:
    baseUrl = "https://proverkacheka.nalog.ru:9999"

    def __init__(self):
        self.headers = {'Device-Id':     uuid.uuid4().hex,
                        'Device-OS':     "Adnroid 4.4.4",
                        'Version':       "2",
                        'ClientVersion': "1.4.1.3",
                        'user-agent':    "okhttp/3.0.1"}
    
    def signUp(self, name, email, phone):
        url = "{}/v1/mobile/users/signup".format(self.baseUrl)
        
        data = {"name": name, "email" : email, "phone": phone}

        response = post(url, headers = self.headers, json = data)
        print(response.status_code)
        return response.text

    def restorePassword(self, phone):
        url = "{}/v1/mobile/users/restore".format(self.baseUrl)
        
        data = {"phone": phone}

        response = post(url, headers = self.headers, json = data)
        print(response.status_code)
        return response.text

    def checkAuthData(self, loginPhone, smsPass):
        """
        If ok 200 + json (email, name)
        fail 403 + the user was not found or the specified password was not correct
        """
        url = "{}/v1/mobile/users/login".format(self.baseUrl)
        auth = (loginPhone, smsPass)

        response = get(url, headers = self.headers, auth = auth)
        print("{} {}".format(response.status_code, response.text))
        return response.text

    def getReceipt(self, fn, fd, fp, loginPhone, smsPass):
        url = "{}/v1/inns/*/kkts/*/fss/{}/tickets/{}?fiscalSign={}&sendToEmail=no".format(self.baseUrl, fn, fd, fp)
        auth = (loginPhone, smsPass)

        response = get(url, headers = self.headers, auth = auth)
        try:
            return json.loads(response.text)['document']['receipt']
        except ValueError:
            raise ValueError('Non-JSON response: "{}"'.format(response.text))