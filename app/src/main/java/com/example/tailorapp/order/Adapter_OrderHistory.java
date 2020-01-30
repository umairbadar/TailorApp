package com.example.tailorapp.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tailorapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_OrderHistory extends RecyclerView.Adapter<Adapter_OrderHistory.ViewHolder> {

    List<Model_OrderHistory> list;
    Context context;

    public Adapter_OrderHistory(List<Model_OrderHistory> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Model_OrderHistory item = list.get(position);

        holder.tv_id.setText("Order ID: " + item.getOrder_id());
        holder.tv_name.setText("Customers Name: " + item.getName());
        holder.tv_date.setText("Date: " + item.getDate());
        holder.tv_products.setText("Total Prodcuts: " + item.getProducts());
        holder.tv_total.setText("Total Price: " + item.getTotal());
        holder.tv_status.setText("Status: " + item.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_id, tv_name, tv_date, tv_products, tv_total, tv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.tv_id);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_products = itemView.findViewById(R.id.tv_products);
            tv_total = itemView.findViewById(R.id.tv_total);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
