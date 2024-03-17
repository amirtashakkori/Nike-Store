package com.example.nikestore.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikestore.Adapters.BannerSliderAdapter;
import com.example.nikestore.Adapters.ProductAdapter;
import com.example.nikestore.ApiServices.ApiCommands.getBannerApi;
import com.example.nikestore.ApiServices.ApiCommands.getProductApi;
import com.example.nikestore.ApiServices.ApiCommands.search;
import com.example.nikestore.ApiServices.ApiService;
import com.example.nikestore.Model.Banner;
import com.example.nikestore.Model.Product;
import com.example.nikestore.ProductListActivity;
import com.example.nikestore.R;
import com.example.nikestore.SharedPreferences.CachedDatas;
import com.example.nikestore.ViewType;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class HomeFragment extends Fragment {

    View view;
    Disposable disposable1 , disposable2;
    TextView latestMoreBtn , popularMoreBtn;
    getProductApi getProductApi;
    getBannerApi getBannerApi;
    CachedDatas cachedDatas;
    com.example.nikestore.ApiServices.ApiCommands.search search;

    EditText searchEdt;
    LinearLayout main_page_layout;
    RecyclerView rv_search;

    //Slider
    BannerSliderAdapter adapter;
    ViewPager bannerVp;
    TabLayout bannerTl;
    Handler h;
    Runnable runnable;
    private int[] pagerIndex = {-1};

    //Latest Product
    RecyclerView latestProductsRv;
    ProductAdapter productAdapter;

    //Popular Product
    RecyclerView popularProductsRv;


    public void cast(){
        bannerVp = view.findViewById(R.id.bannerVp);
        bannerTl = view.findViewById(R.id.bannerTl);
        latestProductsRv = view.findViewById(R.id.latestProductsRv);
        popularProductsRv = view.findViewById(R.id.popularProductsRv);
        latestMoreBtn = view.findViewById(R.id.latestMoreBtn);
        popularMoreBtn = view.findViewById(R.id.popularMoreBtn);
        searchEdt = view.findViewById(R.id.searchEdt);
        main_page_layout = view.findViewById(R.id.main_page_layout);
        rv_search = view.findViewById(R.id.rv_search);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        cast();

        getValues(new ApiService(getActivity()));

        latestMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , ProductListActivity.class);
                intent.putExtra("sort" , Product.SORT_LATEST);
                startActivity(intent);
            }
        });

        popularMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , ProductListActivity.class);
                intent.putExtra("sort" , Product.SORT_POPULAR);
                startActivity(intent);
            }
        });

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 2){

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable1 != null)
            disposable1.dispose();
        if (disposable2 != null)
            disposable2.dispose();
    }

    public void getValues(ApiService apiService){
        getProductApi = new getProductApi(apiService);
        getBannerApi = new getBannerApi(apiService);
        search = new search(apiService);

        getProductApi.getProduct(Product.SORT_LATEST, new getProductApi.getProductCallBack() {
            @Override
            public void onSubscribe(Disposable disposable) {
                disposable1 = disposable;
            }

            @Override
            public void onSuccess(List<Product> products) {
                latestProductsRv.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));
                productAdapter = new ProductAdapter(getContext() , products , cachedDatas);
                latestProductsRv.setAdapter(productAdapter);
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Activity", "مشکلی در برقرار دستگاه با سرور به وجود آمده است!");
            }
        });

        getProductApi.getProduct(Product.SORT_POPULAR, new getProductApi.getProductCallBack() {
            @Override
            public void onSubscribe(Disposable disposable) {
                disposable1 = disposable;
            }

            @Override
            public void onSuccess(List<Product> products) {
                popularProductsRv.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));
                productAdapter = new ProductAdapter(getContext() , products , cachedDatas);
                popularProductsRv.setAdapter(productAdapter);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "Unknown Error!" + e, Toast.LENGTH_SHORT).show();
                Log.i("Activity", "onError: " + e);
            }
        });

        getBannerApi.getBanners(new getBannerApi.getBannerCallBack() {
            @Override
            public void onSubscribe(Disposable disposable) {
                disposable2 = disposable;
            }

            @Override
            public void onSuccess(List<Banner> banners) {
                adapter = new BannerSliderAdapter(getContext() , banners);
                bannerVp.setAdapter(adapter);
                bannerVp.setPageMargin(16);
                bannerTl.setRotationY(180);
                bannerTl.setupWithViewPager(bannerVp);

                h = new Handler();
                int delay = 10000; //1 second

                h.postDelayed(new Runnable() {
                    public void run() {
                        pagerIndex[0]++;
                        if (pagerIndex[0] >= adapter.getCount()) {
                            pagerIndex[0] = 0;
                        }
                        bannerVp.setCurrentItem(pagerIndex[0]);
                        runnable=this;
                        h.postDelayed(runnable, delay);
                    }
                }, delay);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "Unknown Error!" + e, Toast.LENGTH_SHORT).show();
                Log.i("SalamActivity", "onError: " + e);
            }
        });

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q = s.toString();
                if (q.length() > 2){
                    search.search(q, new search.getProductCallBack() {
                        @Override
                        public void onSubscribe(Disposable disposable) {

                        }

                        @Override
                        public void onSuccess(List<Product> products) {
                            main_page_layout.setVisibility(View.GONE);
                            rv_search.setVisibility(View.VISIBLE);
                            rv_search.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
                            productAdapter = new ProductAdapter(getContext() , products , cachedDatas);
                            productAdapter.setViewType(ViewType.LARGE);
                            latestProductsRv.setAdapter(productAdapter);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("Activity", "مشکلی در برقرار دستگاه با سرور به وجود آمده است!");
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean hasNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected())
            isConnected = true;
        return isConnected;
    }

}
