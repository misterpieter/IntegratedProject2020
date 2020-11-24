package be.volders.integratedproject2020.Signature

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import be.volders.integratedproject2020.DrawingView
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.StrokeManager
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter


lateinit var recognize:Button
lateinit var clear:Button
lateinit var drawingView:DrawingView

class SignatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)
        StrokeManager.download()

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
    }
}
