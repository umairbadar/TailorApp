package com.example.tailorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tailorapp.cart.CartActivity;
import com.example.tailorapp.contants.Api;
import com.example.tailorapp.contants.AppController;
import com.example.tailorapp.database.DatabaseHelper;
import com.example.tailorapp.order.OrderHistoryActivity;
import com.example.tailorapp.tabLayout.TabsActivity;
import com.example.tailorapp.tabLayout.category.Model_Category;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Database
    DatabaseHelper databaseHelper;
    private int count;

    private ViewFlipper viewFlipper;

    private NotificationBadge badge;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ImageView img_men, img_women;
    private TextView tv_men, tv_women;
    private String menID, womenID, customID, id, name, image, price;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPre", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar_MainMenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Initializing Views
        initView();
    }

    private void initView(){

        databaseHelper = new DatabaseHelper(this);

        Cursor data = databaseHelper.getCount();
        count = -1;
        while(data.moveToNext()){
            count = data.getInt(0);
        }

        viewFlipper = findViewById(R.id.viewFlipper);

        int images[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3};

        for (int image : images){

            flipperImages(image);
        }

        tv_men = findViewById(R.id.tv_men);
        tv_women = findViewById(R.id.tv_women);

        img_men = findViewById(R.id.img_men);
        img_women = findViewById(R.id.img_women);

        getCategories();
        updateCartCount();
    }

    private void getCategories(){

        StringRequest req = new StringRequest(Request.Method.POST, Api.CategoriesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status){

                                JSONArray jsonArray = jsonObject.getJSONArray("categories");
                                JSONObject customObj = jsonArray.getJSONObject(0);
                                JSONObject menObj = jsonArray.getJSONObject(1);
                                JSONObject womenObj = jsonArray.getJSONObject(2);

                                customID = customObj.getString("category_id");
                                menID = menObj.getString("category_id");
                                womenID = womenObj.getString("category_id");

                                tv_men.setText(menObj.getString("name"));
                                tv_women.setText(womenObj.getString("name"));

                                Picasso
                                        .get()
                                        .load(menObj.getString("image"))
                                        .placeholder(R.drawable.placeholder_image)
                                        .fit().centerCrop()
                                        .into(img_men);

                                Picasso
                                        .get()
                                        .load(womenObj.getString("image"))
                                        .placeholder(R.drawable.placeholder_image)
                                        .fit().centerCrop()
                                        .into(img_women);

                                getCustomTailoringProduct();


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

    private void flipperImages(int image){

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        //animation
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        View view = menu.findItem(R.id.cart).getActionView();
        badge = view.findViewById(R.id.badge);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                cartIntent.putExtra("ParentClassName","MainActivity");
                startActivity(cartIntent);

            }
        });

        updateCartCount();
        return true;
    }

    private void updateCartCount(){

        if (badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                badge.setText(String.valueOf(count));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.logout:
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getApplicationContext(), "User Logged out",
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;

            case R.id.orderHistory:
                startActivity(new Intent(getApplicationContext(), OrderHistoryActivity.class));
                break;
        }

        return true;
    }

    private void getCustomTailoringProduct(){


        StringRequest req = new StringRequest(Request.Method.POST, Api.CategoryListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status){
                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                JSONObject innerObj = jsonArray.getJSONObject(0);
                                id = innerObj.getString("id");
                                name = innerObj.getString("name");
                                price = innerObj.getString("pirce");
                                image = innerObj.getString("thumb");
                                amount = innerObj.getInt("amount");

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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("category_id", customID);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(req);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_men_styles:
                Intent menIntent = new Intent(getApplicationContext(), TabsActivity.class);
                menIntent.putExtra("cat_id", menID);
                menIntent.putExtra("name", "Trending Men Styles");
                startActivity(menIntent);
                break;

            case R.id.btn_women_styles:
                Intent womenIntent = new Intent(getApplicationContext(), TabsActivity.class);
                womenIntent.putExtra("cat_id", womenID);
                womenIntent.putExtra("name", "Trending Women Styles");
                startActivity(womenIntent);
                break;


            case R.id.btn_custom_tailoring:
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("ParentActivityName", "Main");
                intent.putExtra("product_id", id);
                intent.putExtra("product_name", name);
                intent.putExtra("product_price", price);
                intent.putExtra("amount", amount);
                intent.putExtra("product_image", image);
                intent.putExtra("cat_id", customID);
                intent.putExtra("name", "");
                intent.putExtra("upload", "1");
                startActivity(intent);
                break;

                default:
                    break;
        }

    }
}
