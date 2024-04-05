package com.example.nikestore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikestore.Model.Order;
import com.example.nikestore.R;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.item> {

    Context c;
    List<Order> orders;

    public OrderAdapter(Context c, List<Order> orders) {
        this.c = c;
        this.orders = orders;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new item(LayoutInflater.from(c).inflate(R.layout.item_order , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull item item, int position) {
        item.bindOrder(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class item extends RecyclerView.ViewHolder{
        TextView nameTv , familyTv , priceTv , addressTv , dateTv , orderItemsTv;
        public item(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            familyTv = itemView.findViewById(R.id.familyTv);
            priceTv = itemView.findViewById(R.id.priceTv);
            addressTv = itemView.findViewById(R.id.addressTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            orderItemsTv = itemView.findViewById(R.id.orderItemsTv);
        }

        public void bindOrder(Order order){
            nameTv.setText(order.getFirst_name());
            familyTv.setText(order.getLast_name());

            DecimalFormat decimalFormat = new DecimalFormat("0,000");
            priceTv.setText(decimalFormat.format(order.getTotal()) + " تومان");

            addressTv.setText(order.getAddress());
            dateTv.setText(order.getDate());

            orderItemsTv.setText(order.getOrder_items().size() + " محصول خریداری شده");
        }

    }
}
