package be.volders.integratedproject2020

import android.content.Context
import android.util.Base64InputStream
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import be.volders.integratedproject2020.Model.Student
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception

object Helper {

    fun hideKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getStudentsFromCSVString(csvString:String, withoutHeader:Boolean=false):ArrayList<Student>{
        var studentList = ArrayList<Student>()

        var csvString = csvString

        val rows: List<List<String>> = csvReader().readAll(csvString!!)
        for (r in rows) {
            studentList.add(Student(r[0], r[1], r[2], r[3]))
        }

        // check if CSV has headers and remove them from list
        if(withoutHeader == false){
            studentList.removeAt(0)
        }
        Log.d("TAG", "getStudentsFromLocalCSV: ${studentList}")
        return  studentList
    }

    fun getStudentsFromLocalCSV(context: Context,withoutHeader:Boolean=false):ArrayList<Student>{
        var studentList = ArrayList<Student>()

        var csvData: String? = null
        try {
            csvData = loadDataFromAsset("students.csv", context)
        }
        catch (e: Exception){
            Log.d("TAG", "readCSV: ${e.message}")
        }

        val rows: List<List<String>> = csvReader().readAll(csvData!!)
        for (r in rows) {
            studentList.add(Student(r[0], r[1], r[2], r[3]))
        }

        // check if CSV has headers and remove them from list
        if(withoutHeader == false){
            studentList.removeAt(0)
        }
        Log.d("TAG", "getStudentsFromLocalCSV: ")
        return  studentList
    }


    fun getStudentsFromLocalJson(context: Context): ArrayList<Student> {
        val studentList = ArrayList<Student>()

        try {
            val jsonString = loadDataFromAsset("students.json", context)
            val json = JSONObject(jsonString)
            val students = json.getJSONArray("students")

            (0 until students.length()).mapTo(studentList) {
                Student(
                        students.getJSONObject(it).getString("name"),
                        students.getJSONObject(it).getString("lastname"),
                        students.getJSONObject(it).getString("snumber"),
                        students.getJSONObject(it).getString("password")
                )                }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return studentList
    }

    private fun loadDataFromAsset(filename: String, context: Context): String? {
        var data: String? = null

        try {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            data = String(buffer, Charsets.UTF_8)
        } catch (ex: java.io.IOException) {
            ex.printStackTrace()
            return null
        }
        return data
    }

    fun loadDataFromIntent(inputStream: InputStream): String? {
        var data: String? = null
        try {
            val inputStream = inputStream
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            data = String(buffer, Charsets.UTF_8)
        } catch (ex: java.io.IOException) {
            ex.printStackTrace()
            return null
        }
        Log.d("TAG", "loadDataFromIntent: ${data}")
        return data
    }
}