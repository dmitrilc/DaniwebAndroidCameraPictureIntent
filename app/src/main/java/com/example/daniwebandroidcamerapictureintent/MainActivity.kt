package com.example.daniwebandroidcamerapictureintent

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize temp image directory on first launch
        val tempImageUri = initTempUri()

        registerTakePictureLauncher(tempImageUri) //Binds button to launch camera activity
    }

    private fun initTempUri(): Uri {
        //gets the temp_images dir
        val tempImagesDir = File(
            applicationContext.filesDir, //this function gets the external cache dir
            getString(R.string.temp_images_dir)) //gets the directory for the temporary images dir

        tempImagesDir.mkdir() //Create the temp_images dir

        //Creates the temp_image.jpg file
        val tempImage = File(
            tempImagesDir, //prefix the new abstract path with the temporary images dir path
            getString(R.string.temp_image)) //gets the abstract temp_image file name

        //Returns the Uri object to be used with ActivityResultLauncher
        return FileProvider.getUriForFile(
            applicationContext,
            getString(R.string.authorities),
            tempImage)
    }

    private fun registerTakePictureLauncher(path: Uri) {
        val button = findViewById<Button>(R.id.button) //gets the Button object
        val imageView = findViewById<ImageView>(R.id.imageView) //gets the ImageView object

        //Creates the ActivityResultLauncher
        val resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
            imageView.setImageURI(null) //rough handling of image changes. Real code need to handle different API levels.
            imageView.setImageURI(path)
        }

        //Launches the camera when button is pressed.
        button.setOnClickListener {
            resultLauncher.launch(path) //launches the activity here
        }
    }

}