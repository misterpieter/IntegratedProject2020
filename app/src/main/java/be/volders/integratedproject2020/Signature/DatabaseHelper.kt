package be.volders.integratedproject2020

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import be.volders.integratedproject2020.Model.SignatureHelper
import be.volders.integratedproject2020.Model.Student
import java.io.ByteArrayOutputStream


class DatabaseHelpe(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private var DATABASE_NAME = "studenten"
        private val DATABASE_VERSION = 5
        //table
        private val TABLE_STUDENTS = "student"
        private val TABLE_SIGNATURE = "signature"
        //STUDENT
        private val KEY_ID = "id"
        private val STUDENT_ID = "studentID"
        private val FIRSTNAME = "firstname"
        private val LASTNAME = "lastname"
        //SIGNATURE
        private val SIGNATURE_ID = "signatureId"
        private val SIGNATURE_NAME = "signature_name"
        private val SIGNATURE_BITMAP = "signature_bitmap"

        //hier wordt nog enkel naam opgeslagen
        val selectQuery = "SELECT + FROM $TABLE_STUDENTS"
        private val CREATE_TABLE_STUDENTS = ("CREATE TABLE "
                + TABLE_STUDENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                       + STUDENT_ID + " VARCHAR(20), "
                                       + FIRSTNAME + " VARCHAR(20), "
                                       + LASTNAME + " VARCHAR(20) );"
                )

        private val CREATE_TABLE_SIGNATURE = ("CREATE TABLE"
                + TABLE_SIGNATURE + "(" +  SIGNATURE_ID + " INTEGER PRIMARY KEY ,"
                                        + SIGNATURE_NAME + " TEXT,"
                                        + SIGNATURE_BITMAP + " TEXT );"
        )
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_STUDENTS)
        db?.execSQL(CREATE_TABLE_SIGNATURE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SIGNATURE")
        onCreate(db)
    }

    fun addStudent(student: Student): Long{
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(STUDENT_ID, student.snumber)
            put(FIRSTNAME, student.name)
            put(LASTNAME, student.lastname)
        }
        val r = db.insert(TABLE_STUDENTS, null, values)

        return r
    }
    fun insetImage(dbDrawable: DrawingView, imageId: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SIGNATURE_NAME, imageId)
        val bitmap = (dbDrawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        values.put(SIGNATURE_BITMAP, stream.toByteArray())
        db.insert(TABLE_SIGNATURE, null, values)
        db.close()
    }

    fun getImage(imageId: String): SignatureHelper? {
        val db = this.writableDatabase
        val cursor2: Cursor = db.query(TABLE_SIGNATURE, arrayOf(SIGNATURE_ID, SIGNATURE_NAME, SIGNATURE_BITMAP), SIGNATURE_NAME
                + " LIKE '" + imageId + "%'", null, null, null, null)
        val imageHelper = SignatureHelper()
        if (cursor2.moveToFirst()) {
            do {
                imageHelper.setImageId(cursor2.getString(1))
                imageHelper.setImageByteArray(cursor2.getBlob(2))
            } while (cursor2.moveToNext())
        }
        cursor2.close()
        db.close()
        return imageHelper
    }
}