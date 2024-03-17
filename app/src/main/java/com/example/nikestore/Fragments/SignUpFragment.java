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
import com.example.nikestore.Model.MessageResponse;
import com.example.nikestore.R;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpFragment extends Fragment {

    View view;
    Button btn_login_page , btn_signup;
    EditText edt_email , edt_password;
    
    ApiService apiService;
    Disposable disposable;

    public void cast(){
        btn_login_page = view.findViewById(R.id.btn_login_page);
        btn_signup = view.findViewById(R.id.btn_signup);
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        cast();
        apiService = new ApiService(getContext());

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_email.length() > 0 && edt_password.length() > 0){
                    apiService.signUp(edt_email.getText().toString() , edt_password.getText().toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MessageResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onSuccess(MessageResponse messageResponse) {
                            Toast.makeText(getContext(), "ثبتنام موفقیت آمیز بود ، برای ادامه مشخصات را وارد بخش ورود کنید!" , Toast.LENGTH_LONG).show();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container , new LoginFragment());
                            transaction.commit();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                            Log.i("signUp", "onError: " + e);
                        }
                    });
                }
            }
        });

        btn_login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container , new LoginFragment());
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