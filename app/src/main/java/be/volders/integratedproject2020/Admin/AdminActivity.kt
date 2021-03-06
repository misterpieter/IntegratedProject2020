package be.volders.integratedproject2020.Admin

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.Helper.getStudentsFromCSVString
import be.volders.integratedproject2020.Helper.loadDataFromIntent
import be.volders.integratedproject2020.MainActivity
import be.volders.integratedproject2020.Model.SignatureList
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.Model.exportdata
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.Students.StudentListActivity
import kotlinx.android.synthetic.main.activity_admin.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlinx.coroutines.*
import okhttp3.internal.wait

lateinit var studentlist : ArrayList<exportdata>
class AdminActivity : AppCompatActivity() {
    val REQUEST_CODE = 1
    lateinit var dataString:String
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)
    var exporthelper: exportHelper = exportHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        btnExportCSV.setOnClickListener {
            studentlist = databaseHelper!!.getExportData()
            exporthelper.export(studentlist,this)
        }

        btnImportCSV.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        //If database is empty => disable buttons clearDB and btnShowAllStudents
        if (DatabaseEmptyCheck()){
            btnClearDB.isEnabled = false
            btnShowAllStudents.isEnabled = false
            btnSync.isEnabled = false
        } else {
            btnClearDB.isEnabled = true
            btnShowAllStudents.isEnabled = true
            if (haveNetworkConnection()){
                btnSync.isEnabled = true
            }
        }

        btnShowAllStudents.setOnClickListener {
            intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }

        btnClearDB.setOnClickListener{
            databaseHelper?.clearDatabase()
            Toast.makeText(this, "DB is leeg gemaakt", Toast.LENGTH_SHORT).show()
            btnShowAllStudents.isEnabled = false
            btnClearDB.isEnabled = false
            btnSync.isEnabled = false
        }


        btnSync.setOnClickListener{
            //Coroutine : pauses main thread untill update UpdateAdressesBeforeUpload is done
            Toast.makeText(this,"Synchronisatie gestart", Toast.LENGTH_SHORT).show()
            runBlocking {
                UpdateAdressesBeforeUpload()
            }
            BackupToFirebase()
        }

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // finish the current activity
        }
    }

    fun DatabaseEmptyCheck() : Boolean {
        var isEmpty = true
        try {
            var studentList = databaseHelper!!.getAllStudent()
            if (studentList!!.isNotEmpty()) {
                isEmpty = false
            }
        }catch (ex : Exception) {
            Log.e("DatabaseCheck", ex.stackTraceToString())
        }
        return isEmpty
    }


    fun UpdateAdressesBeforeUpload() {
        try {
            UpdateAddresses()
        } catch (e: Exception) {
            Toast.makeText(this, "Kon adress niet updaten", Toast.LENGTH_SHORT).show()
            Log.e("Error while updating adresses", e.stackTraceToString())
        }
    }

    fun BackupToFirebase(){
        try {
            val newDBsyn = NewSyncDatabase(this)
            newDBsyn.saveOrUpdateAllStudents()
            newDBsyn.saveOrUpdateAllLocations()
            newDBsyn.saveOrUpdateAllSignatures()
            Toast.makeText(this, "Synchronisatie geslaagd", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception) {
            Toast.makeText(this, "Kon niet synchroniseren met firebase", Toast.LENGTH_SHORT).show()
            Log.e("Error while synchronizing to firebase", e.stackTraceToString())
        }
        Log.d("SYNCBUTTON", "backed up to firebase" )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                data?.data?.let {
                    contentResolver.openInputStream(it)
                }?.let {
                    dataString = loadDataFromIntent(it)!!

                    var str = getStudentsFromCSVString(dataString)
                    for(s in str){
                        Log.d("getCSV", s.name)
                        val firstname = s.name
                        val lasstname = s.lastname
                        val snr = s.snumber
                        val saveStudent = Student(firstname, lasstname,snr)
                        databaseHelper!!.addStudent(saveStudent)
                    }
                    Toast.makeText(this, "CSV is ingeladen", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Probleem bij het lezen van het bestand",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //loops over all locations and if postcode = 0 => update adress
    private fun UpdateAddresses() {
        val locationList = databaseHelper?.getAllLocationsWithId()!!
        for (location in locationList){
            if(location.postcode == 0){
                UpdateAdress(location)
                Log.d("SYNCBUTTON", "updated ${location.signatureLink}")
            }
        }
    }

    //Use reverse search to save streetname and update the in the database
    private fun UpdateAdress(location : AddressWithIdFirebase) {
        val urlReversedSearch = "https://nominatim.openstreetmap.org/reverse?format=json&lat=${location.lat}&lon=${location.lon}"
        val client = OkHttpClient()
        val request = Request.Builder().url(urlReversedSearch).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("getStreetName", "response failed")
                    return
                }
                val jsonData = response.body!!.string()
                val jsonObject = JSONObject(jsonData)
                Log.d("jsonobjectarray", "jsondata: $jsonData")
                Log.d("jsonobjectarray", "jsonobject: $jsonObject")
                val adresObject = jsonObject.getJSONObject("address")

                //only gives value when field is found
                location.road = adresObject.optString("road", "No road")
                location.houseNumber = adresObject.optInt("house_number", 0)
                location.postcode = adresObject.optInt("postcode", 0)
                location.town = adresObject.optString("town", "NO town")
                location.neighbourhood = adresObject.optString("neighbourhood", "NO neighbourhood")
                location.county = adresObject.optString("county", "NO county")

                //calls update method
                databaseHelper?.updateLocation(location)
            }
        })
        client.dispatcher.executorService.shutdown()
    }

    //Checks if connectedvia WIFI or Mobile to Internet
    private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals(
                    "WIFI",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }
}