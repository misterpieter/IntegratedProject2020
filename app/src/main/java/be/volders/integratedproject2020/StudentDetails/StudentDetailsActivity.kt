package be.volders.integratedproject2020.StudentDetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.MainActivity
import be.volders.integratedproject2020.Model.SignatureList
import be.volders.integratedproject2020.R
import kotlinx.android.synthetic.main.activity_admin.*

lateinit var rvSignatur: RecyclerView
lateinit var signaturelist : ArrayList<SignatureList>

class StudentDetailsActivity : AppCompatActivity() {

    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details_list)

        rvSignatur = findViewById<RecyclerView>(R.id.rvDetailsList)
        rvSignatur.layoutManager = LinearLayoutManager(this)

        val snumber = intent.getStringExtra("studentSnr").toString()
        signaturelist = databaseHelper!!.getSignatureForDetailsList(snumber)

        val adapter = DetailsListAdapter(this, signaturelist)



        rvSignatur.adapter = adapter

        btnHome.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // finish the current activity
        }
    }

}