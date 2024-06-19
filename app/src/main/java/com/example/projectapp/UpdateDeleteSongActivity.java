package com.example.projectapp;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class UpdateDeleteSongActivity extends AppCompatActivity {

    private EditText mArtist_edit_Txt;
    private EditText mTitle_edit_Txt;
    private EditText mIsr_edit_Txt;
    private Spinner mSong_Categories_Spinner;

    private Button mUpdate_btn;
    private Button mDelete_btn;
    private Button mBack_btn;

    private String key;
    private String artist;
    private String titre;
    private String categorie;
    private String release;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_song);
        System.out.print("hello");
        key =getIntent().getStringExtra("key");
        titre = getIntent().getStringExtra("titre");
        categorie = getIntent().getStringExtra("categorie");
        artist = getIntent().getStringExtra("artist");
        release = getIntent().getStringExtra("release");
        System.out.print("hello"+release);


        mArtist_edit_Txt = (EditText) findViewById(R.id.artist_edittxt);
        mArtist_edit_Txt.setText(artist);
        mTitle_edit_Txt = (EditText) findViewById(R.id.Title_edittxt);
        mTitle_edit_Txt.setText(titre);
        mIsr_edit_Txt = (EditText) findViewById(R.id.Isr_edit_Txt);
        mIsr_edit_Txt.setText(release);
        mSong_Categories_Spinner = (Spinner) findViewById(R.id.Song_Categories_Spinner);
        mSong_Categories_Spinner.setSelection(getIndex_SpinnerItem(mSong_Categories_Spinner , categorie));

        mUpdate_btn = (Button) findViewById(R.id.update_song_btn);
        mDelete_btn = (Button) findViewById(R.id.delete_btn);
        mBack_btn = (Button) findViewById(R.id.Back_btn);

        mUpdate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song song = new song();
                song.setTitre(mTitle_edit_Txt.getText().toString());
                song.setArtiste(mArtist_edit_Txt.getText().toString());
                song.setRéaliser(mIsr_edit_Txt.getText().toString());
                song.setCatégorie(mSong_Categories_Spinner.getSelectedItem().toString());

                new FirebaseDatabaseBHelper().updateSong(key, song, new FirebaseDatabaseBHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<song> Songs, List<String> Keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        Toast.makeText(UpdateDeleteSongActivity.this, "The Song record has " + "Been updated Successfully ", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });
        mBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        mDelete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseBHelper().deleteSong(key, new FirebaseDatabaseBHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<song> Songs, List<String> Keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(UpdateDeleteSongActivity.this, "The Song record has " + "Been Deleted Successfully ", Toast.LENGTH_LONG).show();
                        finish();
                        return;

                    }
                });

            }
        });


    }
    private int  getIndex_SpinnerItem (Spinner spinner ,String item){
        int index=0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(item)){
                index = i;
                break;
            }
        }
        return index;
    }
}


