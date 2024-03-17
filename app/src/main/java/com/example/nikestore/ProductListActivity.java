package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikestore.Adapters.ProductAdapter;
import com.example.nikestore.ApiServices.ApiCommands.getProductApi;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.Product;
import com.example.nikestore.R;
import com.example.nikestore.SharedPreferences.CachedDatas;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductListActivity extends AppCompatActivity {

    ImageView viewType_changer , btn_back ;
    RecyclerView rv_product;
    RelativeLayout btn_sort;
    ProductAdapter adapter;
    getProductApi getProductApi;
    GridLayoutManager gridLayoutManager;

    CachedDatas cachedDatas;

    TextView txt_sort;

    String sort;

    Disposable d;

    public void cast(){
        viewType_changer = findViewById(R.id.viewType_changer);
        rv_product = findViewById(R.id.rv_product);
        btn_sort = findViewById(R.id.btn_sort);
        btn_back = findViewById(R.id.btn_back);
        txt_sort = findViewById(R.id.txt_sort);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        cast();
        sort = getIntent().getStringExtra("sort");
        cachedDatas = new CachedDatas(this);

        if (sort.equals(Product.SORT_LATEST))
            txt_sort.setText(R.string.sortLatest);

        else
            txt_sort.setText(R.string.sortPopular);

        getProducts(new ApiService(this));

        viewType_changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null){
                    if (adapter.viewType == ViewType.GRIDE) {
                        adapter.setViewType(ViewType.ROW);
                        gridLayoutManager.setSpanCount(1);
                    }

                    else if (adapter.viewType == ViewType.ROW) {
                        adapter.setViewType(ViewType.LARGE);
                        gridLayoutManager.setSpanCount(1);
                    }

                    else {
                        adapter.setViewType(ViewType.GRIDE);
                        gridLayoutManager.setSpanCount(2);
                    }
                }
            }
        });

        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProductListActivity.this , v );
                popupMenu.getMenuInflater().inflate(R.menu.sort_items_menu , popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.sort_latest:
                                sort = Product.SORT_LATEST;
                                getProducts(new ApiService(ProductListActivity.this));
                                txt_sort.setText(R.string.sortLatest);
                                break;

                            case R.id.sort_popular:
                                sort = Product.SORT_POPULAR;
                                getProducts(new ApiService(ProductListActivity.this));
                                txt_sort.setText(R.string.sortPopular);
                                break;

                            case R.id.sort_desc:
                                sort = Product.SORT_PRICE_DESC;
                                getProducts(new ApiService(ProductListActivity.this));
                                txt_sort.setText(R.string.sortPriceHighToLow);
                                break;

                            case R.id.sort_asc:
                                sort = Product.SORT_PRICE_ASC;
                                getProducts(new ApiService(ProductListActivity.this));
                                txt_sort.setText(R.string.sortPriceLowToHigh);
                                break;
                        }
                        return false;
                    }
                });

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getProducts(ApiService apiService){
        getProductApi = new getProductApi(apiService);

        getProductApi.getProduct(sort, new getProductApi.getProductCallBack() {
            @Override
            public void onSubscribe(Disposable disposable) {
                d = disposable;
            }

            @Override
            public void onSuccess(List<Product> products) {
                gridLayoutManager = new GridLayoutManager(ProductListActivity.this , 2 , RecyclerView.VERTICAL , false);
                rv_product.setLayoutManager(gridLayoutManager);
                adapter = new ProductAdapter(ProductListActivity.this , products , cachedDatas);
                rv_product.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ProductListActivity.this, "Unknown Error!" + e, Toast.LENGTH_SHORT).show();
                Log.i("Activity", "onError: " + e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (d!= null)
            d.dispose();
    }
}