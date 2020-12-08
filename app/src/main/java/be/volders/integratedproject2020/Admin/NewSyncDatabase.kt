package be.volders.integratedproject2020.Admin

import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.MainActivity
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.Students.studentlist
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Model.SignatureHelper
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

/*
    //version control
    fun checkVersion() {
        val docRef = mFirestore.collection("DatabaseVersion").
    }
*/

    fun saveOrUpdateAllSignatures(){
        val batch = mFirestore.batch()
        // val signatureList = databaseHelper?.getAllSignatures()!!

        // println("locationlist print empty? " + signatureList.isEmpty() + "   size: " + signatureList.size)

        //TODO: fix error update signatures

        /*
            Process: be.volders.integratedproject2020, PID: 30944
            java.lang.IllegalArgumentException: Could not serialize object. Serializing Arrays is not supported, please use Lists instead (found in field 'imageByteArray')

        * */

        //TESTDATA
        val bytes = byteArrayOf(0xA1.toByte(), 0x2E.toByte(), 0x38.toByte(), 0xD4.toByte(), 0x89.toByte(), 0xC3.toByte())
        val signature = SignatureHelper("imageIdTest", bytes, "S425316", 7 )

        val imgRef = mFirestore.collection("Students").document("S425316")
            .collection("Locations").document("2020-12-07").collection("Signatures").document(signature.imageId.toString())


        batch[imgRef] = signature
        batch.commit()

    }


    fun saveOrUpdateAllLocations(){
        // TESTDATA
/*
        val millis = System.currentTimeMillis()
        val date = java.sql.Date(millis)
        val testAdress = Address(55.1123 , 4.5645, date,  "S425316")
*/

        val batch = mFirestore.batch()
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

