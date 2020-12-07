package be.volders.integratedproject2020.Admin

import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.MainActivity
import be.volders.integratedproject2020.Model.Student
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context
import be.volders.integratedproject2020.Model.Address
import java.time.LocalDate
import java.util.*

class NewSyncDatabase(context: Context) {
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
        val batch = mFirestore.batch()
        val millis = System.currentTimeMillis()
        val date = java.sql.Date(millis)

        // TESTDATA
//        val testAdress = Address(55.1123 , 4.5645, date,  "S425316")

        val locationList = databaseHelper?.getAllLocations()!!
        println("locationlist print empty? " + locationList.isEmpty() + "   size: " + locationList.size)

            for(address in locationList) {
                //names the document to snumber
                val locationRef = mFirestore.collection("Students").document(address.fkSnumber).collection("Locations").document(address.date.toString())
                batch[locationRef] = address
            }
            batch.commit()
    }


    //saves all students in this list. If changed it overwrites
    fun saveOrUpdateAllStudents(){
        val batch = mFirestore.batch()
        val studentList = databaseHelper?.getAllStudent()!!
        println("studerntlist print empty? " + studentList.isEmpty() + "   size: " + studentList.size)

        for(student in studentList) {
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

