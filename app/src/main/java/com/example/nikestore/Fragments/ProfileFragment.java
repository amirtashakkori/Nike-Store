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

    TextView emptyStateTv , emailTv;
    LinearLayout favoriteProductsBtn , ordersHistoryBtn , logOutBtn;
    LinearLayout cart_item_layout , illLayout;
    ImageView illImage;

    public void cast(){
        favoriteProductsBtn = view.findViewById(R.id.favoriteProductsBtn);
        ordersHistoryBtn = view.findViewById(R.id.ordersHistoryBtn);
        logOutBtn = view.findViewById(R.id.logOutBtn);
        cart_item_layout = view.findViewById(R.id.cart_item_layout);
        illLayout = view.findViewById(R.id.illLayout);
        illImage = view.findViewById(R.id.illImage);
        emptyStateTv = view.findViewById(R.id.emptyStateTv);
        loginBtn = view.findViewById(R.id.loginBtn);
        emailTv = view.findViewById(R.id.emailTv);
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

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenContainer.seveTokens("" , "");
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container , new LoginFragment());
                transaction.commit();
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
            cart_item_layout.setVisibility(View.GONE);
            illLayout.setVisibility(View.VISIBLE);
            emptyStateTv.setText(R.string.youShouldLogin);
            loginBtn.setVisibility(View.VISIBLE);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container , new LoginFragment());
                    transaction.commit();
                }
            });
        }
    }

}