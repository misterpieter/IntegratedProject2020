package be.volders.integratedproject2020.Model

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

data class Student(
        val name: String,
        val lastname: String,
        val snumber: String,
        val password: String
){
    override fun toString(): String {
        return name
    }


    companion object {

        fun getStudentsFromFile(filename: String, context: Context): ArrayList<Student> {
            val studentList = ArrayList<Student>()

            try {
                val jsonString = loadJsonFromAsset("students.json", context)
                val json = JSONObject(jsonString)
                val students = json.getJSONArray("students")

                (0 until students.length()).mapTo(studentList) {
                    Student(students.getJSONObject(it).getString("name"),
                            students.getJSONObject(it).getString("lastname"),
                            students.getJSONObject(it).getString("snumber"),
                            students.getJSONObject(it).getString("password"))                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return studentList
        }

        private fun loadJsonFromAsset(filename: String, context: Context): String? {
            var json: String? = null

            try {
                val inputStream = context.assets.open(filename)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: java.io.IOException) {
                ex.printStackTrace()
                return null
            }
            Log.d("TAG", "loadJsonFromAsset: ${json}")
            return json
        }
    }
}

