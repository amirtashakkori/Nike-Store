package com.example.nikestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nikestore.Adapters.CommentAdapter;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.AddCommentResponse;
import com.example.nikestore.Model.Comment;
import com.example.nikestore.Model.Product;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentActivity extends AppCompatActivity {

    ImageView btn_back;
    RecyclerView rv_comments;
    FloatingActionButton leaveCommentBtn;
    ApiService apiService;
    Bundle bundle;
    Product product;
    Disposable disposable;

    TokenContainer tokenContainer;

    public void cast(){
        rv_comments = findViewById(R.id.rv_comments);
        leaveCommentBtn = findViewById(R.id.leaveCommentBtn);
        btn_back = findViewById(R.id.btn_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        cast();
        tokenContainer = new TokenContainer(CommentActivity.this);
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

        leaveCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog commentDialog = new BottomSheetDialog(CommentActivity.this ,R.style.Bottomsheet_EBook_final);
                View view = LayoutInflater.from(CommentActivity.this).inflate(R.layout.dialog_add_comment , (LinearLayout)findViewById(R.id.parent) , false);

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
                                    Toast.makeText(CommentActivity.this, "" + getString(R.string.commentSubmitted), Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(CommentActivity.this, "" + getString(R.string.commentSubmittedError), Toast.LENGTH_SHORT).show();
                                commentDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(CommentActivity.this, "Submitting your comment failed!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(CommentActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                commentDialog.setContentView(view);
                commentDialog.show();
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