package com.iceoton.canomcake.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    SharedPreferences sharedPref;

    public AppPreference(Context context) {
        sharedPref = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
    }

    public void saveLoginStatus(boolean isLoggedIn){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("loginStatus", isLoggedIn);
        editor.apply();
    }

    public boolean getLoginStatus(){
        return  sharedPref.getBoolean("loginStatus", false);
    }

    public void saveUserId(String id){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_id", id);
        editor.apply();
    }

    public String getUserId(){
        return  sharedPref.getString("user_id", null);
    }

    public void saveUserName(String name) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_name", name);
        editor.apply();
    }

    public String getUserName() {
        return sharedPref.getString("user_name", "");
    }

    public void saveAppLanguage(String language) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("appLanguage", language);
        editor.apply();
    }

    public String getAppLanguage() {
        return sharedPref.getString("appLanguage", "en");
    }

    public void saveApiUrl(String url) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("api_url", url);
        editor.apply();
    }

    public String getApiUrl() {
        return sharedPref.getString("api_url", "");
    }

}
