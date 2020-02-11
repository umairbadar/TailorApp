package com.example.tailorapp.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tailorapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        String[] date = item.getDate().split("/");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        int monthnum = Integer.parseInt(date[0]);
        cal.set(Calendar.MONTH, monthnum);
        String month_name = month_date.format(cal.getTime());

        holder.tv_id.setText("Order: #" + item.getOrder_id());
        holder.tv_name.setText(item.getName());
        holder.tv_month.setText(month_name.toUpperCase());
        holder.tv_day.setText(date[1]);
        holder.tv_year.setText(date[2]);
        holder.tv_products.setText("Total Prodcuts: " + item.getProducts());
        holder.tv_total.setText("Total:\n" + item.getTotal());
        holder.tv_status.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_id, tv_name, tv_day, tv_products, tv_total, tv_status, tv_month, tv_year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.tv_id);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_month = itemView.findViewById(R.id.tv_month);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_products = itemView.findViewById(R.id.tv_products);
            tv_total = itemView.findViewById(R.id.tv_total);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
