package be.volders.integratedproject2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import be.volders.integratedproject2020.Signature.SignatureActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSignature.setOnClickListener {
            intent = Intent(this, SignatureActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnCoordinates.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnExportCsv.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnImportCSV.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }
        btnImportCSV.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }
    }
}