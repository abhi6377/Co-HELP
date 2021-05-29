package com.app_dev.co_help

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_send_email.*

class SendEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_email)

        /* CHANGED BY ME 9 - getting rEmail from MainFeedsActivity from onMailClicked */
        val intent = getIntent()
        val rEmail = intent.getStringExtra("rEmail")
        recipientEmail.text="Recipient: $rEmail"
//        Toast.makeText(this,"EmailIdSEA: $rEmail", Toast.LENGTH_SHORT).show()
        sendEmailBtn.setOnClickListener{
            val recipient = rEmail
            val subject = subject.text.toString()
            val  msg = message.text
            if(recipient?.isNotEmpty()!!)
            {
                sendEmail(recipient,subject,msg)
            }else{
                Toast.makeText(this,"Recipient are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendEmail(recipient: String, subject: String, msg: CharSequence) {

        val intent = Intent(Intent.ACTION_SEND)

        intent.data = Uri.parse("mailto")
        intent.type = "text/plain"

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(recipient))

        intent.putExtra(Intent.EXTRA_SUBJECT, "Volunteering for plasma donation")

        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Hello, \n I would like to offer you help.\n Contact me via this email"
        )

        try {
            startActivity(Intent.createChooser(intent, "Choose Email Client"))

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}