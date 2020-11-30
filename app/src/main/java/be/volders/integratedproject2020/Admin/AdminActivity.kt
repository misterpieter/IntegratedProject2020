package be.volders.integratedproject2020.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import be.volders.integratedproject2020.MainActivity
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.Students.StudentListActivity
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)



//        btnExportCSV.isVisible = false
//        btnImportCSV.isVisible = false
//        btnShowAllStudents.isVisible = false


        btnExportCSV.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnImportCSV.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnShowAllStudents.setOnClickListener {
            intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }

        btnHome.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}