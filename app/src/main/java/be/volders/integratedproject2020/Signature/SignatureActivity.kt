package be.volders.integratedproject2020.Signature

import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaScannerConnection
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import be.volders.integratedproject2020.*
import be.volders.integratedproject2020.Model.Address
import be.volders.integratedproject2020.Model.Student
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.StringBuilder
import java.net.URL
import java.time.LocalDate
import java.util.*


lateinit var recognize:Button
lateinit var clear:Button
lateinit var drawingView: DrawingView
lateinit var btnStore: Button


class SignatureActivity : AppCompatActivity(), LocationListener {
    private val IMAGE_DIRECTORY = "/Pictures"
    private lateinit var locationManager: LocationManager
    private var lat : Double = 0.0
    private var lon : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        var databaseHelper: DatabaseHelpe? = DatabaseHelpe(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)
        StrokeManager.download()

        var bitmap : Bitmap
        var path : String

        val saveStudent = Student(intent.getStringExtra("studentFirstname").toString(),
                                  intent.getStringExtra("studentLastname").toString(),
                                  intent.getStringExtra("studentSnr").toString(), "halima")

        btnStore = findViewById(R.id.buttonSave)
        recognize = findViewById(R.id.recognize)
        clear = findViewById(R.id.clear)
        drawingView = findViewById(R.id.drawingView)


        recognize.setOnClickListener {
            StrokeManager.recognize(this)
        }

        clear.setOnClickListener {
            drawingView.clear()
            //drawingView.clearCanvas()
            StrokeManager.clear()
        }
        btnStore.setOnClickListener{
            bitmap = drawingView.drawToBitmap()
            path = saveImage(bitmap)
            databaseHelper!!.addStudent(saveStudent)
            val bytes = convertSignatur(bitmap)
            databaseHelper!!.insetImage(bytes.toString(),saveStudent.name+ "_" + saveStudent.lastname, saveStudent.snumber)
            //Log.d("check",databaseHelper!!.insetImage(bytes.toString(),saveStudent.name+ "_" + saveStudent.lastname, saveStudent.snumber).toString().plus(" "));
            Log.d("ST", "Signature ok!")
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    //convert
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
       /* try {
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
        }*/
        return ""
    }

    override fun onLocationChanged(location: Location) {
        lat = location.latitude
        lon = location.longitude
        //tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
        val urlReversedSearch = "https://nominatim.openstreetmap.org/reverse?format=json&lat=${location.latitude}&lon=${location.longitude}"
        //val urlReversedSearch = "https://nominatim.openstreetmap.org/reverse?format=json&lat=51.2944529776287&lon=4.485295861959457\n"
        val urlAdress = URL(urlReversedSearch)
        //Toast.makeText(this, "${lat} - ${lon}", Toast.LENGTH_SHORT).show()
        val task = MyAsyncTask()
        task.execute(urlAdress)
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

            /*try {
                adres = Address(
                        lat,
                        lon,
                        LocalDate.now(),
                        "S425316"
                )
                Log.d("TAG", "Address object:\n$adres")
                databaseHelper?.insertLocation(adres)
                tvAddress.text = adres.toString()
            }catch (e:Exception) {
                Log.d("TAG", "EXCEPTION at Mainactivity R233: ${e.message}\n${e.stackTrace}")
            }*/
        }
    }
}
