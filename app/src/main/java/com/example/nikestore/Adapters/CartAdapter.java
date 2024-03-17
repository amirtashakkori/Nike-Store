package com.example.nikestore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.CartItem;
import com.example.nikestore.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.item> {

    Context c;
    List<CartItem> cartItemList;
    ApiService apiService;
    String token;
    changeItemCount callBack;
    int count;

    public CartAdapter(Context c, List<CartItem> cartItemList, ApiService apiService, String token, changeItemCount callBack) {
        this.c = c;
        this.cartItemList = cartItemList;
        this.apiService = apiService;
        this.token = token;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new item(LayoutInflater.from(c).inflate(R.layout.item_cart , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull item item, int position) {
        item.bindCartItem(cartItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class item extends RecyclerView.ViewHolder{
        ImageView img_product , btn_increase_count , btn_decrease_count;
        TextView txt_product_name , btn_remove_from_cart , txt_current_price , txt_item_count;
        public item(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            btn_increase_count = itemView.findViewById(R.id.btn_increase_count);
            btn_decrease_count = itemView.findViewById(R.id.btn_decrease_count);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            btn_remove_from_cart = itemView.findViewById(R.id.btn_remove_from_cart);
            txt_current_price = itemView.findViewById(R.id.txt_current_price);
            txt_item_count = itemView.findViewById(R.id.txt_item_count);
        }

        public void bindCartItem(CartItem cartItem){
            Picasso.get().load(cartItem.product.getImage()).into(img_product);
            txt_product_name.setText(cartItem.product.getTitle());
            txt_current_price.setText(new DecimalFormat("0,000").format(cartItem.product.getPrice()) + " تومان");
            txt_item_count.setText(cartItem.getCount() + "");

            btn_increase_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count = cartItem.getCount();
                    if (count < 5)
                        count = count + 1;
                    else{
                        Toast.makeText(c, "حداکثر تعداد 5 عدد است!" , Toast.LENGTH_SHORT).show();
                        count = 5;
                    }
                    txt_item_count.setText(count + "");
                    callBack.btnIncreaseClicked(token , cartItem.getCart_item_id() , count);
                }
            });

            btn_decrease_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count = cartItem.getCount();
                    if (count > 1)
                        count = count - 1;
                    else{
                        Toast.makeText(c, "حداقل تعداد 1 عدد است!" , Toast.LENGTH_SHORT).show();
                        count = 1;
                    }
                    txt_item_count.setText(count + "");
                    callBack.btnDecreaseClicked(token , cartItem.getCart_item_id() , count);
                }
            });

            btn_remove_from_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.btnRemoveItemClicked(token , cartItem.getCart_item_id());
                }
            });
        }
    }

    public interface changeItemCount{
        void btnRemoveItemClicked(String token , int CartItemId);
        void btnIncreaseClicked(String token , int CartItemId , int count);
        void btnDecreaseClicked(String token , int CartItemId , int count);
    }

}


