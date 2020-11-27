package be.volders.integratedproject2020

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import be.volders.integratedproject2020.Model.Student

class DatabaseHelpe(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private var DATABASE_NAME = "studenten"
        private val DATABASE_VERSION = 5
        private val TABLE_STUDENTS = "student"
        private val KEY_ID = "id"
        private val STUDENT_ID = "studentID"
        private val FIRSTNAME = "firstname"
        private val LASTNAME = "lastname"

        //hier wordt nog enkel naam opgeslagen
        val selectQuery = "SELECT + FROM $TABLE_STUDENTS"
        private val CREATE_TABLE_STUDENTS = ("CREATE TABLE "
                + TABLE_STUDENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                       + STUDENT_ID + " VARCHAR(20), "
                                       + FIRSTNAME + " VARCHAR(20), "
                                       + LASTNAME + " VARCHAR(20) );"
                )

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_STUDENTS)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    fun addStudent(student: Student){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(STUDENT_ID,student.snumber)
            put(FIRSTNAME, student.name)
            put(LASTNAME, student.lastname)
        }
        val r = db.insert(TABLE_STUDENTS, null, values)

        //return r
    }
}