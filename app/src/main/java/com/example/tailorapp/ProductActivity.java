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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tailorapp.database.DatabaseHelper;
import com.example.tailorapp.tabLayout.TabsActivity;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    //Database
    private DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;

    private String category_id, product_id, name, price, image;
    private ImageView product_img;
    private TextView tv_name, tv_price, tv_date, tv_time;
    private DatePickerDialog datePicker;
    private RadioGroup radioGrpFabric, radioGrpMeasurements;
    private RadioButton radioBtnFabric,radioBtnMeasurements;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    private final int CODE_GALLERY_REQUEST = 999;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        category_id = getIntent().getStringExtra("cat_id");
        product_id = getIntent().getStringExtra("product_id");
        name = getIntent().getStringExtra("product_name");
        price = getIntent().getStringExtra("product_price");
        image = getIntent().getStringExtra("product_image");

        Toolbar toolbar = findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book A Garment");

        //Initializing Views
        initViews();
    }

    private void initViews() {

        databaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this);

        tv_name = findViewById(R.id.tv_name);
        tv_price = findViewById(R.id.tv_price);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        product_img = findViewById(R.id.product_image);

        tv_name.setText(name);
        tv_price.setText(price);

        Picasso
                .get()
                .load(image)
                .into(product_img);

        radioGrpFabric = findViewById(R.id.radioGrpFabric);
        radioGrpMeasurements = findViewById(R.id.radioGrpMeasurements);
    }

    public void AddData(String id, String name, String price, String image, String fabric_details, String measurements, String pickupDate, String pickupTime){

        progressDialog.setTitle("Updating Cart");
        progressDialog.setMessage("Please wait while we are updating your cart.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        boolean insertData = databaseHelper.addData(id, name, price, image, fabric_details, measurements, pickupDate, pickupTime);

        if (insertData){

            progressDialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
            intent.putExtra("cat_id", category_id);
            startActivity(intent);
            finish();

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

        Intent intent = new Intent(getApplicationContext(), TabsActivity.class);
        intent.putExtra("cat_id", category_id);
        startActivity(intent);
        finish();
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
                tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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

    private void validations(){

        int fabricID = radioGrpFabric.getCheckedRadioButtonId();
        int measurementID = radioGrpMeasurements.getCheckedRadioButtonId();

        if (fabricID == -1){

            Toast.makeText(getApplicationContext(), "Please select fabric details",
                    Toast.LENGTH_LONG).show();

        } else if (measurementID == -1){

            Toast.makeText(getApplicationContext(), "Please select measurement details",
                    Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(tv_date.getText())){

            Toast.makeText(getApplicationContext(), "Please select pickup date",
                    Toast.LENGTH_LONG).show();
        }

        else if (TextUtils.isEmpty(tv_time.getText())){

            Toast.makeText(getApplicationContext(), "Please select pickup time",
                    Toast.LENGTH_LONG).show();
        } else {

            radioBtnFabric = findViewById(fabricID);
            radioBtnMeasurements = findViewById(measurementID);

            String fabric_details = radioBtnFabric.getText().toString();
            String measurements = radioBtnMeasurements.getText().toString();
            String pickupDate = tv_date.getText().toString();
            String pickupTime = tv_time.getText().toString();

            AddData(product_id, name, price, image, fabric_details, measurements, pickupDate, pickupTime);
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
