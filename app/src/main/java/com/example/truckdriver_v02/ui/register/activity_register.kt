package com.example.truckdriver_v02.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.truckdriver_v02.R
import com.example.truckdriver_v02.data.SQLite.DbHandler
import com.example.truckdriver_v02.data.account.Driver
import com.example.truckdriver_v02.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random

class activity_register : AppCompatActivity() {
    lateinit var txt_FirstName: EditText
    lateinit var txt_LastName: EditText
    lateinit var txt_Phone: EditText
    lateinit var txt_Email: EditText
    lateinit var txtPassword: EditText
    lateinit var txtConfirmPassword: EditText
    lateinit var btn_Signup: Button

    var accountId: Int = 0
    val driverStatus: Int = 1 //available
    val isEnabled: Int = 1 //enable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txt_FirstName = findViewById(R.id.txt_FirstName)
        txt_LastName = findViewById(R.id.txt_LastName)
        txt_Phone = findViewById(R.id.txt_Phone)
        txt_Email = findViewById(R.id.txt_Email)
        txtPassword = findViewById(R.id.txtPassword)
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword)

        btn_Signup = findViewById(R.id.btn_Signup)

        btn_Signup.setOnClickListener(View.OnClickListener { view ->
            val firstName = txt_FirstName.getText().toString()
            val lastName = txt_LastName.getText().toString()
            val phoneNumber = tryParseLong(txt_Phone.getText().toString())
            val email = txt_Email.getText().toString()
            val password = txtPassword.getText().toString()
            val confirmPassword = txtConfirmPassword.getText().toString()

            //validating first and last names
            val nameValidated = nameValidation(firstName, lastName)
            if (!nameValidated) {
                txt_FirstName.setHintTextColor(resources.getColor(R.color.Red_Alizarin))
                txt_LastName.setHintTextColor(resources.getColor(R.color.Red_Alizarin))
                txt_FirstName.setTextColor(resources.getColor(R.color.Red_Alizarin))
                txt_LastName.setTextColor(resources.getColor(R.color.Red_Alizarin))
            } else {
                txt_FirstName.setHintTextColor(resources.getColor(androidx.cardview.R.color.cardview_dark_background))
                txt_LastName.setHintTextColor(resources.getColor(androidx.cardview.R.color.cardview_dark_background))
                txt_FirstName.setTextColor(resources.getColor(R.color.black))
                txt_LastName.setTextColor(resources.getColor(R.color.black))
            }

            //validating phone number
            val phoneValidated = phoneValidation(phoneNumber)
            if (!phoneValidated) {
                txt_Phone.setHintTextColor(resources.getColor(R.color.Red_Alizarin))
                txt_Phone.setTextColor(resources.getColor(R.color.Red_Alizarin))
            } else {
                txt_Phone.setHintTextColor(resources.getColor(androidx.cardview.R.color.cardview_dark_background))
                txt_Phone.setTextColor(resources.getColor(R.color.black))
            }

            //validating email address
            val emailValidated = emailValidation(email)
            if (!emailValidated) {
                txt_Email.setHintTextColor(resources.getColor(R.color.Red_Alizarin))
                txt_Email.setTextColor(resources.getColor(R.color.Red_Alizarin))
            } else {
                txt_Email.setHintTextColor(resources.getColor(androidx.cardview.R.color.cardview_dark_background))
                txt_Email.setTextColor(resources.getColor(R.color.black))
            }

            //validating password
            val passwordValidated = passwordValidation(password, confirmPassword)
            if (!passwordValidated) {
                txtPassword.setHintTextColor(resources.getColor(R.color.Red_Alizarin))
                txtConfirmPassword.setHintTextColor(resources.getColor(R.color.Red_Alizarin))
                txtPassword.setTextColor(resources.getColor(R.color.Red_Alizarin))
                txtConfirmPassword.setTextColor(resources.getColor(R.color.Red_Alizarin))
            } else {
                txtPassword.setHintTextColor(resources.getColor(androidx.cardview.R.color.cardview_dark_background))
                txtConfirmPassword.setHintTextColor(resources.getColor(androidx.cardview.R.color.cardview_dark_background))
                txtPassword.setTextColor(resources.getColor(R.color.black))
                txtConfirmPassword.setTextColor(resources.getColor(R.color.black))
            }

            //If names, phone, email and password are valid, set AccountID and register it.
            if (nameValidated && phoneValidated && emailValidated && passwordValidated) {
                //calling method to set account id

                accountId = SetAccountID()

                //calling method to add a new account
                AccountRegistration(
                    accountId,
                    firstName,
                    lastName,
                    phoneNumber,
                    email,
                    password,
                    view
                )
            } else {
                Toast.makeText(applicationContext, "Invalid personal data!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    //method to set Account ID based on time and random digit
    private fun SetAccountID(): Int {
        val account_id: String

        val rd = Random()
        val digit = rd.nextInt(100).toString()

        val date = Date()

        @SuppressLint("SimpleDateFormat") val year = SimpleDateFormat("yy")
        val twoDigitYear = year.format(date)

        @SuppressLint("SimpleDateFormat") val month = SimpleDateFormat("MM")
        val twoDigitMonth = month.format(date)

        @SuppressLint("SimpleDateFormat") val day = SimpleDateFormat("dd")
        val twoDigitDay = day.format(date)

        account_id = twoDigitYear + twoDigitMonth + twoDigitDay + digit

        return account_id.toInt()
    }

    //method to add a new account
    private fun AccountRegistration(
        accountId: Int,
        firstName: String,
        lastName: String,
        phoneNumber: Long,
        email: String,
        password: String,
        view: View
    ) {
        //instantiating Account class

        var driverModel: Driver
        try {
            //Instantiating Driver and setting account as activated = 1
            driverModel = Driver(
                accountId,
                firstName,
                lastName,
                phoneNumber,
                email,
                password,
                isEnabled,
                driverStatus
            )

            //connecting to the database and inserting data
            val dbHandler = DbHandler(this@activity_register)

            //insert account register
            val accountCreated = dbHandler.createAccount(
                driverModel.accountId,
                driverModel.firstName,
                driverModel.lastName,
                driverModel.phoneNumber,
                driverModel.eMail,
                driverModel.passWord,
                driverModel.isActive,
                driverModel.driverStatus
            )

            //if account created, open Sign In Activity
            if (accountCreated) {
                Toast.makeText(this@activity_register, "SignUp Successful", Toast.LENGTH_SHORT)
                    .show()

                //calling method to open Sign In Activity.
                openSigInActivity(view, driverModel.eMail, driverModel.passWord)
            } else {
                Toast.makeText(this@activity_register, "SignUp Failed", Toast.LENGTH_SHORT).show()
                driverModel.isActive = 0

                //return to initial Activity (SignIn)
                openSigInActivity(view, driverModel.eMail, driverModel.passWord)
            }

            //close dbHandler
            dbHandler.close()
        } catch (e: Exception) {
            Toast.makeText(this@activity_register, "Error creating account.", Toast.LENGTH_SHORT)
                .show()
            driverModel = Driver(accountId, firstName, lastName, phoneNumber, email, password, 0, 0)
        }
    }

    //method to validate password requirements
    private fun passwordValidation(password: String, confirmPassword: String): Boolean {
        return if (password == confirmPassword && password.length > 7) {
            true
        } else {
            false
        }
    }

    //method to validate email requirements
    private fun emailValidation(email: String): Boolean {
        return if ((email.indexOf('@') != -1) && (email.indexOf('.') != -1)) {
            true
        } else {
            false
        }
    }

    //method to validate phone requirements
    private fun phoneValidation(phoneNumber: Long): Boolean {
        val textPhone = phoneNumber.toString()

        return if ((textPhone.length > 9) && (textPhone.length < 15)) {
            true
        } else {
            false
        }
    }

    //method to validate first and last names
    private fun nameValidation(firstName: String, lastName: String): Boolean {
        return if (hasText(firstName) && hasText(lastName)) {
            true
        } else {
            false
        }
    }

    //method to check if the variable has text character
    private fun hasText(text: String?): Boolean {
        return text != null && !text.isEmpty() && !text.trim { it <= ' ' }.isEmpty()
    }

    //Integer Try Parse Method. If invalid, return 0.
    private fun tryParseLong(Entry: String): Long {
        return try {
            Entry.toLong()
        } catch (e: Exception) {
            0
        }
    }

    //method to open SignIn Activity
    fun openSigInActivity(view: View?, email: String?, password: String?) {
        val intent = Intent(this@activity_register, LoginActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }
}