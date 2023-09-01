package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button login_btn;
    EditText mail_edittext,password_edittext;
    TextView goto_signup;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            Intent intent=new Intent(this,Home_chat_List.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn=findViewById(R.id.button_login);
        mail_edittext=findViewById(R.id.login_editTextEmail);
        password_edittext=findViewById(R.id.login_editTextPassword);
        goto_signup=findViewById(R.id.btn_goto_signin);
        CheckBox showpass = findViewById(R.id.showPasswordCheckBox);


        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show password
                    password_edittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    // Hide password
                    password_edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // Move the cursor to the end of the text
                password_edittext.setSelection(password_edittext.length());
            }
        });

                goto_signup.setOnClickListener(view -> {
            Intent intent=new Intent(this,Registration.class);
            startActivity(intent);
        });

        password_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = password_edittext.getText().toString();
                if (password.length() >= 15 || password.length() < 6) {
                    password_edittext.setError("Enter a password within 6-15 characters");
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
                            password_edittext.setError(null);
                            return;
                        }
                    }
                    password_edittext.setError("Password must contain at least one number and one special character");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mail_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Patterns.EMAIL_ADDRESS.matcher(mail_edittext.getText().toString()).matches()) {
                    mail_edittext.setError("please enter valid mail");
                } else if (Patterns.EMAIL_ADDRESS.matcher(mail_edittext.getText().toString()).matches()) {
                    mail_edittext.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



        login_btn.setOnClickListener(view -> {
            String mail,password;
            mail=String.valueOf(mail_edittext.getText());
            password=String.valueOf(password_edittext.getText());

            if (TextUtils.isEmpty(mail)||TextUtils.isEmpty(password)){
                Toast.makeText(this, "please fill both fields", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,Home_chat_List.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "invalid password/mail", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "check your password and mail-id again and try", Toast.LENGTH_LONG).show();

                    }
                }
            });
        });

    }


}