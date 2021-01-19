package com.rizkywrdhana.finregards.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rizkywrdhana.finregards.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PeopleDetailActivity extends AppCompatActivity {

    private static final String TAG = "Error";
    TextView fullname, username, status, access;
    TextView idFrom, name, regard, regid;
    ImageView photo, photoRegard;
    Button statsBtn, accessBtn;
    private FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_detail);

        Intent data = getIntent();
        final String id = data.getStringExtra("id");


        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("users");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        fullname = findViewById(R.id.r_fullname);
        username = findViewById(R.id.r_username);
        status = findViewById(R.id.r_status);
        access = findViewById(R.id.r_access);
        photo = findViewById(R.id.r_photo);

        idFrom = findViewById(R.id.r_idfrom);
        name = findViewById(R.id.r_toName);
        regard = findViewById(R.id.r_toMessage);
        regid = findViewById(R.id.r_regid);
        photoRegard = findViewById(R.id.r_photoregard);

        statsBtn = findViewById(R.id.status_btn);
        accessBtn = findViewById(R.id.access_btn);

        db.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    fullname.setText(documentSnapshot.getString("fullname"));
                    username.setText(documentSnapshot.getString("username"));
                    status.setText(documentSnapshot.getString("status"));
                    access.setText(documentSnapshot.getString("access"));
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

        db.collection("regards").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    idFrom.setText(documentSnapshot.getString("id"));
                    name.setText(documentSnapshot.getString("name"));
                    regard.setText(documentSnapshot.getString("message"));
                    regid.setText(documentSnapshot.getString("regid"));
                    final StorageReference Ref = storageReference.child(documentSnapshot.getString("name") + "/regards.jpg");
                    Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                                    .centerCrop().into(photoRegard);

                        }
                    });

                }
            }


        });
        //init




        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatus();
            }
        });

        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveAccess();
            }
        });

    }

    public void changeStatus() {
        AlertDialog.Builder builderStatus = new AlertDialog.Builder(this);

        builderStatus.setTitle("Confirmation");
        builderStatus.setMessage("Change to \"Dead\"?");

        builderStatus.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (access.getText().toString().equals("yes")) {
                    final ProgressDialog progressDialog = new ProgressDialog(PeopleDetailActivity.this);
                    String idInbox = regid.getText().toString();
                    String idDead = idFrom.getText().toString();

                    progressDialog.setMessage("Sending...");
                    progressDialog.show();

                    final String statusDead = "Dead";

                    Map<String, Object> file1 = new HashMap<>();
                    file1.put("status", statusDead);
                    db.collection("users").document(idDead)
                            .update(file1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PeopleDetailActivity.this, "Regard sent", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PeopleDetailActivity.this, PeopleActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                    if (status.getText().toString().equals("Dead")) {

                        Map<String, Object> file = new HashMap<>();
                        file.put("id", idFrom);
                        file.put("regid", regid);
                        file.put("name", name);
                        file.put("message", regard);
                        db.collection("inbox").document(idInbox)
                                .set(file)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(PeopleDetailActivity.this, "Regard sent", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PeopleDetailActivity.this, PeopleActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PeopleDetailActivity.this, "Regard sent", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PeopleDetailActivity.this, PeopleActivity.class));
                                        finish();
                                    }
                                });
                    }

                } else {
                    Toast.makeText(PeopleDetailActivity.this, "You don't have Access", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        builderStatus.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alertChangeStatus = builderStatus.create();
        alertChangeStatus.show();
    }

    public void giveAccess() {
        AlertDialog.Builder builderAccess = new AlertDialog.Builder(this);

        builderAccess.setTitle("Confirmation");
        builderAccess.setMessage("Give access to change your status?");

        builderAccess.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (access.getText().toString().equals("no")) {
                    final ProgressDialog progressDialog = new ProgressDialog(PeopleDetailActivity.this);
                    String idPeople = idFrom.getText().toString();

                    progressDialog.setMessage("Sending...");
                    progressDialog.show();

                    final String accessYes = "yes";

                    Map<String, Object> file1 = new HashMap<>();
                    file1.put("access", accessYes);
                    db.collection("users").document(idPeople)
                            .update(file1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PeopleDetailActivity.this, "Access gifted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PeopleDetailActivity.this, PeopleActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                } else {
                    Toast.makeText(PeopleDetailActivity.this, "You already gave the access", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        builderAccess.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alertGiveAccess = builderAccess.create();
        alertGiveAccess.show();
    }
}