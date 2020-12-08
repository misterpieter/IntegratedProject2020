package be.volders.integratedproject2020.Model

import android.content.Context
import android.os.Parcelable
import android.util.Log
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

data class Student(
    val name: String,
    val lastname: String,
    val snumber: String,
    val password: String
){

    override fun toString(): String {
        return "${name} ${lastname} - ${snumber}"
    }


    companion object {



    }
}

