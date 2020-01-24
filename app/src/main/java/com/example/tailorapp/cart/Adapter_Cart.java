package com.example.tailorapp.cart;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.R;
import com.example.tailorapp.database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.ViewHolder> {

    List<Model_Cart> list;
    Context context;
    DatabaseHelper databaseHelper;

    public Adapter_Cart(List<Model_Cart> list, Context context) {
        this.list = list;
        this.context = context;

        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Model_Cart item = list.get(position);

        holder.tv_name.setText(item.getName());
        holder.tv_price.setText(item.getPrice());
        holder.tv_fabric_details.setText(item.getFabric_details());
        holder.tv_measurement_details.setText(item.getMeasurements());
        holder.tv_pickup_date.setText(item.getPickupDate());
        holder.tv_pickup_time.setText(item.getPickupTime());

        Picasso
                .get()
                .load(item.getImage())
                .into(holder.img);

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deteleItem(item.getId(), position);

                if (list.size() == 0){
                    CartActivity.emptyCartLayout.setVisibility(View.VISIBLE);
                } else {
                    CartActivity.emptyCartLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void deteleItem(String id, int position){

        databaseHelper.deleteItem(id);
        list.remove(position);
        notifyDataSetChanged();
        Toast.makeText(context, "Item Deleted!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img, img_delete;
        TextView tv_name, tv_price, tv_fabric_details, tv_measurement_details, tv_pickup_date, tv_pickup_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            img_delete = itemView.findViewById(R.id.img_delete);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_fabric_details = itemView.findViewById(R.id.tv_fabric_details);
            tv_measurement_details = itemView.findViewById(R.id.tv_measurement_details);
            tv_pickup_date = itemView.findViewById(R.id.tv_pickup_date);
            tv_pickup_time = itemView.findViewById(R.id.tv_pickup_time);
        }
    }
}
