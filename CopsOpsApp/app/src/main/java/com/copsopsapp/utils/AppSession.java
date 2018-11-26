package com.copsopsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSession {

    private SharedPreferences prefs;

    private static AppSession session;

    public static AppSession getInstance(Context cntx) {
        if (session == null)
            session = new AppSession(cntx);
        return session;
    }

    public AppSession(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }


    public void setlogin(String login) {
        prefs.edit().putString("login", login).commit();
    }

    public String getlogin() {
        String login = prefs.getString("login", "");
        return login;
    }



    public void setusertype(String usertype) {
        prefs.edit().putString("usertype", usertype).commit();
    }

    public String getusertype() {
        String usertype = prefs.getString("usertype", "");
        return usertype;
    }




}