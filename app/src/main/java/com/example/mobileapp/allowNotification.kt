package com.example.mobileapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class allowNotification : AppCompatActivity() {
    var send: Button? = null
    var deny: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_allow_notification)
        send = findViewById(R.id.allow)
        val phoneNumber = "5551234567"
        val message = "You will recieve SMS from event tracker."
        send?.setOnClickListener(View.OnClickListener { //Requesting permission from user to send messages
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS), 1)

            //Send user message if permission is granted
            if (applicationContext.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                try {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                    Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
                val eventScreen = Intent(this@allowNotification, events::class.java)
                this@allowNotification.startActivity(eventScreen)
            }
        })


        //Proceed without sending message if user doesn't grant permission
        deny = findViewById(R.id.dontallow)
        deny?.setOnClickListener(View.OnClickListener {
            val eventScreen = Intent(this@allowNotification, events::class.java)
            this@allowNotification.startActivity(eventScreen)
        })
    }
}