package be.volders.integratedproject2020.Signature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import be.volders.integratedproject2020.DrawingView
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.StrokeManager

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
