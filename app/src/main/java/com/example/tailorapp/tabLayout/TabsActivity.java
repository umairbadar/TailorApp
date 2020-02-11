package com.example.tailorapp.tabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tailorapp.MainActivity;
import com.example.tailorapp.R;
import com.example.tailorapp.cart.CartActivity;
import com.example.tailorapp.contants.Api;
import com.example.tailorapp.contants.AppController;
import com.example.tailorapp.database.DatabaseHelper;
import com.google.android.material.tabs.TabLayout;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public static String category_id, name;
    private ArrayList<String> cat_ids;

    private NotificationBadge badge;
    private int count;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        category_id = getIntent().getStringExtra("cat_id");
        name = getIntent().getStringExtra("name");

        Toolbar toolbar = findViewById(R.id.toolbar_mens);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        initViews();
    }

    private void initViews(){

        databaseHelper = new DatabaseHelper(this);

        Cursor data = databaseHelper.getCount();
        count = -1;
        while(data.moveToNext()){
            count = data.getInt(0);
        }

        cat_ids = new ArrayList<>();

        mViewPager = findViewById(R.id.mens_tabPager);
        mTabLayout = findViewById(R.id.mens_tabs);

        mViewPager.setOffscreenPageLimit(1);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tabs_menu, menu);

        View view = menu.findItem(R.id.cart).getActionView();
        badge = view.findViewById(R.id.badge);
        ImageView imageView = view.findViewById(R.id.cart_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cartIntent = new Intent(TabsActivity.this, CartActivity.class);
                cartIntent.putExtra("ParentClassName","tabLayout.TabsActivity");
                cartIntent.putExtra("cat_id",category_id);
                cartIntent.putExtra("name",name);
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

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject innerObj = jsonArray.getJSONObject(i);

                                    String cat_id = innerObj.getString("category_id");
                                    cat_ids.add(cat_id);

                                    mTabLayout.addTab(mTabLayout.newTab().setText(innerObj.getString("name")));
                                }

                                if (mTabLayout.getTabCount() == 3) {
                                    mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                                } else {
                                    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                                }

                                PagerAdapter menPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount(), cat_ids);
                                mViewPager.setAdapter(menPagerAdapter);
                                mViewPager.setCurrentItem(0);

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
                map.put("category_id", category_id);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(req);

    }
}
