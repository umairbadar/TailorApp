package com.example.tailorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.tailorapp.Mens.MensActivity;
import com.example.tailorapp.Womens.WomensActivity;
import com.nex3z.notificationbadge.NotificationBadge;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewFlipper viewFlipper;

    private NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_MainMenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Initializing Views
        initView();
    }

    private void initView(){

        viewFlipper = findViewById(R.id.viewFlipper);

        int images[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3};

        for (int image : images){

            flipperImages(image);
        }
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
        ImageView imageView = view.findViewById(R.id.cart_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Cart", Toast.LENGTH_LONG).show();
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
                badge.setText("0");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.logout:
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_men_styles:
                startActivity(new Intent(getApplicationContext(), MensActivity.class));
                break;

            case R.id.btn_women_styles:
                startActivity(new Intent(getApplicationContext(), WomensActivity.class));
                break;

                default:
                    break;
        }

    }
}
