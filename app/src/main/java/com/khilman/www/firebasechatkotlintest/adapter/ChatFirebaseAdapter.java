package com.khilman.www.firebasechatkotlintest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.khilman.www.firebasechatkotlintest.R;
import com.khilman.www.firebasechatkotlintest.model.ChatModel;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<ChatModel, ChatFirebaseAdapter.MyChatViewHolder> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;

    private String nameUser;

    public ChatFirebaseAdapter(Class<ChatModel> modelClass, int modelLayout, Class<MyChatViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public ChatFirebaseAdapter(DatabaseReference ref, String nameUser, Context ctx) {
        super(ChatModel.class, R.layout.item_message_left, ChatFirebaseAdapter.MyChatViewHolder.class, ref);
        this.nameUser = nameUser;
    }

    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right,parent,false);
            return new MyChatViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left,parent,false);
            return new MyChatViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model = getItem(position);
        if (model.getUserModel().getName().equals(nameUser)){
            return RIGHT_MSG;
        }else{
            return LEFT_MSG;
        }
    }

    @Override
    protected void populateViewHolder(MyChatViewHolder viewHolder, ChatModel model, int position) {
        viewHolder.setIvUser(model.getUserModel().getPhoto_profile());
        viewHolder.setTxtMessage(model.getMessage());
        viewHolder.setTvTimestamp(model.getTimeStamp());
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimestamp;
        EmojiconTextView txtMessage;
        CircleImageView ivUser;
        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = itemView.findViewById(R.id.timestamp);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            ivUser = itemView.findViewById(R.id.ivUserChat);
        }
        public void setTxtMessage(String message){
            if (txtMessage == null)return;
            txtMessage.setText(message);
        }

        public void setIvUser(String urlPhotoUser){
            Log.d("Adapter", "Image photo " + urlPhotoUser);
            if (ivUser == null)return;
           Glide.with(ivUser.getContext()).load(urlPhotoUser).centerCrop().into(ivUser);
        }

        public void setTvTimestamp(String timestamp){
            if (tvTimestamp == null)return;
            tvTimestamp.setText(converteTimestamp(timestamp));
        }
    }
    private CharSequence converteTimestamp(String mileSegundos){
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos),System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }
}
