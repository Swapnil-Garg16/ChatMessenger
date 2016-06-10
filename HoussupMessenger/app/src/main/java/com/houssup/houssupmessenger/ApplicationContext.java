package com.houssup.houssupmessenger;

import com.firebase.client.Firebase;

/**
 * Created by SOMEX on 04-06-2016.
 */
public class ApplicationContext  extends android.app.Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
