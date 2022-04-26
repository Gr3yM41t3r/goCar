package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.http.Body;

public class SaveSharedPreference
{
    private static final String PREF_EMAIL= "email";
    private static String masterKeyAlias;

    static {
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }


    public SaveSharedPreference() throws GeneralSecurityException, IOException {
    }


    static SharedPreferences getSharedPreferences(Context ctx) throws GeneralSecurityException, IOException {
        final SharedPreferences secret_shared_prefs = EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                masterKeyAlias,
                ctx,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        return secret_shared_prefs;
    }

    public static void setUsersEmail(Context ctx, String userName) throws GeneralSecurityException, IOException {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, userName);
        editor.apply();
    }

    public static Boolean isLogedIn(Context ctx) throws GeneralSecurityException, IOException {
        return getSharedPreferences(ctx).getString(PREF_EMAIL,"").length()!=0;
    }
}
