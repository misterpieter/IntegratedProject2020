package be.volders.integratedproject2020.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import be.volders.integratedproject2020.Helper.getStudentsFromCSVString
import be.volders.integratedproject2020.Helper.getStudentsFromLocalCSV
import be.volders.integratedproject2020.Helper.loadDataFromIntent
import be.volders.integratedproject2020.MainActivity
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.Students.StudentListActivity
import kotlinx.android.synthetic.main.activity_admin.*
import java.io.BufferedReader
import java.io.InputStreamReader

class AdminActivity : AppCompatActivity() {
    val REQUEST_CODE = 1
    lateinit var dataString:String

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
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(intent, REQUEST_CODE)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                data?.data?.let {
                    contentResolver.openInputStream(it)
                }?.let {
                    dataString = loadDataFromIntent(it)!!
                    getStudentsFromCSVString(dataString)
                    Toast.makeText(this, "CSV is ingeladen", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                        this,
                        "Probleem bij het lezen van het bestand",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            try {
//                data?.data?.let {
//                    contentResolver.openInputStream(it)
//                }?.let {
//                    val r = BufferedReader(InputStreamReader(it))
//                    while (true) {
//                        val line: String? = r.readLine() ?: break
//                        println(line)
//                    }
//                }
//            } catch (e: Exception) {
//                Toast.makeText(
//                        this,
//                        "Probleem bij het lezen van het bestand",
//                        Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

}