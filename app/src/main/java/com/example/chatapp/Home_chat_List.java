package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Adapters.UserAdapter;
import com.example.chatapp.Fragments.Users_Fragment;
import com.example.chatapp.Model.MyUser;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home_chat_List extends AppCompatActivity {


    ImageView profile_img;
    TextView profile_name;
    ViewPager viewPager;


    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    UserAdapter userAdapter;
    android.widget.SearchView searchView;

    private Users_Fragment users_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chat_list);


//        profile_img = findViewById(R.id.home_profileImageview);
//        profile_name = findViewById(R.id.home_toolbarUsername);
       // TabLayout tabLayout=findViewById(R.id.tabLayout);
         viewPager=findViewById(R.id.page_viewer);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.baseline_more_vert_24));
        setSupportActionBar(toolbar);


        ViewPager2Adapter viewPager2Adapter=new ViewPager2Adapter(getSupportFragmentManager());


        users_fragment = new Users_Fragment();

//        viewPager2Adapter.addFragments(new Chat_Fragment(),"Chats");
     viewPager2Adapter.addFragments(users_fragment,"");


        viewPager.setAdapter(viewPager2Adapter);
        //tabLayout.setupWithViewPager(viewPager);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("logged_in_user_cred").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyUser myUser =  new MyUser();
                String email = snapshot.child("mail").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);
                String username = snapshot.child("username").getValue(String.class);
                String mobile=snapshot.child("id").getValue(String.class);

                String profileImageUrl = myUser.getProfileImage(); // Get the profile image URL

                // Load the profile image using a library like Picasso or Glide
               // Picasso.get().load(profileImageUrl).into(profile_img);

                myUser.setMyusername(username);
              //  profile_name.setText(myUser.getMyusername());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu);
        searchView = (android.widget.SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                users_fragment.onQuerySubmit(newText);
                return false;
            }
        });
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.logout_menu:
//                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                if (firebaseUser != null) {
//                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("logged_in_user_cred").child(firebaseUser.getUid());
//
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("is_online", false);
//                    databaseReference.updateChildren(data);
//                }
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                finish();
//                return true;
//            case R.id.settings_menu:
//                Intent intent1 = new Intent(this, UserProfile.class);
//                startActivity(intent1);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("logged_in_user_cred").child(firebaseUser.getUid());
                    Map<String, Object> data = new HashMap<>();
                    data.put("is_online", false);
                    databaseReference.updateChildren(data);

                }
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.settings_menu:

                Intent intent1=new Intent(this,UserProfile.class);
                startActivity(intent1);
                break;

            case R.id.camera_menu:
                Toast.makeText(this, "don't use camera", Toast.LENGTH_SHORT).show();
                break;
            case R.id.New_group_menu:
                Toast.makeText(this, "can't create group now", Toast.LENGTH_SHORT).show();
                break;
            case R.id.New_broadcast_menu:
                Toast.makeText(this, "no broadcast available", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Linked_ddev_menu:
                Toast.makeText(this, "no device linked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Starred_messages_menu:
                Toast.makeText(this, "no starred messages", Toast.LENGTH_SHORT).show();
                break;
            case R.id.payments_menu:
                Toast.makeText(this, "PayTM karoo", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public interface OnSearchQueryListener {
        void onQuerySubmit(String query);
    }

    class ViewPager2Adapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

         public ViewPager2Adapter(@NonNull FragmentManager fm) {
             super(fm);
             this.fragments = new ArrayList<>();
             this.titles=new ArrayList<>();
         }

         @NonNull
         @Override
         public Fragment getItem(int position) {
             return fragments.get(position);
         }

         @Override
         public int getCount() {
             return fragments.size();
         }
         public void addFragments(Fragment fragment,String title){
             fragments.add(fragment);
             titles.add(title);
         }

         @Nullable
         @Override
         public CharSequence getPageTitle(int position) {
             return titles.get(position);
         }
     }

}