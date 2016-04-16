package com.socialinfotech.socialchat.domain.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.Firebase;

public final class LibraryClass {
    public static String PREF = "package com.socialinfotech.socialchat.PREF";
    private static Firebase firebase;


    private LibraryClass(){}

    public static Firebase getFirebase(){
        if( firebase == null ){
            firebase = new Firebase("https://firebaseio.com");
        }

        return( firebase );
    }

    static public void saveSP(Context context, String key, String value ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    static public String getSP(Context context, String key ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key, "");
        return( token );
    }
}
