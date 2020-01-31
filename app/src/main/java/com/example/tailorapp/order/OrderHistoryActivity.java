package com.example.tailorapp.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tailorapp.R;
import com.example.tailorapp.contants.Api;
import com.example.tailorapp.contants.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderHistoryList;
    private Adapter_OrderHistory adapter;
    private List<Model_OrderHistory> list;
    private ProgressDialog progressDialog;
    private TextView tv_no_order;
    private String user_id, token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        Toolbar toolbar = findViewById(R.id.toolbar_orderHistory);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initView();
    }

    private void initView(){

        progressDialog = new ProgressDialog(this);
        tv_no_order = findViewById(R.id.tv_no_order);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPre", MODE_PRIVATE);
        user_id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        orderHistoryList = findViewById(R.id.orderHistoryList);
        orderHistoryList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list = new ArrayList<>();
        adapter = new Adapter_OrderHistory(list, getApplicationContext());
        orderHistoryList.setAdapter(adapter);
        getOrders();
    }

    private void getOrders(){

        progressDialog.setTitle("Loading Orders");
        progressDialog.setMessage("Please wait while we load your order details");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest req = new StringRequest(Request.Method.POST, Api.OrderHistoryURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status){
                                tv_no_order.setVisibility(View.INVISIBLE);
                                orderHistoryList.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                                JSONArray jsonArray = jsonObject.getJSONArray("orders");
                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String id = innerObj.getString("order_id");
                                    String name = innerObj.getString("name");
                                    String order_status = innerObj.getString("status");
                                    String date = innerObj.getString("date_added");
                                    String products = innerObj.getString("products");
                                    String total = innerObj.getString("total");

                                    list.add(new Model_OrderHistory(
                                            id,
                                            name,
                                            order_status,
                                            date,
                                            products,
                                            total
                                    ));
                                }

                                adapter.notifyDataSetChanged();

                            } else {
                                tv_no_order.setVisibility(View.VISIBLE);
                                orderHistoryList.setVisibility(View.INVISIBLE);
                                progressDialog.hide();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", user_id);
                map.put("token", token);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(req);
    }
}
