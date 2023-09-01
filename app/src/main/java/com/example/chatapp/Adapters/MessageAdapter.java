package com.example.chatapp.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.MediaRouteButton;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Model.MessageModel;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VT_MYMESSAGE = 0;
    private static final int VT_REMESSAGE = 1;

    private static final String TAG = "Log: " + MessageAdapter.class.getSimpleName();

    private final Context context;
    private final List<MessageModel> msgModelList;

    public List<MessageModel> filteredList;

    ImageView tick_mark;

    private String currentUserId;

    LayoutInflater layoutInflater;

    public MessageAdapter(Context context) {
        this.context = context;
        msgModelList = new ArrayList<>();
        filteredList = new ArrayList<>();
        currentUserId = FirebaseAuth.getInstance().getUid();
        layoutInflater = LayoutInflater.from(context);
    }

    public void add(MessageModel msgModel) {
        msgModelList.add(msgModel);
        sortMessagesByTimestamp(); // Sort messages whenever a new one is added
        notifyDataSetChanged();
    }

    private boolean isReceiverOnline;
    @SuppressLint("NotifyDataSetChanged")
    public void setReceiverOnline(Boolean isReceiverOnline){
        if(isReceiverOnline != null && isReceiverOnline) {
            isReceiverOnline = true;
        }
        else {
            isReceiverOnline = false;
        }
        notifyDataSetChanged();
    }

    public void clear() {
        msgModelList.clear();
        notifyDataSetChanged();
    }

    private void sortMessagesByTimestamp() {
        Collections.sort(msgModelList, new Comparator<MessageModel>() {
            @Override
            public int compare(MessageModel messageModel1, MessageModel messageModel2) {
                return Long.compare(messageModel1.getTimestamp(), messageModel2.getTimestamp());
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VT_MYMESSAGE) {
            return new SenderViewHoler(layoutInflater.inflate(R.layout.message_row_right, parent, false));
        } else if (viewType == VT_REMESSAGE) {
            return new ReceiverViewHoler(layoutInflater.inflate(R.layout.message_row_left, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = msgModelList.get(position);
        if (getItemViewType(position) == VT_MYMESSAGE) {
            SenderViewHoler senderViewHoler = (SenderViewHoler) holder;
            senderViewHoler.msg.setText(messageModel.getMessage());
            if (!messageModel.getMessage().isEmpty()){
                senderViewHoler.msg.setVisibility(View.VISIBLE);
            }else{
                senderViewHoler.msg.setVisibility(View.GONE);
            }
            senderViewHoler.time_span.setText(time(messageModel.getTimestamp()));

            if(messageModel.getMediaURL() != null && messageModel.getMediaType().contains("image")) {
                senderViewHoler.imageView_send.setVisibility(View.VISIBLE);
                Glide.with(context).load(messageModel.getMediaURL()).into(senderViewHoler.imageView_send);
                senderViewHoler.imageView_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create a dialog to display the image preview
                        Dialog imagePreviewDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                        imagePreviewDialog.setContentView(R.layout.dialogue_image_view);
                        ImageView imagePreview = imagePreviewDialog.findViewById(R.id.imagePreview);

                        // Load and display the image in the dialog
                        Glide.with(context).load(messageModel.getMediaURL()).into(imagePreview);

                        // Close the dialog when the user taps on the image
                        imagePreview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imagePreviewDialog.dismiss();
                            }
                        });

                        // Show the dialog
                        imagePreviewDialog.show();
                    }
                });
            }
            else {
                senderViewHoler.imageView_send.setVisibility(View.GONE);
            }

            // Code for showing download button
            if(messageModel.getMediaURL() != null && !messageModel.getMediaType().contains("image")) {
                senderViewHoler.btn_send_doc.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Open File", Toast.LENGTH_SHORT).show();
            }
            else {
                senderViewHoler.btn_send_doc.setVisibility(View.GONE);
            }

        } else {
            ReceiverViewHoler receiverViewHoler = (ReceiverViewHoler) holder;
            receiverViewHoler.msg.setText(messageModel.getMessage());
            receiverViewHoler.time_span.setText(time(messageModel.getTimestamp()));
            if(messageModel.getMediaURL() != null && messageModel.getMediaType().contains("image")) {
                receiverViewHoler.imageView_res.setVisibility(View.VISIBLE);
                Glide.with(context).load(messageModel.getMediaURL()).into(receiverViewHoler.imageView_res);
                receiverViewHoler.imageView_res.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create a dialog to display the image preview
                        Dialog imagePreviewDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                        imagePreviewDialog.setContentView(R.layout.dialogue_image_view);
                        ImageView imagePreview = imagePreviewDialog.findViewById(R.id.imagePreview);

                        // Load and display the image in the dialog
                        Glide.with(context).load(messageModel.getMediaURL()).into(imagePreview);

                        // Close the dialog when the user taps on the image
                        imagePreview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imagePreviewDialog.dismiss();
                            }
                        });

                        // Show the dialog
                        imagePreviewDialog.show();
                    }
                });

            }
            else {
                receiverViewHoler.imageView_res.setVisibility(View.GONE);
            }


            if(isReceiverOnline) {
                receiverViewHoler.tick_mark.setVisibility(View.VISIBLE);

            }
            else {

            }

            if(messageModel.getMediaURL() != null && !messageModel.getMediaType().contains("image")) {
                receiverViewHoler.btn_rec_doc.setVisibility(View.VISIBLE);


                receiverViewHoler.btn_rec_doc.setOnClickListener(view -> {
                    // Get the message model at the clicked position
//                    MessageModel messageModel = msgModelList.get(position);

                    // Check if the message contains a media URL
                    if (messageModel.getMediaURL() != null) {
                        // Get the filename from the URL (you might need to adapt this based on your URL structure)
                        String filename = messageModel.getMediaURL().substring(messageModel.getMediaURL().lastIndexOf('/') + 1);

                        // Get a reference to Firebase Storage
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("message_files").child(filename);

                        // Create a local file to save the downloaded document
                        File localFile = new File(context.getExternalFilesDir(null), filename);

                        // Start the download
                        storageReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                            // The file has been successfully downloaded, you can now open it
                            // For demonstration, I'll show a toast
                            Toast.makeText(context, "Downloaded: " + localFile.getPath(), Toast.LENGTH_SHORT).show();

                            // Here you can implement the logic to open the downloaded file
                            // For example, you can use an Intent to open a PDF viewer or other appropriate app
                        }).addOnFailureListener(exception -> {
                            // Handle any errors that occur during download
                            Toast.makeText(context, "Download failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // The message doesn't have a media URL, handle this case appropriately
                        Toast.makeText(context, "No document URL in this message", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(context, "Oen File", Toast.LENGTH_SHORT).show();
            }
            else {
                receiverViewHoler.btn_rec_doc.setVisibility(View.GONE);
            }
        }
    }

    public int getLastItemPosition() {
        return msgModelList.size() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = msgModelList.get(position);
        if (messageModel.getSenderId().equals(currentUserId)) {
            return VT_MYMESSAGE;
        } else {
            return VT_REMESSAGE;
        }
    }

    private String time(long mills) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormat.format(new Date(mills));
    }

    @Override
    public int getItemCount() {
        return msgModelList.size();
    }

    public Filter getFilter() {
        Log.d("TAG", "getFilter: ");
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                ArrayList<MessageModel> filteredItems = new ArrayList<>();

                for (MessageModel item : msgModelList) {
                    if (item.getMessage().toLowerCase().contains(query)) {
                        filteredItems.add(item);
                    }
                }

                results.values = filteredItems;
                results.count = filteredItems.size();
                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<MessageModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    public static class SenderViewHoler extends RecyclerView.ViewHolder {

        public TextView msg, time_span;
        public RelativeLayout main;
        public ImageView imageView_send,tick_mark;


        Button btn_send_doc;
        public SenderViewHoler(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.right_message);
            time_span = itemView.findViewById(R.id.right_time_span);
            main = itemView.findViewById(R.id.right_main_msg_layout);
            imageView_send=itemView.findViewById(R.id.mediaImageView_right);
            btn_send_doc=itemView.findViewById(R.id.btn_send_doc);
            //tick_mark=itemView.findViewById(R.id.tick_mark);
        }
    }
    public static class ReceiverViewHoler extends RecyclerView.ViewHolder {

        public TextView msg, time_span;
        public RelativeLayout main;

        public ImageView imageView_res;
        public ImageView tick_mark;
        Button btn_rec_doc;

        public ReceiverViewHoler(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.left_message);
            time_span = itemView.findViewById(R.id.left_time_span);
            main = itemView.findViewById(R.id.left_main_msg_layout);
            imageView_res = itemView.findViewById(R.id.mediaImageView_left);
            btn_rec_doc=itemView.findViewById(R.id.btn_res_doc);
        }
    }

}


