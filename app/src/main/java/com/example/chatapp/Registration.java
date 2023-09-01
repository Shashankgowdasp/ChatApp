package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Registration extends AppCompatActivity {

    EditText sign_mail,sign_password,sign_re_username,sign_id;
    TextView goto_login;
    Button signup_btn;

    DatabaseReference databaseReference;


    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        sign_mail=findViewById(R.id.signup_editTextEmail);
        sign_password=findViewById(R.id.signup_editTextPassword);
        sign_re_username=findViewById(R.id.signup_editTextUsername);
        goto_login=findViewById(R.id.back_to_login);
        signup_btn=findViewById(R.id.signup_buttonRegister);
        sign_id=findViewById(R.id.signup_editTextid);
        CheckBox showpass_sign=findViewById(R.id.showPasswordCheckBox_sign);

        sign_re_username.setFilters(new InputFilter[]{new AlphanumericInputFilter()});


        databaseReference = FirebaseDatabase.getInstance().getReference();

        goto_login.setOnClickListener(view -> {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        });

        showpass_sign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show password
                    sign_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Hide password
                    sign_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Move the cursor to the end of the text
                sign_password.setSelection(sign_password.length());
            }
        });




        sign_re_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (sign_re_username.length() >= 15|| sign_re_username.length()<1) {
                    sign_re_username.setError("enter the name within 1-15 ");
                } else if (sign_re_username.length() <= 14) {
                    sign_re_username.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        sign_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (sign_id.length() > 10||sign_id.length()<10) {
                    sign_id.setError("enter the valid mobile no. ");
                } else if (sign_id.length() == 10) {
                    sign_id.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        sign_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = sign_password.getText().toString();
                if (password.length() >= 15 || password.length() < 6) {
                    sign_password.setError("Enter a password within 6-15 characters");
                } else if (password.length() <= 14) {
                    boolean hasNumber = false;
                    boolean hasSpecialChar = false;
                    for (int j = 0; j < password.length(); j++) {
                        char c = password.charAt(j);
                        if (Character.isDigit(c)) {
                            hasNumber = true;
                        } else if (!Character.isLetterOrDigit(c)) {
                            hasSpecialChar = true;
                        }
                        if (hasNumber && hasSpecialChar) {
                            sign_password.setError(null);
                            return;
                        }
                    }
                    sign_password.setError("Password must contain at least one number and one special character");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        sign_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Patterns.EMAIL_ADDRESS.matcher(sign_mail.getText().toString()).matches()) {
                    sign_mail.setError("please enter valid mail");
                } else if (Patterns.EMAIL_ADDRESS.matcher(sign_mail.getText().toString()).matches()) {
                    sign_mail.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        signup_btn.setOnClickListener(view -> {
            String username,mail,password,id;
            username=String.valueOf(sign_re_username.getText());
            mail=String.valueOf(sign_mail.getText());
            password=String.valueOf(sign_password.getText());
            id=String.valueOf(sign_id.getText());

            if (TextUtils.isEmpty(mail)||TextUtils.isEmpty(password)){
                Toast.makeText(this, "please fill both fields", Toast.LENGTH_SHORT).show();
                return;
            } if (sign_password.getText().toString().equals(sign_re_username.getText().toString())) {
                Toast.makeText(this, "password do not match", Toast.LENGTH_SHORT).show();
                     return;
            }else {
                firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserDataToDatabase(username,mail,password,id);
                            Toast.makeText(Registration.this, "Registration successfull", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Registration.this, "Registration/Authentication failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Registration.this, "try again with valid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }
    private void saveUserDataToDatabase(String username,String mail,String password,String id){
        String Id=firebaseAuth.getCurrentUser().getUid();
        User user=new User(username,mail,password,id);
        user.setUuid(Id);
        databaseReference.child("logged_in_user_cred").child(Id).setValue(user);
    }
    public static String date() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }
}
class User{
    public String username;
    public  String mail;
    public  String password;
    public  String id;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String uuid;


    public User() {
    }

    public User(String username,String mail,String password,String id) {
        this.username=username;
        this.mail = mail;
        this.password=password;
        this.id=id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}




    class AlphanumericInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Define the allowed characters (alphanumeric and space)
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";

        // Create a StringBuilder to hold the filtered text
        StringBuilder filteredText = new StringBuilder();

        // Loop through each character in the source text
        for (int i = start; i < end; i++) {
            char currentChar = source.charAt(i);

            // Check if the character is in the list of allowed characters
            if (allowedChars.indexOf(currentChar) != -1) {
                filteredText.append(currentChar);
            }
        }

        // If the filtered text is different from the source, return the filtered text
        if (!TextUtils.equals(source, filteredText.toString())) {
            return filteredText.toString();
        }

        // If no changes were made, return null to indicate that no filtering is necessary
        return null;
    }
}