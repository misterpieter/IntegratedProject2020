package be.volders.integratedproject2020

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Signature.SignatureActivity
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.StringBuilder
import java.net.URL

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2
    private lateinit var jsonResult :JsonObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSignature.setOnClickListener {
            intent = Intent(this, SignatureActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnCoordinates.setOnClickListener {
            getLocation()
        }

        btnExportCsv.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnImportCSV.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }

        btnShowAllStudents.setOnClickListener {
            Toast.makeText(this, "Nog niet geimplementeerd", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        tvGpsLocation = findViewById(R.id.tvCoorddinates)
        tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
//        val urlReversedSearch = "https://nominatim.openstreetmap.org/reverse?format=json&lat=${location.latitude}&lon=${location.longitude}"
        val urlReversedSearch = "        https://nominatim.openstreetmap.org/reverse?format=json&lat=51.2944529776287&lon=4.485295861959457\n"
        val urlAdress = URL(urlReversedSearch)

        Toast.makeText(this, "EMULATOR -> address object = hardcoded", Toast.LENGTH_SHORT).show()
        val task = MyAsyncTask()
        task.execute(urlAdress)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    inner class MyAsyncTask : AsyncTask<URL, Int, String>() {

        var response = ""

        override fun onPreExecute(){
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: URL?): String {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(params[0]!!)
                    .build()
            response = client.newCall(request).execute().body!!.string()
            return response
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            val jsonString = StringBuilder(result!!)

            val parser: Parser = Parser.default()
            val obj = parser.parse(jsonString) as JsonObject
            val address = obj["address"] as JsonObject

            Log.d("TAG", "obj: ${obj.toJsonString(true)}")
            Log.d("TAG", "display_name: ${obj.get("display_name")}")
            Log.d("TAG", "address: ${address.toJsonString(true)}")
//
//            Log.d("TAG", "road: ${address.get("road").toString()}")
//            Log.d("TAG", "house_number: ${address.get("house_number")}")
//            Log.d("TAG", "postcode: ${address.get("postcode")}")
//            Log.d("TAG", "town: ${address.get("town")}")

            try {
                var addressObject = Address(
                        address["road"]?.toString(),
                        address["house_number"]?.toString()?.toInt(),
                        address["postcode"]?.toString()?.toInt(),
                        address["town"]?.toString(),
                        address["neighbourhood"]?.toString(), address["county"]?.toString())
                Log.d("TAG", "Address object:\n$addressObject")
                tvAddress.text = addressObject.toString()
            }catch (e:Exception){
                Log.d("TAG", "EXCEPTION: ${e.message}\n${e.stackTrace}")
            }
        }
    }





}




