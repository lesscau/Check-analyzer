package com.trpo6.receiptanalyzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Добавление элементов списка продуктов
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Product> productList;

    ProductAdapter(Context context, int resource, ArrayList<Product> products) {
        super(context, resource, products);
        this.productList = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Product product = productList.get(position);

        viewHolder.nameView.setText(product.getName());
        viewHolder.countView.setText(formatValue(product.getSelectedCount()));
        viewHolder.priceView.setText(Float.toString(product.getPrice()));

        // Обработчик кнопки - увеличить выбранное количество продукта
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = product.getSelectedCount()-1;
                if(count<0) count=0;
                product.setSelectedCount(count);
                viewHolder.countView.setText(formatValue(count));
            }
        });

        // Обработчик кнопки - уменьшить выбранное количество продукта
        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int count = product.getSelectedCount()+1;
                product.setSelectedCount(count);
                viewHolder.countView.setText(formatValue(count));
            }
        });
        return convertView;
    }

    private String formatValue(int count){
        return String.valueOf(count) ;
    }
    private class ViewHolder {
        final Button addButton, removeButton;
        final TextView nameView, countView, priceView;
        ViewHolder(View view){
            addButton = (Button) view.findViewById(R.id.addButton);
            removeButton = (Button) view.findViewById(R.id.removeButton);
            nameView = (TextView) view.findViewById(R.id.nameView);
            countView = (TextView) view.findViewById(R.id.countView);
            priceView = (TextView) view.findViewById(R.id.priceView);
        }
    }
}

