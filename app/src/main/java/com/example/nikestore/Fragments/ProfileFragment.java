package com.example.nikestore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.nikestore.R;
import com.example.nikestore.SettingPageActivity;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    View view;
    TokenContainer tokenContainer;
    String token , refresh_token;
    Button loginBtn;

    TextView emailTv , logTv;
    ImageView lockImg;
    LinearLayout favoriteProductsBtn , ordersHistoryBtn , logBtn ;
    LinearLayout cart_item_layout;

    public void cast(){
        favoriteProductsBtn = view.findViewById(R.id.favoriteProductsBtn);
        ordersHistoryBtn = view.findViewById(R.id.ordersHistoryBtn);
        logBtn = view.findViewById(R.id.logBtn);
        cart_item_layout = view.findViewById(R.id.cart_item_layout);
        lockImg = view.findViewById(R.id.lockImg);
        loginBtn = view.findViewById(R.id.loginBtn);
        emailTv = view.findViewById(R.id.emailTv);
        logTv = view.findViewById(R.id.logTv);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        cast();
        tokenContainer = new TokenContainer(getContext());
        emailTv.setText(tokenContainer.getEmail());
        token = "Bearer " + tokenContainer.getToken();
        refresh_token = "Bearer " + tokenContainer.getRefreshToken();

        if (tokenContainer.getToken().equals("") && tokenContainer.getRefreshToken().equals("")){
            Authorize(token , refresh_token);
        }

        ordersHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tokenContainer.getToken().equals("") && tokenContainer.getRefreshToken().equals("")){
                    Toast.makeText(getActivity(), "" + R.string.youShouldLogin, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity() , SettingPageActivity.class);
                    intent.putExtra("settingPage" , 1);
                    startActivity(intent);
                }
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tokenContainer.getToken().equals("") && tokenContainer.getRefreshToken().equals("")){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container , new LoginFragment());
                    transaction.commit();
                } else {
                    tokenContainer.seveTokens("" , "");
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container , new LoginFragment());
                    transaction.commit();
                }
            }
        });

        favoriteProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , SettingPageActivity.class);
                intent.putExtra("settingPage" , 0);
                startActivity(intent);
            }
        });

        return view;
    }

    public void Authorize(String token , String refresh_token){
        if (token.equals("Bearer ") && refresh_token.equals("Bearer ")){
            emailTv.setText(R.string.guestText);
            lockImg.setVisibility(View.VISIBLE);
            logTv.setText(R.string.login);
         }
    }

}