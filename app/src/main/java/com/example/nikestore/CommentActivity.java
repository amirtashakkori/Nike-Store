package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nikestore.Adapters.CommentAdapter;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.Comment;
import com.example.nikestore.Model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentActivity extends AppCompatActivity {

    ImageView btn_back;
    RecyclerView rv_comments;
    FloatingActionButton btn_insert_comment;
    ApiService apiService;
    Bundle bundle;
    Product product;
    Disposable disposable;

    public void cast(){
        rv_comments = findViewById(R.id.rv_comments);
        btn_insert_comment = findViewById(R.id.btn_insert_comment);
        btn_back = findViewById(R.id.btn_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        cast();
        apiService = new ApiService(this);

        bundle = getIntent().getExtras();

        if (bundle!=null){
            product = bundle.getParcelable("product");
        }
        apiService.getComments(product).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Comment>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(List<Comment> comments) {
                rv_comments.setLayoutManager(new LinearLayoutManager(CommentActivity.this , RecyclerView.VERTICAL , false));
                rv_comments.setAdapter(new CommentAdapter(CommentActivity.this , comments , true));
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(CommentActivity.this, "Unknown Error!" + e, Toast.LENGTH_LONG).show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null)
            disposable.dispose();
    }
}