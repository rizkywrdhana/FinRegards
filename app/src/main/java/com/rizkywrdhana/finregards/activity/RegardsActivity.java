package com.rizkywrdhana.finregards.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.rizkywrdhana.finregards.R;
import com.rizkywrdhana.finregards.Regards;
import com.rizkywrdhana.finregards.adapter.RegardsAdapter;

public class RegardsActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    String userId;
    FloatingActionButton fabAdd;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private RegardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regards);
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        fabAdd = findViewById(R.id.fab_add);

        Query query = fStore.collection("regards");
        FirestoreRecyclerOptions<Regards> options = new FirestoreRecyclerOptions.Builder<Regards>()
                .setQuery(query, Regards.class)
                .build();

        adapter = new RegardsAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.rv_regards1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddRegardsActivity.class));
                finish();
            }

        });

        adapter.setOnClickListener(new RegardsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(DocumentSnapshot documentSnapshot, int position) {

                String id = documentSnapshot.getId();

                Intent i = new Intent(RegardsActivity.this, RegardsDetailActivity.class);
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