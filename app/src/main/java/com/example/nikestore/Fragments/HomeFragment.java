package com.example.nikestore.Fragments;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.ImageView;
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

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    View view;
    Disposable disposable1 , disposable2;
    TextView latestMoreBtn , popularMoreBtn , emptyStateTv;
    getProductApi getProductApi;
    getBannerApi getBannerApi;
    EditText searchEdt;
    LinearLayout mainLayout , searchLayout , illLayout;
    RecyclerView searchListRv;

    //Slider
    BannerSliderAdapter adapter;
    ViewPager bannerVp;
    TabLayout bannerTl;
    Handler h;
    Runnable runnable;
    private int[] pagerIndex = {-1};
    private boolean isBackButtonVisible = false;

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
        mainLayout = view.findViewById(R.id.mainLayout);
        searchLayout = view.findViewById(R.id.searchLayout);
        searchListRv = view.findViewById(R.id.searchListRv);
        illLayout = view.findViewById(R.id.illLayout);
        emptyStateTv = view.findViewById(R.id.emptyStateTv);
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

        searchEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchLayout.setVisibility(View.VISIBLE);
                    mainLayout.setVisibility(View.GONE);

                } else {
                    mainLayout.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.GONE);
                }
            }
        });

//        searchEdt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showBackButton();
//            }
//        });

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

        getProductApi.getProduct(Product.SORT_LATEST, new getProductApi.getProductCallBack() {
            @Override
            public void onSubscribe(Disposable disposable) {
                disposable1 = disposable;
            }

            @Override
            public void onSuccess(List<Product> products) {
                latestProductsRv.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));
                productAdapter = new ProductAdapter(getContext() , products , ViewType.GRIDE);
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
                productAdapter = new ProductAdapter(getContext() , products , ViewType.GRIDE);
                popularProductsRv.setAdapter(productAdapter);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), "مشکلی حین بارگزاری پیش آماده است، دسترسی به اینترنت را چک کنید!" , Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "مشکلی حین بارگزاری پیش آماده است، دسترسی به اینترنت را چک کنید!" , Toast.LENGTH_SHORT).show();
            }
        });

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                illLayout.setVisibility(View.VISIBLE);
                searchListRv.setVisibility(View.GONE);
                String q = s.toString();
                if (!q.equals("")){
                    apiService.search(q).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Product>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<Product> products) {
                            if (products.size() > 0){
                                illLayout.setVisibility(View.GONE);
                                emptyStateTv.setVisibility(View.GONE);
                                searchListRv.setVisibility(View.VISIBLE);
                                searchListRv.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
                                productAdapter = new ProductAdapter(getActivity() , products , ViewType.ROW);
                                searchListRv.setAdapter(productAdapter);
                            } else {
                                illLayout.setVisibility(View.VISIBLE);
                                searchListRv.setVisibility(View.GONE);
                                emptyStateTv.setVisibility(View.VISIBLE);
                                emptyStateTv.setText("محصولی با این عنوان موجود نیست");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                            Log.d("searchTAG", "onError: " + e);
                        }
                    });
                } else {
                    illLayout.setVisibility(View.VISIBLE);
                    emptyStateTv.setText("عنوان مورد نظر را وارد کنید");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void onResume() {
        super.onResume();
        // Add back button press event handling
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (searchEdt.hasFocus()) {
                    searchEdt.clearFocus();
                    searchEdt.setText("");
                } else {
                    // Handle other back button press actions if needed
                    requireActivity().onBackPressed();
                }
            }
        });
    }

}
