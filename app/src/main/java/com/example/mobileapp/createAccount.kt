package com.example.mobileapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class createAccount : AppCompatActivity() {
    var username: EditText? = null
    var password: EditText? = null
    var passwordRetype: EditText? = null
    var signUp: Button? = null
    var logInDirect: TextView? = null
    var db: databaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        username = findViewById(R.id.username_input)
        password = findViewById(R.id.password_input)
        passwordRetype = findViewById(R.id.password_retype)
        signUp = findViewById(R.id.create_account)
        logInDirect = findViewById(R.id.login_screen)
        db = databaseHelper(this)
        signUp?.setOnClickListener(View.OnClickListener {
            val user = username?.getText().toString()
            val pass = password?.getText().toString()
            val passRetype = passwordRetype?.getText().toString()
            println("hello")
            println(user)
            if (user.isEmpty() or pass.isEmpty()) {
                Toast.makeText(this@createAccount, "Empty username or password", Toast.LENGTH_LONG)
                    .show()
            } else if (passRetype != pass) {
                Toast.makeText(this@createAccount, "Passwords do not match", Toast.LENGTH_LONG)
                    .show()
            } else {
                val checkUser = db!!.checkUser(user, pass) //Check is user already exists
                if (checkUser) {
                    Toast.makeText(this@createAccount, "Account Already Exists", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val insertUser =
                        db!!.insertUserData(user, pass) //if user doesn't exist add user
                    if (!insertUser) {
                        Toast.makeText(this@createAccount, "Internal Error", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(
                            this@createAccount,
                            "Account Created Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val toLogIn = Intent(this@createAccount, Login::class.java)
                        this@createAccount.startActivity(toLogIn)
                    }
                }
            }
        })
        logInDirect?.setOnClickListener(View.OnClickListener {
            val intent2 = Intent(this@createAccount, Login::class.java)
            this@createAccount.startActivity(intent2)
        })
    }
}