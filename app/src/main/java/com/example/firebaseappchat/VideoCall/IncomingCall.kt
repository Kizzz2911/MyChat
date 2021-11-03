package com.example.firebaseappchat.VideoCall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.firebaseappchat.R
import com.example.firebaseappchat.registerlogin.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.opentok.android.*


const val RC_VIDEO_APP_PERM: Int = 124

class IncomingCall : AppCompatActivity() {

    private lateinit var btnClose: ImageView
    private lateinit var Ref: DatabaseReference
    private lateinit var pubController: FrameLayout
    private lateinit var subController: FrameLayout

    var toUser: SignUpActivity.getUser? = null
    private lateinit var NguoiDung: FirebaseUser
    lateinit var mSession: Session
    lateinit var mPublisher: Publisher
    lateinit var mSubscriber: Subscriber

    private var API_KEY: String = "47367941"

    private var SESSION_ID: String =
        "1_MX40NzM2Nzk0MX5-MTYzNTU4NzM4MTQyN34vYytyMUJjekhyZGQ3RDl0WWdXWVFoSFR-fg"
    private var TOKEN: String =
        "T1==cGFydG5lcl9pZD00NzM2Nzk0MSZzaWc9NmM5ODlhYTI1NmFlOTc3ZjI3ZWUzMWMxYmZiNWI1MzMyYWI2MDEwOTpzZXNzaW9uX2lkPTFfTVg0ME56TTJOemswTVg1LU1UWXpOVFU0TnpNNE1UUXlOMzR2WXl0eU1VSmpla2h5WkdRM1JEbDBXV2RYV1ZGb1NGUi1mZyZjcmVhdGVfdGltZT0xNjM1NTg3NDI1Jm5vbmNlPTAuNDY5MDAwMzc1MTQ3ODQxNzYmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYzODE3OTQyNCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ=="
    private lateinit var LOG_TAG: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nhan_call)

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        Ref = FirebaseDatabase.getInstance().reference.child("user-call")
        NguoiDung = FirebaseAuth.getInstance().currentUser!!
        btnClose = findViewById(R.id.close_video_btn)

        CheckGoi()

        btnClose.setOnClickListener {
            Ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun CheckGoi() {
        Ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Goi()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun Goi() {
        Ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(NguoiDung.uid).hasChild("Ringing")){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

