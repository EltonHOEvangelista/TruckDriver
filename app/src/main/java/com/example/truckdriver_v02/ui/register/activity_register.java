package com.example.truckdriver_v02.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.truckdriver_v02.R;
import com.example.truckdriver_v02.data.SQLite.DbHandler;
import com.example.truckdriver_v02.data.account.Driver;
import com.example.truckdriver_v02.ui.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class activity_register extends AppCompatActivity {

    EditText txt_FirstName;
    EditText txt_LastName;
    EditText txt_Phone;
    EditText txt_Email;
    EditText txtPassword;
    EditText txtConfirmPassword;
    Button btn_Signup;

    int accountId;
    final int driverStatus = 1; //available
    final int isEnabled = 1;    //enable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_FirstName = findViewById(R.id.txt_FirstName);
        txt_LastName = findViewById(R.id.txt_LastName);
        txt_Phone = findViewById(R.id.txt_Phone);
        txt_Email = findViewById(R.id.txt_Email);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        btn_Signup = findViewById(R.id.btn_Signup);

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = txt_FirstName.getText().toString();
                String lastName = txt_LastName.getText().toString();
                long phoneNumber = tryParseLong(txt_Phone.getText().toString());
                String email = txt_Email.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

                //validating first and last names
                boolean nameValidated = nameValidation(firstName, lastName);
                if (!nameValidated){
                    txt_FirstName.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_LastName.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_FirstName.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_LastName.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                }
                else{
                    txt_FirstName.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txt_LastName.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txt_FirstName.setTextColor(getResources().getColor(R.color.black));
                    txt_LastName.setTextColor(getResources().getColor(R.color.black));
                }

                //validating phone number
                boolean phoneValidated = phoneValidation(phoneNumber);
                if (!phoneValidated){
                    txt_Phone.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_Phone.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                }
                else{
                    txt_Phone.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txt_Phone.setTextColor(getResources().getColor(R.color.black));
                }

                //validating email address
                boolean emailValidated = emailValidation(email);
                if (!emailValidated){
                    txt_Email.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_Email.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                }
                else{
                    txt_Email.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txt_Email.setTextColor(getResources().getColor(R.color.black));
                }

                //validating password
                boolean passwordValidated = passwordValidation(password, confirmPassword);
                if (!passwordValidated){
                    txtPassword.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txtConfirmPassword.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txtPassword.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txtConfirmPassword.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                }
                else{
                    txtPassword.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txtConfirmPassword.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txtPassword.setTextColor(getResources().getColor(R.color.black));
                    txtConfirmPassword.setTextColor(getResources().getColor(R.color.black));
                }

                //If names, phone, email and password are valid, set AccountID and register it.
                if (nameValidated && phoneValidated && emailValidated && passwordValidated){

                    //calling method to set account id
                    accountId = SetAccountID();

                    //calling method to add a new account
                    AccountRegistration(accountId, firstName, lastName, phoneNumber, email, password, view);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid personal data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //method to set Account ID based on time and random digit
    private int SetAccountID() {

        String account_id;

        Random rd = new Random();
        String digit = String.valueOf(rd.nextInt(100));

        Date date = new Date();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat year = new SimpleDateFormat("yy");
        String twoDigitYear = year.format(date);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat month = new SimpleDateFormat("MM");
        String twoDigitMonth = month.format(date);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat day = new SimpleDateFormat("dd");
        String twoDigitDay = day.format(date);

        account_id = twoDigitYear + twoDigitMonth + twoDigitDay + digit;

        return  Integer.parseInt(account_id);
    }

    //method to add a new account
    private void AccountRegistration(int accountId, String firstName, String lastName, Long phoneNumber, String email, String password, View view) {

        //instantiating Account class
        Driver driverModel;
        try {
            //Instantiating Driver and setting account as activated = 1
            driverModel = new Driver(accountId, firstName, lastName, phoneNumber, email, password, isEnabled, driverStatus);

            //connecting to the database and inserting data
            DbHandler dbHandler = new DbHandler(activity_register.this);

            //insert account register
            boolean accountCreated =  dbHandler.createAccount(
                    driverModel.getAccountId(),
                    driverModel.getFirstName(),
                    driverModel.getLastName(),
                    driverModel.getPhone(),
                    driverModel.getEmail(),
                    driverModel.getPassword(),
                    driverModel.getActiveAccount(),
                    driverModel.getDriverStatus());

            //if account created, open Sign In Activity
            if(accountCreated){

                Toast.makeText(activity_register.this, "SignUp Successful", Toast.LENGTH_SHORT).show();

                //calling method to open Sign In Activity.
                openSigInActivity(view, driverModel.getEmail(), driverModel.getPassword());
            }
            else{

                Toast.makeText(activity_register.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
                driverModel.setActiveAccount(0);

                //return to initial Activity (SignIn)
                openSigInActivity(view, driverModel.getEmail(), driverModel.getPassword());
            }

            //close dbHandler
            dbHandler.close();
        }
        catch (Exception e){
            Toast.makeText(activity_register.this, "Error creating account.", Toast.LENGTH_SHORT).show();
            driverModel = new Driver(accountId, firstName, lastName, phoneNumber, email, password, 0, 0);
        }
    }

    //method to validate password requirements
    private boolean passwordValidation(String password, String confirmPassword) {

        if (password.equals(confirmPassword) && password.length() > 7){
            return true;
        }
        else{
            return false;
        }
    }

    //method to validate email requirements
    private boolean emailValidation(String email) {

        if ((email.indexOf('@') != -1) && (email.indexOf('.') != -1)){
            return true;
        }
        else{
            return false;
        }
    }

    //method to validate phone requirements
    private boolean phoneValidation(long phoneNumber) {

        String textPhone = String.valueOf(phoneNumber);

        if ((textPhone.length() > 9) && (textPhone.length() < 15)){
            return true;
        }
        else{
            return false;
        }
    }

    //method to validate first and last names
    private boolean nameValidation(String firstName, String lastName) {

        if (hasText(firstName) && hasText(lastName)){
            return true;
        }
        else{
            return false;
        }
    }
    //method to check if the variable has text character
    private boolean hasText(String text) {

        return text != null && !text.isEmpty() && !text.trim().isEmpty();
    }

    //Integer Try Parse Method. If invalid, return 0.
    private long tryParseLong(String Entry) {

        try {
            return Long.parseLong(Entry);
        }
        catch (Exception e){
            return 0;
        }
    }

    //method to open SignIn Activity
    public void openSigInActivity(View view, String email, String password) {

        Intent intent = new Intent(activity_register.this, LoginActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }
}