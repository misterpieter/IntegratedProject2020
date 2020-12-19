package be.volders.integratedproject2020

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import be.volders.integratedproject2020.Admin.AddressWithIdFirebase
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Model.SignatureHelper
import be.volders.integratedproject2020.Model.SignatureList
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
        private val ROAD = "road"
        private val HOUSE_NUMBER = "house_number"
        private val POSTCODE = "postcode"
        private val TOWN = "town"
        private val NEIGHBOURHOOD = "neighbourhood"
        private val COUNTRY = "country"

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
                + FK_STUDENT_ID + " VARCHAR(20),"
                + " FOREIGN KEY( " + FK_STUDENT_ID + " ) REFERENCES " + TABLE_STUDENTS + " ( " + STUDENT_ID + " ));"
                )
        private val JOIN = (
                "select st."+FIRSTNAME+", st."+LASTNAME+", st."+STUDENT_ID+
                        " from "+TABLE_STUDENTS+" as st " +
                        " left join "+TABLE_LOCATION+" as lo on st."+STUDENT_ID+" = lo."+FK_STUDENT_ID
                )
        private  val GETSIGNATURE = (
                "select s."+SIGNATURE_BITMAP+", l."+ ROAD+",  l."+HOUSE_NUMBER+", l."+POSTCODE+", l."+TIMESTAMP+
                        ", l."+TOWN+", l."+NEIGHBOURHOOD+", l."+COUNTRY+" from "+TABLE_SIGNATURE+" as s "+
                        "left join "+TABLE_LOCATION+" as l on l."+FK_STUDENT_ID+" = s."+FK_STUDENT_ID
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

    fun insetImage(dbBitmap: ByteArray, imageId: String?, studentNr: String): Boolean {
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
        var dbRoad : String
        var dbHouseNubmer : Int
        var dbPostCode : Int
        var dbTown : String
        var dbNeibhourhood : String
        var dbCountry : String

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


                val location = AddressWithIdFirebase(dbLocId, dbLat, dbLon, date, fkSnumber, dbRoad, dbHouseNubmer, dbPostCode, dbTown, dbNeibhourhood, dbCountry)
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

        val result = db.insert(TABLE_LOCATION, null, values)
        Log.d("FIL", "location opgeslagen: ${values}")
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
        db.close()
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
        var i = 0
        val selectQuery = GETSIGNATURE+" where s."+FK_STUDENT_ID+" = '"+snumber+"'"

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
                dbDatum = c.getString(c.getColumnIndex(TIMESTAMP))
                var s = SignatureList(img,dbRoad,dbHouseNubmer,dbPostCode,dbTown,dbNeibhourhood,dbCountry,dbDatum)
                signatureList.add(s)
                Log.d("sig", "adres: ${s.imageByteArray.toString()}")
            }while(c.moveToNext())
        }
        db.close()
        return signatureList
    }
}