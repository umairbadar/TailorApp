package com.example.tailorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tailorapp.cart.DbBitmapUtility;
import com.example.tailorapp.contants.Api;
import com.example.tailorapp.contants.AppController;
import com.example.tailorapp.database.DatabaseHelper;
import com.example.tailorapp.tabLayout.TabsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    //Database
    private DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;

    private String parentActivityName, category_id, product_id, name, price, image, cat_name, upload;
    private int amount, discount;
    private ImageView product_img;
    private TextView tv_name, tv_price, tv_date, tv_time, tv_note;
    private DatePickerDialog datePicker;
    private RadioGroup radioGrpFabric, radioGrpMeasurements;
    private RadioButton radioBtnFabric, radioBtnMeasurements;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    private final int CODE_GALLERY_REQUEST = 999;
    private Bitmap bitmap = null;
    private int img_req = 0;
    private boolean insertData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        parentActivityName = getIntent().getStringExtra("ParentActivityName");

        category_id = getIntent().getStringExtra("cat_id");
        cat_name = getIntent().getStringExtra("name");
        product_id = getIntent().getStringExtra("product_id");
        amount = getIntent().getIntExtra("amount", 0);
        upload = getIntent().getStringExtra("upload");

        name = getIntent().getStringExtra("product_name");
        price = getIntent().getStringExtra("product_price");
        image = getIntent().getStringExtra("product_image");

        Log.e("image", image);

        Toolbar toolbar = findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book A Garment");

        //Initializing Views
        initViews();


        if (parentActivityName.equals("Main")){
            tv_note.setVisibility(View.VISIBLE);
        } else {
            tv_note.setVisibility(View.GONE);
        }
    }

    private void initViews() {

        databaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this);

        tv_name = findViewById(R.id.tv_name);
        tv_price = findViewById(R.id.tv_price);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_note = findViewById(R.id.tv_note);
        product_img = findViewById(R.id.product_image);

        tv_name.setText(name);
        tv_price.setText(price);

        Picasso
                .get()
                .load(image)
                .into(product_img);

        radioGrpFabric = findViewById(R.id.radioGrpFabric);
        radioGrpMeasurements = findViewById(R.id.radioGrpMeasurements);

        getProductDiscount();
    }

    public void AddData(String id, String name, String price, String image, String fabric_details, String measurements, String pickupDate, String pickupTime, String image_status, int amount, String fabric_id, String measurement_id) {

        progressDialog.setTitle("Updating Cart");
        progressDialog.setMessage("Please wait while we are updating your cart.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Bitmap bitmap = ((BitmapDrawable) product_img.getDrawable()).getBitmap();
        insertData = databaseHelper.addData(
                id,
                name,
                price,
                DbBitmapUtility.getBytes(bitmap),
                fabric_details,
                measurements,
                pickupDate,
                pickupTime,
                image_status,
                amount,
                fabric_id,
                measurement_id
        );

        if (insertData) {

            progressDialog.dismiss();

            if (parentActivityName.equals("Tabs")) {

                Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
                intent.putExtra("cat_id", category_id);
                intent.putExtra("name", cat_name);
                startActivity(intent);
                finish();

            } else {

                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }

            Toast.makeText(getApplicationContext(), "Product Added to Cart",
                    Toast.LENGTH_LONG).show();
        } else {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), "Product not added to Cart",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

            try {
                upload = "0";
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                product_img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
    }

    @Override
    public void onBackPressed() {

        if (parentActivityName.equals("Tabs")) {

            Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
            intent.putExtra("cat_id", category_id);
            intent.putExtra("name", cat_name);
            startActivity(intent);
            finish();

        } else {

            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        }

    }

    public void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current date
        // dateEditText picker dialog
        datePicker = new DatePickerDialog(ProductActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                // set date of month , month and year value in the edit text
                tv_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, mYear, mMonth, mDay);
        datePicker.show();
    }

    private void showTimePicker() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ProductActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                tv_time.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void getProductDiscount() {

        StringRequest req = new StringRequest(Request.Method.POST, Api.ProductDetailURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status) {

                                JSONObject innerObj = jsonObject.getJSONObject("product");
                                JSONArray jsonArray = innerObj.getJSONArray("options");
                                JSONObject Obj1 = jsonArray.getJSONObject(1);
                                JSONArray jsonArray1 = Obj1.getJSONArray("product_option_value");
                                JSONObject Obj2 = jsonArray1.getJSONObject(0);
                                discount = Obj2.getInt("amount");

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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("product_id", product_id);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(req);
    }

    private void validations() {

        int fabricID = radioGrpFabric.getCheckedRadioButtonId();
        int measurementID = radioGrpMeasurements.getCheckedRadioButtonId();

        if (upload.equals("1")){
          Toast.makeText(getApplicationContext(), "Please upload design", Toast.LENGTH_LONG).show();
        } else if (fabricID == -1) {

            Toast.makeText(getApplicationContext(), "Please select fabric details",
                    Toast.LENGTH_LONG).show();

        } else if (measurementID == -1) {

            Toast.makeText(getApplicationContext(), "Please select measurement details",
                    Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(tv_date.getText())) {

            Toast.makeText(getApplicationContext(), "Please select pickup date",
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(tv_time.getText())) {

            Toast.makeText(getApplicationContext(), "Please select pickup time",
                    Toast.LENGTH_LONG).show();
        } else {

            radioBtnFabric = findViewById(fabricID);
            radioBtnMeasurements = findViewById(measurementID);

            String fabric_details = radioBtnFabric.getText().toString();
            String fabric_id = radioBtnFabric.getTag().toString();
            String measurements = radioBtnMeasurements.getText().toString();
            String measurement_id = radioBtnMeasurements.getTag().toString();
            String pickupDate = tv_date.getText().toString();
            String pickupTime = tv_time.getText().toString();

            if (img_req == 1) {

                if (radioBtnFabric.getId() == R.id.radioBtnHaveFabric) {

                    int finalPrice = amount - discount;
                    AddData(product_id, name, price, image, fabric_details, measurements, pickupDate, pickupTime, "true", finalPrice, fabric_id, measurement_id);

                } else {

                    AddData(product_id, name, price, image, fabric_details, measurements, pickupDate, pickupTime, "true", amount, fabric_id, measurement_id);
                }

            } else {

                if (radioBtnFabric.getId() == R.id.radioBtnHaveFabric) {

                    int finalPrice = amount - discount;
                    AddData(product_id, name, price, image, fabric_details, measurements, pickupDate, pickupTime, "false", finalPrice, fabric_id, measurement_id);

                } else {

                    AddData(product_id, name, price, image, fabric_details, measurements, pickupDate, pickupTime, "false", amount, fabric_id, measurement_id);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_date:
                showDatePicker();
                break;

            case R.id.tv_time:
                showTimePicker();
                break;

            case R.id.btn_image_upload:
                img_req = 1;
                requestPermission();
                break;

            case R.id.btn_add_to_cart:
                validations();
                break;

            default:
                break;
        }
    }
}
