package com.amir.redoneiv.Common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.amir.redoneiv.Activity.LoginActivity;

import java.util.HashMap;


public class PreferenceManagerLogin {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "RedOneIv";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String CALLER_ID = "CALLER_ID";
    public static final String TOKEN = "TOKEN";

    public PreferenceManagerLogin(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String caller_id, String token){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(CALLER_ID, caller_id);
        editor.putString(TOKEN, token);

        // commit changes
        editor.commit();
    }
    public boolean checkLogin(){
        if(!this.isLoggedIn()){

            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(CALLER_ID, pref.getString(CALLER_ID, null));
        user.put(TOKEN, pref.getString(TOKEN, null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}