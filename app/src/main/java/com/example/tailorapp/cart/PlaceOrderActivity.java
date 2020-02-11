package com.example.tailorapp.cart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tailorapp.MainActivity;
import com.example.tailorapp.R;
import com.example.tailorapp.contants.Api;
import com.example.tailorapp.contants.AppController;
import com.example.tailorapp.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup radioGrpShippingMethods;
    private RadioButton radioButton, radioBtnCashOnDelivery;
    private RadioButton[] rbArray = new RadioButton[10];
    private int[] shippingPrice = new int[10];
    private int radioID;

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private String subTotal;
    private TextView tv_totalPrice;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        subTotal = getIntent().getStringExtra("sub_total");

        Toolbar toolbar = findViewById(R.id.toolbar_placeOrder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Confirm Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initViews();
    }

    private void initViews() {

        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences("MyPre", MODE_PRIVATE);

        databaseHelper = new DatabaseHelper(this);

        radioGrpShippingMethods = findViewById(R.id.radioGrpShippingMethods);
        getShippingMethods();

        radioBtnCashOnDelivery = findViewById(R.id.radioBtnCashOnDelivery);

        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        tv_totalPrice.setText(subTotal);

        radioGrpShippingMethods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int radioID = radioGrpShippingMethods.getCheckedRadioButtonId();
                int subTotalPrice = Integer.parseInt(subTotal);
                tv_totalPrice.setText(String.valueOf(subTotalPrice + shippingPrice[radioID]));
            }
        });
    }

    private void getShippingMethods() {

        StringRequest req = new StringRequest(Request.Method.GET, Api.ShippingMethodsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status) {
                                JSONArray jsonArray = jsonObject.getJSONArray("methods");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    rbArray[i] = new RadioButton(PlaceOrderActivity.this);

                                    JSONObject innerObj = jsonArray.getJSONObject(i);

                                    rbArray[i].setText(innerObj.getString("title"));
                                    rbArray[i].setTag(innerObj.getString("code"));
                                    rbArray[i].setId(i);
                                    shippingPrice[i] = innerObj.getInt("cost");
                                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                                    radioGrpShippingMethods.addView(rbArray[i], params);

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("error"),
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        AppController.getInstance().addToRequestQueue(req);
    }

    private String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    public JSONArray getAllProducts() {

        JSONArray jsonArray = new JSONArray();
        Cursor data = databaseHelper.getData();

        try {
            while (data.moveToNext()) {

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("id", data.getString(0));
                    jsonObject.put("name", data.getString(1));
                    jsonObject.put("price", data.getString(2));

                    if (data.getString(8).equals("true")) {
                        Bitmap bitmap = DbBitmapUtility.getImage(data.getBlob(3));
                        String image = bitmapToBase64(bitmap);
                        jsonObject.put("upload_a_style_instead", "data:image/jpeg;base64," + image);
                        //Log.e("Image", "data:image/jpeg;base64," + image);

                    } else {
                        jsonObject.put("upload_a_style_instead", "false");
                    }
                    jsonObject.put("fabric_details", data.getString(10));
                    jsonObject.put("measurements", data.getString(11));
                    jsonObject.put("pickup_date", data.getString(6));
                    jsonObject.put("pickup_time", data.getString(7));
                    jsonObject.put("amount", data.getString(9));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArray.put(jsonObject);
            }

        } finally {
            data.close();
        }

        return jsonArray;
    }

    private void sendData() {

        progressDialog.setTitle("Creating Order");
        progressDialog.setMessage("Please wait while we are creating your order");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        radioID = radioGrpShippingMethods.getCheckedRadioButtonId();
        radioButton = findViewById(radioID);

        StringRequest req = new StringRequest(Request.Method.POST, Api.PlaceOrderURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status) {
                                progressDialog.dismiss();
                                showOKDialog(PlaceOrderActivity.this, "Order has been created.");

                            } else {
                                progressDialog.hide();
                                Toast.makeText(getApplicationContext(), "Oppsss. Something went wrong. Please try again later.",
                                        Toast.LENGTH_LONG).show();
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", sharedPreferences.getString("id", ""));
                map.put("token", sharedPreferences.getString("token", ""));
                map.put("shipping_method", radioButton.getText().toString());
                map.put("shipping_code", radioButton.getTag().toString());
                map.put("shipping", String.valueOf(shippingPrice[radioID]));
                map.put("sub_total", subTotal);
                map.put("total", tv_totalPrice.getText().toString());
                map.put("products", getAllProducts().toString());
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(req);
    }

    private void validations() {

        int radioID = radioGrpShippingMethods.getCheckedRadioButtonId();

        if (radioID == -1) {

            Toast.makeText(getApplicationContext(), "Please select Shipping Method",
                    Toast.LENGTH_LONG).show();
        } else if (!radioBtnCashOnDelivery.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please select Payment Method",
                    Toast.LENGTH_LONG).show();
        } else {
            showDialog(this, "Are you sure you want to place an order?");
        }
    }

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sendData();
            }
        });
        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showOKDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                databaseHelper.delete();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_order) {

            validations();
        }
    }
}
