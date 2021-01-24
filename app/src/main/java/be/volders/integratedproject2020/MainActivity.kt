package be.volders.integratedproject2020

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import be.volders.integratedproject2020.Admin.AdminActivity
import be.volders.integratedproject2020.Helper.getStudentsFromLocalCSV
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.Signature.SignatureActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private val locationPermissionCode = 2
    var parentView:View?=null
    private lateinit var adres : Address
    private lateinit var selectedStudent:Student
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)

    private val ADMIN_NAME = "Admin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPermissions()
        setContentView(R.layout.activity_main)

        // LOGIN
        btnLogin.isEnabled = false
        etPassword.isVisible = false

        // lijst imported csv
        var sList = getStudentsFromLocalCSV(this)

        try {
            var slist = databaseHelper!!.getAllStudent()
            for(r in slist){
                sList.add(r)
            }
        }catch (e: Exception){
            Log.d("kld", "er zijn geen student in de db ")
        }

        parentView = findViewById(R.id.parentView)

        //val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sList)
        actvStudents.setAdapter(adapter)

        // event click listener op zoekbalk van studenten
        actvStudents.setOnItemClickListener { parent, view, position, id ->
            selectedStudent = parent.getItemAtPosition(position) as Student
            Helper.hideKeyboard(parentView!!,this)

            var boolAdmin = selectedStudent.name == ADMIN_NAME

            if (!boolAdmin) {
                etPassword.isVisible = false
                btnLogin.isEnabled = true
            }
            else etPassword.isVisible = true
        }



        btnLogin?.setOnClickListener {
            var password:String = etPassword.text.toString()
            var studentname = selectedStudent.name
            var boolAdmin = studentname == ADMIN_NAME
            //not admin
            if (!boolAdmin ){
                intent = Intent(this, SignatureActivity::class.java)
                intent.putExtra("studentSnr",selectedStudent.snumber)
                intent.putExtra("studentFirstname",selectedStudent.name)
                intent.putExtra("studentLastname",selectedStudent.lastname)
                startActivity(intent)

                resetPage()
//                Toast.makeText(this, "STUDENT", Toast.LENGTH_SHORT).show()
            }
            else if (boolAdmin && selectedStudent.password == password) {
                intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                resetPage()
//                Toast.makeText(this, "ADMIN", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "FOUT wachtwoord ingegeven${selectedStudent.password}", Toast.LENGTH_SHORT).show()
            }
        }

        etPassword.addTextChangedListener(textWatcher)

        btnAdmin.setOnClickListener {
            intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        btnSignature.setOnClickListener {
            intent = Intent(this, SignatureActivity::class.java)
            startActivity(intent)
        }

    }

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            Log.d("TAG", "afterTextChanged: ${s.toString()}")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            isPasswordEmpty(s.toString(),5)
        }
    }

    private fun isPasswordEmpty(string:String, minimumPasswordLength:Int = 0) {
        var length = string.trim().length
        btnLogin.isEnabled = length >= minimumPasswordLength
    }

    private fun resetPage(){
        btnLogin.isEnabled = false
        actvStudents.setText("")
        etPassword.setText("")
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMISSIONHANDLING", "Permission to location denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode)
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Toegang verleend", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Toegang geweigerd", Toast.LENGTH_SHORT).show()
                moveTaskToBack(true)
                exitProcess(-1)
            }
        }
    }
}




