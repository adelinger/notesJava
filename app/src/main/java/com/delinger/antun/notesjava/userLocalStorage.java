package com.delinger.antun.notesjava;

import android.content.Context;
import android.content.SharedPreferences;

public class userLocalStorage {
    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public userLocalStorage(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(String username, String password, Boolean zapamtiMe) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("username", username);
        userLocalDatabaseEditor.putString("password", password);
        userLocalDatabaseEditor.putBoolean("zapamtiMe", zapamtiMe);
        userLocalDatabaseEditor.apply();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.apply();
    }
}
