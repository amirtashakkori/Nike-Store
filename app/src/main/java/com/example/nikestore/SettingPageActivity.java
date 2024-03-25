package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.nikestore.Adapters.ProductAdapter;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.DataBase.ProductDataBase;
import com.example.nikestore.DataBase.ProductDataBaseClass;
import com.example.nikestore.Model.OrderItem;
import com.example.nikestore.Model.OrderItemList;
import com.example.nikestore.Model.Product;
import com.example.nikestore.SharedPreferences.TokenContainer;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingPageActivity extends AppCompatActivity {

    RecyclerView listRv;

    List<Product> products;

    int page;

    public void cast(){
        listRv = findViewById(R.id.listRv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        cast();
        page = getIntent().getIntExtra("settingPage" , -1);

        if (page == 0){
            ProductDataBase dao = ProductDataBaseClass.getAppDataBase(SettingPageActivity.this).getDataBaseDao();
            products = dao.getProducts();
            listRv.setLayoutManager(new LinearLayoutManager(SettingPageActivity.this , RecyclerView.VERTICAL , false));
            listRv.setAdapter(new ProductAdapter(SettingPageActivity.this , products , ViewType.ROW));
        } else if (page == 1){
            ApiService apiService = new ApiService(SettingPageActivity.this);
        }
    }
}