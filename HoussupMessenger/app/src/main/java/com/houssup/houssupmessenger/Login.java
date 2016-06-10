package com.houssup.houssupmessenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.authentication.Constants;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    EditText emailEditText, passwordEditText;
    Button loginButton;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;





    private boolean isExpired(AuthData authdata)
    {

        return (System.currentTimeMillis()/1000) >= authdata.getExpires();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //gettting firebse authrization instance
        mAuth = FirebaseAuth.getInstance();

        //checking if user is already authenticated------------------------------------------------
     /*   Firebase f=  new Firebase("https://testchat-264db.firebaseio.com");


        if(!(f.getAuth()==null || isExpired(f.getAuth()) ) )
        {
            Intent t = new Intent(this,MessengerTabbed.class);
            startActivity(t);
            Toast.makeText(Login.this,"authorised",Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(Login.this,"not authoriesed",Toast.LENGTH_LONG).show();
        }

*/
        //-----------------------------------------------------------------------------------------

        //google Auth--------------------------------------------------------------
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //--------------------------------------------------------------------------



        //google Api client------------------------------------------------------------------------
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("google signin","failed");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //------------------------------------------------------------------------------------------

        //google button instantiation----------------------------------
        //googleSignin = (SignInButton) findViewById(R.id.googleButton);

        //---------------------------------------------------------------
        //when user click on the login button (step G1)
                /*googleSignin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, 9001);
                    }
                });*/
        //---------------------------------------------------------------------
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                   // Toast.makeText(Login.this,"already logged in",Toast.LENGTH_LONG).show();
                    Intent t = new Intent(Login.this,MessengerTabbed.class);
                    MessengerSingleton.userUID=user.getUid();
                    startActivity(t);
                    finish();

                    Log.d("firebase user", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("firebase user", "onAuthStateChanged:signed_out");

                }
                // ...

            }

        };

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //Email and password login Section--------------------------------------------------------------------------
        loginButton = (Button) findViewById(R.id.btn_sign_in);
        emailEditText =(EditText) findViewById(R.id.Username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.setEnabled(false);

                /**
                 * checking the credentials weather entered or not*/
                String email,password;
                email =emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                ShapeDrawable shape ;
                shape = new ShapeDrawable(new RectShape());
                shape.getPaint().setColor(Color.RED);
                shape.getPaint().setStyle(Paint.Style.STROKE);
                shape.getPaint().setStrokeWidth(3);

                if(email.length()==0 || password.length() ==0)
                {
                    passwordEditText.setBackground(shape);
                    emailEditText.setBackground(shape);
                    Toast.makeText(getApplicationContext(),"Please Enter Credentials",Toast.LENGTH_LONG).show();
                    loginButton.setEnabled(true);
                    return;

                }
                /**changing login button color*/
                loginButton.setBackgroundColor(Color.GRAY);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("mAuth SignIN", "signInWithEmail:onComplete:" + task.isSuccessful());
                                Toast.makeText(Login.this, "Authentication",
                                        Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w("mAuth SignIN", "signInWithEmail", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed." + mAuth.toString(),
                                            Toast.LENGTH_SHORT).show();
                                    loginButton.setEnabled(true);
                                    loginButton.setBackgroundColor(Color.rgb(233,178,0));
                                }
                            }
                        });
            }
        });
        //-----------------------------------------------------------------------------------------------------------------

                /*mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("firebase", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                Toast.makeText(Login.this, "created." +task.isSuccessful()+" "+ mAuth.toString(),
                                        Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });*/
    }
    //for google when the signing activity starts (Step G2)----------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    //--------------------------------------------------------------------------------------------->


    // [START handleSignInResult]

    //for google sign in (Step G3)-----------------------------------------------------------------
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("signin handle", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(Login.this, "Authentication" + acct.getDisplayName() + "  " + acct.getDisplayName(),
                    Toast.LENGTH_SHORT).show();
            Intent t = new Intent(this,MessengerTabbed.class);
            startActivity(t);

            /*mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);*/
        } else {
            // Signed out, show unauthenticated UI.
           /* updateUI(false);*/
        }
    }
    // [END handleSignInResult]
    //---------------------------------------------------------------------------------------------


    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        //when application starts start to check authentication
        mAuth.addAuthStateListener(mAuthListener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
     /*   // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.houssup.www.houssupchat/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/



    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
   /*     // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.houssup.www.houssupchat/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);*/

        //if the app is stopped stop listening
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


}
