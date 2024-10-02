package com.example.truckdriver_v02.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.truckdriver_v02.MainActivity
import com.example.truckdriver_v02.R
import com.example.truckdriver_v02.data.SQLite.DbHandler
import com.example.truckdriver_v02.data.account.Driver
import com.example.truckdriver_v02.data.session.Session
import com.example.truckdriver_v02.databinding.ActivityLoginBinding
import com.example.truckdriver_v02.ui.register.activity_register
import java.util.Date

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var txt_email: EditText
    lateinit var txt_password: EditText
    lateinit var btn_ask_signup: Button
    lateinit var btn_signin: Button

    //Instantiating SignIn Model
    var signInModel: Driver = Driver(null.toString(), null.toString())

    private val isEnabled = 1 //account enabled: yes=1 no=0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        val loadingProgressBar = binding!!.loading

        txt_email = findViewById(R.id.txt_Email)
        txt_password = findViewById(R.id.txtPassword)

        btn_signin = findViewById(R.id.btn_SignIn)
        btn_ask_signup = findViewById(R.id.btn_ask_signUp)

        //get email address and password from Sign Up success.
        val extras = intent.extras
        if (extras != null) {
            txt_email.setText(extras.getString("email"))
            txt_password.setText(extras.getString("password"))
        }

        //SignIn button
        btn_signin.setOnClickListener(View.OnClickListener {
            //calling method to Sign In including email and password parameters
            val signedIn = SignIn(txt_email.getText().toString(), txt_password.getText().toString())
            if (signedIn) {
                //calling method to start session

                startSession()

                Toast.makeText(
                    this@LoginActivity,
                    "Welcome, " + signInModel.firstName,
                    Toast.LENGTH_SHORT
                ).show()

                //calling method to open MainActivity
                openMainActivity()
            }
        })

        btn_ask_signup.setOnClickListener(View.OnClickListener { v -> // Method to open Sign Up Activity
            openSigUpActivity(v)
        })
    }

    private fun startSession() {
        finishingPreviousSessions() //call method to finish previous sessions

        val session = Session(signInModel.accountId) //get session id

        session.signedIn = signInModel.isActive //set session on

        //get date
        val date = Date()

        session.time_begin = date.toString()

        session.time_end = ""

        //connecting to the database and inserting data
        val dbHandler = DbHandler(this@LoginActivity)

        val StartSession =
            dbHandler.StartSession(
                session.accountId,
                session.time_begin,
                session.time_end,
                session.signedIn
            )

        //close dbHandler
        dbHandler.close()
    }

    private fun finishingPreviousSessions() {
        //connecting to the database and inserting data

        val dbHandler = DbHandler(this@LoginActivity)
        val sessionsEnded = dbHandler.endAllSession()
        dbHandler.close()
    }

    private fun SignIn(email: String, password: String): Boolean {
        signInModel.eMail = email
        signInModel.passWord = password

        //Instantiating DbHandler
        val dbHandler = DbHandler(this@LoginActivity)

        //read password and account status from database to sign in
        val driver = dbHandler.getAccountCredential(signInModel.eMail)

        //calling method to authenticate account
        AccountAuthentication(signInModel.eMail, driver.eMail)

        if (signInModel.isAuthenticated == true) {
            //calling method to validate password

            PasswordValidation(signInModel.passWord, driver.passWord)

            if (signInModel.isValidated == true) {
                //calling method to check if account is enabled
                val isEnabled = CheckAccountStatus(driver.isActive)

                if (isEnabled) {
                    //get account details to start session
                    signInModel.eMail = driver.eMail
                    signInModel.isActive = driver.isActive
                    signInModel.firstName = driver.firstName
                    signInModel.accountId = driver.accountId

                    //return true for user authenticated, validated and enabled.
                    return true
                } else {
                    signInModel.isAuthenticated = false
                    signInModel.isValidated = false
                    Toast.makeText(this@LoginActivity, "Account Blocked", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //close dbHandler
        dbHandler.close()

        return false
    }

    //method to check if account is enabled. yes=1 no=0
    private fun CheckAccountStatus(s: Int?): Boolean {
        return if (s == isEnabled) {
            true
        } else {
            false
        }
    }

    private fun PasswordValidation(inputPassword: String?, validPassword: String?) {
        if (inputPassword == validPassword) {
            signInModel.isValidated = true
        } else {
            signInModel.isValidated = false
            //Toast Invalid Credentials
            Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }

    //method to authenticate Account
    private fun AccountAuthentication(emailInput: String?, dbEmailResult: String?) {
        if (emailInput == dbEmailResult) {
            signInModel.isAuthenticated = true
        } else {
            signInModel.isAuthenticated = false
            //Toast Invalid Credentials
            Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to open Sign Up Activity
    fun openSigUpActivity(view: View?) {
        val intent = Intent(this@LoginActivity, activity_register::class.java)
        startActivity(intent)
    }

    //Once loggedIn, method to open MainActivity
    private fun openMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("FistName", signInModel.firstName)
        startActivity(intent)
    }
}