package com.trpo6.receiptanalyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.trpo6.receiptanalyzer.R;

import java.util.List;

/**
 * Created by lessc on 02.12.2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private List<String> records;

    public UserAdapter(List<String> records) {
        this.records = records;
    }
    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
     */
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_edit_user, parent, false);
        return new ViewHolder(v);
    }
    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */
    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        String record = records.get(position);
        holder.name.setText(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private Button del;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.user_text_view);
            del = (Button) itemView.findViewById(R.id.del_user_button);
        }
    }
}
