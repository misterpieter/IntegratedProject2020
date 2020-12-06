package be.volders.integratedproject2020.Signature

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import be.volders.integratedproject2020.*
import be.volders.integratedproject2020.Model.Student
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

        var bitmap : Bitmap
        var path : String

        val saveStudent = Student(intent.getStringExtra("studentFirstname").toString(),
                                  intent.getStringExtra("studentLastname").toString(),
                                  intent.getStringExtra("studentSnr").toString(), "halima")

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
            path = saveImage(bitmap)
            databaseHelper!!.addStudent(saveStudent)
            val bytes = convertSignatur(bitmap)
            databaseHelper!!.insetImage(bytes.toString(),saveStudent.name+ "_" + saveStudent.lastname, saveStudent.snumber)
            //Log.d("check",databaseHelper!!.insetImage(bytes.toString(),saveStudent.name+ "_" + saveStudent.lastname, saveStudent.snumber).toString().plus(" "));
            Log.d("ST", "Signature ok!")
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    //convert
    private  fun convertSignatur(myBitmap: Bitmap): ByteArray {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val byteArray = bytes.toByteArray()
        return byteArray
    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = convertSignatur(myBitmap)
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
            //Log.d("img", wallpaperDirectory.toString())
        }
        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis().toString() + ".png")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes)
            MediaScannerConnection.scanFile(this, arrayOf(f.getPath()), arrayOf("image/png"), null)
            fo.close()
            Log.d("save", "File Saved::--->" + f.getAbsolutePath())
            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

}
