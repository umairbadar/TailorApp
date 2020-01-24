package com.example.tailorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ShareActionProvider;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_email, et_password;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        //Initializing Views
        initViews();
    }

    private void initViews(){

        sharedPreferences = getSharedPreferences("MyPre", MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
    }

    private void callNextActivity(){

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginUser(){

        progressDialog.setTitle("Logging User");
        progressDialog.setMessage("Please wait while we verify your credentials");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest req = new StringRequest(Request.Method.POST, Api.LoginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status){

                                progressDialog.dismiss();
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONObject address = data.getJSONObject("address");
                                editor = sharedPreferences.edit();
                                editor.putString("id", data.getString("customer_id"));
                                editor.putString("name", data.getString("firstname"));
                                editor.putString("gender", data.getString("gender"));
                                editor.putString("email", data.getString("email"));
                                editor.putString("phone", data.getString("telephone"));
                                editor.putString("address", address.getString("address_1"));
                                editor.putBoolean("savelogin", true);
                                editor.apply();

                                callNextActivity();


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
                map.put("email", et_email.getText().toString());
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

        if (TextUtils.isEmpty(et_email.getText())){
            et_email.setError("Please enter email");
            et_email.requestFocus();
        } else if (!email.matches(emailPattern)){
            et_email.setError("Please enter valid email");
            et_email.requestFocus();
        } else if (TextUtils.isEmpty(et_password.getText())){
            et_password.setError("Please enter password");
            et_password.requestFocus();
        } else {
            loginUser();
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

        switch (view.getId()) {

            case R.id.btn_login:
                isNetworkAvailable();
                break;

            case R.id.tv_create_account:
                Intent signupIntent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(signupIntent);
        }
    }
}
