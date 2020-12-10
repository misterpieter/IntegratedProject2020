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


class AdminActivity : AppCompatActivity() {
    val REQUEST_CODE = 1
    lateinit var dataString:String
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)



//        btnExportCSV.isVisible = false
//        btnImportCSV.isVisible = false
//        btnShowAllStudents.isVisible = false


        btnExportCSV.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnImportCSV.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        btnShowAllStudents.setOnClickListener {
            intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }

        btnHome.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        if (haveNetworkConnection()){
            btnSync.isEnabled = true
        }

        btnSync.setOnClickListener{
            val newDBsyn = NewSyncDatabase(this)
            newDBsyn.saveOrUpdateAllStudents()
            newDBsyn.saveOrUpdateAllLocations()
            newDBsyn.saveOrUpdateAllSignatures()
            Toast.makeText(this, "Succesfully synchronized", Toast.LENGTH_SHORT).show()
        }


        if (haveNetworkConnection()) {
            // UpdateAddresses()
        }


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
            }
        }
    }


    //Use reverse search to save streetname
    private fun UpdateAdress(location : AddressWithIdFirebase)  /* : AddressWithIdFirebase */ {
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


                /*
                    val adresObject = jsonObject.getJSONObject("address")
                    Log.d("adressobject", "print a state " + adresObject.getString("state"))
                    streetName = adresObject.getString("country") + " " + adresObject.getString("state") + " " +
                            adresObject.getString("town")
                */




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


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            try {
//                data?.data?.let {
//                    contentResolver.openInputStream(it)
//                }?.let {
//                    val r = BufferedReader(InputStreamReader(it))
//                    while (true) {
//                        val line: String? = r.readLine() ?: break
//                        println(line)
//                    }
//                }
//            } catch (e: Exception) {
//                Toast.makeText(
//                        this,
//                        "Probleem bij het lezen van het bestand",
//                        Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

}