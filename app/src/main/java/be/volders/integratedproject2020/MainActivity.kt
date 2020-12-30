package be.volders.integratedproject2020

import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import be.volders.integratedproject2020.Admin.AdminActivity
import be.volders.integratedproject2020.Helper.getStudentsFromLocalCSV
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.Signature.SignatureActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val locationPermissionCode = 2
    var parentView:View?=null
    private lateinit var adres : Address
    private lateinit var selectedStudent:Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // lijst hardcoded van studenten
        val studentList = ArrayList<Student>()
        studentList.add(Student( "Admin","Admin","pnumber"))
        studentList.add(Student( "Barrack","Obama","snumber1"))
        studentList.add(Student("Angela", "Merkel","snumber2"))
        studentList.add(Student("Kim", "Jong-Un","snumber3"))
        studentList.add(Student("Donald", "Trump","snumber4"))
        studentList.add(Student("Pieter", "Volders","snumber5"))
        studentList.add(Student("Jonas", "Adriaanssens","snumber6"))
        studentList.add(Student("Halima", "Rahimi","S425316"))
        studentList.add(Student("Halima", "Rahimi","S425315"))

        // lijst imported csv
        var sList = getStudentsFromLocalCSV(this)
        parentView = findViewById(R.id.parentView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        actvStudents.setAdapter(adapter)
        // event click listener op zoekbalk van studenten
        actvStudents.setOnItemClickListener { parent, view, position, id ->
            selectedStudent = parent.getItemAtPosition(position) as Student
            Helper.hideKeyboard(parentView!!,this)
        }

        // LOGIN
        //btnLogin.isEnabled = false

        btnLogin?.setOnClickListener {
            var password:String = etPassword.text.toString()
            var studentname = selectedStudent.name
            var boolAdmin = studentname == "Admin"
            //not admin
            if (!boolAdmin ){
                intent = Intent(this, SignatureActivity::class.java)
                intent.putExtra("studentSnr",selectedStudent.snumber)
                intent.putExtra("studentFirstname",selectedStudent.name)
                intent.putExtra("studentLastname",selectedStudent.lastname)
                startActivity(intent)

                resetPage()
                Toast.makeText(this, "STUDENT", Toast.LENGTH_SHORT).show()
            }
            else if (boolAdmin ) {
                intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                resetPage()
                Toast.makeText(this, "ADMIN", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "FOUTE INPUT!", Toast.LENGTH_SHORT).show()
            }
        }

        // DEV  => visibility in comment zetten

        //etPassword.addTextChangedListener(textWatcher)

        btnAdmin.setOnClickListener {
            intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        btnSignature.setOnClickListener {
            intent = Intent(this, SignatureActivity::class.java)
            startActivity(intent)
        }


    }
/*
    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            Log.d("TAG", "afterTextChanged: ${s.toString()}")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            isPasswordEmpty(s.toString())
        }
    }

    private fun isPasswordEmpty(string:String, minimumPasswordLength:Int = 0) {
        var length = string.trim().length
        btnLogin.isEnabled = length >= minimumPasswordLength
    }
*/
    private fun resetPage(){
        btnLogin.isEnabled = false
        actvStudents.setText("")
        etPassword.setText("")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}




