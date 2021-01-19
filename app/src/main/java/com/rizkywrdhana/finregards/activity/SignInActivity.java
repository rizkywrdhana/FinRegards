package com.rizkywrdhana.finregards.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rizkywrdhana.finregards.R;

public class SignInActivity extends AppCompatActivity {
    EditText inputEmail, inputPass;
    Button submit;
    TextView signUp;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        inputEmail = findViewById(R.id.email_input);
        inputPass = findViewById(R.id.password_input);
        signUp = findViewById(R.id.signUp);
        submit = findViewById(R.id.login_btn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Please insert Email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPass.setError("Please insert Password");
                    return;
                }

                if (password.length() < 8) {
                    inputPass.setError("Password must be at least 8 Characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Signed In successfully",Toast.LENGTH_LONG).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(SignInActivity.this, "Invalid Email or Password!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }

        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fAuth.getCurrentUser();
        updateUI(currentUser);
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser account) {
        if(account != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

    }

}