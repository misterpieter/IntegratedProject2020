package be.volders.integratedproject2020

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.core.database.getStringOrNull
import be.volders.integratedproject2020.Admin.AddressWithIdFirebase
import be.volders.integratedproject2020.Model.*
import java.sql.SQLException
import java.time.LocalDate
import kotlin.math.sign


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
        private val LOCATION_LINK = "location_link"
        private val RELEASE_COUNT = "release_count"
        private val VECTOR_COUNT = "vector_count"
        private val SUSPICIOUS = "supsicious"

        //LOCATIE
        private val LOCATION_ID = "location_id"
        private val TIMESTAMP = "locationTime"
        private val LONGITUDE = "longitude"
        private val LATTITUDE = "latitude"
        private val ROAD = "road"
        private val HOUSE_NUMBER = "house_number"
        private val POSTCODE = "postcode"
        private val TOWN = "town"
        private val NEIGHBOURHOOD = "neighbourhood"
        private val COUNTRY = "country"
        private val SIGNATURE_LINK = "signature_link"

        val selectQuery = "SELECT + FROM $TABLE_STUDENTS"
        private val CREATE_TABLE_STUDENTS = ("CREATE TABLE IF not exists "
                + TABLE_STUDENTS + "(" + STUDENT_ID + " VARCHAR(20)  PRIMARY KEY, "
                + FIRSTNAME + " VARCHAR(20), "
                + LASTNAME + " VARCHAR(20) );"
                )

        private val CREATE_TABLE_SIGNATURE = ( "CREATE TABLE IF not exists "
                + TABLE_SIGNATURE + "(" + SIGNATURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SIGNATURE_NAME + " VARCHAR(20), "
                + SIGNATURE_BITMAP + " BLOB , "
                + FK_LOCATION_ID + " INTEGER, "
                + FK_STUDENT_ID + " VARCHAR(20), "
                + LOCATION_LINK + " VARCHAR(50), "
                + RELEASE_COUNT + " INTEGER, "
                + VECTOR_COUNT + " INTEGER, "
                + SUSPICIOUS + " BOOL, "
                + " FOREIGN KEY( " + FK_STUDENT_ID + " ) REFERENCES " + TABLE_STUDENTS + " ( " + STUDENT_ID + " ), "
                + " FOREIGN KEY( " + FK_LOCATION_ID + " ) REFERENCES " + TABLE_LOCATION + " ( " + LOCATION_ID + " ));"
                )

        private val CREATE_TABLE_LOCATION = ("CREATE TABLE IF not exists "
                + TABLE_LOCATION + " ( " + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TIMESTAMP + " DATE, "
                + LONGITUDE + " DOUBLE, "
                + LATTITUDE + " DOUBLE, "
                + ROAD + " VARCHAR(100), "
                + HOUSE_NUMBER + " INTEGER, "
                + POSTCODE + " INTEGER, "
                + TOWN + " VARCHAR(40), "
                + NEIGHBOURHOOD + " VARCHAR(40), "
                + COUNTRY + " VARCHAR(40), "
                + SIGNATURE_LINK + " VARCHAR(50), "
                + FK_STUDENT_ID + " VARCHAR(20), "
                + " FOREIGN KEY( " + FK_STUDENT_ID + " ) REFERENCES " + TABLE_STUDENTS + " ( " + STUDENT_ID + " ));"
                )
        private val JOIN = (
                "select st."+FIRSTNAME+", st."+LASTNAME+", st."+STUDENT_ID+
                        " from "+TABLE_STUDENTS+" as st " +
                        " left join "+TABLE_LOCATION+" as lo on st."+STUDENT_ID+" = lo."+FK_STUDENT_ID
                )
        private  val GETSIGNATURE = (
                "select distinct s."+SIGNATURE_BITMAP+", l."+ ROAD+",  l."+HOUSE_NUMBER+", l."+POSTCODE+", l."+TIMESTAMP+
                        ", l."+TOWN+", l."+NEIGHBOURHOOD+", l."+COUNTRY+" , s." + SIGNATURE_ID + " , s." + SUSPICIOUS +  " from "+TABLE_SIGNATURE+" as s "+
                        "left join "+TABLE_LOCATION+" as l on l."+ SIGNATURE_LINK+" = s."+ LOCATION_LINK
                )
        private val EXPORTDATASTUDENT = (
                "select st."+FIRSTNAME+", st."+LASTNAME+", st."+STUDENT_ID+", l."+ ROAD+",  l."+HOUSE_NUMBER+", l."+POSTCODE+
                        ", l."+TOWN+", l."+NEIGHBOURHOOD+", l."+COUNTRY+", l."+TIMESTAMP+
                        " from "+TABLE_LOCATION+" as l " +
                        " left join "+TABLE_STUDENTS+" as st on st."+STUDENT_ID+" = l."+FK_STUDENT_ID
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

    fun  clearDatabase(){
        val db = this.readableDatabase
        val selectQuery = "DROP TABLE IF EXISTS "
        db.execSQL(selectQuery+TABLE_LOCATION)
        db.execSQL(selectQuery+TABLE_SIGNATURE)
        db.execSQL(selectQuery+TABLE_STUDENTS)
        onCreate(db)
        db.close()
    }

    fun addStudent(student: Student){
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(STUDENT_ID, student.snumber)
                put(FIRSTNAME, student.name)
                put(LASTNAME, student.lastname)
            }
            // inserts new if not exists. If exists => replace
            db.replace(TABLE_STUDENTS, null, values)
            db.close()
    }

    fun getSuspiciousOrNot(snumber: String) : Boolean {
        var hasFraude = false

        val selectQuery = "SELECT * FROM $TABLE_SIGNATURE  WHERE $FK_STUDENT_ID =  \"$snumber\" AND $SUSPICIOUS=1"

        try {
            val db = this.readableDatabase
            val c = db.rawQuery(selectQuery, null)
            if (c.moveToFirst()) {
                hasFraude = true
            }
        }catch (ex: SQLException) {
            Log.e("GetSuspisiousOrNot", ex.stackTraceToString())
        }
        return hasFraude
    }

    fun getAllStudent(): ArrayList<Student>{
        val studentList = ArrayList<Student>()
        var stname:String
        var stfirstname:String
        var stsnr:String
        val selectQuery ="SELECT * FROM $TABLE_STUDENTS"
        try {
            val db = this.readableDatabase
            val c = db.rawQuery(selectQuery,null)
            if(c.moveToFirst()){
                do{
                    stname = c.getString(c.getColumnIndex(LASTNAME))?:""
                    stfirstname = c.getString(c.getColumnIndex(FIRSTNAME))?:""
                    stsnr = c.getString(c.getColumnIndex(STUDENT_ID))?:""

                    val s : Student = Student(stname,stfirstname,stsnr)
                    studentList.add(s)
                }while(c.moveToNext())
            }
            c.close()
        }catch (ex: SQLiteException){
            Log.e("getAllStudents", ex.stackTraceToString())
        }
        return studentList
    }

    fun getAllSignatures() : ArrayList<SignatureHelper> {
        var signList = ArrayList<SignatureHelper>()
        var dbImageId : String
        var dbImageByteArray : ByteArray
        var fkStudent : String
        var locationLink: String
        var releaseCount: Int
        var vectorCount: Int
        var suspiscious = false


        val selectQuery ="SELECT * FROM $TABLE_SIGNATURE"
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery,null)
        if(c.moveToFirst()){
            do{
                dbImageId = c.getString(c.getColumnIndex(SIGNATURE_ID))
                dbImageByteArray = c.getBlob(c.getColumnIndex(SIGNATURE_BITMAP))//c.getColumnIndex(SIGNATURE_BITMAP))
                fkStudent = c.getString(c.getColumnIndex(FK_STUDENT_ID))
                locationLink = c.getString(c.getColumnIndex(LOCATION_LINK))
                releaseCount = c.getInt(c.getColumnIndex(RELEASE_COUNT))
                vectorCount = c.getInt(c.getColumnIndex(VECTOR_COUNT))
                val tmp = c.getInt(c.getColumnIndex(SUSPICIOUS))
                if (tmp == 1) {
                    suspiscious = true
                }

                var signature = SignatureHelper(dbImageId, dbImageByteArray ,fkStudent, locationLink, releaseCount, vectorCount, suspiscious)
                signList.add(signature)
            }while(c.moveToNext())
        }
        c.close()
        return signList
    }

    fun insetImage(dbBitmap: ByteArray, imageId: String?, studentNr: String, locationLink: String, releaseCount: Int, vectorCount: Int, suspiscious: Boolean): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(SIGNATURE_NAME, imageId)
        values.put(FK_STUDENT_ID, studentNr)
        values.put(SIGNATURE_BITMAP, dbBitmap)
        values.put(LOCATION_LINK, locationLink)
        values.put(RELEASE_COUNT, releaseCount)
        values.put(VECTOR_COUNT, vectorCount)
        values.put(SUSPICIOUS, suspiscious)

        val result = db.insert(TABLE_SIGNATURE, null, values)
        db.close()
        return !result.equals( -1)
    }

    fun getAllLocationsWithId() : ArrayList<AddressWithIdFirebase> {
        val locationList = ArrayList<AddressWithIdFirebase>()

        //adding location ID to this
        var dbLocId: Int
        var dbLat : Double
        var dbLon : Double
        var date : LocalDate
        var fkSnumber : String
        var dbRoad : String
        var dbHouseNubmer : Int
        var dbPostCode : Int
        var dbTown : String
        var dbNeibhourhood : String
        var dbCountry : String
        var signatureLink : String

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
                dbRoad = c.getString(c.getColumnIndex(ROAD))
                dbHouseNubmer = c.getInt(c.getColumnIndex(HOUSE_NUMBER))
                dbPostCode = c.getInt(c.getColumnIndex(POSTCODE))
                dbTown = c.getString(c.getColumnIndex(TOWN))
                dbNeibhourhood = c.getString(c.getColumnIndex(NEIGHBOURHOOD))
                dbCountry = c.getString(c.getColumnIndex(COUNTRY))
                signatureLink = c.getString(c.getColumnIndex(SIGNATURE_LINK))


                val location = AddressWithIdFirebase(dbLocId, dbLat, dbLon, date, fkSnumber , signatureLink, dbRoad, dbHouseNubmer, dbPostCode, dbTown, dbNeibhourhood, dbCountry)
                locationList.add(location)
            }while(c.moveToNext())
        }
        c.close()
        return locationList
    }

    //update location based on id
    fun updateLocation(adres: AddressWithIdFirebase){

        val db = this.writableDatabase
        val values = ContentValues()
        values.put(LONGITUDE, adres.lon)
        values.put(LATTITUDE, adres.lat)
        values.put(TIMESTAMP, LocalDate.now().toString())
        values.put(FK_STUDENT_ID,adres.fkSnumber)
        values.put(ROAD, adres.road)
        values.put(HOUSE_NUMBER, adres.houseNumber)
        values.put(POSTCODE, adres.postcode)
        values.put(TOWN, adres.town)
        values.put(NEIGHBOURHOOD, adres.neighbourhood)
        values.put(COUNTRY, adres.county)
        db.update(TABLE_LOCATION, values, "location_id=" + adres.addressId, null)
        db.close()
    }

    fun insertLocation(adres : Address): Boolean{
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(LONGITUDE, adres.lon)
        values.put(LATTITUDE, adres.lat)
        values.put(TIMESTAMP, LocalDate.now().toString())
        values.put(FK_STUDENT_ID,adres.fkSnumber)
        values.put(ROAD, adres.road)
        values.put(HOUSE_NUMBER, adres.houseNumber)
        values.put(POSTCODE, adres.postcode)
        values.put(TOWN, adres.town)
        values.put(NEIGHBOURHOOD, adres.neighbourhood)
        values.put(COUNTRY, adres.county)
        values.put(SIGNATURE_LINK, adres.signatureLink)

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
        try {
            val db = this.readableDatabase
            var c = db.rawQuery(selectQuery,null)
            if(c.moveToFirst()){
                do{
                    stname = c.getString(c.getColumnIndex(LASTNAME))
                    stfirstname = c.getString(c.getColumnIndex(FIRSTNAME))
                    stsnr = c.getString(c.getColumnIndex(STUDENT_ID))
                    var s = Student(stname,stfirstname,stsnr)
                    StudentList.add(s)
                    Log.d("FIL", "afterTextChanged: ${s.name}")
                }while(c.moveToNext())
            }
            db.close()
        }catch (e: SQLiteException){

        }
        return StudentList
    }

    fun getSignatureForDetailsList(snumber: String): ArrayList<SignatureList>  {
        val signatureList = ArrayList<SignatureList>()
        var imageByteArray: ByteArray
        var dbRoad : String
        var dbHouseNubmer : Int
        var dbPostCode : Int
        var dbTown : String
        var dbNeibhourhood : String
        var dbCountry : String
        var dbDatum: String
        var dbSignatureId : Int
        var dbSuspicion = false

        val selectQuery = GETSIGNATURE+" where s."+FK_STUDENT_ID+" = '" + snumber +  "'  order by " + SIGNATURE_ID

        val db = this.readableDatabase
        var c = db.rawQuery(selectQuery,null)
        if(c.moveToFirst()){
            do{
                imageByteArray = c.getBlob(0)//c.getColumnIndex(SIGNATURE_BITMAP))
                val img = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                dbRoad = c.getString(c.getColumnIndex(ROAD))?:""
                dbHouseNubmer = c.getInt(c.getColumnIndex(HOUSE_NUMBER))?:0
                dbPostCode = c.getInt(c.getColumnIndex(POSTCODE))?:0
                dbTown = c.getString(c.getColumnIndex(TOWN))?:""
                dbNeibhourhood = c.getString(c.getColumnIndex(NEIGHBOURHOOD))?:""
                dbCountry = c.getString(c.getColumnIndex(COUNTRY))?:""
                dbDatum = c.getStringOrNull(c.getColumnIndex(TIMESTAMP)).toString()//c.getString(c.getColumnIndex(TIMESTAMP))
                dbSignatureId = c.getInt(c.getColumnIndex(SIGNATURE_ID))
                var tmp = c.getInt(c.getColumnIndex(SUSPICIOUS))
                if (tmp == 1) {
                    dbSuspicion = true
                }
                var s = SignatureList(img,dbRoad,dbHouseNubmer,dbPostCode,dbTown,dbNeibhourhood,dbCountry,dbDatum, dbSignatureId, dbSuspicion)
                signatureList.add(s)
                Log.d("sig", "adres: ${s.imageByteArray.toString()}")
            }while(c.moveToNext())
        }
        db.close()

        return signatureList
    }

    fun getExportData(): ArrayList<exportdata>{
        val studentlist = ArrayList<exportdata>()
        var dbsudetid: String
        var dbfirstname: String
        var dblastname: String
        var dbRoad : String
        var dbHouseNubmer : Int
        var dbPostCode : Int
        var dbTown : String
        var dbNeibhourhood : String
        var dbCountry : String
        var dbDatum: String

        val selectQuery = EXPORTDATASTUDENT
        val db = this.readableDatabase
        var c = db.rawQuery(selectQuery,null)
        if(c.moveToFirst()){
            do{
                dbsudetid = c.getString(c.getColumnIndex(STUDENT_ID))
                dbfirstname = c.getString(c.getColumnIndex(LASTNAME))
                dblastname = c.getString(c.getColumnIndex(FIRSTNAME))
                dbRoad = c.getString(c.getColumnIndex(ROAD))?:""
                dbHouseNubmer = c.getInt(c.getColumnIndex(HOUSE_NUMBER))?:0
                dbPostCode = c.getInt(c.getColumnIndex(POSTCODE))?:0
                dbTown = c.getString(c.getColumnIndex(TOWN))?:""
                dbNeibhourhood = c.getString(c.getColumnIndex(NEIGHBOURHOOD))?:""
                dbCountry = c.getString(c.getColumnIndex(COUNTRY))?:""
                dbDatum = c.getStringOrNull(c.getColumnIndex(TIMESTAMP)).toString()//c.getString(c.getColumnIndex(TIMESTAMP))
                var s = exportdata(dbsudetid,dbfirstname,dblastname,dbDatum,dbRoad,dbHouseNubmer,dbPostCode,dbTown,dbNeibhourhood,dbCountry)
                studentlist.add(s)
            }while(c.moveToNext())
        }
        db.close()
        return studentlist
    }

    // SELECT  * from signature where fk_student_id='snumber6'  ORDER by signature_id ASC limit 1
    fun getFirstSignature(snumber: String): SignatureCheck {
        var imageId : String? = null
        var imageByteArray: ByteArray = byteArrayOf()
        var fkStudent = ""
        var releaseCounter = 0
        var vectorCounter = 0


        val selectQuery = "SELECT * FROM " + TABLE_SIGNATURE + " WHERE " + FK_STUDENT_ID +
                " = '" + snumber + "' order by " + SIGNATURE_ID + " ASC LIMIT 1"

        val db = this.readableDatabase
        var c = db.rawQuery(selectQuery, null)
        if(c.moveToFirst()) {
            do {
                imageId = c.getString(c.getColumnIndex(SIGNATURE_ID))
                imageByteArray = c.getBlob(c.getColumnIndex(SIGNATURE_BITMAP))
                fkStudent = c.getString(c.getColumnIndex(FK_STUDENT_ID))
                releaseCounter = c.getInt(c.getColumnIndex(RELEASE_COUNT))
                vectorCounter = c.getInt(c.getColumnIndex(VECTOR_COUNT))
            }while (c.moveToNext())
        }
        db.close()
        return SignatureCheck(imageId, imageByteArray, fkStudent, releaseCounter, vectorCounter)

    }

}