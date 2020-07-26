package mm.androidlearn.mynotes.view

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import mm.androidlearn.mynotes.BuildConfig
import mm.androidlearn.mynotes.R
import mm.androidlearn.mynotes.utils.AppConstant
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNotesActivity : AppCompatActivity() {

    lateinit var editTextTitle: EditText
    lateinit var editTextDescription: EditText
    lateinit var imageViewNotes: ImageView
    lateinit var buttonSubmit: Button
    val REQUEST_CODE_GALLERY = 2
    val REQUEST_CODE_CAMERA = 4
    var picturepath = ""
    val MY_PERMISSION_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        bindViews()
        clickListeners()
    }

    private fun clickListeners() {
        imageViewNotes.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(checkRequestPermissions()) {
                    setupDialog()
                }
            }
        })

        buttonSubmit.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
               val intent=Intent()
                intent.putExtra(AppConstant.TITLE,editTextTitle.text.toString())
                intent.putExtra(AppConstant.DESCRIPTION,editTextDescription.text.toString())
                intent.putExtra(AppConstant.IMAGE_PATH,picturepath)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }

        })
    }

    private fun checkRequestPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionNeeded = ArrayList<String>()
        if(cameraPermission!= PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(android.Manifest.permission.CAMERA)
        }
        if(storagePermission!= PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(listPermissionNeeded.isNotEmpty()){
            ActivityCompat.requestPermissions(this,listPermissionNeeded.toTypedArray<String>(),MY_PERMISSION_CODE)
            return false
        }
       return true

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            MY_PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    setupDialog()
                }
            }
        }

    }

    private fun setupDialog() {
       val view = LayoutInflater.from(this).inflate(R.layout.dialog_selector,null)
       val textViewCam: TextView = view.findViewById(R.id.textViewCamera)
       val textViewGallery: TextView = view.findViewById(R.id.textViewGallery)
       val dialog = AlertDialog.Builder(this)
               .setView(view)
               .setCancelable(true)
               .create()
        textViewGallery.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,REQUEST_CODE_GALLERY)
                dialog.hide()
            }
        })
        textViewCam.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                var photoFile: File? = null
                photoFile = createImage()
                if(photoFile!= null){
                    val photoURI = FileProvider.getUriForFile(this@AddNotesActivity,BuildConfig.APPLICATION_ID+".provider",photoFile)
                    picturepath = photoFile.absolutePath
                    Log.d("ADDNOTESACT",picturepath)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(takePictureIntent,REQUEST_CODE_CAMERA)
                    dialog.hide()
                }
            }


        })

        dialog.show()
    }

    private fun createImage(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val fileName = "JPEG"+ timeStamp +"_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg",storageDir)
    }

    private fun bindViews() {
       editTextTitle = findViewById(R.id.editTextTitle)
       editTextDescription = findViewById(R.id.editTextDescription)
       imageViewNotes = findViewById(R.id.notesImage)
       buttonSubmit = findViewById(R.id.submitBtn)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == AppCompatActivity.RESULT_OK){
            when (requestCode){
                REQUEST_CODE_GALLERY -> {
                    val selectedImage = data?.data
                    val filepath = arrayOf(MediaStore.Images.Media.DATA)
                    val c =contentResolver.query(selectedImage!!,filepath,null,null,null )
                    c!!.moveToFirst()
                    val columnIndex = c.getColumnIndex(filepath[0])
                    picturepath = c.getString(columnIndex!!)
                    c!!.close()
                    Glide.with(this).load(picturepath).into(imageViewNotes)
                }

                REQUEST_CODE_CAMERA ->{
                    Glide.with(this).load(picturepath).into(imageViewNotes)
                }
            }
        }
    }
}