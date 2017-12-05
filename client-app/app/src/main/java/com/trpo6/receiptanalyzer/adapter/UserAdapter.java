package com.trpo6.receiptanalyzer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trpo6.receiptanalyzer.R;

import java.util.List;

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
