package com.houssup.houssupmessenger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
/**
 * Created by SOMEX on 04-06-2016.
 */
public class followerAdapter extends RecyclerView.Adapter<followerAdapter.ViewHolder>{
    private List<followersClass> mDataSet;
    private String mId;
    private Context context;


    private static final int CHAT_RIGHT = 2;
    private static final int CHAT_LEFT = 2;

    /**
     * Inner Class for a recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,designation;


        public ViewHolder(View v) {
            super(v);                               //here comes the layout for follower
            v.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);
            designation= (TextView) itemView.findViewById(R.id.designation);
        }

        @Override
        public void onClick(View v) {
            TextView textView =(TextView) v.findViewById(R.id.name);
            String follower_name= textView.getText().toString();
            Intent intent = new Intent(context,SetDatabase.class);
            Toast.makeText(v.getContext(),follower_name,Toast.LENGTH_SHORT).show();
            context.startActivity(intent.putExtra("userFollowerName",follower_name));
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param dataSet Message list
     * @param id      Device id
     */
    public followerAdapter(List<followersClass> dataSet, String id, Context c) {
        mDataSet = dataSet;
        mId = id;
        context =c;
    }

    @Override
    public followerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == CHAT_RIGHT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_row_follower, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_row_follower, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        /*if (mDataSet.get(position).getId().equals(mId))
            return CHAT_RIGHT;*/

        return CHAT_LEFT;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final followersClass followers = mDataSet.get(position);
        holder.name.setText(followers.getName());
        holder.designation.setText(followers.getUid());


     /* holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"position = ",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,SetDatabase.class);
                Log.d("userId follower class",followers.getFollower());
                context.startActivity(intent.putExtra("userId",followers.getFollower()));



            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
