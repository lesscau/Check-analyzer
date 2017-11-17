from requests import get, post
import json
import uuid


class FtsRequest:
    """
    Requests to Federal Tax Service

    :var     baseUrl:   Base Federal Tax Service URL
    :vartype baseUrl:   str
    :ivar    headers:   Request headers
    :vartype headers:   dict
    """
    baseUrl = "https://proverkacheka.nalog.ru:9999"

    def __init__(self):
        self.headers = {'Device-Id':     uuid.uuid4().hex,
                        'Device-OS':     "Adnroid 4.4.4",
                        'Version':       "2",
                        'ClientVersion': "1.4.1.3",
                        'user-agent':    "okhttp/3.0.1"}

    def signUp(self, name, email, phone):
        """
        Create new user in Federal Tax Service and get password in SMS

        :param name:  Name of user
        :type  name:  str
        :param email: User email
        :type  email: str
        :param phone: User phone number
        :type  phone: str
        :return:      JSON response
        :rtype:       dict
        """
        url = "{}/v1/mobile/users/signup".format(self.baseUrl)
        data = {"name": name, "email": email, "phone": phone}
        response = post(url, headers=self.headers, json=data)

        if response.ok:
            return {"ftsRequestSuccess": True}
        else:
            return {"ftsRequestSuccess": False,
                    "responseCode": response.status_code,
                    "error": response.text}

    def restorePassword(self, phone):
        """
        Restore SMS password for existing user in Federal Tax Service

        :param phone: User phone number
        :type  phone: str
        :return:      JSON response
        :rtype:       dict
        """
        url = "{}/v1/mobile/users/restore".format(self.baseUrl)
        data = {"phone": phone}
        response = post(url, headers=self.headers, json=data)

        if response.ok:
            return {"ftsRequestSuccess": True}
        else:
            return {"ftsRequestSuccess": False,
                    "responseCode": response.status_code,
                    "error": response.text}

    def checkAuthData(self, loginPhone, smsPass):
        """
        Check if user exists in Federal Tax Service

        :param loginPhone: User phone number
        :type  loginPhone: str
        :param smsPass:    Password from SMS
        :type  smsPass:    str/int
        :return:           True if user exists, False otherwise
        :rtype:            bool
        """
        url = "{}/v1/mobile/users/login".format(self.baseUrl)
        auth = (loginPhone, smsPass)
        response = get(url, headers=self.headers, auth=auth)
        return response.ok

    def getReceipt(self, fn, fd, fp, loginPhone, smsPass):
        """
        Get receipt JSON from Federal Tax Service

        :param fn:         ФН number
        :type  fn:         str
        :param fd:         ФД number
        :type  fd:         str
        :param fp:         ФП number
        :type  fp:         str
        :param loginPhone: User phone number
        :type  loginPhone: str
        :param smsPass:    Password from SMS
        :type  smsPass:    str/int
        :return:           JSON response
        :rtype:            dict
        """
        url = "{}/v1/inns/*/kkts/*/fss/{}/tickets/{}" \
              "?fiscalSign={}&sendToEmail=no".format(self.baseUrl, fn, fd, fp)
        auth = (loginPhone, smsPass)
        response = get(url, headers=self.headers, auth=auth)

        # If response 202 code (no body), try 10 times again
        # and return 408 if JSON not received
        if response.status_code == 202:
            calls = 0
            while calls < 10 and response.status_code == 202:
                response = get(url, headers=self.headers, auth=auth)
                calls += 1
            if response.status_code == 202:
                return {"ftsRequestSuccess": False,
                        "responseCode": 408,
                        "error": "Empty JSON response"}

        if response.status_code == 200:
            JSON = json.loads(response.text)['document']['receipt']
            JSON["ftsRequestSuccess"] = True
            return JSON
        else:
            return {"ftsRequestSuccess": False,
                    "responseCode": response.status_code,
                    "error": response.text}
