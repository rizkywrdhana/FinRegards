package com.rizkywrdhana.finregards.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rizkywrdhana.finregards.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText regFullname, regUsername, regStatus, regAccess, regEmail, regPass;
    Button submit;
    TextView signIn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        regFullname = findViewById(R.id.fullname_reg);
        regUsername = findViewById(R.id.username_reg);
        regEmail = findViewById(R.id.email_reg);
        regPass = findViewById(R.id.password_reg);
        signIn = findViewById(R.id.signIn);
        submit = findViewById(R.id.signup_btn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = regEmail.getText().toString().trim();
                String password = regPass.getText().toString().trim();
                final String fullName = regFullname.getText().toString();
                final String username = regUsername.getText().toString();
                final String status = "Alive";
                final String access = "No";

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
                Pattern lowerCasePatten = Pattern.compile("[a-z ]");
                Pattern digitCasePatten = Pattern.compile("[0-9 ]");

                if (TextUtils.isEmpty(fullName)) {
                    regFullname.setError("Full name is Required.");
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    regUsername.setError("Username is Required.");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is Required.");
                    return;
                }

                if (!email.matches(emailPattern)){
                    regEmail.setError("Email is Invalid.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    regPass.setError("Password is Required.");
                    return;
                }

                if (password.length() < 8) {
                    regPass.setError("Password Must be at least 8 Characters");
                    return;
                }

                if (!UpperCasePatten.matcher(password).find()){
                    regPass.setError("Password must have at least one uppercase character");
                    return;
                }

                if (!lowerCasePatten.matcher(password).find()){
                    regPass.setError("Password must have at least one lowercase character");
                    return;
                }

                if (!digitCasePatten.matcher(password).find()){
                    regPass.setError("Password must have at least one digit character");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            final String userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

                            Map<String, Object> user = new HashMap<>();
                            user.put("userid", userID);
                            user.put("fullname", fullName);
                            user.put("username", username);
                            user.put("status", status);
                            user.put("access", access);
                            user.put("email", email);

                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            FirebaseUser fUser = fAuth.getCurrentUser();
                            updateUI(fUser);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser account) {
        if(account != null){
            Toast.makeText(this,"Signed Up successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }
}