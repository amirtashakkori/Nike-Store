package com.example.nikestore.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.appbar.AppBarLayout;
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


    AppBarLayout cartToolbar;
    NestedScrollView nested;
    LinearLayout loadingAnimLayout , illLayout;
    LottieAnimationView loadingLottie;
    ImageView illImage;
    ExtendedFloatingActionButton payBtn;
    Button loginBtn;

    TextView totalPriceTv , shippingCostTv , payablePriceTv , emptyStateTv;
    RecyclerView cartProductRv;


    public void cast(){
        totalPriceTv = view.findViewById(R.id.totalPriceTv);
        shippingCostTv = view.findViewById(R.id.shippingCostTv);
        payablePriceTv = view.findViewById(R.id.payablePriceTv);
        cartProductRv = view.findViewById(R.id.cartProductRv);
        illLayout = view.findViewById(R.id.illLayout);
        emptyStateTv = view.findViewById(R.id.emptyStateTv);
        loginBtn = view.findViewById(R.id.loginBtn);
        illImage = view.findViewById(R.id.illImage);
        payBtn = view.findViewById(R.id.payBtn);
        loadingLottie = view.findViewById(R.id.loadingLottie);
        nested = view.findViewById(R.id.nested);
        loadingAnimLayout = view.findViewById(R.id.loadingAnimLayout);
        cartToolbar = view.findViewById(R.id.cartToolbar);
        illLayout = view.findViewById(R.id.illLayout);
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

        payBtn.setOnClickListener(new View.OnClickListener() {
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
            loadingAnimLayout.setVisibility(View.GONE);
            nested.setVisibility(View.GONE);
            illLayout.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
            cartToolbar.setVisibility(View.VISIBLE);

            illImage.setImageResource(R.drawable.ill_login);

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

    public void getCartItems(ApiService apiService , String token){
        getCartApi.getCartItem(token, new getCartApi.getCartItemApiCallBack() {
            @Override
            public void onSubscribe(Disposable disposable) {
                d = disposable;
            }

            @Override
            public void onSuccess(CartResponse cartResponse) {
                loadingAnimLayout.setVisibility(View.GONE);
                cartProductRv.setVisibility(View.VISIBLE);
                cartToolbar.setVisibility(View.VISIBLE);
                nested.setVisibility(View.VISIBLE);
                payBtn.setVisibility(View.VISIBLE);

                if (cartResponse.getCart_items().size()>0){
                    nested.setVisibility(View.VISIBLE);
                    payBtn.setVisibility(View.VISIBLE);
                    illLayout.setVisibility(View.GONE);

                    DecimalFormat decimalFormat = new DecimalFormat("0,000");

                    String totalPrice = decimalFormat.format(cartResponse.getTotal_price()) + " تومان";
                    String shipingCost = decimalFormat.format(cartResponse.getShipping_cost()) + " تومان";
                    String payablePrice = decimalFormat.format(cartResponse.getPayable_price()) + " تومان";

                    paymentInfo = new PaymentInfo();
                    paymentInfo.setTotalPrice(totalPrice);
                    paymentInfo.setShipingCost(shipingCost);
                    paymentInfo.setPayablePrice(payablePrice);

                    totalPriceTv.setText(totalPrice);
                    shippingCostTv.setText(shipingCost);
                    payablePriceTv.setText(payablePrice);

                    cartProductRv.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
                    adapter = new CartAdapter(getContext(), cartResponse.getCart_items(), apiService, token , CartFragment.this);
                    cartProductRv.setAdapter(adapter);

                }
                else {
                    nested.setVisibility(View.GONE);
                    payBtn.setVisibility(View.GONE);
                    illLayout.setVisibility(View.VISIBLE);
                    illImage.setImageResource(R.drawable.ill_cart_empty_state);
                    emptyStateTv.setText("محصولی در سبد خرید شما وجود ندارد!");
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