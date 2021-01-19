package com.rizkywrdhana.finregards.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rizkywrdhana.finregards.R;

public class HomeActivity extends AppCompatActivity {

    private CardView cvRegards, cvPeople, cvProfile, cvInbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cvRegards = findViewById(R.id.cv_regards);
        cvPeople = findViewById(R.id.cv_people);
        cvProfile = findViewById(R.id.cv_profile);
        cvInbox = findViewById(R.id.cv_inbox);

        cvRegards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegardsActivity.class));
                finish();
            }

        });

        cvPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PeopleActivity.class));
                finish();
            }

        });

        cvInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), InboxActivity.class));
                finish();
            }

        });

        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }

        });

    }
}