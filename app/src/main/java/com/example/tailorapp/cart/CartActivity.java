package com.example.tailorapp.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tailorapp.R;
import com.example.tailorapp.contants.Api;
import com.example.tailorapp.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView cartList;
    private List<Model_Cart> list;
    private Adapter_Cart adapter;

    private DatabaseHelper databaseHelper;
    public static LinearLayout emptyCartLayout;
    public static LinearLayout subTotalLayout;
    public static TextView tv_subTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initViews();
    }

    private void initViews() {

        databaseHelper = new DatabaseHelper(this);
        emptyCartLayout = findViewById(R.id.emptyCartLayout);
        subTotalLayout = findViewById(R.id.subTotalLayout);
        tv_subTotal = findViewById(R.id.tv_subTotal);
        tv_subTotal.setText(String.valueOf(databaseHelper.sum()));

        cartList = findViewById(R.id.cartList);
        cartList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list = new ArrayList<>();
        adapter = new Adapter_Cart(list, getApplicationContext());
        cartList.setAdapter(adapter);
        getCartList();
    }

    private int getCount() {

        Cursor cursor = databaseHelper.getCount();

        int count = -1;
        while (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    private void getCartList() {

        if (getCount() > 0) {

            emptyCartLayout.setVisibility(View.INVISIBLE);
            subTotalLayout.setVisibility(View.VISIBLE);
            Cursor data = databaseHelper.getData();
            while (data.moveToNext()) {

                Model_Cart item = new Model_Cart(
                        data.getString(0),
                        data.getString(1),
                        data.getString(2),
                        data.getBlob(3),
                        data.getString(4),
                        data.getString(5),
                        data.getString(6),
                        data.getString(7),
                        data.getInt(9)
                );

                list.add(item);
            }
            adapter.notifyDataSetChanged();

        } else {

            emptyCartLayout.setVisibility(View.VISIBLE);
            subTotalLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_place_order) {

            Intent placeOrderIntent = new Intent(getApplicationContext(), PlaceOrderActivity.class);
            placeOrderIntent.putExtra("sub_total", tv_subTotal.getText().toString());
            startActivity(placeOrderIntent);
        }

    }
}
