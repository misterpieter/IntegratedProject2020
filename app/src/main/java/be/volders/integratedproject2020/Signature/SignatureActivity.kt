package be.volders.integratedproject2020.Signature

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment;
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.DrawingView
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.StrokeManager
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


lateinit var recognize:Button
lateinit var clear:Button
lateinit var drawingView: DrawingView
lateinit var btnStore: Button


class SignatureActivity : AppCompatActivity() {
    private val IMAGE_DIRECTORY = "/Pictures"
    override fun onCreate(savedInstanceState: Bundle?) {
        var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)
        StrokeManager.download()

        var bitmap : Bitmap;
        var path : String;

        val saveStudent = Student("Barrack", "Obama", "snumber1", "password1")
        btnStore = findViewById(R.id.buttonSave)
        recognize = findViewById(R.id.recognize)
        clear = findViewById(R.id.clear)
        drawingView = findViewById(R.id.drawingView)


        recognize.setOnClickListener {
            StrokeManager.recognize(this)
        }

        clear.setOnClickListener {
            drawingView.clear()
            //drawingView.clearCanvas()
            StrokeManager.clear()
        }
        btnStore.setOnClickListener{
            bitmap = drawingView.drawToBitmap()
            path = saveImage(bitmap);
            databaseHelper!!.addStudent(saveStudent)
            Toast.makeText(this, saveStudent.name + "studen stored!", Toast.LENGTH_SHORT).show()
        }

    }
    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
            Log.d("img", wallpaperDirectory.toString())
        }

        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis().toString() + ".jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.getPath()), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("save", "File Saved::--->" + f.getAbsolutePath())
            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }
}
