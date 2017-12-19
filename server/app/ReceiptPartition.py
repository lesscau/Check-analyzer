from app.models import Table

def ReceiptPartition(tableID):
    table = Table.query.filter_by(id=tableID).first()
    userProducts = table.getUserProducts()
    products = table.getProducts()
    result = userProducts
    userNum = 0
    for users in userProducts['users']:
        subSum = 0
        for items in users['items']:
            subSum += items['quantity'] * items['price']
            for index in range(len(products['items'])):
                if products['items'][index]['id'] == items['id']:
                    products['items'][index].update({'quantity': products['items'][index]['quantity'] - items['quantity']})
                    break
        result['users'][userNum].update({
            'total': subSum
        })
        userNum += 1

    freeSum = 0
    for items in products['items']:
        freeSum += items['quantity'] * items['price']
    for users in result['users']:
        users.update({'total': users['total'] + freeSum//len(userProducts['users'])})
    return result