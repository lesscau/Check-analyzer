from app.models import Table

def ReceiptPartition(tableKey):
    table = Table.query.filter_by(table_key=tableKey).first()
    userProducts = table.getUserProducts()
    products = table.getProducts()
    result = {}
    result['userSums'] = []
    for users in userProducts['users']:
        subSum = 0
        for items in users['items']:
            subSum += items['quantity'] * items['price']
            for index in range(len(products['items'])):
                if products['items'][index]['id'] == items['id']:
                    products['items'][index].update({'quantity': products['items'][index]['quantity'] - items['quantity']})
                    break
        result['userSums'].append({
            'id': users['id'],
            'username': users['username'],
            'total': subSum
        })

    freeSum = 0
    for items in products['items']:
        freeSum += items['quantity'] * items['price']
    for userSums in result['userSums']:
        userSums.update({'total': userSums['total'] + freeSum//len(userProducts['users'])})
    return result

