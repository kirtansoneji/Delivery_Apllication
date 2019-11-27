package com.example.greatfresh;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SharePreferenceClass {
    public static SharedPreferences sharedPreference = null;

    public static SharedPreferences.Editor editor = null;

    public static final String PREFS_KEY = "user";

    public static final String TOKEN = "token";


    public static void init(Context context) {


        if (sharedPreference == null){

            sharedPreference = (SharedPreferences) context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
            editor.apply();
        }
    }

    public static void putString(String key ,String token){
        editor.putString(key, token);
        editor.apply();
    }

    public static String getString(String key){
        String token = sharedPreference.getString(key, "");
        return token;
    }

    public static void putValue(String key, DataModel user){

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(key, json);
        editor.apply();
    }

    public static void clearSharedPreference(Context context) {
        editor.clear();
        //sharedPreference.edit().clear().apply();
        // editor.remove("");
        editor.apply();
    }

    public static DataModel getValue(String key) {
        Gson gson = new Gson();
        String json = sharedPreference.getString(key, "");
        DataModel obj = gson.fromJson(json, DataModel.class);
        return obj;
    }
}
