package com.trpo6.receiptanalyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.model.Item;

import java.util.List;

/**
 * Редактирование списка продуктов
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    /**
     * Метод inflate парсит xml в объекты view
     */
    private LayoutInflater inflater;
    /**
     * Номер разметки
     */
    private int layout;
    /**
     * Список продуктов
     */
    private List<Item> itemListList;
    /**
     * Тег
     */
    private static final String TAG = "ProductAdapter";

    /**
     * Конструктор списка продуктов
     *
     * @param items Список продуктов
     */
    public ProductAdapter(List<Item> items) {
        this.itemListList = items;
    }

    /**
     * Создание объекта паттерна ViewHolder. Суть паттерна заключается в том, что для каждого элемента списка создаётся объект,
     * хранящий ссылки на отдельные вьюхи внутри элемента.
     *
     * @param parent   Родительский группа View
     * @param viewType Тип View
     * @return Возвращает результирующий объект паттерна
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Отображение определенной позиции в списке продуктов
     *
     * @param viewHolder Объект паттерна ViewHolder
     * @param position   Позиция в списке продуктов
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Item item = itemListList.get(position);
        viewHolder.nameView.setText(item.getName());
        viewHolder.countView.setText(formatValue(item.getSelectedCount()));
        viewHolder.priceView.setText(Float.toString(item.getPrice()));
        viewHolder.incButtonListener.setItem(item);
        viewHolder.decButtonListener.setItem(item);
        viewHolder.deleteButtonListener.setItem(item);
        viewHolder.numberPicker.setMinValue(0);
        viewHolder.numberPicker.setMaxValue(item.getQuantity());
    }

    /**
     * Получение количества продуктов в списке
     *
     * @return Возвращает размер списка продуктов
     */
    @Override
    public int getItemCount() {
        return itemListList.size();
    }

    /**
     * Обработчик кнопки - увеличить/уменьшить выбранное количество продукта
     */
    private void incDecCount(Item item, int count) {
        //int count = item.getSelectedCount()+delta;
        //if(count<0) count=0;
        item.setSelectedCount(count);
        notifyItemChanged(itemListList.indexOf(item));
    }

    /**
     * Обработчик кнопки удаления продукта
     *
     * @param item Конкретный элемент из списка продуктов
     */
    private void delProduct(Item item) {
        int pos = itemListList.indexOf(item);
        itemListList.remove(item);
        notifyItemRemoved(pos);
    }

    /**
     * Форматирование числа в строку
     *
     * @param count Конвертируемое число
     * @return Результирующая строка
     */
    private String formatValue(int count) {
        return String.valueOf(count);
    }

    /**
     * Шаблон каждой строки из списка продуктов
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        final NumberPicker numberPicker;
        final Button incButton, decButton, delButton;
        final TextView nameView, countView, priceView;
        final IncButtonListener incButtonListener;
        final DecButtonListener decButtonListener;
        final DeleteButtonListener deleteButtonListener;
        final NumberPickerListener numberPickerListener;

        /**
         * Конструктор паттерна
         *
         * @param view Исходное View
         */
        public ViewHolder(View view) {
            super(view);
            numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
            numberPicker.setWrapSelectorWheel(false);
            incButton = (Button) view.findViewById(R.id.delButton);
            decButton = (Button) view.findViewById(R.id.delButton);
            delButton = (Button) view.findViewById(R.id.delButton);
            nameView = (TextView) view.findViewById(R.id.nameView);
            countView = (TextView) view.findViewById(R.id.priceView);
            priceView = (TextView) view.findViewById(R.id.priceView);
            incButtonListener = new IncButtonListener();
            decButtonListener = new DecButtonListener();
            deleteButtonListener = new DeleteButtonListener();
            numberPickerListener = new NumberPickerListener();
            incButton.setOnClickListener(incButtonListener);
            decButton.setOnClickListener(decButtonListener);
            delButton.setOnClickListener(deleteButtonListener);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                }
            });
        }
    }

    /**Класс для указания количества конкретного продукта*/
    private class NumberPickerListener implements NumberPicker.OnValueChangeListener {
        private Item item;

        /**
         * Указывает количество конкретного продукта
         *
         * @param numberPicker Объект класса numberPicker
         * @param _old         Старое значение
         * @param _new         Новое значение
         */
        @Override
        public void onValueChange(NumberPicker numberPicker, int _old, int _new) {
            item.setSelectedCount(_new);
        }
    }

    /**Класс для увеличения количества конкретного продукта*/
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
    /**Класс для уменьшения количества конкретного продукта*/
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
    /**Класс для удаления конкретной позиции в списке продуктов*/
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

