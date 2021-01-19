package com.rizkywrdhana.finregards.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rizkywrdhana.finregards.Inbox;
import com.rizkywrdhana.finregards.R;
import com.rizkywrdhana.finregards.adapter.InboxAdapter;

public class InboxActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    String userId;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private InboxAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        Query query = fStore.collection("inbox");
        FirestoreRecyclerOptions<Inbox> options = new FirestoreRecyclerOptions.Builder<Inbox>()
                .setQuery(query, Inbox.class)
                .build();

        adapter = new InboxAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.rv_inbox1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new InboxAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(DocumentSnapshot documentSnapshot, int position) {

                String id = documentSnapshot.getId();

                Intent i = new Intent(InboxActivity.this, InboxDetailActivity.class);
                i.putExtra("id", id);
                startActivity(i);


            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}