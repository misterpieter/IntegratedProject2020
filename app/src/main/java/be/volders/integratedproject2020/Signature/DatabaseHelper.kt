package be.volders.integratedproject2020

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.util.Log
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Model.SignatureHelper
import be.volders.integratedproject2020.Model.Student
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime


class DatabaseHelpe(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private var DATABASE_NAME = "studenten"
        private val DATABASE_VERSION = 5
        //table
        private val TABLE_STUDENTS = "student"
        private val TABLE_SIGNATURE = "signature"
        private val TABLE_LOCATION = "signature"
        //STUDENT
        private val KEY_ID = "id"
        private val STUDENT_ID = "student_id"
        private val FIRSTNAME = "firstname"
        private val LASTNAME = "lastname"
        //SIGNATURE
        private val SIGNATURE_ID = "signature_id"
        private val SIGNATURE_NAME = "signature_name"
        private val SIGNATURE_BITMAP = "signature_bitmap"
        private val FK_STUDENT_ID = "fk_student_id"

        //LOCATIE
        private val LOCATION_ID = "location_id"
        private val HOUSE_NUMBER = "huis_number"
        private val ROAD = "road"
        private val TOWN = "town"
        private val COUNTRY = "country"
        private val POSTCODE = "postcode"
        private val TIMESTAMP = "locationTime"
        private val LONGITUDE = "longitude"
        private val LATTITUDE = "latitude"
        private val FK_SIGNATURE_ID = "fk_signature_id"

        val selectQuery = "SELECT + FROM $TABLE_STUDENTS"
        private val CREATE_TABLE_STUDENTS = ("CREATE TABLE IF not exists "
                + TABLE_STUDENTS + "(" + STUDENT_ID + " VARCHAR(20)  PRIMARY KEY, "
                + FIRSTNAME + " VARCHAR(20), "
                + LASTNAME + " VARCHAR(20) );"
                )

        public val CREATE_TABLE_SIGNATURE = ( "CREATE TABLE IF not exists "
                + TABLE_SIGNATURE + "(" + SIGNATURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FK_STUDENT_ID + " VARCHAR(20), "
                + SIGNATURE_NAME + " VARCHAR(20), "
                + SIGNATURE_BITMAP + " TEXT ,"
                + " FOREIGN KEY( " + FK_STUDENT_ID + " ) REFERENCES " + TABLE_STUDENTS + " ( " + STUDENT_ID + " ));"
                )

        private val CREATE_TABLE_LOCATION = ("CREATE TABLE IF not exists "
                + TABLE_LOCATION + " ( " + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HOUSE_NUMBER + " INTEGER,"
                + ROAD + " VARCHAR(50),"
                + TOWN + " VARCHAR(50),"
                + COUNTRY + " VARCHAR(50),"
                + POSTCODE + " INTEGER,"
                + TIMESTAMP + " DATE, "
                + LONGITUDE + " INTEGER, "
                + LATTITUDE + " INTEGER, "
                + FK_SIGNATURE_ID + " INTEGER,"
                +" FOREIGN KEY("+ FK_SIGNATURE_ID+") REFERENCES " + TABLE_SIGNATURE + " (" + SIGNATURE_ID +"));"
                )
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_STUDENTS)
        db?.execSQL(CREATE_TABLE_SIGNATURE)
        db?.execSQL(CREATE_TABLE_LOCATION)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOCATION")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SIGNATURE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    //addstudent moet enkel uitgevoerd worden wanneer csv upgeload word
    fun addStudent(student: Student): Long{
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(STUDENT_ID, student.snumber)
            put(FIRSTNAME, student.name)
            put(LASTNAME, student.lastname)
        }
        return db.insert(TABLE_STUDENTS, null, values)
    }

    fun insetImage(dbBitmap: String, imageId: String?, studentNr: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(SIGNATURE_NAME, imageId)
        values.put(FK_STUDENT_ID, studentNr)
        values.put(SIGNATURE_BITMAP, dbBitmap)

        val result = db.insert(TABLE_SIGNATURE, null, values)
        db.close()
        return !result .equals( -1)
    }

    fun insertLocation(adres : Address): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(HOUSE_NUMBER, adres.houseNumber)
        values.put(ROAD, adres.road)
        values.put(POSTCODE, adres.postcode)
        values.put(TOWN, adres.town)
        values.put(COUNTRY, adres.county)
        values.put(LONGITUDE, adres.lon)
        values.put(LATTITUDE, adres.lat)
        values.put(TIMESTAMP, LocalDate.now().toString())

        val result = db.insert(TABLE_LOCATION, null, values)
        db.close()
        return !result .equals( -1)
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