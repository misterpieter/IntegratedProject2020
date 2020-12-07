package be.volders.integratedproject2020.Admin

import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.parcel.Parcelize

/*class SyncDatabase : AppCompatActivity() {




   // var db = FirebaseFirestore.getInstance()
    private val mFirestore  : FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}

    private lateinit var mStorageRef: StorageReference
    private lateinit var mDatabaseRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.activity_admin)

        println("enter the gungeon")

        mStorageRef = FirebaseStorage.getInstance().reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Students")

        uploadStudentOne()


    }


    fun Student.convertToMap(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["name"] = name
        map["lastname"] = lastname
        map["snumber"] = snumber
        map["password"] = password

        return map
    }


    fun uploadStudentOne()  {



       val  studentJonas =  Student("s109439", "Jonas", "Adriaenssens", "password" )


        val sRef: StorageReference = mStorageRef?.child("Students")!!.child(studentJonas.toString())


        mDatabaseRef.setValue(studentJonas).addOnSuccessListener {  Toast.makeText(this,"succes", Toast.LENGTH_SHORT).show(); println("SUCCESFULLY SETVALUE")}
        mDatabaseRef.push()


        mDatabaseRef?.updateChildren(studentJonas.convertToMap())


        val batch =  mFirestore.batch()
        val studentRef = mFirestore.collection("Students").document()

        batch.set(studentRef, studentJonas)

        batch.commit()


    /*    val sRef: StorageReference = mStorageRef.child("Students").child("S109439")
        sRef.

        sRef.putFile(uploadUri, metadata).addOnSuccessListener { taskSnapshot ->


//                                if (flag == 0) {
            imageDetails = ImageDetails(imageId, taskSnapshot.getDownloadUrl().toString())
            dbHandler.addImage(imageDetails, userName)
            val uploadId: String = mDatabaseRef.push().getKey()
            mDatabaseRef.child(uploadId).setValue(imageDetails)
            uploadingDialog.dismiss()
            Snackbar.make(
                findViewById(R.id.rootView),
                "Image Uploaded Successfully",
                Snackbar.LENGTH_SHORT
            ).show()


//                                }
            //extra safety ? :p

//                startActivity(backIntent);
        }.addOnFailureListener { e ->
            uploadingDialog.dismiss()
            Snackbar.make(
                findViewById(R.id.rootView),
                "Error Uploading Image",
                Snackbar.LENGTH_LONG
            )
                .show()
            Log.d("onFaliure", e.toString())
        }*
    }



}*/