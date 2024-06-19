package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountActivity extends AppCompatActivity {


    Button mCaptureBtn ,loadBtn;
    ImageView mImageView;
    Uri image_uri;
    SharedPreferences sharedPref ;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference usersRef;
    private FirebaseDatabase mDatabase;
    private DatabaseReference  mReferenceSongs;

    EditText Quaot;
    private FirebaseAuth mAuth;
    public static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser user = mAuth.getCurrentUser();
        String displayName = user.getEmail();
        String userId = user.getUid();

        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome : " + displayName);

        mDatabase= FirebaseDatabase.getInstance();
        mReferenceSongs = mDatabase.getReference("Users");

        mReferenceSongs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot keyNode :datasnapshot.getChildren()) {
                    User userProfile=keyNode.getValue(User.class);
                    if (userProfile != null) {
                        // Assuming the email stored in UserProfile matches the logged-in user's email
                        welcomeText.setText("Welcome : " + userProfile.getFullName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        TextView logoutButton = findViewById(R.id.textViewLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                finish();
            }
        });
        Button Home_btn = findViewById(R.id.Home_btn);
        Home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, Liste_songActivity.class));
                finish();
            }
        });

        mImageView = findViewById(R.id.image_view);

        StorageReference userImageRef = storageReference.child("images/" + userId);
        // Check if image exists and load it
        userImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Image exists, load it into the ImageView
                Picasso.get().load(uri).into(mImageView); // Using Picasso library to load the image
            }
        });


        mCaptureBtn =findViewById(R.id.capture_image_btn);
        // loadBtn = findViewById(R.id.load_btn);;
        //Image
        //Button upload Clicked
        /*loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });*/
        //Button capture Clicked
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                            ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //Request Permession
                        String[] permession = {Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //PopUp TO Request Permessions
                        requestPermissions(permession ,PERMISSION_CODE );
                    }
                    else {
                        //Permession Already Granted
                        openCamera();
                    }
                }
            }
        });
        //SharedPreferences
        sharedPref = getSharedPreferences("My_Prefs", Context.MODE_PRIVATE);
    }


    ////////////////////////////////////////FIN ON CREATE////////////////////////////////////
   /* private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }*/
    private void openCamera() {
        ContentValues values =new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        image_uri =getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , values);
        //Intenet to  camera
        Intent cameraIntent = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT , image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //If user  decline/accept the  permessions
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permession Denied..", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        //capture is  Done
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)  {
            //edit My Image
            mImageView.setImageURI(image_uri);
            Upload();
        }
        //en cas il a choisi une photo
        else if(requestCode == 1 && resultCode == RESULT_OK)
        {
            image_uri= data.getData();
            mImageView.setImageURI(image_uri);
            Upload();
        }
    }
    public void Upload(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Image uploading...");
        progressDialog.show();

        final String randomkey = UUID.randomUUID().toString();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid(); // Get the user's ID

        StorageReference riversRef = storageReference.child("images/" + userId);
        riversRef.putFile(image_uri)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to uploaded.",Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.",Snackbar.LENGTH_LONG).show();
            }
        }) .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("percentage:" + (int)progressPercent +"%" );
            }
        });
    }


}