package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikestore.ApiServices.ApiCommands.submitOrder;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.BilingResponse;
import com.example.nikestore.Model.PaymentInfo;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.disposables.Disposable;

public class PaymentActivity extends AppCompatActivity {

    TextInputEditText edt_name , edt_family , edt_postal_code , edt_phone , edt_address;
    TextView txt_total_price , txt_shiping_cost , txt_payable_price;
    MaterialButton btn_cash_on_delivery , btn_online_payment;
    ImageView btn_back;

    submitOrder submitOrder;
    PaymentInfo paymentInfo;
    TokenContainer tokenContainer;
    String token;

    public static final String PAYMENT_METHOD_COD = "cash_on_delivery";
    public static final String PAYMENT_METHOD_ONLINE = "online";

    public void cast(){
        edt_name = findViewById(R.id.edt_name);
        edt_family = findViewById(R.id.edt_family);
        edt_postal_code = findViewById(R.id.edt_postal_code);
        edt_phone = findViewById(R.id.edt_phone);
        edt_address = findViewById(R.id.edt_address);
        txt_total_price = findViewById(R.id.txt_total_price);
        txt_shiping_cost = findViewById(R.id.txt_shiping_cost);
        txt_payable_price = findViewById(R.id.txt_payable_price);
        btn_cash_on_delivery = findViewById(R.id.btn_cash_on_delivery);
        btn_online_payment = findViewById(R.id.btn_online_payment);
        btn_back = findViewById(R.id.btn_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cast();

        tokenContainer = new TokenContainer(this);
        token = "Bearer " + tokenContainer.getToken();

        getPaymentInfo();

        btn_online_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder(new ApiService(PaymentActivity.this) , PAYMENT_METHOD_ONLINE);
            }
        });

        btn_cash_on_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder(new ApiService(PaymentActivity.this) , PAYMENT_METHOD_COD);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getPaymentInfo(){
        Bundle bundle = getIntent().getExtras();
        paymentInfo = bundle.getParcelable("peyment");

        txt_total_price.setText(paymentInfo.getTotalPrice());
        txt_shiping_cost.setText(paymentInfo.getShipingCost());
        txt_payable_price.setText(paymentInfo.getPayablePrice());

    }

    public void submitOrder(ApiService apiService , String paymentMethod){
        submitOrder = new submitOrder(apiService);

        String name = edt_name.getText().toString();
        String family = edt_family.getText().toString();
        String postalCode = edt_postal_code.getText().toString();
        String mobile = edt_phone.getText().toString();
        String address = edt_address.getText().toString();

        if (name.length() > 0 && family.length() > 0 && postalCode.length() > 0 && mobile.length() > 0 && address.length() > 0 ){
            submitOrder.submitOrder(token, name, family, postalCode, mobile, address, paymentMethod, new submitOrder.submitOrderCalLBack() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onSuccess(BilingResponse bilingResponse) {
                    if (paymentMethod.equals("online")){
                        Toast.makeText(PaymentActivity.this, "هم اکنون به درگاه بانکی هدایت خواهید شد", Toast.LENGTH_SHORT).show();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW , Uri.parse(bilingResponse.getBank_gateway_url()));
                        startActivity(browserIntent);

                    }

                    else {
                        Bundle bundle = getIntent().getExtras();
                        paymentInfo = bundle.getParcelable("peyment");

                        Toast.makeText(PaymentActivity.this, "سفارش با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PaymentActivity.this , PaymentResultActivity.class);
                        intent.putExtra("price" , paymentInfo.getPayablePrice() );
                        startActivity(intent);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    Toast.makeText(PaymentActivity.this, "مشکلی حین بارگزاری پیش آماده است، دسترسی به اینترنت را چک کنید!" , Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "لطفا اطلاعات خواسته شده را به صورت کامل وارد کنید.", Toast.LENGTH_SHORT).show();
        }
    }

}




















