package com.trpo6.receiptanalyzer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.activity.ProductListActivity;

import java.util.List;
import java.util.Set;

/**
 * Created by lessc on 02.12.2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    /** Список пользователей*/
    private List<String> records;

    public UserAdapter(Context context, List<String> records) {
        this.records = records;
        this.context = context;
    }
    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
     */
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new ViewHolder(v);
    }
    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String record = records.get(position);
        holder.name.setText(record);

    }
        /**
         * Обработчик кнопки удаления участника
         *
         * @param pos Конкретный элемент из списка
         */
        void delUser(int pos){
            Log.i("records pos",records.get(pos)+" "+pos);

            records.remove(pos);
            //ProductListActivity.tempUsers.remove(pos);
            notifyItemRemoved(pos);

            Log.i("main list size",""+ProductListActivity.tempUsers.size());
            Log.i("local list size",""+records.size());
        }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void removeRecord(int position){
            records.remove(position);
            notifyItemRemoved(position);
    }

    public void restoreRecord(String record, int position){
        records.add(position, record);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_text_view);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }



}
