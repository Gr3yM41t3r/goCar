package com.example.myapplication.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import retrofit2.http.Body;

public class SaveSharedPreference
{
    private static final String PREF_SESSION_ID= "session_id";
    private static final String EMAIL= "email";
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
        SharedPreferences secret_shared_prefs = EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                masterKeyAlias,
                ctx,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        return secret_shared_prefs;
    }

    public static void setSessionId(Context ctx, String sessionid,String email) throws GeneralSecurityException, IOException {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_SESSION_ID, sessionid);
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public static Boolean isLogedIn(Context ctx) throws GeneralSecurityException, IOException {
        return getSharedPreferences(ctx).getString(PREF_SESSION_ID,"").length()!=0;
    }

    public static String getEmail(Context ctx) throws GeneralSecurityException, IOException {
        String email = getSharedPreferences(ctx).getString("email","");
        String session_id = getSharedPreferences(ctx).getString("session_id","");
        Log.e("email",email.toString());
        Log.e("email",session_id.toString());
        return email;
    }

    public static String getSessionId(Context ctx) throws GeneralSecurityException, IOException {
        return  getSharedPreferences(ctx).getString("session_id","");

    }
}
