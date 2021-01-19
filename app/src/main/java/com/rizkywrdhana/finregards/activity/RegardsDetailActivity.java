package com.rizkywrdhana.finregards.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rizkywrdhana.finregards.R;
import com.squareup.picasso.Picasso;

public class RegardsDetailActivity extends AppCompatActivity {

    private static final String TAG = "Error";
    TextView name, message;
    ImageView photo;
    FloatingActionButton fabEdit;
    private FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regards_detail);

        Intent data = getIntent();
        final String id = data.getStringExtra("id");


        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("regards");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        name = findViewById(R.id.view_username);
        message = findViewById(R.id.view_regard);
        photo = findViewById(R.id.view_photo);
        fabEdit = findViewById(R.id.fab_edit);

        db.collection("regards").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    name.setText(documentSnapshot.getString("name"));
                    message.setText(documentSnapshot.getString("message"));
                    final StorageReference Ref = storageReference.child(documentSnapshot.getString("name")  + "/regards.jpg");
                    Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                                    .centerCrop().into(photo);

                        }
                    });

                }
            }


        });
        //init

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RegardsDetailActivity.this, EditRegardsActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

    }


}