package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Please Login ")
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)

        mAuth = Firebase.auth
        if (mAuth.currentUser != null) {
            login()
        }
    }

    fun loginSignup(view: View) {
        mAuth.signInWithEmailAndPassword(
            emailEditText?.text.toString(),
            passwordEditText?.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    login()
                } else {
                    mAuth.createUserWithEmailAndPassword(
                        emailEditText?.text.toString(),
                        passwordEditText?.text.toString()
                    ).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userId = mAuth.currentUser?.uid
                            if (userId != null) {
                                FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(userId).child("email")
                                    .setValue(emailEditText?.text.toString())
                            }
                            login()
                        } else {
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()

                        }
                    }
                }
            }
    }

    fun login() {
        val intent = Intent(this, SnapActivity::class.java)
        startActivity(intent)
    }
}
