#! /usr/bin/env python
# -*- coding: utf-8 -*-

from app.views import getReceiptFromFts
import unittest, json

class getReceiptFromFtsTesting(unittest.TestCase):

    def setUp(self):
        self.fn = "8710000100331763"
        self.fd = "24423"
        self.fp = "1625615565"
        self.login = "+79312887716"
        self.passw  = "375159"

    def testGetCorrectReceipt(self):
        data = {'fiscalDocumentNumber': 24423, 'fiscalSign': 1625615565, 'stornoItems': [], 'fiscalDriveNumber': '8710000100331763', 'cashTotalSum': 1076200, 'receiptCode': 3, 'operator': 'Евгения Рудакова', 'ecashTotalSum': 0, 'totalSum': 1076200, 'modifiers': [], 'shiftNumber': 55, 'requestNumber': 336, 'userInn': '5029069967', 'items': [{'price': 29100, 'sum': 58200, 'name': 'Kлей д/стеклообоев AXTON25м2', 'quantity': 2, 'nds18': 8878, 'modifiers': []}, {'price': 217600, 'sum': 652800, 'name': 'Ст/обои 1х12,5м,Пекин 170г/м бел W55', 'quantity': 3, 'nds18': 99580, 'modifiers': []}, {'price': 87800, 'sum': 87800, 'name': 'Холст флиз 1х25м,130г/м бел', 'quantity': 1, 'nds18': 13393, 'modifiers': []}, {'price': 253000, 'sum': 253000, 'name': 'Kраска Special KITCHEN BATHROOM A 5л', 'quantity': 1, 'nds18': 38593, 'modifiers': []}, {'price': 24400, 'sum': 24400, 'name': 'Kраска д/потолков и стен Лакра бел 6,5кг', 'quantity': 1, 'nds18': 3722, 'modifiers': []}], 'kktRegId': '0000645616019712', 'taxationType': 1, 'user': 'ООО "Леруа Мерлен Восток"', 'rawData': '//8AHKUDARAJhxAAAQAzF2MAX2dL4tRVBABeFwACAhIDAA4CEQQQADg3MTAwMDAxMDAzMzE3NjMNBBQAMDAwMDY0NTYxNjAxOTcxMiAgICD6AwwANTAyOTA2OTk2NyAgEAQEAGdfAAD0AwQAqDeKWTUEBgAxBGDk7M0OBAQANwAAABIEBABQAQAAHgQBAAH8AwMA6GsQIwQ5AAYEHABLq6WpIKQv4eKlqquurqGupaIgQVhUT04yNawyNwQCAKxx/wMDAAPQBxMEAgBY404EAgCuIiMERAAGBCQAkeIvrqGuqCAx5TEyLDWsLI+lqqitIDE3MKMvrCChpasgVzU1NwQDAABSA/8DAwADuAsTBAMAAPYJTgQDAPyEASMEOgAGBBsAla6r4eIg5KuopyAx5TI1rCwxMzCjL6wgoaWrNwQDAPhWAf8DAwAD6AMTBAMA+FYBTgQCAFE0IwRDAAYEJABL4KDhqqAgU3BlY2lhbCBLSVRDSEVOIEJBVEhST09NIEEgNas3BAMASNwD/wMDAAPoAxMEAwBI3ANOBAIAwZYjBEUABgQoAEvgoOGqoCCkL6+u4q6rqq6iIKgg4eKlrSCLoKrgoCChpasgNiw1qqM3BAIAUF//AwMAA+gDEwQCAFBfTgQCAIoOBwQDAOhrEDkEAQAA/QMQAIWio6WtqO8gkOOkoKquoqAfBAEAARgEGQCOjo4gIoul4OOgIIyl4KulrSCCruHirqoiTgQDAEaBAoEG8nQYEI5Z', 'operationType': 1, 'nds18': 164166, 'dateTime': '2017-08-08T19:14:00'}

        newJson = getReceiptFromFts(self.fn, self.fd, self.fp, self.login, self.passw)
        testJson = json.dumps(data, sort_keys=True, ensure_ascii=False)
        newJson = json.dumps(newJson, sort_keys=True, ensure_ascii=False)
        self.assertEqual(newJson, testJson)

    def testUseWrongFn(self):
        fnBad = "87100001003317643"

        with self.assertRaises(ValueError) as cm: getReceiptFromFts(fnBad, self.fd, self.fp, self.login, self.passw)
        self.assertEqual('Non-JSON response: "the ticket was not found"', str(cm.exception))

    def testUseWrongFd(self):
        fdBad = "25423"
    
        with self.assertRaises(ValueError) as cm: getReceiptFromFts(self.fn, fdBad, self.fp, self.login, self.passw)
        self.assertEqual('Non-JSON response: "the ticket was not found"', str(cm.exception))

    def testUseWrongFp(self):
        fpBad = "1627615565"
    
        with self.assertRaises(ValueError) as cm: getReceiptFromFts(self.fn, self.fd, fpBad, self.login, self.passw)
        self.assertEqual('Non-JSON response: "the ticket was not found"', str(cm.exception))
    
    def testUseWrongLogin(self):
        loginBad = ["89312887716", "+79312887726", "+7931288771"]
        loginBad1 = "89312887716"
        loginBad2 = "+79312887726"
        loginBad3 = "+7931288771"
    
        self.maxDiff = None
        for login in loginBad:
            with self.subTest(login = login):
                with self.assertRaises(ValueError) as cm: getReceiptFromFts(self.fn, self.fd, self.fp, login, self.passw)
                self.assertEqual('Non-JSON response: "the user was not found or the specified password was not correct"', str(cm.exception))
    
    def testUseWrongPassword(self):
        passwBad  = "385159"
    
        with self.assertRaises(ValueError) as cm: getReceiptFromFts(self.fn, self.fd, self.fp, self.login, passwBad)
        self.assertEqual('Non-JSON response: "the user was not found or the specified password was not correct"', str(cm.exception))

if __name__ == '__main__':
    unittest.main()