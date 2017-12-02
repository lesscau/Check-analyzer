package com.trpo6.receiptanalyzer.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mikepenz.materialdrawer.Drawer;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.adapter.ProductAdapter;
import com.trpo6.receiptanalyzer.adapter.UserAdapter;
import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.utils.AppToolbar;

import java.util.ArrayList;


/**
 * Окно списка продуктов
 */
public class ProductListActivity extends AppCompatActivity {
    /** Список временных пользователей */
    public ArrayList<String> tempUsers = new ArrayList();
    /** Адаптер списка пользователей */
    UserAdapter userAdapter;
    /**View для работы со списком продуктов*/
    RecyclerView userList;


    /**Адаптер списка продуктов*/
    ProductAdapter productAdapter;
    /**Список продуктов*/
    static ArrayList<Item> items = new ArrayList();
    /**View для работы со списком продуктов*/
    RecyclerView productList;

    /**Запуск окна продуктов*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = AppToolbar.setToolbar(this, "");
        Drawer drawer = AppToolbar.setMenu(this);

        productList = (RecyclerView) findViewById(R.id.productList);
        productAdapter = new ProductAdapter(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        productList.setAdapter(productAdapter);
        productList.setLayoutManager(layoutManager);
        productList.setItemAnimator(itemAnimator);

        /** все для свайпа начинается здесь */
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) {    //if swipe left
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ProductListActivity.this,R.style.dialog_theme)); //alert for confirm to delete
                    builder.setTitle("Удаление")
                             .setMessage("Удалить "+ items.get(position).getName()+"?")    //set message
                             .setPositiveButton("Да", new DialogInterface.OnClickListener() { //when click on DELETE
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    items.remove(position);
                                    productAdapter.notifyItemRemoved(position); //item removed from recylcerview


                                    return;
                                }
                            })
                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    productAdapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                                    productAdapter.notifyItemRangeChanged(position, productAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                                    return;
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(productList); //set swipe to recylcerview
        //все для свайпа заканчивается здесь
    }


    /**Обработчик кнопки добавления продукта*/
    public void add(View view){
        /**Получение названия*/
        EditText productEditText = (EditText) findViewById(R.id.addProduct);
        String product = productEditText.getText().toString();
        if(product.isEmpty() && items.contains(product)) return;

        /**Получение общего количества*/
        EditText countEditText = (EditText) findViewById(R.id.addCount);
        String strCount = countEditText.getText().toString();
        if (strCount.equals("")) return;
        int count = Integer.parseInt(strCount);

        /** Получение цены*/
        EditText priceEditText = (EditText) findViewById(R.id.addPrice);
        String strPrice = priceEditText.getText().toString();
        if (strPrice.equals("")) return;
        float price = Float.parseFloat(strPrice);

        /** Добавление продукта в список*/
        items.add(new Item(product,count,price));
        productEditText.setText("");
        countEditText.setText("");
        priceEditText.setText("");
        productAdapter.notifyItemInserted(items.size()-1);
    }

    /** Переход к главной активити по нажатию кнопки назад */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer itemId = item.getItemId();
        /*
        if(itemId == R.id.action_edit_user){
            Dialog dialog = new Dialog(getBaseContext());
            dialog.setContentView(R.layout.dialog_edit_user);
            dialog.setTitle("Список участников");
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();

            EditText newUser = (EditText) dialog.findViewById(R.id.et_new_user);
            Button addButt = (Button) dialog.findViewById(R.id.del_user_button);
            Button closeButt = (Button) dialog.findViewById(R.id.close_dialog_button);
            tempUsers.add("fff");

            userList = (RecyclerView) dialog.findViewById(R.id.userListView);
            userAdapter = new UserAdapter(tempUsers);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

            userList.setAdapter(userAdapter);
            userList.setLayoutManager(layoutManager);
            userList.setItemAnimator(itemAnimator);
        }
        */
        if(itemId == R.id.action_qr){
            Intent intent = new Intent("QRscanner");
            startActivity(intent);
            return  true;
        }
        return false;
    }
}