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
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.Students.StudentListActivity
import kotlinx.android.synthetic.main.activity_admin.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlinx.coroutines.*
import okhttp3.internal.wait


class AdminActivity : AppCompatActivity() {
    val REQUEST_CODE = 1
    lateinit var dataString:String
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        btnExportCSV.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnImportCSV.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(intent, REQUEST_CODE)
            btnShowAllStudents.isEnabled = true
        }

        btnShowAllStudents.setOnClickListener {
            intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }

        btnHome.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnClearDB.setOnClickListener{
            databaseHelper?.clearDatabase()
            Toast.makeText(this, "DB is leeg gemaakt", Toast.LENGTH_SHORT).show()
            btnShowAllStudents.isEnabled = false
        }

        if (haveNetworkConnection()){
            btnSync.isEnabled = true
            btnUpdateLocation.isEnabled = true
        }

        btnSync.setOnClickListener{
            //Coroutine : pauses main thread untill update UpdateAdressesBeforeUpload is done
            runBlocking {
                UpdateAdressesBeforeUpload()
            }
            BackupToFirebase()
        }

        btnUpdateLocation.setOnClickListener{
            UpdateAddresses()
            Toast.makeText(this, "Succesfully updated locations", Toast.LENGTH_SHORT).show()
        }
    }

    fun UpdateAdressesBeforeUpload() {
        try {
            UpdateAddresses()
        } catch (e: Exception) {
            Toast.makeText(this, "Could not update adresses", Toast.LENGTH_SHORT).show()
            Log.e("Error while updating adresses", e.stackTraceToString())
        }
    }

    fun BackupToFirebase(){
        try {
            val newDBsyn = NewSyncDatabase(this)
            newDBsyn.saveOrUpdateAllStudents()
            newDBsyn.saveOrUpdateAllLocations()
            newDBsyn.saveOrUpdateAllSignatures()
        }
        catch (e: Exception) {
            Toast.makeText(this, "Could not synchronize to firebase", Toast.LENGTH_SHORT).show()
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
                    getStudentsFromCSVString(dataString)
                    Toast.makeText(this, "CSV is ingeladen", Toast.LENGTH_SHORT).show()
                    Log.e("getCSV", dataString
                    )
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Probleem bij het lezen van het bestand",
                    Toast.LENGTH_LONG
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