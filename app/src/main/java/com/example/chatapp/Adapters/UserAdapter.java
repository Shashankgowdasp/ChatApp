package com.example.chatapp.Adapters;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Home_chat_List;
import com.example.chatapp.Message_Activity;
import com.example.chatapp.Model.FirebaseUser;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<FirebaseUser> users;
    private ArrayList<FirebaseUser> filteredUsers;

    DatabaseReference reference;
    public UserAdapter(Context mContext) {
        this.mContext = mContext;
        this.users = new ArrayList<>();
        this.filteredUsers = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(com.example.chatapp.R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(ArrayList<FirebaseUser> items) {
        users.clear();
        this.filteredUsers.clear();
        users.addAll(items);
        this.filteredUsers.addAll(items);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseUser user = filteredUsers.get(position);

//        if (user.getMail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
//            // Skip binding for the current user
//            return;
//        }

        holder.usernamelist.setText(user.getUsername());
        holder.mobilelist.setText(user.getId());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, Message_Activity.class);
            intent.putExtra("userName", user.getUsername());
            intent.putExtra("id", user.getId());
            intent.putExtra("uuid", user.getUuid());
            intent.putExtra("email", user.getMail());
           // Toast.makeText(mContext, user.getUuid(), Toast.LENGTH_SHORT).show();
            mContext.startActivity(intent);
        });

        Log.d("Log: InAdapter", "onBindViewHolder: " + user.toString());
        holder.statuslist.setText( (user.getOnline() != null && user.getOnline())  ? "Online" : "Offline" );




    }

    @Override
    public int getItemCount() {
        return this.filteredUsers.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        filteredUsers.clear();
        if (query.isEmpty()) {
            filteredUsers.addAll(users);
        } else {
            for (FirebaseUser item : users) {
                if (item.getUsername().toLowerCase().contains(query.toLowerCase())) {
                    filteredUsers.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView usernamelist,mobilelist,statuslist;
        public ImageView profile_image;

        DatabaseReference reference;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usernamelist = itemView.findViewById(com.example.chatapp.R.id.username_item_name);
            mobilelist=itemView.findViewById(R.id.item_mobile_no);
            statuslist=itemView.findViewById(R.id.item_status);
            profile_image = itemView.findViewById(com.example.chatapp.R.id.user_profileImageview_recyc);

//            boolean isOnlineStatus = false;
//            if (getIntent() != null) {
//                isOnlineStatus = getIntent().getBooleanExtra("isOnlineStatus", false);
//            }
//
//            statuslist.setText(isOnlineStatus ? "Online" : "Offline");

}

        private Intent getIntent() {
            return null;
        }

    }

}
