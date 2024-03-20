package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.nikestore.Adapters.ProductAdapter;
import com.example.nikestore.Model.Product;

import java.util.List;

public class SettingPageActivity extends AppCompatActivity {

    RecyclerView rv;

    List<Product> products;


    int page;

    public void cast(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        cast();
        page = getIntent().getIntExtra("settingPage" , -1);

        if (page == 0){
            rv.setLayoutManager(new LinearLayoutManager(SettingPageActivity.this , RecyclerView.VERTICAL , false));
            rv.setAdapter(new ProductAdapter(SettingPageActivity.this , products));
        }
    }
}