package com.example.nikestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.nikestore.Fragments.CartFragment;
import com.example.nikestore.Fragments.HomeFragment;
import com.example.nikestore.Fragments.LoginFragment;
import com.example.nikestore.Fragments.ProfileFragment;
import com.example.nikestore.SharedPreferences.TokenContainer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation_main;
    FrameLayout fragment_container;
    FragmentTransaction transaction;

    TokenContainer tokenContainer;
    //Tokens
    public static String token;
    public static String refresh_token;

    public void cast(){
        bottom_navigation_main = findViewById(R.id.bottom_navigation_main);
        fragment_container = findViewById(R.id.fragment_container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cast();
        tokenContainer = new TokenContainer(this);
        token = tokenContainer.getToken();
        refresh_token = tokenContainer.getRefreshToken();

        bottom_navigation_main.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.home:
                        transaction.replace(R.id.fragment_container , new HomeFragment());
                        break;

                    case R.id.cart:
                        if (tokenContainer == null){
                            transaction.replace(R.id.fragment_container , new LoginFragment());
                        }
                        else
                            transaction.replace(R.id.fragment_container , new CartFragment());
                        break;

                    case R.id.profile:
                        transaction.replace(R.id.fragment_container , new ProfileFragment());
                        break;
                }
                transaction.commit();
                return true;
            }
        });

    }
}