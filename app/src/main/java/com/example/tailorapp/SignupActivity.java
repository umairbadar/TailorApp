package com.example.tailorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tailorapp.contants.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name, et_email, et_address, et_phone, et_password, et_cpassword;
    private ProgressDialog progressDialog;

    private Spinner spn_gender;
    private ArrayList<String> gender_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar_signup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initViews();
    }

    private void initViews(){


        progressDialog = new ProgressDialog(this);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_address = findViewById(R.id.et_address);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        et_cpassword = findViewById(R.id.et_cpassword);

        spn_gender = findViewById(R.id.spn_gender);
        gender_list = new ArrayList<>();
        gender_list.add("Select Gender");
        gender_list.add("Male");
        gender_list.add("Female");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender_list);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_gender.setAdapter(genderAdapter);
    }

    private void registerUser(){

        progressDialog.setTitle("Registering User");
        progressDialog.setMessage("Please wait while we create your account");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest req = new StringRequest(Request.Method.POST, Api.SignupURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status){
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Please Login to continue",
                                        Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            } else {

                                progressDialog.hide();
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", et_name.getText().toString());
                map.put("email", et_email.getText().toString());
                map.put("phone", et_phone.getText().toString());
                map.put("address", et_address.getText().toString());
                map.put("gender", spn_gender.getSelectedItem().toString());
                map.put("password", et_password.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }

    private void validation(){

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String cpassword = et_cpassword.getText().toString();

        if (TextUtils.isEmpty(et_name.getText())){
            et_name.setError("Please enter name");
            et_name.requestFocus();
        } else if (TextUtils.isEmpty(et_email.getText())){
            et_email.setError("Please enter email");
            et_email.requestFocus();
        } else if (!email.matches(emailPattern)){
            et_email.setError("Please enter valid email address");
            et_email.requestFocus();
        } else if (TextUtils.isEmpty(et_address.getText())){
            et_address.setError("Please enter address");
            et_address.requestFocus();
        } else if (TextUtils.isEmpty(et_phone.getText())){
            et_phone.setError("Please enter phone number");
            et_phone.requestFocus();
        } else if (spn_gender.getSelectedItemPosition() == 0){
            Toast.makeText(getApplicationContext(), "Please Select Gender",
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(et_password.getText())){
            et_password.setError("Please enter password");
            et_password.requestFocus();
        } else if (TextUtils.isEmpty(et_cpassword.getText())){
            et_cpassword.setError("Please enter confirm password");
            et_cpassword.requestFocus();
        } else if (!password.equals(cpassword)){
            Toast.makeText(getApplicationContext(), "Password not match",
                    Toast.LENGTH_LONG).show();
        } else {
            registerUser();
        }
    }

    private void isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {

            validation();
        } else if (networkInfo == null) {
            Toast.makeText(getApplicationContext(),R.string.no_internet_msg,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_signup){

            isNetworkAvailable();
        }

    }
}
