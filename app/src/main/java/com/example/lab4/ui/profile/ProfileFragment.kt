package com.example.lab4.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lab4.R
import com.example.lab4.utils.SessionManager
import java.io.File
import java.io.FileOutputStream
import android.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var ivProfile: ImageView

    // Képkiválasztó launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                val file = getFileFromUri(it)
                if (file != null) {
                    viewModel.uploadProfileImage(file)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivProfile = view.findViewById(R.id.ivProfileImage)
        val tvUsername = view.findViewById<TextView>(R.id.tvUsername)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvDesc = view.findViewById<TextView>(R.id.tvDescription)
        val btnUpload = view.findViewById<Button>(R.id.btnUploadImage)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                tvUsername.text = profile.username
                tvEmail.text = profile.email
                tvDesc.text = profile.description ?: "No description provided."


                if (!profile.profileImageBase64.isNullOrEmpty()) {
                    try {
                        val decodedString = Base64.decode(profile.profileImageBase64, Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        ivProfile.setImageBitmap(decodedByte)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }

        viewModel.message.observe(viewLifecycleOwner) { msg ->
            if (msg.isNotEmpty()) Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }


        btnLogout.setOnClickListener {
            SessionManager(requireContext()).clearAuthToken()

            activity?.finish()
            activity?.intent?.let { intent ->
                startActivity(intent)
            }
        }
    }


    private fun getFileFromUri(uri: Uri): File? {
        val context = requireContext()
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return tempFile
    }

    private fun showEditProfileDialog() {
        val currentProfile = viewModel.profile.value ?: return

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)
        val etUsername = dialogView.findViewById<TextInputEditText>(R.id.etEditUsername)
        val etDescription = dialogView.findViewById<TextInputEditText>(R.id.etEditDescription)

        etUsername.setText(currentProfile.username)
        etDescription.setText(currentProfile.description)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newUsername = etUsername.text.toString()
                val newDescription = etDescription.text.toString()

                if (newUsername.isNotEmpty()) {
                    viewModel.updateProfileDetails(newUsername, newDescription)
                } else {
                    Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}