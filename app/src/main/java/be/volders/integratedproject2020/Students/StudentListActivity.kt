package be.volders.integratedproject2020.Students

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.Helper.getStudentsFromLocalCSV
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.Students.rvPersons
import kotlinx.android.synthetic.main.activity_student_list.ettFilter
import java.util.*
import kotlin.collections.ArrayList


lateinit var btfilter: Button
lateinit var etFilter: String
lateinit var rvPersons: RecyclerView
lateinit var sortlist: List<Student>
lateinit var studentlist : ArrayList<Student>

abstract class StudentListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)


        rvPersons = findViewById<RecyclerView>(R.id.rvPersons)
        rvPersons.layoutManager = LinearLayoutManager(this)

        studentlist = databaseHelper!!.getAllStudent()
        //Log.d("TAG", "databaseHelper list of student: ${studentlist}")
        val spinner: Spinner = findViewById(R.id.spSorteren)
        val adapterSort = ArrayAdapter.createFromResource(this, R.array.sort_list, android.R.layout.simple_spinner_item)

        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSort
        spinner.setOnItemSelectedListener(this)

        btfilter = findViewById(R.id.btFilter)
        btfilter?.setOnClickListener{
            etFilter = ettFilter.text.toString()
            sortlist=  studentlist.filter { x -> x.name == etFilter || x.lastname == etFilter || x.snumber == etFilter }
            //Log.d("LIST", "databaseHelper list of student: ${sortlist}")
            val adapter = StudentAdapter(this, sortlist)
            rvPersons.adapter = adapter
        }

        //databaseHelper!!.filterStudent()
        //val studentList = getStudentsFromLocalCSV(this)
    }

/*    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var etFilter = ettFilter.text.toString()
        when (position) {
            0 -> {
                sortlist =  databaseHelper!!.filterStudent("firstname")
                val adapter = StudentAdapter(this, sortlist)
                rvPersons.adapter = adapter

            }
            1 -> { //lastname
                sortlist= studentlist.sortedWith(compareBy({it.lastname}))
                val adapter = StudentAdapter(this, sortlist)
                rvPersons.adapter = adapter
            }
            2 -> { //student_id
                sortlist= studentlist.sortedWith(compareBy({it.snumber}))
                val adapter = StudentAdapter(this, sortlist)
                rvPersons.adapter = adapter
            }
            3 -> { //locationTime
                sortlist= studentlist.sortedWith(compareBy({it.snumber}))
                val adapter = StudentAdapter(this, sortlist)
                rvPersons.adapter = adapter
            }
        }
    }*/

    override fun onNothingSelected(parent: AdapterView<*>?) {
        val adapter = StudentAdapter(this, studentlist)
        rvPersons.adapter = adapter
    }
    fun filterList(list: ArrayList<Student>, filter: String): Optional<Student>{
        var listt = list.stream().filter { x -> x.name == filter || x.lastname == filter || x.snumber == filter }.findAny()
        return listt
    }

}