package com.example.nikestore.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.nikestore.R;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    View view;
    TokenContainer tokenContainer;
    String token , refresh_token;
    MaterialButton btn_login;

    TextView txt_empty_state , txt_email;
    RelativeLayout btn_favorite_List , btn_purchase_history , btn_logOut;
    LinearLayout cart_item_layout , animation_layout;
    LottieAnimationView lottie;

    public void cast(){
        btn_favorite_List = view.findViewById(R.id.btn_favorite_List);
        btn_purchase_history = view.findViewById(R.id.btn_purchase_history);
        btn_logOut = view.findViewById(R.id.btn_logOut);
        cart_item_layout = view.findViewById(R.id.cart_item_layout);
        animation_layout = view.findViewById(R.id.animation_layout);
        lottie = view.findViewById(R.id.lottie);
        txt_empty_state = view.findViewById(R.id.txt_empty_state);
        btn_login = view.findViewById(R.id.btn_login);
        txt_email = view.findViewById(R.id.txt_email);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        cast();
        tokenContainer = new TokenContainer(getContext());
        txt_email.setText(tokenContainer.getEmail());
        token = "Bearer " + tokenContainer.getToken();
        refresh_token = "Bearer " + tokenContainer.getRefreshToken();

        if (tokenContainer.getToken().equals("") && tokenContainer.getRefreshToken().equals("")){
            Authorize(token , refresh_token);
        }

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenContainer.seveTokens("" , "");
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container , new LoginFragment());
                transaction.commit();
            }
        });

        return view;
    }

    public void Authorize(String token , String refresh_token){
        if (token.equals("Bearer ") && refresh_token.equals("Bearer ")){
            cart_item_layout.setVisibility(View.GONE);
            animation_layout.setVisibility(View.VISIBLE);
            lottie.setAnimation(R.raw.login_anim);
            lottie.playAnimation();
            lottie.loop(true);
            txt_empty_state.setText(R.string.youShouldLogin);
            btn_login.setVisibility(View.VISIBLE);
            btn_login.setOnClickListener(new View.OnClickListener() {
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