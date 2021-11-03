package com.example.firebaseappchat.VideoCall


import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import androidx.core.view.isVisible
import com.example.firebaseappchat.NewMessActivity
import com.example.firebaseappchat.R
import com.example.firebaseappchat.messages.MainActivity
import com.example.firebaseappchat.registerlogin.LoginActivity
import com.example.firebaseappchat.registerlogin.SignUpActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import fragment.HomeFragment
import kotlinx.android.synthetic.main.user_row.view.*
import kotlinx.android.synthetic.main.user_row_call.view.*
import java.util.*
import kotlin.collections.HashMap

class Outcoming : AppCompatActivity() {
    private lateinit var username: TextView
    private lateinit var btnCancell: ImageView
    private lateinit var btnAccept: ImageView
    private lateinit var avatar: CircleImageView
    private lateinit var mediaPlayer: MediaPlayer

    var toUser: SignUpActivity.getUser? = null

    private lateinit var ocuRef: DatabaseReference

    private lateinit var NguoiGoi: FirebaseUser

    private lateinit var checker: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outcoming)
        toUser = intent.getParcelableExtra(VideoChatActivity.USER_KEY)

        NguoiGoi = FirebaseAuth.getInstance().currentUser!!

        username = findViewById(R.id.txtUsername_incomingCall)
        avatar = findViewById(R.id.img_userIncomingCall)
        btnCancell = findViewById(R.id.btn_rejectcall)

        ocuRef = FirebaseDatabase.getInstance().getReference("user-call")
        mediaPlayer = MediaPlayer.create(this, R.raw.ringing)


        checker = "clicked"
        Check()
        if (toUser != null) {
            username.text = toUser!!.name
            Picasso.get().load(toUser!!.Urlphoto).into(avatar)
        }

        btnCancell.setOnClickListener() {
            mediaPlayer.stop()
            checker = "clicked"
            cancelCalling()
        }
    }

    private fun Check() {
        FirebaseDatabase.getInstance().getReference("user-call")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uid = FirebaseAuth.getInstance().uid
                    if (!snapshot.child(uid.toString()).hasChild("Calling")) {
                        mediaPlayer.stop()
                        startActivity(Intent(this@Outcoming, VideoChatActivity::class.java))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val name = NguoiGoi.displayName.toString()
        val uid = NguoiGoi.uid
        mediaPlayer.start()
        val callingInfo = mapOf<String, String>(
            "name" to name,
            "uid" to uid
        )
        //Call Event
        ocuRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ocuRef.child(NguoiGoi.uid).child("Calling").child("uidNghe")
                    .setValue(toUser?.uid.toString())
                ocuRef.child(NguoiGoi.uid)
                    .updateChildren(callingInfo)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val ringingInfo = mapOf<String, String>(
                                "name" to toUser?.name.toString(),
                                "uid" to toUser?.uid.toString()
                            )
                            ocuRef.child(toUser!!.uid).child("Ringing").child("uidCall")
                                .setValue(NguoiGoi.uid)
                            ocuRef.child(toUser!!.uid)
                                .updateChildren(ringingInfo)
                        }
                    }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun cancelCalling() {
        mediaPlayer.stop()
        ocuRef.child(NguoiGoi.uid).child("Calling").child(toUser?.uid.toString()).removeValue()
            .addOnCompleteListener {
                ocuRef.child(toUser?.uid.toString()).child("Ringing").child(NguoiGoi.uid)
                    .removeValue().addOnCompleteListener {
                        startActivity(Intent(this, VideoChatActivity::class.java))
                    }
            }
    }

}

