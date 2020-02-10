package com.example.tailorapp.tabLayout.category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tailorapp.ProductActivity;
import com.example.tailorapp.R;
import com.example.tailorapp.tabLayout.TabsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.ViewHolder> {

    List<Model_Category> list;
    Context context;

    public Adapter_Category(List<Model_Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Model_Category item = list.get(position);
        holder.tv_name.setText(item.getName());
        holder.tv_price.setText(item.getPrice());

        Picasso
                .get()
                .load(item.getImage())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.img_cat);

        holder.productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ProductActivity.class);
                intent.putExtra("product_id", item.getId());
                intent.putExtra("product_name", item.getName());
                intent.putExtra("product_price", item.getPrice());
                intent.putExtra("amount", item.getAmount());
                intent.putExtra("product_image", item.getImage());
                intent.putExtra("cat_id", TabsActivity.category_id);
                intent.putExtra("name", TabsActivity.name);
                view.getContext().startActivity(intent);
                ((Activity)context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_cat;
        TextView tv_name, tv_price;
        LinearLayout productLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_cat = itemView.findViewById(R.id.img_cat);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            productLayout = itemView.findViewById(R.id.productLayout);
        }
    }
}
