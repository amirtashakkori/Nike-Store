package com.example.nikestore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.MainActivity;
import com.example.nikestore.Model.TokenResponse;
import com.example.nikestore.R;
import com.example.nikestore.SharedPreferences.TokenContainer;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends Fragment {

    ApiService apiService;
    Disposable disposable;
    TokenContainer tokenContainer;

    View view;
    Button btn_signup_page , btn_login;
    EditText edt_email , edt_password;

    public void cast(){
        btn_signup_page = view.findViewById(R.id.btn_signup_page);
        btn_login = view.findViewById(R.id.btn_login);
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        cast();
        apiService = new ApiService(getContext());
        tokenContainer = new TokenContainer(getContext());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_email.length() > 0 && edt_password.length() > 0){
                    apiService.login(edt_email.getText().toString() , edt_password.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<TokenResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onSuccess(TokenResponse tokenResponse) {
                            tokenContainer.seveTokens(tokenResponse.getAccess_token() , tokenResponse.getRefresh_token());
                            tokenContainer.saveEmail(edt_email.getText().toString());
                            Toast.makeText(getContext(), "عملیات ورود موفقیت آمیز بود!" , Toast.LENGTH_LONG).show();
                            getActivity().startActivity(new Intent(getActivity() , MainActivity.class));
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(), "مشکلی حین بارگزاری پیش آماده است، دسترسی به اینترنت را چک کنید!" , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_signup_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container , new SignUpFragment());
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}