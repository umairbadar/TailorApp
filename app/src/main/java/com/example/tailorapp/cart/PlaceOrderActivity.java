package com.example.tailorapp.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    RadioButton[] rbArray = new RadioButton[10];

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        Toolbar toolbar = findViewById(R.id.toolbar_placeOrder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Confirm Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initViews();
    }

    private void initViews() {

        sharedPreferences = getSharedPreferences("MyPre", MODE_PRIVATE);

        databaseHelper = new DatabaseHelper(this);

        radioGrpShippingMethods = findViewById(R.id.radioGrpShippingMethods);
        getShippingMethods();

        radioBtnCashOnDelivery = findViewById(R.id.radioBtnCashOnDelivery);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }

    private String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    public JSONObject getAllProducts() {

        int radioID = radioGrpShippingMethods.getCheckedRadioButtonId();
        radioButton = findViewById(radioID);

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("user_id", sharedPreferences.getString("id", ""));
            jsonObj.put("token", sharedPreferences.getString("token", ""));
            jsonObj.put("shipping_method", radioButton.getTag().toString());

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
                            jsonObject.put("image", image);

                        } else {
                            jsonObject.put("image", "false");
                        }
                        //Log.e("image", "data:image/jpeg;base64," + image);
                        jsonObject.put("fabric", data.getString(4));
                        jsonObject.put("measurement", data.getString(5));
                        jsonObject.put("date", data.getString(6));
                        jsonObject.put("time", data.getString(7));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonArray.put(jsonObject);
                }

                jsonObj.put("products", jsonArray);

            } finally {
                data.close();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    private void sendData() {

        String url = "http://110.93.225.221/tailor/index.php?key=ssred1&route=feed/rest_api/print_data";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);
                        //databaseHelper.delete();
                        //Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("data", getAllProducts().toString());
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

            Log.e("Data", getAllProducts().toString());
            //sendData();
        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_order) {

            validations();
        }

    }
}
