#!/bin/bash

fn=`grep -o 'fn=[0-9]*' qr.txt | sed 's/fn=//g'`
fd=`grep -o 'i=[0-9]*' qr.txt | sed 's/i=//g'`
fpd=`grep -o 'fp=[0-9]*' qr.txt | sed 's/fp=//g'`

curl "https://lk.platformaofd.ru/web/noauth/cheque?fn=$fn&fp=$fpd&i=$fd" > bill.txt

grep -oP "<div class=\"check\-section\">(<div class=\"[ a-zA-Z0-9\-]*\">(.*?)<\/div>)*<\/div>" bill.txt | while read -r line; do
	echo $line | grep -oP "<div class=\"check\-product\-name\">(.*?)<\/div>" | sed 's/<div[^>]*>//g' | sed 's/<\/div>//g'
        echo $line | grep -oP "<div class=\"check\-row\">(.*?)<\/div>" | grep -oP "[0-9\.]+ [xXХх] [0-9\.]+"
       
done

rm bill.txt
 

