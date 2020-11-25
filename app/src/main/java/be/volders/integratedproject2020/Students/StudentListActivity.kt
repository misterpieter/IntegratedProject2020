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
//
//        val studentList = ArrayList<Student>()
//        studentList.add(Student("Obama","Barrack","snumber1","password1"))
//        studentList.add(Student("Angela", "Merkel","snumber2","password2"))
//        studentList.add(Student("Kim", "Jong-Un","snumber3","password3"))
//        studentList.add(Student("Donald", "Trump","snumber4","password4"))


        val studentList = Student.getStudentsFromFile("students.json", this)
        Log.d("TAG", "onCreate: ${studentList[0].password}")

        val adapter = StudentAdapter(this,studentList)
        rvPersons.adapter = adapter
    }
}