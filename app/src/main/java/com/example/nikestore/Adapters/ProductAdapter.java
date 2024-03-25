package com.example.nikestore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikestore.DataBase.ProductDataBase;
import com.example.nikestore.DataBase.ProductDataBaseClass;
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

    public ViewType viewType;

    public ProductAdapter(Context c, List<Product> productList , ViewType viewType) {
        this.c = c;
        this.productList = productList;
        this.viewType = viewType;
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
        ImageView productImg , addToFavoritesBtn;
        TextView productTitleTv , prevCostTv ,currentCostTv;
        public item(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.productImg);
            productTitleTv = itemView.findViewById(R.id.productTitleTv);
            prevCostTv = itemView.findViewById(R.id.prevCostTv);
            currentCostTv = itemView.findViewById(R.id.currentCostTv);
            addToFavoritesBtn = itemView.findViewById(R.id.addToFavoritesBtn);
        }

        public void bindProduct(Product product){
            Picasso.get().load(product.getImage()).into(productImg);
            productTitleTv.setText(product.getTitle());
            prevCostTv.setText(product.getPrevious_price());
            currentCostTv.setText(product.getPrice());

            if (product.isFavorite())
                addToFavoritesBtn.setImageResource(R.drawable.ic_favorite_full);
            else
                addToFavoritesBtn.setImageResource(R.drawable.ic_favorites);

            addToFavoritesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product.isFavorite()){
                        addToFavoritesBtn.setImageResource(R.drawable.ic_favorites);
                        product.setFavorite(true);
                        long id = dao.addToList(product);
                        if (id > -1)
                            Toast.makeText(c, "به لیست علاقمندی افزوده شد", Toast.LENGTH_SHORT).show();
                    } else {
                        addToFavoritesBtn.setImageResource(R.drawable.ic_favorite_full);
                        int delete = dao.delete(product);
                        if (delete > 0)
                            Toast.makeText(c, "از لیست علاقمندی ها خذف شد", Toast.LENGTH_SHORT).show();
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
