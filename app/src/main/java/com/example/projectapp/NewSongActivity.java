package com.example.projectapp;





import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;

public class NewSongActivity extends AppCompatActivity {
    private EditText mArtist_edit_Txt ;
    private EditText mTitre_edit_Txt;
    private DatePicker mRéaliser_edit_Txt;
    private Spinner mCategorie_Spinner;
    private Button mAdd_btn;
    private Button mBack_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_song);
        mAuth = FirebaseAuth.getInstance();


        mArtist_edit_Txt = (EditText) findViewById(R.id.artist_edittxt);
        mTitre_edit_Txt = (EditText) findViewById(R.id.Title_edittxt);
        mRéaliser_edit_Txt = (DatePicker) findViewById(R.id.Isr_edit_Txt);
        mCategorie_Spinner = (Spinner) findViewById(R.id.Song_Categories_Spinner);

        mAdd_btn = (Button) findViewById(R.id.update_song_btn);
        mBack_btn = (Button) findViewById(R.id.Back_btn);

        mAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    com.example.projectapp.song song = new com.example.projectapp.song();
                    song.setArtiste(mArtist_edit_Txt.getText().toString().trim());
                    song.setTitre(mTitre_edit_Txt.getText().toString().trim());

                    String dd = (mRéaliser_edit_Txt.getMonth()) + "/" + (mRéaliser_edit_Txt.getDayOfMonth()) + "/" + (mRéaliser_edit_Txt.getYear());
                    song.setRéaliser(dd);

                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId = user.getUid();

                    song.setUserId(userId);

                    song.setCatégorie(mCategorie_Spinner.getSelectedItem().toString().trim());
                    new FirebaseDatabaseBHelper().addSong(song, new FirebaseDatabaseBHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<com.example.projectapp.song> Songs, List<String> Keys) {

                        }

                        @Override
                        public void DataIsInserted() {
                            Toast.makeText(NewSongActivity.this, "The Song record has Been inserted Successfully ", Toast.LENGTH_LONG).show();
                            clearFields();
                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                } else {
                    Toast.makeText(NewSongActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }
        private void clearFields() {
            mArtist_edit_Txt.setText("");
            mTitre_edit_Txt.setText("");
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            mRéaliser_edit_Txt.updateDate(year, month, dayOfMonth);
            mCategorie_Spinner.setSelection(0);
        }

    private boolean validateFields() {
        String artiste = mArtist_edit_Txt.getText().toString().trim();
        String titre = mTitre_edit_Txt.getText().toString().trim();
        // Validate other fields as needed
        boolean dateIsValid = validateDatePicker();
        boolean spinnerIsValid = validateSpinner();

        // Check if any of the fields is empty or invalid
        if (artiste.isEmpty() || titre.isEmpty() || !dateIsValid || !spinnerIsValid) {
            return false;
        }
        return true;
    }
    private boolean validateDatePicker() {
        // Example validation: Check if a valid date is selected
        // You can add more specific validation logic here based on your requirements
        int month = mRéaliser_edit_Txt.getMonth();
        int dayOfMonth = mRéaliser_edit_Txt.getDayOfMonth();
        int year = mRéaliser_edit_Txt.getYear();

        // Example: Check if a valid date is selected (not default initial date)
        return !(month == 0 && dayOfMonth == 1 && year == 1970); // Adjust this condition based on your DatePicker's default behavior
    }

    private boolean validateSpinner() {
        // Example validation: Check if an item is selected in the Spinner
        return mCategorie_Spinner.getSelectedItemPosition() != 0; // Adjust the condition based on your Spinner's default selection
    }
}