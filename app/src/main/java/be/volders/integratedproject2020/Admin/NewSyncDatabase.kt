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


    //testFunction
    fun addStudent() {
        val batch = mFirestore.batch()
        val studentRef = mFirestore.collection("Students").document()
        val s = Student("123", "456", "789", "password")
        batch[studentRef] = s
        batch.commit()
    }


    fun deleteDocumentsStudent(){


    }

/*  NICE TO HAVE
    //version control
    fun checkVersion() {
        val docRef = mFirestore.collection("DatabaseVersion").
    }
*/


    fun saveOrUpdateAllSignatures(){

    }


    fun saveOrUpdateAllLocations(){

    }


    //saves all students in this list. If changed it overwrites
    fun saveOrUpdateAllStudents(){
        val batch = mFirestore.batch()
        studentlist = databaseHelper?.getAllStudent()!!
        println("studerntlist print empty? " + studentlist.isEmpty() + "   size: " + studentlist.size)

        for(student in studentlist) {
            //names the document to snumber
            val studentRef = mFirestore.collection("Students").document(student.snumber)
            batch[studentRef] = student
        }
            batch.commit()
      }


    // isn't used delete later ?
    fun Student.convertToMap(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["name"] = name
        map["lastname"] = lastname
        map["snumber"] = snumber
        map["password"] = password

        return map
    }


}

