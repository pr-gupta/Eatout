package com.eatout.android

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.eatout.android.databinding.ActivityProfileBinding
import com.eatout.android.model.view.ProfileActivityViewModel
import com.eatout.android.util.imgur.controller.PostImageController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class ProfileActivity : Activity(), ProfileActivityViewModel.OnChangeListener {

    private lateinit var _binding: ActivityProfileBinding
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        _binding.viewModel = ProfileActivityViewModel(context = this)
    }

    override fun updateProfileImage(src: String) {
        Log.v(TAG, src)
        Glide.with(applicationContext).load(src).asBitmap().centerCrop().into(_binding.profileImage2)
    }

    override fun chooseImage() {
        checkStorageAccessPermission()
    }

    override fun finishActivity() {
        finish()
    }

    private fun checkStorageAccessPermission() {
        val res = this.checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (res != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, kotlin.arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        else
            pickImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        when (requestCode) {
            1 -> {
                if (grantResults!!.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "User gave permission to get access storage")
                    pickImage()

                } else {
                    Log.w(TAG, "User didn't gave permission to access storage")
                }
                return
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun pickImage() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    private val PICK_IMAGE = 1

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            val filename = File(Environment.getExternalStorageDirectory(), "imageName.jpg")
            val out = FileOutputStream(filename)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out)
            out.flush()
            out.close()

            val base64 = getStringImage(filename)

            Log.i(TAG, base64)
            PostImageController({ imgUrl ->
                FirebaseDatabase.getInstance().reference
                        .child("users").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("profileImageURL")
                        .setValue(imgUrl)
            }
            ).postImage(base64!!)
        }
    }

    private fun getStringImage(file: File): String? {
        try {
            val fin = FileInputStream(file)
            val imageBytes = ByteArray(file.length().toInt())
            fin.read(imageBytes, 0, imageBytes.size)
            fin.close()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            Toast.makeText(this, "Image Size is Too High to upload.", Toast.LENGTH_SHORT).show()
        }

        return null
    }


}
