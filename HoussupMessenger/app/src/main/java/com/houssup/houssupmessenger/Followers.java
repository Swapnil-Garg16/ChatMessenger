package com.houssup.houssupmessenger;


import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.snapshot.StringNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Followers extends Fragment {
    protected RecyclerView.LayoutManager mLayoutManager;

    View v;
    private Firebase firebase;
    private List<followersClass> list;
    private RecyclerView recyclerView;
    private followerAdapter adapter;
    private String mId;
    public Followers() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_followers,container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.followerRecyclerView);



        mLayoutManager = new LinearLayoutManager(getActivity());
        list = new ArrayList<>();

        mId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        adapter = new followerAdapter(list, mId,getContext());
        recyclerView.setAdapter(adapter);

        /**
         * Firebase - Inicialize
         */
        //firebase persistence
        try {
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Firebase.setAndroidContext(v.getContext());
        firebase = new Firebase("https://houssup-messanger.firebaseio.com/houssup/chat/users/3qlJGytZdIOPMMXbZDvRVsjLRi33/follower");

        firebase.keepSynced(true);

        /**
         * Firebase - Receives message
         */
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try{
                        followersClass model = dataSnapshot.getValue(followersClass.class);

                        list.add(model);
                        recyclerView.scrollToPosition(list.size() - 1);
                        adapter.notifyItemInserted(list.size() - 1);
                    } catch (Exception ex) {
                        Log.e("FOLLOWER_ERROR", ex.getMessage());
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

   /* *//*on click listener for each follower*//*
        v = inflater.inflate(R.layout.fragment_followers,container, false);
        RecyclerView custom_follower = (RecyclerView) v.findViewById (R.id.followerRecyclerView);
        custom_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "follower is clicked", Toast.LENGTH_LONG).show();
            }
        });*/


        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}

