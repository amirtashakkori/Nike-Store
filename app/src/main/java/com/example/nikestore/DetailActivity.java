package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikestore.Adapters.CommentAdapter;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.AddToCartResponse;
import com.example.nikestore.Model.Comment;
import com.example.nikestore.Model.Product;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    ImageView img_product , btn_back;
    TextView txt_product_name , txt_product_previous_price , txt_product_current_price , txt_product_title;
    RecyclerView rv_comment;
    MaterialButton btn_view_all_comments;
    ExtendedFloatingActionButton btn_add_to_cart;

    List<Comment> commentList;

    ApiService apiService;
    Disposable disposable , disposable1;
    Product product;
    TokenContainer tokenContainer;

    Bundle bundle;

    public void cast(){
        img_product = findViewById(R.id.img_product);
        txt_product_name = findViewById(R.id.txt_product_name);
        txt_product_previous_price = findViewById(R.id.txt_product_previous_price);
        txt_product_current_price = findViewById(R.id.txt_product_current_price);
        txt_product_title = findViewById(R.id.txt_product_title);
        rv_comment = findViewById(R.id.rv_comment);
        btn_view_all_comments = findViewById(R.id.btn_view_all_comments);
        btn_back = findViewById(R.id.btn_back);
        btn_add_to_cart = findViewById(R.id.btn_add);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cast();
        tokenContainer = new TokenContainer(this);
        apiService = new ApiService(this);
        String token = tokenContainer.getToken();
        bundle = getIntent().getExtras();

        if (bundle!=null){
            product = bundle.getParcelable("product");
            Picasso.get().load(product.getImage()).into(img_product);
            txt_product_name.setText(product.getTitle());
            txt_product_title.setText(product.getTitle());
            DecimalFormat decimalFormat = new DecimalFormat("0,000");
            txt_product_previous_price.setText(decimalFormat.format(product.getPrevious_price()) + " تومان");
            txt_product_current_price.setText(decimalFormat.format(product.getPrice()) + " تومان");
        }

        apiService.getComments(product).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Comment>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(List<Comment> comments) {
                commentList = comments;
                rv_comment.setLayoutManager(new GridLayoutManager(DetailActivity.this , 1 , RecyclerView.HORIZONTAL , false));
                rv_comment.setAdapter(new CommentAdapter(DetailActivity.this , comments));
                if (comments.size() > 3)
                    btn_view_all_comments.setVisibility(View.VISIBLE);

                else
                    btn_view_all_comments.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(DetailActivity.this, "Unknown Error!" + e, Toast.LENGTH_LONG).show();
                Log.i("Detail", "onError: " + e);
            }
        });

        btn_view_all_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this , CommentActivity.class);
                intent.putExtra("product" , product);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.addToCart("Bearer " + tokenContainer.getToken() , product.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<AddToCartResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable1 = d;
                    }

                    @Override
                    public void onSuccess(AddToCartResponse addToCartResponse) {
                        Toast.makeText(DetailActivity.this, "با موفقیت اضافه شد", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(DetailActivity.this, "ابتدا وارد حساب کاربری خود شوید", Toast.LENGTH_SHORT).show();
                        Log.i("addToCart", "onError: " + e);
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null)
            disposable.dispose();

        if (disposable1!=null)
            disposable1.dispose();
    }
}