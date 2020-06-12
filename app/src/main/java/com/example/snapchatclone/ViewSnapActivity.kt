package com.example.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapActivity : AppCompatActivity() {

    var messageTextView: TextView? = null
    var snapImageView: ImageView? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(" Snaps from "+FirebaseAuth.getInstance().currentUser!!.email!!)
        messageTextView = findViewById(R.id.textmessage)
        snapImageView = findViewById(R.id.snapImageView)
        auth = Firebase.auth
        messageTextView?.text = intent.getStringExtra("message")
        val imageDownloader = ImageDownloader()
        val myimage: Bitmap
        try {
            myimage = imageDownloader.execute(intent.getStringExtra("imageURL"))
                    .get()!!
            snapImageView?.setImageBitmap(myimage)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    inner class ImageDownloader : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            try {
                val url = URL(urls[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()
                val `in` = urlConnection.inputStream
                return BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().getReference().child("users").child(auth.currentUser!!.uid).child("snaps").child(intent.getStringExtra("snapkey")).removeValue()
        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName")).delete()
    }


}
