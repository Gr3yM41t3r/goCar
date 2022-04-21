package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import retrofit2.http.Body;

public class SaveSharedPreference
{
    private static final String PREF_EMAIL= "email";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUsersEmail(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, userName);
        editor.apply();
    }

    public static Boolean isLogedIn(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_EMAIL,"").length()!=0;
    }
}
