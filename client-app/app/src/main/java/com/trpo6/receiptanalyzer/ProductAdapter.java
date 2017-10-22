package com.trpo6.receiptanalyzer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

/**
 * Добавление элементов списка продуктов
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>  {
    private LayoutInflater inflater;
    private int layout;
    private List<Product> productList;
    private static final String TAG = "ProductAdapter";
    public ProductAdapter(List<Product> products) {
        //super(context, resource, products);
        this.productList = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Product product = productList.get(position);
        viewHolder.nameView.setText(product.getName());
        viewHolder.countView.setText(formatValue(product.getSelectedCount()));
        viewHolder.priceView.setText(Float.toString(product.getPrice()));
        viewHolder.incButtonListener.setProduct(product);
        viewHolder.decButtonListener.setProduct(product);
        viewHolder.deleteButtonListener.setProduct(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Обработчик кнопки - увеличить/уменьшить выбранное количество продукта
    private void incDecCount(Product product, int delta){
        int count = product.getSelectedCount()+delta;
        if(count<0) count=0;
        product.setSelectedCount(count);
        notifyItemChanged(productList.indexOf(product));
    }

    // Обработчик кнопки удаления продукта
    private void delProduct(Product product){
        int pos = productList.indexOf(product);
        productList.remove(product);
        notifyItemRemoved(pos);
    }

    private String formatValue(int count){
        return String.valueOf(count) ;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        final Button incButton, decButton, delButton;
        final TextView nameView, countView, priceView;
        final IncButtonListener incButtonListener;
        final DecButtonListener decButtonListener;
        final DeleteButtonListener deleteButtonListener;

        public ViewHolder(View view){
            super(view);
            incButton = (Button) view.findViewById(R.id.incButton);
            decButton = (Button) view.findViewById(R.id.decButton);
            delButton = (Button) view.findViewById(R.id.delButton);
            nameView = (TextView) view.findViewById(R.id.nameView);
            countView = (TextView) view.findViewById(R.id.countView);
            priceView = (TextView) view.findViewById(R.id.priceView);
            incButtonListener = new IncButtonListener();
            decButtonListener = new DecButtonListener();
            deleteButtonListener = new DeleteButtonListener();
            incButton.setOnClickListener(incButtonListener);
            decButton.setOnClickListener(decButtonListener);
            delButton.setOnClickListener(deleteButtonListener);
        }
    }

    private class IncButtonListener implements View.OnClickListener {
        private Product product;

        @Override
        public void onClick(View view) {
            incDecCount(product,1);
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    private class DecButtonListener implements View.OnClickListener {
        private Product product;

        @Override
        public void onClick(View view) {
            incDecCount(product,-1);
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    private class DeleteButtonListener implements View.OnClickListener {
        private Product product;

        @Override
        public void onClick(View view) {
            delProduct(product);
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }
}

