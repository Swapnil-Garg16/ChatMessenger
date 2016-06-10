package com.houssup.houssupmessenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

/*        Button getStarted = (Button)findViewById(R.id.get_started);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });*/
        String TAG ="app v ersio";

        SharedPreferences sharedPreferences = getSharedPreferences("VersionCode", 0);
        int savedVersionCode = sharedPreferences.getInt("VersionCode", 0);

        int appVershionCode = 0;

        try {
            appVershionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException nnfe) {
            Log.w(TAG, "$ Exception caz of appVershionCode : " + nnfe);
        }

        if(savedVersionCode == appVershionCode){
            {
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
                finish();
            }
        }else{
            Log.d(TAG, "$$ savedVersionCode != appVershionCode");

            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putInt("VersionCode", appVershionCode);
            sharedPreferencesEditor.commit();


        }

        Button getStarted= (Button) findViewById(R.id.get_started);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
