package com.houssup.houssupmessenger;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetDatabase extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private EditText metText;
    private ImageView mbtSent;
    private Firebase mFirebaseRef;

    private List<Chat> mChats;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private String mId;
    private String sender;

    /*image upload*/
    ImageView uploadFromGallery,uploadFromCamera;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323,GALLERY_REQUEST=22131;
    ImageView imageView;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    //forebase storage referecne for images
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_database);

        //firebase persistence

        Toolbar myToolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        sender=getIntent().getStringExtra("userId");

        metText = (EditText) findViewById(R.id.editTextMessage);
        mbtSent = (ImageView) findViewById(R.id.buttonSendMessage);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mChats = new ArrayList<>();

        mId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new ChatAdapter(mChats, mId);
        mRecyclerView.setAdapter(mAdapter);

        /**
         * Firebase - Inicialize
         */
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://testchat-264db.firebaseio.com/").child("chat");

        mFirebaseRef.keepSynced(true);

        //------------------------initializing view object------------------
        uploadFromCamera = (ImageView) findViewById(R.id.cameraAttachmentButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        //-----------------------------------------------------------------------
        cameraPhoto = new CameraPhoto(this);
        galleryPhoto = new GalleryPhoto(this);

        //creating reference for the firbase Storage bucket
        storageReference = storage.getReferenceFromUrl("gs://houssup-messanger.appspot.com");
        //------------------------------------------------
        /**
         * camera and gallery upload*/
        uploadFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                    cameraPhoto.addToGallery();

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"something went wronng in opeing camera "+ e, Toast.LENGTH_LONG).show();
                }

            }
        });




    /**
     * send button code*/
        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString();

                if (!message.isEmpty()) {
                    /**
                     * Firebase - Send message
                     */
                    mFirebaseRef.push().setValue(new Chat(message, mId,sender,"time","hasAttachment","attachmentLink"));
                }

                metText.setText("");
            }
        });


        /**
         * Firebase - Receives message
         */
        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{

                        Chat model = dataSnapshot.getValue(Chat.class);

                        mChats.add(model);
                        mRecyclerView.scrollToPosition(mChats.size() - 1);
                        mAdapter.notifyItemInserted(mChats.size() - 1);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    /**
     * Fetch result from camera and gallery*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                try {

                    Bitmap image = ImageLoader.init().from(cameraPhoto.getPhotoPath()).requestSize(200, 200).getBitmap();
                    //imageView.setImageBitmap(image);


                    Uri file = Uri.fromFile(new File(cameraPhoto.getPhotoPath()));
                    StorageReference riversRef = storageReference.child("images/"+file.getLastPathSegment());  //// TODO: 07-06-2016 set original path
                    UploadTask uploadTask = riversRef.putFile(file);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(),"fail to upload camera"+exception,Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(),"Success upload camera" + downloadUrl.toString(),Toast.LENGTH_LONG).show();
                        }
                    });



                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "something went wronng", Toast.LENGTH_LONG).show();
                }
            }

            if(requestCode == GALLERY_REQUEST)
            {
                try {
                    galleryPhoto.setPhotoUri(data.getData());
                    Bitmap image = ImageLoader.init().from(galleryPhoto.getPath()).requestSize(200, 200).getBitmap();
                   // imageView.setImageBitmap(image);
                    Uri file = Uri.fromFile(new File(galleryPhoto.getPath()));
                    StorageReference riversRef = storageReference.child("images/"+file.getLastPathSegment());  //// TODO: 07-06-2016 set original path
                    UploadTask uploadTask = riversRef.putFile(file);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(),"fail to upload gallery "+exception,Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Toast.makeText(getApplicationContext(),"Success upload gallery" + downloadUrl.toString(),Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "something went wronng in wrting", Toast.LENGTH_LONG).show();
                }
            }

        }

    }
}