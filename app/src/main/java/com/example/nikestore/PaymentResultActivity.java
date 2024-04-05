package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikestore.ApiServices.ApiCommands.checkout;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.CheckoutResponse;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

import io.reactivex.disposables.Disposable;

public class PaymentResultActivity extends AppCompatActivity {

    MaterialButton btn_back_to_home , btn_purchase_history;
    TextView price;
    checkout checkout;
    TokenContainer tokenContainer;
    Disposable d;

    public void cast(){
        btn_back_to_home = findViewById(R.id.btn_back_to_home);
        btn_purchase_history = findViewById(R.id.btn_purchase_history);
        price = findViewById(R.id.price);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);
        cast();
        tokenContainer = new TokenContainer(this);
        String token = "Bearer " + tokenContainer.getToken();

        Uri uri = getIntent().getData();
        if (uri != null){
            String order_ids = uri.getQueryParameter("order_id");
            int order_id = Integer.parseInt(order_ids);
            setCheckout(new ApiService(this) , token , order_id);
        }
        else {
            Bundle bundle = getIntent().getExtras();
            String priceS = bundle.getString("price");
            price.setText(priceS);
        }

        btn_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentResultActivity.this , MainActivity.class));
            }
        });

        btn_purchase_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentResultActivity.this , SettingPageActivity.class);
                intent.putExtra("settingPage" , 1);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this , MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (d != null)
            d.dispose();
    }

    public void setCheckout(ApiService apiService , String token , int order_id){
        checkout = new checkout(apiService);
        checkout.checkOut(token, order_id, new checkout.checkOutCallBack() {
            @Override
            public void onSubscribe(Disposable d) {
                d = d;
            }

            @Override
            public void onSuccess(CheckoutResponse checkoutResponse) {
                price.setText(new DecimalFormat("0,000").format(checkoutResponse.getPayable_price()) + " تومان ");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(PaymentResultActivity.this , "مشکلی حین بارگزاری پیش آماده است، دسترسی به اینترنت را چک کنید!" , Toast.LENGTH_SHORT).show();
            }
        });
    }
}