package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikestore.Adapters.CommentAdapter;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.DataBase.ProductDataBase;
import com.example.nikestore.DataBase.ProductDataBaseClass;
import com.example.nikestore.Model.AddCommentResponse;
import com.example.nikestore.Model.AddToCartResponse;
import com.example.nikestore.Model.Comment;
import com.example.nikestore.Model.Product;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

    ImageView productImg , backBtn , favoriteBtn;
    TextView productTitle , prevPriceTv , currentPriceTv , leaveCommentBtn;
    RecyclerView commentRv;
    MaterialButton commentsMoreBtn;
    ExtendedFloatingActionButton addToCartBtn;

    List<Comment> commentList;

    ApiService apiService;
    ProductDataBase dao;
    Disposable disposable , disposable1;
    Product product , searchedProduct;
    TokenContainer tokenContainer;

    Bundle bundle;

    public void cast(){
        productImg = findViewById(R.id.productImg);
        productTitle = findViewById(R.id.productTitle);
        prevPriceTv = findViewById(R.id.prevPriceTv);
        currentPriceTv = findViewById(R.id.currentPriceTv);
        commentRv = findViewById(R.id.commentRv);
        commentsMoreBtn = findViewById(R.id.commentsMoreBtn);
        backBtn = findViewById(R.id.backBtn);
        favoriteBtn = findViewById(R.id.favoriteBtn);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        leaveCommentBtn = findViewById(R.id.leaveCommentBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        cast();
        tokenContainer = new TokenContainer(this);
        apiService = new ApiService(this);
        dao = ProductDataBaseClass.getAppDataBase(this).getDataBaseDao();
        String token = tokenContainer.getToken();
        bundle = getIntent().getExtras();

        if (bundle!=null){
            product = bundle.getParcelable("product");
            Picasso.get().load(product.getImage()).into(productImg);
            productTitle.setText(product.getTitle());
            DecimalFormat decimalFormat = new DecimalFormat("0,000");
            prevPriceTv.setText(decimalFormat.format(product.getPrevious_price()) + " تومان");
            currentPriceTv.setText(decimalFormat.format(product.getPrice()) + " تومان");

            searchedProduct = dao.searchProduct(product.getTitle());
            if (searchedProduct != null)
                favoriteBtn.setImageResource(R.drawable.ic_favorite_full);
            else
                favoriteBtn.setImageResource(R.drawable.ic_favorites);
        }

        apiService.getComments(product).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Comment>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(List<Comment> comments) {
                commentList = comments;
                commentRv.setLayoutManager(new GridLayoutManager(DetailActivity.this , 1 , RecyclerView.HORIZONTAL , false));
                commentRv.setAdapter(new CommentAdapter(DetailActivity.this , comments));
                if (comments.size() > 3)
                    commentsMoreBtn.setVisibility(View.VISIBLE);

                else
                    commentsMoreBtn.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(DetailActivity.this, "مشکلی حین بارگزاری پیش آماده است، دسترسی به اینترنت را چک کنید!" , Toast.LENGTH_SHORT).show();
            }
        });

        commentsMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this , CommentActivity.class);
                intent.putExtra("product" , product);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
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

        leaveCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog commentDialog = new BottomSheetDialog(DetailActivity.this ,R.style.Bottomsheet_EBook_final);
                View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.dialog_add_comment , (LinearLayout)findViewById(R.id.parent) , false);

                EditText commentTitleEdt = view.findViewById(R.id.commentTitleEdt);
                EditText commentContentEdt = view.findViewById(R.id.commentContentEdt);
                MaterialButton submitBtn = view.findViewById(R.id.submitBtn);

                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        apiService.leaveComments("Bearer " + tokenContainer.getToken() , product , commentTitleEdt.getText().toString() , commentContentEdt.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<AddCommentResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                
                            }

                            @Override
                            public void onSuccess(AddCommentResponse commentResponse) {
                                if (!tokenContainer.getToken().isEmpty())
                                    Toast.makeText(DetailActivity.this, "" + getString(R.string.commentSubmitted), Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(DetailActivity.this, "" + getString(R.string.commentSubmittedError), Toast.LENGTH_SHORT).show();
                                commentDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(DetailActivity.this, "مشکلی حین بارگزاری پیش آماده است، دسترسی به اینترنت را چک کنید!" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                commentDialog.setContentView(view);
                commentDialog.show();
            }
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedProduct != null){
                    favoriteBtn.setImageResource(R.drawable.ic_favorites);
                    int res = dao.delete(searchedProduct);
                    if (res > 0)
                        Toast.makeText(DetailActivity.this, "محصول با موفقیت از لیست علاقمندی ها حذف شد!", Toast.LENGTH_SHORT).show();
                } else {
                    favoriteBtn.setImageResource(R.drawable.ic_favorite_full);
                    long id = dao.addToList(product);
                    if (id != -1)
                        Toast.makeText(DetailActivity.this, "محصول با موفقیت به لیست علاقمندی ها اضاقه شد!", Toast.LENGTH_SHORT).show();
                }
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