package be.volders.integratedproject2020.Students

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.R

class StudentListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        val rvPersons = findViewById<RecyclerView>(R.id.rvPersons)
        rvPersons.layoutManager = LinearLayoutManager(this)

        val studentList = Student.getStudentsFromFile("students.json", this)
        Log.d("TAG", "onCreate: ${studentList[0]}")

        val adapter = StudentAdapter(this,studentList)
        rvPersons.adapter = adapter
    }
}