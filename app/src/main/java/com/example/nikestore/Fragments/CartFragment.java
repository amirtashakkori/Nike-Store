package com.example.nikestore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.nikestore.Adapters.CartAdapter;
import com.example.nikestore.ApiServices.ApiCommands.getCartApi;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.AddToCartResponse;
import com.example.nikestore.Model.CartResponse;
import com.example.nikestore.Model.MessageResponse;
import com.example.nikestore.Model.PaymentInfo;
import com.example.nikestore.PaymentActivity;
import com.example.nikestore.R;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.DecimalFormat;

import io.reactivex.disposables.Disposable;

public class CartFragment extends Fragment implements CartAdapter.changeItemCount {

    View view;

    getCartApi getCartApi;
    ApiService apiService;
    Disposable d;
    TokenContainer tokenContainer;
    CartAdapter adapter;

    PaymentInfo paymentInfo;

    LottieAnimationView lottie;
    ExtendedFloatingActionButton btn_login , btn_pay;
    LinearLayout animation_layout , cart_item_layout;

    TextView txt_total_price , txt_shiping_cost , txt_payable_price , txt_empty_state;
    RecyclerView rv_cart_product;


    public void cast(){
        txt_total_price = view.findViewById(R.id.txt_total_price);
        txt_shiping_cost = view.findViewById(R.id.txt_shiping_cost);
        txt_payable_price = view.findViewById(R.id.txt_payable_price);
        rv_cart_product = view.findViewById(R.id.rv_cart_product);
        animation_layout = view.findViewById(R.id.animation_layout);
        cart_item_layout = view.findViewById(R.id.cart_item_layout);
        txt_empty_state = view.findViewById(R.id.txt_empty_state);
        btn_login = view.findViewById(R.id.btn_login);
        lottie = view.findViewById(R.id.lottie);
        btn_pay = view.findViewById(R.id.btn_pay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart, container, false);
        cast();
        apiService = new ApiService(getContext());
        getCartApi = new getCartApi(apiService);
        tokenContainer = new TokenContainer(getContext());
        String token = "Bearer " + tokenContainer.getToken();
        String refresh_token = "Bearer " + tokenContainer.getRefreshToken();

        Authorize(token , refresh_token);

        if (!tokenContainer.getToken().equals("") && !tokenContainer.getRefreshToken().equals("")){
            getCartItems(apiService , token);
        }

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra("peyment" , paymentInfo);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    public void Authorize(String token , String refresh_token){
        if (token.equals("Bearer ") && refresh_token.equals("Bearer ")){
            cart_item_layout.setVisibility(View.GONE);
            animation_layout.setVisibility(View.VISIBLE);
            btn_pay.setVisibility(View.GONE);
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

    public void getCartItems(ApiService apiService , String token){
        getCartApi.getCartItem(token, new getCartApi.getCartItemApiCallBack() {
            @Override
            public void onSubscribe(Disposable disposable) {
                d = disposable;
            }

            @Override
            public void onSuccess(CartResponse cartResponse) {
                if (cartResponse.getCart_items().size()>0){
                    cart_item_layout.setVisibility(View.VISIBLE);
                    btn_pay.setVisibility(View.VISIBLE);
                    animation_layout.setVisibility(View.GONE);

                    DecimalFormat decimalFormat = new DecimalFormat("0,000");

                    String totalPrice = decimalFormat.format(cartResponse.getTotal_price()) + " تومان";
                    String shipingCost = decimalFormat.format(cartResponse.getShipping_cost()) + " تومان";
                    String payablePrice = decimalFormat.format(cartResponse.getPayable_price()) + " تومان";

                    paymentInfo = new PaymentInfo();
                    paymentInfo.setTotalPrice(totalPrice);
                    paymentInfo.setShipingCost(shipingCost);
                    paymentInfo.setPayablePrice(payablePrice);

                    txt_total_price.setText(totalPrice);
                    txt_shiping_cost.setText(shipingCost);
                    txt_payable_price.setText(payablePrice);

                    rv_cart_product.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
                    adapter = new CartAdapter(getContext(), cartResponse.getCart_items(), apiService, token , CartFragment.this);
                    rv_cart_product.setAdapter(adapter);

                }
                else {
                    btn_pay.setVisibility(View.GONE);
                    cart_item_layout.setVisibility(View.GONE);
                    animation_layout.setVisibility(View.VISIBLE);
                    lottie.setAnimation(R.raw.empty_state_anim);
                    lottie.playAnimation();
                    lottie.loop(true);
                    txt_empty_state.setText("محصولی در سبد خرید شما وجود ندارد!");
                    btn_login.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                Log.i("getCartItems", "onError: " + e);
            }
        });
    }


    public void RemoveItem(ApiService apiService , String token , int CartItemId){
        getCartApi.removeFromCart(token, CartItemId, new getCartApi.removeItemCallBack() {
            @Override
            public void onSuccess(MessageResponse messageResponse) {
                Toast.makeText(getContext(), "ssssssss", Toast.LENGTH_SHORT).show();
                getCartItems(apiService , token);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                Log.i("changeItemCount", "onError: " + e);
            }
        });
    }

    public void changeItemCount(ApiService apiService , String token , int CartItemId , int count){
        getCartApi.changeCartItemCount(token, CartItemId, count, new getCartApi.changeCartItemCountCallBack() {
            @Override
            public void onSubscribe(Disposable disposable) {
                disposable = d;
            }

            @Override
            public void onSuccess(AddToCartResponse addToCartResponse) {
                getCartItems(apiService , token);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                Log.i("changeItemCount", "onError: " + e);
            }
        });
    }

    @Override
    public void btnRemoveItemClicked(String token, int CartItemId) {
        RemoveItem(apiService , token , CartItemId);
    }

    @Override
    public void btnIncreaseClicked(String token , int CartItemId, int count) {
        changeItemCount(apiService , token , CartItemId , count);
    }

    @Override
    public void btnDecreaseClicked(String token , int CartItemId, int count) {
        changeItemCount(apiService , token , CartItemId , count);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (d!=null)
            d.dispose();
    }
}