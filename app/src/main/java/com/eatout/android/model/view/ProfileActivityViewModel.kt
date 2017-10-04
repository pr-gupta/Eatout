package com.eatout.android.model.view

import android.content.Context
import android.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by prashant.gup on 03/10/17.
 */
class ProfileActivityViewModel(
        val firstName: ObservableField<String> = ObservableField(""),
        val lastName: ObservableField<String> = ObservableField(""),
        val profileImageURL: ObservableField<String> = ObservableField(""),
        val emailID: ObservableField<String> = ObservableField(""),
        val isImageLoading: ObservableField<Boolean> = ObservableField(false),
        val context: Context
) {

    private val mListener: OnChangeListener

    init {
        if (context !is OnChangeListener)
            throw throw RuntimeException(context.toString() + " must implement OnChangeListener")
        mListener = context

        FirebaseDatabase.getInstance().reference
                .child("users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        p0?.let {
                            firstName.set("${(it.value as HashMap<*, *>)["FirstName"]}")
                            lastName.set("${(it.value as HashMap<*, *>)["LastName"]}")
                            profileImageURL.set("${(it.value as HashMap<*, *>)["profileImageURL"]}")
                            mListener.updateProfileImage(profileImageURL.get())
                        }
                    }

                })

        emailID.set(FirebaseAuth.getInstance().currentUser!!.email)

    }

    fun chooseImage() {
        mListener.chooseImage()
    }

    fun onClickUpdate() {
        val ref = FirebaseDatabase.getInstance().reference
                .child("users").child(FirebaseAuth.getInstance().currentUser!!.uid)

        ref.child("FirstName").setValue(firstName.get())
        ref.child("LastName").setValue(lastName.get())

        mListener.finishActivity()
    }

    interface OnChangeListener {
        fun updateProfileImage(src: String)
        fun chooseImage()
        fun finishActivity()
    }
}