package com.example.tailorapp.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tailorapp.R;
import com.example.tailorapp.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView cartList;
    private List<Model_Cart> list;
    private Adapter_Cart adapter;

    private DatabaseHelper databaseHelper;
    public static LinearLayout emptyCartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initViews();
    }

    private void initViews() {

        databaseHelper = new DatabaseHelper(this);
        emptyCartLayout = findViewById(R.id.emptyCartLayout);

        cartList = findViewById(R.id.cartList);
        cartList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list = new ArrayList<>();
        adapter = new Adapter_Cart(list, getApplicationContext());
        cartList.setAdapter(adapter);
        getCartList();
    }

    private int getCount(){

        Cursor cursor = databaseHelper.getCount();

        int count = -1;
        while(cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        return count;
    }

    private void getCartList() {

        if (getCount() > 0) {

            emptyCartLayout.setVisibility(View.INVISIBLE);
            Cursor data = databaseHelper.getData();
            while (data.moveToNext()) {

                Model_Cart item = new Model_Cart(
                        data.getString(0),
                        data.getString(1),
                        data.getString(2),
                        data.getString(3),
                        data.getString(4),
                        data.getString(5),
                        data.getString(6),
                        data.getString(7)
                );

                list.add(item);
            }
            adapter.notifyDataSetChanged();

        } else {

            emptyCartLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
