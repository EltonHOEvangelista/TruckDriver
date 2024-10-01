package com.example.truckdriver_v02.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.truckdriver_v02.MainActivity;
import com.example.truckdriver_v02.R;
import com.example.truckdriver_v02.data.SQLite.DbHandler;
import com.example.truckdriver_v02.data.account.Driver;
import com.example.truckdriver_v02.data.session.Session;
import com.example.truckdriver_v02.databinding.ActivityLoginBinding;
import com.example.truckdriver_v02.ui.register.activity_register;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    EditText txt_email;
    EditText txt_password;
    Button btn_ask_signup;
    Button btn_signin;

    //Instantiating SignIn Model
    Driver signInModel = new Driver(null, null);

    private final int isEnabled = 1;        //account enabled: yes=1 no=0

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final ProgressBar loadingProgressBar = binding.loading;

        txt_email = findViewById(R.id.txt_Email);
        txt_password = findViewById(R.id.txtPassword);

        btn_signin = findViewById(R.id.btn_SignIn);
        btn_ask_signup = findViewById(R.id.btn_ask_signUp);

        //get email address and password from Sign Up success.
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            txt_email.setText(extras.getString("email"));
            txt_password.setText(extras.getString("password"));
        }

        //SignIn button
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //calling method to Sign In including email and password parameters
                boolean signedIn = SignIn(txt_email.getText().toString(), txt_password.getText().toString());

                if(signedIn){

                    //calling method to start session
                    startSession();

                    Toast.makeText(LoginActivity.this, "Welcome, " + signInModel.getFirstName(), Toast.LENGTH_SHORT).show();

                    //calling method to open MainActivity
                    openMainActivity();
                }
            }
        });

        btn_ask_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Method to open Sign Up Activity
                openSigUpActivity(v);
            }
        });
    }

    private void startSession() {

        finishingPreviousSessions();    //call method to finish previous sessions

        Session session = new Session(signInModel.getAccountId());  //get session id

        session.setSignedIn(signInModel.getActiveAccount());    //set session on

        //get date
        Date date = new Date();

        session.setTime_begin(date.toString());

        session.setTime_end("");

        //connecting to the database and inserting data
        DbHandler dbHandler = new DbHandler(LoginActivity.this);

        boolean StartSession =  dbHandler.StartSession(              //int accountId, String time_begin, String time_end, int signedIn
                session.getAccountId(),
                session.getTime_begin(),
                session.getTime_end(),
                session.getSignedIn());

        //close dbHandler
        dbHandler.close();
    }

    private void finishingPreviousSessions() {

        //connecting to the database and inserting data
        DbHandler dbHandler = new DbHandler(LoginActivity.this);
        boolean sessionsEnded = dbHandler.endAllSession();
        dbHandler.close();
    }

    private boolean SignIn(String email, String password) {

        signInModel.setEmail(email);
        signInModel.setPassword(password);

        //Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(LoginActivity.this);

        //read password and account status from database to sign in
        String[] credential = dbHandler.getAccountCredential(signInModel.getEmail());

        //calling method to authenticate account
        AccountAuthentication(signInModel.getEmail(), credential[0]);

        if(signInModel.isAuthenticated()){

            //calling method to validate password
            PasswordValidation(signInModel.getPassword(), credential[1]);

            if(signInModel.isValidated()){
                //calling method to check if account is enabled
                boolean isEnabled = CheckAccountStatus(credential[2]);

                if(isEnabled) {
                    //get account details to start session
                    signInModel.setEmail(credential[0]);
                    signInModel.setActiveAccount(Integer.parseInt(credential[2]));
                    signInModel.setFirstName(credential[3]);
                    signInModel.setAccountId(Integer.parseInt(credential[4]));

                    //return true for user authenticated, validated and enabled.
                    return true;
                }
                else{
                    signInModel.setAuthenticated(false);
                    signInModel.setValidated(false);
                    Toast.makeText(LoginActivity.this, "Account Blocked", Toast.LENGTH_SHORT).show();
                }
            }
        }

        //close dbHandler
        dbHandler.close();

        return false;
    }

    //method to check if account is enabled. yes=1 no=0
    private boolean CheckAccountStatus(String s) {

        if(Integer.parseInt(s) == isEnabled){
            return true;
        }
        else{
            return false;
        }
    }

    private void PasswordValidation(String inputPassword, String validPassword) {

        if(inputPassword.equals(validPassword)){
            signInModel.setValidated(true);
        }
        else{
            signInModel.setValidated(false);
            //Toast Invalid Credentials
            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }

    }

    //method to authenticate Account
    private void AccountAuthentication(String emailInput, String dbEmailResult) {

        if(emailInput.equals(dbEmailResult)){
            signInModel.setAuthenticated(true);
        }
        else{
            signInModel.setAuthenticated(false);
            //Toast Invalid Credentials
            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to open Sign Up Activity
    public void openSigUpActivity(View view) {

        Intent intent = new Intent(LoginActivity.this, activity_register.class);
        startActivity(intent);
    }

    //Once loggedIn, method to open MainActivity
    private void openMainActivity() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("FistName", signInModel.getFirstName());
        startActivity(intent);
    }
}