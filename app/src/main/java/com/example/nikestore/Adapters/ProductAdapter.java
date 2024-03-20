package com.example.nikestore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikestore.DetailActivity;
import com.example.nikestore.Model.Product;
import com.example.nikestore.R;
import com.example.nikestore.SharedPreferences.CachedDatas;
import com.example.nikestore.ViewType;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.item> {

    Context c;
    List<Product> productList;
    ProductDataBase dao;

    CachedDatas cachedDatas;

    public ViewType viewType = ViewType.GRIDE;

    public ProductAdapter(Context c, List<Product> productList , CachedDatas cachedDatas) {
        this.c = c;
        this.productList = productList;
        this.cachedDatas = cachedDatas;
    }

    public ProductAdapter(Context c, List<Product> productList) {
        this.c = c;
        this.productList = productList;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = view = LayoutInflater.from(c).inflate(R.layout.item_gride_product , viewGroup , false);;
        if (viewType == ViewType.ROW.getValue())
            view = LayoutInflater.from(c).inflate(R.layout.item_row_product , viewGroup , false);

        if (viewType == ViewType.LARGE.getValue())
            view = LayoutInflater.from(c).inflate(R.layout.item_large_product , viewGroup , false);

        if (viewType == ViewType.GRIDE.getValue())
            view = LayoutInflater.from(c).inflate(R.layout.item_gride_product , viewGroup , false);

        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item item, int position) {
        item.bindProduct(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class item extends RecyclerView.ViewHolder{
        ImageView img_product , btn_add_favorite;
        TextView txt_product_name , txt_product_previous_price ,txt_product_current_price;
        public item(@NonNull View itemView) {
            super(itemView);
            img_product = itemView.findViewById(R.id.img_product);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_product_previous_price = itemView.findViewById(R.id.txt_product_previous_price);
            txt_product_current_price = itemView.findViewById(R.id.txt_product_current_price);
            btn_add_favorite = itemView.findViewById(R.id.btn_add_favorite);
        }

        public void bindProduct(Product product){
            Picasso.get().load(product.getImage()).into(img_product);
            txt_product_name.setText(product.getTitle());
            DecimalFormat decimalFormat = new DecimalFormat("0,000");
            txt_product_previous_price.setText(decimalFormat.format(product.getPrevious_price())+" تومان");
            txt_product_current_price.setText(decimalFormat.format(product.getPrice())+" تومان");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c , DetailActivity.class);
                    intent.putExtra("product" , product);
                    c.startActivity(intent);
                }
            });


            dao = ProductDataBaseClass.getAppDataBase(c).getDataBaseDao();
            btn_add_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product.isFavorite()==false){
                        btn_add_favorite.setImageResource(R.drawable.ic_favorite_full);
                        product.setFavorite(true);

                        dao.addToList(product);
                    }
                    else {
                        btn_add_favorite.setImageResource(R.drawable.ic_favorites);
                        product.setFavorite(false);
                        dao.delete(product);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return viewType.getValue();
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

}
