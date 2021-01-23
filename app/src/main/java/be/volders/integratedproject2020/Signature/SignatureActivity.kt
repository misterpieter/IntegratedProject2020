package be.volders.integratedproject2020.Signature

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import be.volders.integratedproject2020.*
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Model.Student
import kotlinx.android.synthetic.main.activity_admin.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.time.LocalDate
import java.util.*

lateinit var recognize:Button
lateinit var clear:Button
lateinit var drawingView: DrawingView
lateinit var btnStore: Button


class SignatureActivity : AppCompatActivity(), LocationListener {
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)
    private val IMAGE_DIRECTORY = "/Pictures"
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var lat : Double = 0.0
    private var lon : Double = 0.0
    private lateinit var adres : Address
    private var snumber:String = ""
    private var sigAndLocationLink = UUID.randomUUID()
    private var suspiciousSignature = false

    override fun onCreate(savedInstanceState: Bundle?) {
        //var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)
        StrokeManager.download()
        var bitmap : Bitmap
        var path : String

        val saveStudent = Student(intent.getStringExtra("studentFirstname").toString(),
                intent.getStringExtra("studentLastname").toString(),
                intent.getStringExtra("studentSnr").toString())

        btnStore = findViewById(R.id.buttonSave)
        recognize = findViewById(R.id.recognize)
        clear = findViewById(R.id.clear)
        drawingView = findViewById(R.id.drawingView)

        recognize.setOnClickListener {
            StrokeManager.recognize(this)
        }

        btnHome.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        clear.setOnClickListener {
            drawingView.clear()
            //drawingView.clearCanvas()
            StrokeManager.clear()
        }
        btnStore.setOnClickListener{

            snumber = saveStudent.snumber
            bitmap = drawingView.drawToBitmap()

            //Method that checks suspicion level and sets suspicion value
            CheckSuspicion(snumber, drawingView.getReleaseCounter(), drawingView.getVectorCounter())
            Log.d("SUSPISION", "Is this signature suspiscious ?     $suspiciousSignature")

            path = saveImage(bitmap)

            databaseHelper!!.addStudent(saveStudent)
            //databaseHelper!!.addStudent(saveStudent)
            val bytes = convertSignatur(bitmap)
            databaseHelper!!.insetImage(bytes, saveStudent.name + "_" + saveStudent.lastname, saveStudent.snumber, sigAndLocationLink.toString(), drawingView.getReleaseCounter(), drawingView.getVectorCounter(), suspiciousSignature)

            getLocation()

            Log.d("ST", "Signature ok!")
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun CheckSuspicion(snumber: String, releases: Int, vectors: Int) {

        val signatureCheck = databaseHelper!!.getFirstSignature(snumber)
        // if there is a first signature
        if (signatureCheck.imageId != null){

            // if amount of releases do not match => suspicious
            if (signatureCheck.releaseCounter != releases) {
                suspiciousSignature = true
            }

            // if vector deviation is > 20 => suspicious
            val absDifference : Int = Math.abs(signatureCheck.vectorCounter - vectors)
            if (absDifference > 20) {
                suspiciousSignature = true
            }

        } else {
            //first signature => not suspicious
            suspiciousSignature = false
        }


    }

    //convert img
    private  fun convertSignatur(myBitmap: Bitmap): ByteArray {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val byteArray = bytes.toByteArray()
        return byteArray
    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = convertSignatur(myBitmap)
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
            //Log.d("img", wallpaperDirectory.toString())
        }
        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis().toString() + ".png")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes)
            MediaScannerConnection.scanFile(this, arrayOf(f.getPath()), arrayOf("image/png"), null)
            fo.close()
            Log.d("save", "File Saved::--->" + f.getAbsolutePath())
            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        var urlAdress : URL? = null
        val task = MyAsyncTask()

        if (haveNetworkConnection()){
            lat = location.latitude
            lon = location.longitude
            val urlReversedSearch = "https://nominatim.openstreetmap.org/reverse?format=json&lat=${location.latitude}&lon=${location.longitude}"
            urlAdress = URL(urlReversedSearch)
        }

        task.execute(urlAdress)
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



    inner class MyAsyncTask : AsyncTask<URL, Int, String>() {
        var response : String? = null

        override fun doInBackground(vararg params: URL?): String? {

            if(haveNetworkConnection()) {
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url(params[0]!!)
                        .build()
                response = client.newCall(request).execute().body!!.string()
            }


            return response
        }

        fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

         /*   val jsonString = StringBuilder(result!!)

            val parser: Parser = Parser.default()
            val obj = parser.parse(jsonString) as JsonObject
            val address = obj["address"] as JsonObject
         */
            // getAddressDisplayName(lat, lon)

            try {
                adres = Address(
                        lat,
                        lon,
                        LocalDate.now(),
                        snumber,
                        sigAndLocationLink.toString()
                )
                Log.d("TAG", "Address object:\n${adres.date}")
                databaseHelper?.insertLocation(adres)
            }catch (e: Exception){
                Log.d("TAG", "EXCEPTION at Mainactivity R233: ${e.message}\n${e.stackTrace}")
            }
        }
    }
}