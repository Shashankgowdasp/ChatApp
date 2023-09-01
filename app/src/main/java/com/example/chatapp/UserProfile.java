package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    ImageView prof_image;
    EditText prof_name, prof_mail,prof_mobile;
    TextView add_img;
    DatabaseReference reference;
    FirebaseUser fUser;
    Button update_prof;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        prof_image = findViewById(R.id.profile_pic_settings);
        prof_name = findViewById(R.id.profile_name_settings);

        prof_mobile=findViewById(R.id.profile_mobile_settings);

        add_img=findViewById(R.id.dp_change_btn);



        prof_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (prof_name.length() >= 15|| prof_name.length()<1) {
                    prof_name.setError("enter the name within 1-15 ");
                } else if (prof_name.length() <= 14) {
                    prof_name.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        prof_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (prof_mobile.length() > 10||prof_mobile.length()<10) {
                    prof_mobile.setError("enter the valid mobile no. ");
                } else if (prof_mobile.length() == 10) {
                    prof_mobile.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.prof_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24));


        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("logged_in_user_cred").child(fUser.getUid());
        toolbar.setNavigationOnClickListener(v-> onBackPressed());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.chatapp.Model.FirebaseUser user = snapshot.getValue(com.example.chatapp.Model.FirebaseUser.class);
                prof_name.setText(user.getUsername());
                prof_mobile.setText(user.getId());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Button updateProfileBtn = findViewById(R.id.prof_update);
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = prof_name.getText().toString();
                String newMobile= prof_mobile.getText().toString();
                updateProfileData(newName,newMobile);
            }
        });

        add_img.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);


        });
    }

    private void updateProfileData(String newName,String newMobile) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("username", newName);
        updates.put("id",newMobile);

        reference.updateChildren(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            prof_image.setImageURI(imageUri); // Display the selected image in ImageView

            // Now you can upload the image to Firebase Storage and update the URL in the database
            // Refer to the next step for uploading the image to Firebase Storage.
            // Inside the onActivityResult() method
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images/" + fUser.getUid());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Get the download URL of the uploaded image
                            String imageUrl = uri.toString();
                            updateProfileImage(imageUrl); // Update the image URL in the database
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle the image upload failure
                        Toast.makeText(this, "image upload failed", Toast.LENGTH_SHORT).show();
                    });

        }
    }
    private void updateProfileImage(String imageUrl) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("profileImage", imageUrl); // Update the image URL field in your FirebaseUser model

        reference.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UserProfile.this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserProfile.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                });
    }



}