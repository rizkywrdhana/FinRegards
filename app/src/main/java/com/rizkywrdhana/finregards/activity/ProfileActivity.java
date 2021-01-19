package com.rizkywrdhana.finregards.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rizkywrdhana.finregards.R;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private Button btnEdit, btnLogout;
    private ProgressBar pb;
    TextView fullname, username;
    ImageView profileIv;
    RelativeLayout rv_data;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnEdit = findViewById(R.id.editProfile_btn);
        btnLogout = findViewById(R.id.editLogout_btn);
        fullname = findViewById(R.id.r_fullname);
        username = findViewById(R.id.r_username);
        profileIv = findViewById(R.id.profileImageView);
        pb = findViewById(R.id.progressBar);
        rv_data = findViewById(R.id.relative_data);

        fstore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        rv_data.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        profileIv.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        DocumentReference docRef = fstore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fullname.setText(documentSnapshot.getString("fullname"));
                username.setText(documentSnapshot.getString("username"));
                showImage();
                display();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                finish();
            }

        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }

        });
    }

    private void showImage() {
        StorageReference profileRef = storageReference.child("users/" + userId + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                        .centerCrop().into(profileIv);

            }
        });
    }

    private void display() {
        btnEdit.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        profileIv.setVisibility(View.VISIBLE);
        rv_data.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}