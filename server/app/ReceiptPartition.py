from app.models import Table

def ReceiptPartition(tableID):
    table = Table.query.filter_by(id=tableID).first()
    userProducts = table.getUserProducts()
    products = table.getProducts()
    result = userProducts
    userNum = 0
    countTempUsers = 0
    for users in userProducts['users']:
        subSum = 0
        result['users'][userNum].update({
            'total': {'': subSum}
        })
        for items in users['items']:
            for index in range(len(products['items'])):
                if products['items'][index]['id'] == items['id']:
                    products['items'][index].update({'quantity': products['items'][index]['quantity'] - items['quantity']})
                    break
            if items['temp_username'] is None:
                subSum += items['quantity'] * items['price']
            elif result['users'][userNum]['total'].get(items['temp_username']) is None:
                result['users'][userNum]['total'].update({
                    items['temp_username']: items['quantity'] * items['price']
                })
            else:
                result['users'][userNum]['total'].update({
                    items['temp_username']: result['users'][userNum]['total'][items['temp_username']] + (items['quantity'] * items['price'])
                })
        result['users'][userNum]['total'].update({
            '': subSum
        })
        countTempUsers += len(result['users'][userNum]['total'])-1
        userNum += 1

    freeSum = 0
    for items in products['items']:
        freeSum += items['quantity'] * items['price']
    result2 = {'users': []}
    userNum = 0
    for users in result['users']:
        result2['users'].append({'id': users['id'], 'total': []})
        for tempUsername in users['total'].keys():
            users['total'].update({tempUsername: users['total'][tempUsername] + freeSum//(len(userProducts['users']) + countTempUsers)})
            result2['users'][userNum]['total'].append({'temp_username': tempUsername, 'total': users['total'][tempUsername]})
        userNum += 1
    return result2