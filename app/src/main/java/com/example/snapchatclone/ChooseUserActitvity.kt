package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class ChooseUserActitvity : AppCompatActivity() {
    var chooserListView: ListView? = null
    var emails: ArrayList<String> = ArrayList()
    var keys: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user_actitvity)
        setTitle("where to send")
        chooserListView = findViewById(R.id.chooseUserListView)
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        chooserListView?.adapter = adapter

        FirebaseDatabase.getInstance().getReference().child("users")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val email = p0.child("email").value as String
                    emails.add(email)
                    keys.add(p0.key!!)
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(p0: DatabaseError) {}
                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
                override fun onChildRemoved(p0: DataSnapshot) {}

            })
        chooserListView?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val snapMap: Map<String, String> =
                    mapOf(
                        "from" to FirebaseAuth.getInstance().currentUser!!.email!!,
                        "imageName" to intent.getStringExtra("imageName"),
                        "imageURL" to intent.getStringExtra("imageURL"),
                        "message" to intent.getStringExtra("message")
                    )
                FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(i))
                    .child("snaps").push().setValue(snapMap)
                //Toast.makeText(this,intent.getStringExtra("imageURL"),Toast.LENGTH_LONG).show()
                val intent = Intent(this, SnapActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
    }
}
