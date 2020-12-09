package be.volders.integratedproject2020

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import be.volders.integratedproject2020.Admin.AddressWithIdFirebase
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Model.SignatureHelper
import be.volders.integratedproject2020.Model.Student
import java.time.LocalDate


class DatabaseHelpe(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private var DATABASE_NAME = "studenten"
        private val DATABASE_VERSION = 5

        //table
        private val TABLE_STUDENTS = "student"
        private val TABLE_SIGNATURE = "signature"
        private val TABLE_LOCATION = "location"

        //STUDENT
        private val STUDENT_ID = "student_id"
        private val FIRSTNAME = "firstname"
        private val LASTNAME = "lastname"

        //SIGNATURE
        private val SIGNATURE_ID = "signature_id"
        private val SIGNATURE_NAME = "signature_name"
        private val SIGNATURE_BITMAP = "signature_bitmap"
        private val FK_STUDENT_ID = "fk_student_id"
        private val FK_LOCATION_ID = "fk_location_id"

        //LOCATIE
        private val LOCATION_ID = "location_id"
        private val TIMESTAMP = "locationTime"
        private val LONGITUDE = "longitude"
        private val LATTITUDE = "latitude"

        val selectQuery = "SELECT + FROM $TABLE_STUDENTS"
        private val CREATE_TABLE_STUDENTS = ("CREATE TABLE IF not exists "
                + TABLE_STUDENTS + "(" + STUDENT_ID + " VARCHAR(20)  PRIMARY KEY, "
                + FIRSTNAME + " VARCHAR(20), "
                + LASTNAME + " VARCHAR(20) );"
                )

        private val CREATE_TABLE_SIGNATURE = ( "CREATE TABLE IF not exists "
                + TABLE_SIGNATURE + "(" + SIGNATURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SIGNATURE_NAME + " VARCHAR(20), "
                + SIGNATURE_BITMAP + " TEXT ,"
                + FK_LOCATION_ID + " INTEGER ,"
                + FK_STUDENT_ID + " VARCHAR(20), "
                + " FOREIGN KEY( " + FK_STUDENT_ID + " ) REFERENCES " + TABLE_STUDENTS + " ( " + STUDENT_ID + " ), "
                + " FOREIGN KEY( " + FK_LOCATION_ID + " ) REFERENCES " + TABLE_LOCATION + " ( " + LOCATION_ID + " ));"
                )

        private val CREATE_TABLE_LOCATION = ("CREATE TABLE IF not exists "
                + TABLE_LOCATION + " ( " + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TIMESTAMP + " DATE, "
                + LONGITUDE + " DOUBLE, "
                + LATTITUDE + " DOUBLE, "
                + FK_STUDENT_ID + " VARCHAR(20),"
                + " FOREIGN KEY( " + FK_STUDENT_ID + " ) REFERENCES " + TABLE_STUDENTS + " ( " + STUDENT_ID + " ));"
                )
        private val JOIN = (
                "select st."+FIRSTNAME+", st."+LASTNAME+", st."+STUDENT_ID+
                        " from "+TABLE_STUDENTS+" as st " +
                        " left join "+TABLE_LOCATION+" as lo on st."+STUDENT_ID+" = lo."+FK_STUDENT_ID
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

    fun addStudent(student: Student): Long{
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(STUDENT_ID, student.snumber)
            put(FIRSTNAME, student.name)
            put(LASTNAME, student.lastname)
        }
        return db.insert(TABLE_STUDENTS, null, values)
    }


    fun getAllStudent(): ArrayList<Student>{
        val studentList = ArrayList<Student>()
        var stname:String
        var stfirstname:String
        var stsnr:String
        val selectQuery ="SELECT * FROM $TABLE_STUDENTS"
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery,null)
        if(c.moveToFirst()){
            do{
                stname = c.getString(c.getColumnIndex(LASTNAME))
                stfirstname = c.getString(c.getColumnIndex(FIRSTNAME))
                stsnr = c.getString(c.getColumnIndex(STUDENT_ID))

                val s : Student = Student(stname,stfirstname,stsnr, "password")
                studentList.add(s)
            }while(c.moveToNext())
        }
        c.close()
        return studentList
    }

    //TODO: fix error in cursor pointing to row 0 col -1 (cause => FK_LOCATION_ID column not in database)
    /*
        E/CursorWindow: Failed to read row 0, column -1 from a CursorWindow which has 13 rows, 4 columns.
        D/AndroidRuntime: Shutting down VM
        E/AndroidRuntime: FATAL EXCEPTION: main
        Process: be.volders.integratedproject2020, PID: 18983
        java.lang.IllegalStateException: Couldn't read row 0, col -1 from CursorWindow.  Make sure the Cursor is initialized correctly before accessing data from it.

     */
    fun getAllSignatures() : ArrayList<SignatureHelper> {
        var signList = ArrayList<SignatureHelper>()
        var dbImageId : String
        var dbImageByteArray : ByteArray
        var fkStudent : String
        var fkAddress : Int


        val selectQuery ="SELECT * FROM $TABLE_SIGNATURE"
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery,null)
        if(c.moveToFirst()){
            do{
                dbImageId = c.getString(c.getColumnIndex(SIGNATURE_ID))
                dbImageByteArray = c.getString(c.getColumnIndex(SIGNATURE_BITMAP)).toByteArray()
                fkStudent = c.getString(c.getColumnIndex(FK_STUDENT_ID))
                fkAddress = c.getInt(c.getColumnIndex(FK_LOCATION_ID))
                var signature = SignatureHelper(dbImageId,dbImageByteArray,fkStudent,fkAddress)
                signList.add(signature)
            }while(c.moveToNext())
        }
        c.close()
        return signList
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

    fun getAllLocationsWithId() : ArrayList<AddressWithIdFirebase> {
        val locationList = ArrayList<AddressWithIdFirebase>()
        //adding location ID to this
        var dbLocId: Int
        var dbLat : Double
        var dbLon : Double
        var date : LocalDate
        var fkSnumber : String

        val selectQuery ="SELECT * FROM $TABLE_LOCATION"
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery,null)
        if(c.moveToFirst()){
            do{
                dbLocId = c.getInt(c.getColumnIndex(LOCATION_ID))
                dbLat = c.getDouble(c.getColumnIndex(LATTITUDE))
                dbLon = c.getDouble(c.getColumnIndex(LONGITUDE))
                date =  LocalDate.parse(c.getString(c.getColumnIndex(TIMESTAMP)))
                fkSnumber = c.getString(c.getColumnIndex(FK_STUDENT_ID))
                val location = AddressWithIdFirebase(dbLocId, dbLat, dbLon, date, fkSnumber)
                locationList.add(location)
            }while(c.moveToNext())
        }
        c.close()
        return locationList
    }

    fun insertLocation(adres : Address): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()


        values.put(LONGITUDE, adres.lon)
        values.put(LATTITUDE, adres.lat)
        values.put(TIMESTAMP, LocalDate.now().toString())
        values.put(FK_STUDENT_ID,adres.fkSnumber)

        val result = db.insert(TABLE_LOCATION, null, values)
        db.close()
        return !result.equals( -1)
    }

    fun filterStudent(datum: String): ArrayList<Student> {

        val StudentList = ArrayList<Student>()
        var stname:String
        var stfirstname:String
        var stsnr:String
        val selectQuery = JOIN+" where lo.locationTime = '"+datum+"' ORDER BY st.firstname"
        val db = this.readableDatabase
        var c = db.rawQuery(selectQuery,null)
        if(c.moveToFirst()){
            do{
                stname = c.getString(c.getColumnIndex(LASTNAME))
                stfirstname = c.getString(c.getColumnIndex(FIRSTNAME))
                stsnr = c.getString(c.getColumnIndex(STUDENT_ID))
                var s = Student(stname,stfirstname,stsnr,"password") //PASSWORD MAG WEG
                StudentList.add(s)
                Log.d("FIL", "afterTextChanged: ${s.name}")
            }while(c.moveToNext())
        }
        return StudentList
    }

    /*
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
*/

}