package be.volders.integratedproject2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}