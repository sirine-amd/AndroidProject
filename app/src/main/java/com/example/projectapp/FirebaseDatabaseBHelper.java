package com.example.projectapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseBHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference  mReferenceSongs;
    private List<song> Songs = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<song> Songs , List<String> Keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseBHelper() {
        mDatabase= FirebaseDatabase.getInstance();
        mReferenceSongs = mDatabase.getReference("songs");
    }

    public void readSongs(final DataStatus dataStatus, String userId){
        mReferenceSongs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                Songs.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode :datasnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    song song=keyNode.getValue(song.class);
                    if(song.getUserId().equals(userId))
                    {Songs.add(song);}
                }
                dataStatus.DataIsLoaded(Songs,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addSong(song song ,final DataStatus dataStatus){
        String key = mReferenceSongs.push().getKey();
        mReferenceSongs.child(key).setValue(song).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void updateSong (String key,song song ,final DataStatus dataStatus){
        mReferenceSongs.child(key).setValue(song).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteSong(String key, final DataStatus dataStatus){
        mReferenceSongs.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                dataStatus.DataIsDeleted();
            }
        });

    }
}
