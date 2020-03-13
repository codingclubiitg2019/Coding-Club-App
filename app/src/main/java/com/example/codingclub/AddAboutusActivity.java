package com.example.codingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddAboutusActivity extends AppCompatActivity
{
    Toolbar toolbar;
    EditText nameText, positionText, yearText;
    Button addMemberBtn;
    ImageView imageView, add_image_btn;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Uri filePath = null;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aboutus);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Add event");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        nameText = findViewById(R.id.input_name);
        positionText = findViewById(R.id.input_position);
        yearText = findViewById(R.id.input_year);

        imageView = findViewById(R.id.imageView);
        add_image_btn = findViewById(R.id.add_image_btn);

        add_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        addMemberBtn = findViewById(R.id.btn_add_member);
        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMember() {

        if (!validate()) {
            return;
        }

        addMemberBtn.setEnabled(false);

        new AddAboutusActivity.AddTask().execute();
    }

    public class AddTask extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        public AddTask() {
            super();
            progressDialog = new ProgressDialog(AddAboutusActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading image...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... values) {
            uploadImage(progressDialog);
            return "Done";
        }
    }

    private void getImageUrl(final StorageReference ref, final ProgressDialog progressDialog){
        ref.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl = uri.toString();
                        progressDialog.setMessage("Adding Image...");
                        addMemberWithUri(downloadUrl, progressDialog);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        addMemberBtn.setEnabled(true);
                        Toast.makeText(AddAboutusActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadImage(final ProgressDialog progressDialog){
        if(filePath != null) {
            final StorageReference ref = storageReference.child("images/members/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getImageUrl(ref, progressDialog);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            addMemberBtn.setEnabled(true);
                            Toast.makeText(AddAboutusActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploading image: "+(int)progress+"%");
                        }
                    });
        }
        else{
            addMemberWithUri(null, progressDialog);
        }
    }

    private void addMemberWithUri(final String downloadUrl, final ProgressDialog progressDialog){
        final String name = nameText.getText().toString();
        final String year = yearText.getText().toString();
        final String position = positionText.getText().toString();
        final String id = UUID.randomUUID().toString();


        db
                .collection("users")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                            {
                                Team member = new Team();
                                member.setId(id);
                                member.setYear(year);
                                member.setPosition(position);
                                if(downloadUrl != null){
                                    member.setImage(downloadUrl);
                                }
                                member.setDocumentReference(document.getReference());
                                db.collection("core_team")
                                        .add(member)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.dismiss();
                                                onEventAdded();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                onEventFailure();
                                                progressDialog.dismiss();
                                            }
                                        });
                            }

                        }
                    }
                });


    }

    public void onEventAdded(){
        Toast.makeText(getBaseContext(), "Member added", Toast.LENGTH_LONG).show();
        addMemberBtn.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    public void onEventFailure(){
        Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
        addMemberBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String year = yearText.getText().toString();
        String position = positionText.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        yearText.setError(null);
        positionText.setError(null);

        if(!valid){
            Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
        }

        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
