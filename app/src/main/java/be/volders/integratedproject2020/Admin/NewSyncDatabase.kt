package be.volders.integratedproject2020.Admin

import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.MainActivity
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.Students.studentlist
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context

class NewSyncDatabase(context: Context) {
    // private val mFirestore  : FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}


    private val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(context)
    //    private val dbHelper: DatabaseHelpe = DatabaseHelpe()
    fun addStudent() {
        val batch = mFirestore.batch()
        val studentRef = mFirestore.collection("Students").document()
        val s = Student("123", "456", "789", "password")
        batch[studentRef] = s
        batch.commit()
    }


/*
    //version control
    fun checkVersion() {
        val docRef = mFirestore.collection("DatabaseVersion").
    }
*/

    fun saveAllStudents(){
        val batch = mFirestore.batch()
        studentlist = databaseHelper?.getAllStudent()!!
        println("studerntlist print empty? " + studentlist.isEmpty() + "   size: " + studentlist.size)

        for(student in studentlist) {
            val studentRef = mFirestore.collection("Students").document()
            batch[studentRef] = student

            println(student.toString())

        }
            batch.commit()
      }



    fun Student.convertToMap(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["name"] = name
        map["lastname"] = lastname
        map["snumber"] = snumber
        map["password"] = password

        return map
    }


}

