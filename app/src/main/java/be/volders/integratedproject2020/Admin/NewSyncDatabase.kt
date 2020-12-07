package be.volders.integratedproject2020.Admin

import be.volders.integratedproject2020.Model.Student
import com.google.firebase.firestore.FirebaseFirestore

class NewSyncDatabase {
    // private val mFirestore  : FirebaseFirestore by lazy {FirebaseFirestore.getInstance()}
    private val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    fun addStudent() {
        val batch = mFirestore.batch()
        val studentRef = mFirestore.collection("Students").document()
        val s = Student("123", "456", "789", "password")
        batch[studentRef] = s
        batch.commit()
    }

}