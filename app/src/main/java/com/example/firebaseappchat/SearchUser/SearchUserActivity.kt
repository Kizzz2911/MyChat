package com.example.firebaseappchat.SearchUser

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseappchat.Friend.Profile_Other_User_Activity
import com.example.firebaseappchat.NewMessActivity.Companion.USER_KEY
import com.example.firebaseappchat.R
import com.example.firebaseappchat.registerlogin.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.layout_list_find.view.*
import kotlinx.android.synthetic.main.user_row.view.iVUser
import kotlinx.android.synthetic.main.user_row.view.TxtUserName

class SearchUserActivity : AppCompatActivity() {

    private lateinit var mSearchField: EditText
    private lateinit var mResultList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        mSearchField = findViewById(R.id.TxtSearch)
        mResultList = findViewById(R.id.ListSearch)

        mSearchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    firebaseUserSearch(s.toString())
                } else {
                    mResultList.adapter = null
                }
            }
        })
    }

    private fun firebaseUserSearch(name: String?) {
        val ref = FirebaseDatabase.getInstance().getReference("/user")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach() {
                    val user = it.getValue(SignUpActivity.getUser::class.java)
                    val userNguoiDung = FirebaseAuth.getInstance().currentUser
                    if (userNguoiDung != null) {
                        if (user != null && name != "") {
                            if (userNguoiDung.uid != user.uid && user.name.contains(name.toString(),true)) {
                                adapter.add(UItem(user))
                            }
                        }
                    }
                }
                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UItem

                    val intent = Intent(view.context, Profile_Other_User_Activity::class.java)
                    Log.d("New Message", userItem.user.name)
                    intent.putExtra("USER_KEY", userItem.user)
                    startActivity(intent)
                    finish()
                }
                mResultList.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error: ", "${error.message}")
            }
        })
    }

    class UItem(val user: SignUpActivity.getUser) : Item<GroupieViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.layout_list_find
        }

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            if (user.Urlphoto.isEmpty()) {
                val ImgDefault = "https://th.bing.com/th/id/R.502a73beb3f9263ca076457d525087c6?" +
                        "rik=OP8RShVgw6uFhQ&riu=http%3a%2f%2fdvdn247.net%2fwp-content%2fuploads%2f2020%2f07%2" +
                        "favatar-mac-dinh-1.png&ehk=NSFqDdL3jl9cMF3B9A4%2bzgaZX3sddpix%2bp7R%2bmTZHsQ%3d&risl=" +
                        "&pid=ImgRaw&r=0"
                viewHolder.itemView.Txtusername.text = user.name
                Picasso.get().load(ImgDefault).into(viewHolder.itemView.IVUser)
                Log.d("New Message :", "User Name : ${user.name} \n PhotoUrl : $ImgDefault")
            } else {
                viewHolder.itemView.Txtusername.text = user.name
                Picasso.get().load(user.Urlphoto).into(viewHolder.itemView.IVUser)
                Log.d("New Message :", "User Name : ${user.name} \n PhotoUrl : ${user.Urlphoto}")
            }
        }
    }
}