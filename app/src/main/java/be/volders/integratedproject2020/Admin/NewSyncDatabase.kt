package be.volders.integratedproject2020.Admin

import android.content.Context
import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.Model.SignatureHelper
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Date
import java.util.*

class NewSyncDatabase(context: Context) {
    private val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(context)

    fun saveOrUpdateAllSignatures(){
        val batch = mFirestore.batch()
        val signList = databaseHelper?.getAllSignatures()!!
        println("locationlist print empty? " + signList.isEmpty() + "   size: " + signList.size)

        for(sign in signList) {
            //names the document to snumber
            val imgRef = mFirestore.collection("Students").document(sign.fkStudent)
                    .collection("Locations").document(sign.locationLink).collection("Signatures").document(sign.imageId.toString())
            batch[imgRef] = signatureConverter(sign)
        }
        batch.commit()
    }

    fun saveOrUpdateAllLocations(){
        val batch = mFirestore.batch()
        val locationList = databaseHelper?.getAllLocationsWithId()!!
        println("locationlist print empty? " + locationList.isEmpty() + "   size: " + locationList.size)

            for(address in locationList) {
                //names the document to snumber
                val locationRef = mFirestore.collection("Students").document(address.fkSnumber).collection("Locations").document(address.signatureLink)
                batch[locationRef] = locationConverter(address)
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



    private fun signatureConverter(signature : SignatureHelper) : SignatureConvertedFirebase {
        val encodedImage: String = Base64.getEncoder().encodeToString(signature.imageByteArray)
        return SignatureConvertedFirebase(signature.imageId, encodedImage, signature.fkStudent, signature.locationLink, signature.releaseCounter, signature.vectorCounter, signature.suspicion)
    }

    private fun locationConverter(address : AddressWithIdFirebase) : LocationUsingDate {
        var newDate : Date = Date.valueOf(address.date.toString())
        return LocationUsingDate(address.addressId, address.lat, address.lon, newDate, address.fkSnumber, address.signatureLink , address.road, address.houseNumber, address.postcode, address.town, address.neighbourhood, address.county)
    }

    private class SignatureConvertedFirebase(
            var imageId: String?,
            var imageByteArray: String,
            var fkStudent: String,
            var locationLink: String,
            var releaseCounter: Int,
            var vectorCounter: Int,
            var suspsicion : Boolean
    )

    private class LocationUsingDate(
            var addressId: Int,
            var lat: Double,
            var lon: Double,
            var date: Date,
            var fkSnumber: String,
            var signatureLink: String,
            var road: String? = "NO road",
            var houseNumber: Int? = 0,
            var postcode: Int? = 0,
            var town: String? = "NO town",
            var neighbourhood: String? = "NO neighbourhood",
            var county: String? = "NO county"
    )


}


