package com.example.codingclub;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    Toolbar mTopToolbar;
    EditText chooseTime, chooseDate, nameText, detailsText, venueText;
    Button addEventBtn;
    ImageView imageView, add_image_btn;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Uri filePath = null;

    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mTopToolbar = findViewById(R.id.my_toolbar);
        mTopToolbar.setTitle("Add event");
        setSupportActionBar(mTopToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        chooseTime = findViewById(R.id.input_time);

        chooseTime.setOnClickListener(new View.OnClickListener() {
            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, true);

                timePickerDialog.show();

            }
        });

        chooseDate = findViewById(R.id.input_date);
        chooseDate.setOnClickListener(new View.OnClickListener(){
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            @Override
            public void onClick(View view){
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        chooseDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear, year));
                    }

                    }, currentYear, currentMonth, currentDay);

                datePickerDialog.show();
            }
        });

        nameText = findViewById(R.id.input_name);
        detailsText = findViewById(R.id.input_details);
        venueText = findViewById(R.id.input_venue);
        imageView = findViewById(R.id.imageView);
        add_image_btn = findViewById(R.id.add_image_btn);

        add_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        addEventBtn = findViewById(R.id.btn_signup);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_event();
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

    public void add_event() {

        if (!validate()) {
            return;
        }

        addEventBtn.setEnabled(false);

        new AddTask().execute();
    }

    public class AddTask extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        public AddTask() {
            super();
            progressDialog = new ProgressDialog(AddEventActivity.this);
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
                            progressDialog.setMessage("Adding event...");
                            addEventWithUri(downloadUrl, progressDialog);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            addEventBtn.setEnabled(true);
                            Toast.makeText(AddEventActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    private void uploadImage(final ProgressDialog progressDialog){
        if(filePath != null) {
            final StorageReference ref = storageReference.child("images/events/"+ UUID.randomUUID().toString());
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
                            addEventBtn.setEnabled(true);
                            Toast.makeText(AddEventActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
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
            addEventWithUri(null, progressDialog);
        }
    }

    private void addEventWithUri(final String downloadUrl, final ProgressDialog progressDialog){
        String name = nameText.getText().toString();
        String details = detailsText.getText().toString();
        String venue = venueText.getText().toString();
        String date = chooseDate.getText().toString();
        String time = chooseTime.getText().toString();

        Map<String, Object> event = new HashMap<>();
        event.put("name", name);
        event.put("details", details);
        event.put("venue", venue);
        event.put("date", date);
        event.put("time", time);
        if(downloadUrl != null){
            event.put("image", downloadUrl);
        }

        db.collection("events")
                .add(event)
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

    public void onEventAdded(){
        Toast.makeText(getBaseContext(), "Event added", Toast.LENGTH_LONG).show();
        addEventBtn.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    public void onEventFailure(){
        Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
        addEventBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String details = detailsText.getText().toString();
        String venue = venueText.getText().toString();
        String date = chooseDate.getText().toString();
        String time = chooseTime.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        detailsText.setError(null);
        venueText.setError(null);

        if(date.isEmpty()){
            chooseDate.setError("Date of the event is required");
            valid = false;
        }
        else{
            chooseDate.setError(null);
        }

        if(time.isEmpty()){
            chooseTime.setError("Time of the event is required");
            valid = false;
        }
        else{
            chooseTime.setError(null);
        }

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
