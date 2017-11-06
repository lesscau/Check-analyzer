package com.trpo6.receiptanalyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.model.Item;

import java.util.List;

/**
 * Добавление элементов списка продуктов
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>  {
    private LayoutInflater inflater;
    private int layout;
    private List<Item> itemListList;
    private static final String TAG = "ProductAdapter";
    public ProductAdapter(List<Item> items) {
        this.itemListList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Item item = itemListList.get(position);
        viewHolder.nameView.setText(item.getName());
        viewHolder.countView.setText(formatValue(item.getSelectedCount()));
        viewHolder.priceView.setText(Float.toString(item.getPrice()));
        viewHolder.numberPicker.setMinValue(0);
        viewHolder.numberPicker.setMaxValue(item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return itemListList.size();
    }

    // Обработчик кнопки - увеличить/уменьшить выбранное количество продукта
    private void incDecCount(Item item, int count){
        //int count = item.getSelectedCount()+delta;
        //if(count<0) count=0;
        item.setSelectedCount(count);
        notifyItemChanged(itemListList.indexOf(item));
    }

    // Обработчик кнопки удаления продукта
    private void delProduct(Item item){
        int pos = itemListList.indexOf(item);
        itemListList.remove(item);
        notifyItemRemoved(pos);
    }

    private String formatValue(int count){
        return String.valueOf(count) ;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        final NumberPicker numberPicker;
        final TextView nameView, countView, priceView;
        final NumberPickerListener numberPickerListener;

        public ViewHolder(View view){
            super(view);
            numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
            numberPicker.setWrapSelectorWheel(false);
            nameView = (TextView) view.findViewById(R.id.nameView);
            countView = (TextView) view.findViewById(R.id.priceView);
            priceView = (TextView) view.findViewById(R.id.priceView);
            numberPickerListener = new NumberPickerListener();
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                }
            });
        }
    }

    private class NumberPickerListener implements NumberPicker.OnValueChangeListener{
        private Item item;

        @Override
        public void onValueChange(NumberPicker numberPicker, int _old, int _new) {
            item.setSelectedCount(_new);
        }
    }

    private class IncButtonListener implements View.OnClickListener {
        private Item item;

        @Override
        public void onClick(View view) {
            incDecCount(item,1);
        }

        public void setItem(Item item) {
            this.item = item;
        }
    }

    private class DecButtonListener implements View.OnClickListener {
        private Item item;

        @Override
        public void onClick(View view) {
            incDecCount(item,-1);
        }

        public void setItem(Item item) {
            this.item = item;
        }
    }

    private class DeleteButtonListener implements View.OnClickListener {
        private Item item;

        @Override
        public void onClick(View view) {
            delProduct(item);
        }

        public void setItem(Item item) {
            this.item = item;
        }
    }
}

