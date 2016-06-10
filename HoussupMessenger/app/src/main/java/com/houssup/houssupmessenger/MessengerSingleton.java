package com.houssup.houssupmessenger;

/**
 * Created by SOMEX on 08-06-2016.
 */
public class MessengerSingleton {

    private static MessengerSingleton ourInstance = new MessengerSingleton();

    public static MessengerSingleton getInstance() {
        return ourInstance;
    }

    private MessengerSingleton() {
    }
    public static String userUID;
}
