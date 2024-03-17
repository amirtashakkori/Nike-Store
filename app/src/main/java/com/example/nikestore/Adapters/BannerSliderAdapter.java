package com.example.nikestore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nikestore.Model.Banner;
import com.example.nikestore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerSliderAdapter extends PagerAdapter {

    Context c;
    List<Banner> bannerList;

    public BannerSliderAdapter(Context c, List<Banner> bannerList) {
        this.c = c;
        this.bannerList = bannerList;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_slider , container , false );

        ImageView img_slider = view.findViewById(R.id.img_slider);
        Picasso.get().load(bannerList.get(position).getImage()).into(img_slider);

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
