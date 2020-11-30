package be.volders.integratedproject2020.Model

import android.content.Context
import android.util.Log
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
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


        fun getStudentsFromLocalCSV(context: Context):List<Student>{
            var studentList = ArrayList<Student>()

            var csvData: String? = null
            try {
                 csvData = loadDataFromAsset("students.csv", context)
            }
            catch (e:Exception){
                Log.d("TAG", "readCSV: ${e.message}")
            }

            val rows: List<List<String>> = csvReader().readAll(csvData!!)
            for (r in rows) {
                studentList.add(Student(r[0], r[1], r[2], r[3]))
            }
            return  studentList
        }


        fun getStudentsFromLocalJson(filename: String, context: Context): ArrayList<Student> {
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
    }
}

