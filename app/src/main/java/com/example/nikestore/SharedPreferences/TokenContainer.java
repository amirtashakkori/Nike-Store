package com.example.nikestore.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class
TokenContainer {

    SharedPreferences sharedPreferencesToken;
    SharedPreferences sharedPreferencesEmail;

    public TokenContainer(Context context) {
        sharedPreferencesToken = context.getSharedPreferences("tokens" , Context.MODE_PRIVATE);
        sharedPreferencesEmail = context.getSharedPreferences("email" , Context.MODE_PRIVATE);
    }

    public void saveEmail(String email){
        SharedPreferences.Editor editor = sharedPreferencesEmail.edit();
        editor.putString("email" , email);
        editor.apply();
    }

    public void seveTokens(String token , String refresh_token){
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        editor.putString("token" , token);
        editor.putString("refresh_token" , refresh_token);
        editor.apply();
    }

    public String getToken(){
        return sharedPreferencesToken.getString("token" , "");
    }

    public String getRefreshToken(){
        return sharedPreferencesToken.getString("refresh_token" , "");
    }

    public String getEmail(){
        return sharedPreferencesEmail.getString("email" , "کاربر میهمان");
    }



}
