package com.trpo6.receiptanalyzer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mikepenz.materialdrawer.Drawer;
import com.trpo6.receiptanalyzer.R;
import com.trpo6.receiptanalyzer.adapter.ProductAdapter;
import com.trpo6.receiptanalyzer.adapter.RecyclerProductTouchHelper;
import com.trpo6.receiptanalyzer.adapter.RecyclerUserTouchHelper;
import com.trpo6.receiptanalyzer.adapter.UserAdapter;
import com.trpo6.receiptanalyzer.model.Item;
import com.trpo6.receiptanalyzer.utils.AppToolbar;
import com.trpo6.receiptanalyzer.utils.AuthInfo;

import java.util.ArrayList;

/**
 * Окно списка продуктов
 */
public class ProductListActivity extends AppCompatActivity implements RecyclerUserTouchHelper.RecyclerItemTouchHelperListener,
        RecyclerProductTouchHelper.RecyclerItemTouchHelperListener{
    LayoutInflater inflater;
    /** custom dialog view */
    View mDialogView;
    View mActivityView;
    /** Список временных пользователей */
    public static ArrayList<String> tempUsers = new ArrayList();
    {
        tempUsers.add(AuthInfo.getName());
    }

    /** Адаптер списка пользователей */
    UserAdapter userAdapter;

    /**Адаптер списка продуктов*/
    ProductAdapter productAdapter;
    /**Список продуктов*/
    static ArrayList<Item> items = new ArrayList();
    /**View для работы со списком продуктов*/
    RecyclerView productListView;

    /**Запуск окна продуктов*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = AppToolbar.setToolbar(this, AuthInfo.getTableKey());
        Drawer drawer = AppToolbar.setMenu(this);
        mActivityView = getLayoutInflater().inflate(R.layout.activity_product_list, null);

        productListView = (RecyclerView) findViewById(R.id.productList);
        productAdapter = new ProductAdapter(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        productListView.setAdapter(productAdapter);
        productListView.setLayoutManager(layoutManager);
        productListView.setItemAnimator(itemAnimator);

        // adding item touch helper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerProductTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(productListView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.UP){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(productListView);
    }


    /**Обработчик кнопки добавления продукта*/
    public void add(View view){
        /**Получение названия*/
        EditText productEditText = (EditText) findViewById(R.id.addProduct);
        String product = productEditText.getText().toString();
        if(product.isEmpty()) return;

        /**Получение общего количества*/
        EditText countEditText = (EditText) findViewById(R.id.addCount);
        String strCount = countEditText.getText().toString();
        if (strCount.isEmpty()) return;
        int count = Integer.parseInt(strCount);

        /** Получение цены*/
        EditText priceEditText = (EditText) findViewById(R.id.addPrice);
        String strPrice = priceEditText.getText().toString();
        if (strPrice.isEmpty()) return;
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
        // inflate the custom dialog view
        inflater = getLayoutInflater();
        mDialogView = inflater.inflate(R.layout.dialog_edit_user,null);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // Меню редактирования списка участников
        if(itemId == R.id.action_edit_user) {
            // Инициализация диалога
            final Dialog dialog = new Dialog(ProductListActivity.this);
            dialog.setContentView(mDialogView);
            dialog.setTitle("Список участников");
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
            dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);

            final EditText newUser = (EditText) dialog.findViewById(R.id.et_new_user);
            FloatingActionButton addButt = (FloatingActionButton) dialog.findViewById(R.id.del_user_button);
            FloatingActionButton closeButt = (FloatingActionButton) dialog.findViewById(R.id.close_dialog_button);

            RecyclerView recyclerView = dialog.findViewById(R.id.userListView);
            userAdapter = new UserAdapter(this,tempUsers);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(userAdapter);

            // adding item touch helper
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerUserTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

            ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.UP){

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    // Row is swiped from recycler view
                    // remove it from adapter
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };

            // attaching the touch helper to recycler view
            new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);



            addButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (newUser.getText().toString().isEmpty())
                        return;
                    tempUsers.add(newUser.getText().toString());
                    userAdapter.notifyItemInserted(tempUsers.size()-1);
                    newUser.setText("");
                    Log.i("tempUsers size",""+tempUsers.size());
                }
            });

            closeButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

        if(itemId == R.id.action_qr){
            Intent intent = new Intent("QRscanner");
            startActivity(intent);
            return  true;
        }
        return false;
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof ProductAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            Item item = items.get(viewHolder.getAdapterPosition());

            // backup of removed item for undo purpose
            final Item deletedItem = items.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            // remove the item from recycler view
            productAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(getCurrentFocus(), item.getName() + " удалён!", Snackbar.LENGTH_LONG);
            snackbar.setAction("ОТМЕНА", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    productAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
        if (viewHolder instanceof UserAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = tempUsers.get(viewHolder.getAdapterPosition());

            // backup of removed item for undo purpose
            final String deletedItem = tempUsers.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            userAdapter.removeRecord(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option

            Snackbar snackbar = Snackbar
                    .make(mDialogView, name + " удалён!", Snackbar.LENGTH_LONG);
            snackbar.setAction("ОТМЕНА", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    userAdapter.restoreRecord(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}