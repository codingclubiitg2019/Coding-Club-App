package com.example.codingclub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_HOME = 1;

    ImageView circle;
    TextView welcome;
    ImageView icon;
    TextView codingClubTxt;
    TextView iitGuwTxt;
    TextView developTxt;
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;

    Spanned url;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    int delay = 800;
    int circletime = 1200;
    int logotime = 800;
    int logintime = 800;
    int developtime = 800;

    int MinPasswdLength = 4;
    int MaxPasswdLength = 16;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circle = findViewById(R.id.circle);
        circle.animate().translationY(-1300).setDuration(circletime).setStartDelay(delay);

        welcome = findViewById(R.id.welcomeText);
        welcome.animate().translationY(-1000).setDuration(circletime).setStartDelay(delay);

        icon = findViewById(R.id.codingclubicon);
        icon.setAlpha(0f);
        icon.animate().alpha(1f).setDuration(logotime).setStartDelay(circletime+delay);

        codingClubTxt = findViewById(R.id.codingClubTxt);
        codingClubTxt.setAlpha(0f);
        codingClubTxt.animate().alpha(1f).setDuration(logotime).setStartDelay(circletime+delay);

        iitGuwTxt = findViewById(R.id.iitGuwTxt);
        iitGuwTxt.setAlpha(0f);
        iitGuwTxt.animate().alpha(1f).setDuration(logotime).setStartDelay(circletime+delay);

        emailText = findViewById(R.id.input_email);
        emailText.setAlpha(0f);
        emailText.animate().alpha(1f).setDuration(logintime).setStartDelay(circletime+delay+logotime);

        passwordText = findViewById(R.id.input_password);
        passwordText.setAlpha(0f);
        passwordText.animate().alpha(1f).setDuration(logintime).setStartDelay(circletime+delay+logotime);

        loginButton = findViewById(R.id.btn_login);
        loginButton.setAlpha(0f);
        loginButton.animate().alpha(1f).setDuration(logintime).setStartDelay(circletime+delay+logotime);

        signupLink = findViewById(R.id.link_signup);
        signupLink.setAlpha(0f);
        signupLink.animate().alpha(1f).setDuration(logintime).setStartDelay(circletime+delay+logotime);

        developTxt = findViewById(R.id.developTxt);

        url = Html.fromHtml("Developed by " + "<a href='https://www.facebook.com/lavish.gulati'>Lavish Gulati</a>");

        developTxt.setMovementMethod(LinkMovementMethod.getInstance());
        developTxt.setText(url);
        developTxt.setAlpha(0f);
        developTxt.animate().alpha(1f).setDuration(logintime).setStartDelay(circletime+delay+logotime);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivityForResult(intent, REQUEST_HOME);
        }
    }

    public void login() {

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onLoginSuccess();
                            progressDialog.dismiss();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            onLoginFailed();
                            progressDialog.dismiss();
                            updateUI(null);
                        }

                    }
                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
            }
        }
        else if(requestCode == REQUEST_HOME){
            if(resultCode == RESULT_OK){

            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_LONG).show();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }


}
