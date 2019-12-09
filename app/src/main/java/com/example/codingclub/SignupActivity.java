package com.example.codingclub;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    EditText nameText, emailText, passwordText, rollNoText, confirmPassText;
    Button signupButton;
    TextView loginLink;
    Spinner departmentSpin, programmeSpin;

    int minPassLength = 4;
    int maxPassLength = 16;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameText = findViewById(R.id.input_name);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        signupButton = findViewById(R.id.btn_signup);
        loginLink = findViewById(R.id.link_login);
        rollNoText = findViewById(R.id.input_roll);
        confirmPassText = findViewById(R.id.input_confirmPasswd);

        departmentSpin = findViewById(R.id.departmentSpin);
        String[] department = new String[]{
                "Department",
                "Biosciences and Bioengineering",
                "Chemical Engineering",
                "Chemistry",
                "Civil Engineering",
                "Computer Science and Engineering",
                "Design",
                "Electronics and Electrical Engineering",
                "Humanities and Social Sciences",
                "Mathematics",
                "Mechanical Engineering",
                "Physics"
        };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item, department){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    return false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        departmentSpin.setAdapter(spinnerArrayAdapter);



        programmeSpin = findViewById(R.id.programmeSpin);
        String[] programme = new String[]{
                "Programme",
                "B.Tech.",
                "B.Des.",
                "M.Tech.",
                "M.Des.",
                "Ph.D."
        };
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this,R.layout.spinner_item, programme){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    return false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_item);
        programmeSpin.setAdapter(spinnerArrayAdapter2);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    public void signup() {

        if (!validate()) {
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = emailText.getText().toString();

        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("email", email);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){

                                String name = nameText.getText().toString();
                                String email = emailText.getText().toString();
                                String password = passwordText.getText().toString();
                                String rollNo = rollNoText.getText().toString();
                                String programme = programmeSpin.getSelectedItem().toString();
                                String department = departmentSpin.getSelectedItem().toString();

                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("email", email);
                                user.put("rollNo", rollNo);
                                user.put("programme", programme);
                                user.put("department", department);
                                user.put("password", password);
                                user.put("status", "student");

                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                createUser(progressDialog);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                onSignupFailed();
                                                progressDialog.dismiss();
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(getBaseContext(), "User with this email already exists", Toast.LENGTH_LONG).show();
                                signupButton.setEnabled(true);
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
                            signupButton.setEnabled(true);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void createUser(final ProgressDialog progressDialog){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            onSignupSuccess();
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            onSignupFailed();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }


    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Registration successful", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String rollNo = rollNoText.getText().toString();
        String confirmPasswd = confirmPassText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if(rollNo.isEmpty()){
            rollNoText.setError("Roll Number should not be empty");
            valid = false;
        }
        else{
            CharacterIterator it = new StringCharacterIterator(rollNo);
            while(it.current() != CharacterIterator.DONE){
                if(!(it.current() >= '0' && it.current() <= '9')){
                    valid = false;
                    break;
                }
                it.next();
            }
            if(!valid){
                rollNoText.setError("Roll Number should contain only digits");
            }
            else{
                rollNoText.setError(null);
            }
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        }
        else{
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < minPassLength || password.length() > maxPassLength) {
            passwordText.setError("between " + minPassLength + " and " + maxPassLength + " alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        Log.d("password", password + " " + confirmPasswd);

        if(!password.equals(confirmPasswd)){
            confirmPassText.setError("Passwords do not match");
            valid = false;
        }
        else{
            confirmPassText.setError(null);
        }

        if(!valid){
            Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        }

        return valid;
    }
}
