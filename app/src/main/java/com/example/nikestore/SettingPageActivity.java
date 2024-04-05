package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nikestore.Adapters.OrderAdapter;
import com.example.nikestore.Adapters.ProductAdapter;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.DataBase.ProductDataBase;
import com.example.nikestore.DataBase.ProductDataBaseClass;
import com.example.nikestore.Model.Order;
import com.example.nikestore.Model.Product;
import com.example.nikestore.SharedPreferences.TokenContainer;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingPageActivity extends AppCompatActivity {

    RecyclerView listRv;
    ImageView backBtn;
    LinearLayout illLayout;

    List<Product> products;
    ProductDataBase dao;
    ApiService apiService;
    TokenContainer container;

    Disposable d;

    int page;

    public void cast(){
        listRv = findViewById(R.id.listRv);
        backBtn = findViewById(R.id.backBtn);
        illLayout = findViewById(R.id.illLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        cast();
        container = new TokenContainer(this);
        page = getIntent().getIntExtra("settingPage" , -1);

        if (page == 0){
            getFavorites();
        } else if (page == 1) {
            getPurchaseHistory();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (page == 0){
            dao = ProductDataBaseClass.getAppDataBase(SettingPageActivity.this).getDataBaseDao();
            products = dao.getProducts();
            listRv.setLayoutManager(new LinearLayoutManager(SettingPageActivity.this , RecyclerView.VERTICAL , false));
            listRv.setAdapter(new ProductAdapter(SettingPageActivity.this , products , ViewType.ROW));
        }
    }

    public void getFavorites(){
        dao = ProductDataBaseClass.getAppDataBase(SettingPageActivity.this).getDataBaseDao();
        products = dao.getProducts();
        listRv.setLayoutManager(new LinearLayoutManager(SettingPageActivity.this , RecyclerView.VERTICAL , false));
        listRv.setAdapter(new ProductAdapter(SettingPageActivity.this , products , ViewType.ROW));

        if (products.size() > 0)
            illLayout.setVisibility(View.GONE);
        else
            illLayout.setVisibility(View.VISIBLE);
    }

    public void getPurchaseHistory(){
        String token = container.getToken();
        apiService = new ApiService(SettingPageActivity.this);
        apiService.getOrderList("Bearer " + token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Order>>() {
            @Override
            public void onSubscribe(Disposable d) {
                d = d;
            }

            @Override
            public void onSuccess(List<Order> orders) {
                listRv.setLayoutManager(new LinearLayoutManager(SettingPageActivity.this , RecyclerView.VERTICAL , false));
                listRv.setAdapter(new OrderAdapter(SettingPageActivity.this , orders));
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SettingPageActivity.this, "مشکلی حین بارگزاری پیش آماده است، دسترسی به اینترنت را چک کنید!" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (d != null)
            d.dispose();
    }
}