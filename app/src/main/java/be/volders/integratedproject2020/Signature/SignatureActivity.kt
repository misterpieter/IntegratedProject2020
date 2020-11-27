package be.volders.integratedproject2020.Signature

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.DrawingView
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.StrokeManager
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter


lateinit var recognize:Button
lateinit var clear:Button
lateinit var drawingView:DrawingView
lateinit var btnStore: Button


class SignatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)
        StrokeManager.download()

        val saveStudent = Student( "Barrack","Obama","snumber1","password1")
        btnStore = findViewById(R.id.buttonSave)
        recognize = findViewById(R.id.recognize)
        clear = findViewById(R.id.clear)
        drawingView = findViewById(R.id.drawingView)


        recognize.setOnClickListener {
            StrokeManager.recognize(this)
        }

        clear.setOnClickListener {
            drawingView.clear()
            StrokeManager.clear()
        }
        btnStore.setOnClickListener{
            databaseHelper!!.addStudent(saveStudent)
            Toast.makeText(this, saveStudent.name+"studen stored!", Toast.LENGTH_SHORT).show()
            //Toast.makeText(this, "student is toegevoegd", Toast.LENGTH_SHORT).show()
        }
    }

}
