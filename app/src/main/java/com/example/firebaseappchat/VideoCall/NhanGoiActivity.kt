package com.example.firebaseappchat.VideoCall

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.firebaseappchat.R
import com.example.firebaseappchat.messages.MainActivity
import com.example.firebaseappchat.registerlogin.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class NhanGoiActivity : AppCompatActivity() {
    private lateinit var btn_Tuchoi: ImageView
    private lateinit var ocuRef: DatabaseReference
    private lateinit var NguoiNhan: FirebaseUser
    private lateinit var mediaPlayer: MediaPlayer
    var user: SignUpActivity.getUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nhan_goi)
        supportActionBar?.hide()

        NguoiNhan = FirebaseAuth.getInstance().currentUser!!
        btn_Tuchoi = findViewById(R.id.btn_Tuchoi)
        ocuRef = FirebaseDatabase.getInstance().getReference("user-call")
        mediaPlayer = MediaPlayer.create(this, R.raw.ringing)
        val NguoiGoi = intent.getStringExtra("uidCall").toString()

        Load()
        btn_Tuchoi.setOnClickListener {
            mediaPlayer.stop()
            ocuRef.child(NguoiGoi).child("Calling").removeValue()
                .addOnCompleteListener {
                    ocuRef.child(NguoiNhan.uid).child("Ringing").removeValue()
                        .addOnCompleteListener {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                }
        }
    }

    private fun Load() {
        FirebaseDatabase.getInstance().getReference("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach(){
                    user = it.getValue(SignUpActivity.getUser::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}