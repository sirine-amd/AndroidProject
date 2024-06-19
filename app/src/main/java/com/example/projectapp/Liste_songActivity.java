package com.example.projectapp;



import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Liste_songActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_song);
        mAuth = FirebaseAuth.getInstance();

        Button Profile_btn = findViewById(R.id.hometoProf_btn);
        Profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Liste_songActivity.this, AccountActivity.class));
                finish();
            }
        });

        mRecyclerView =(RecyclerView) findViewById(R.id.recycler_view_songs);
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        new  FirebaseDatabaseBHelper().readSongs(new FirebaseDatabaseBHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<song> Songs, List<String> Keys) {
                new RecyclerView_Config().SetConfig(mRecyclerView,Liste_songActivity.this,Songs,Keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        },userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.New_Song:
                startActivity(new Intent (this , NewSongActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}