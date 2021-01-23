package be.volders.integratedproject2020.Admin

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import be.volders.integratedproject2020.Model.exportdata
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.io.FileOutputStream

class exportHelper (){
    /*
    fun exportData() {
        // read from `String`
        val csvData: String = "student id, firstname, lastname, locationtime, road, housenumber, postcode, town"
        val rows: List<List<String>> = csvReader().readAll(csvData)
        val row2 = listOf("d", "e", "f")
        csvWriter().open(File("data.csv")) {
            writeRow(rows)
            writeRow(row2)
            writeRow("g", "h", "i")
            writeRows(listOf(rows, row2))
        }*/

        fun export(exportdata: ArrayList<exportdata>,context: Context) {

            //generate data
            val data = StringBuilder()
            data.append("studentnr,name, lastname, date, road, houseNumber, postcode, town, neighbourhood, county")
            for(s in exportdata){
                data.append("\n"+"${s.snumber},${s.name},${s.lastname},${s.date},${s.road},${s.houseNumber},${s.postcode},${s.town},${s.neighbourhood},${s.county}")
            }
            // data.append("\n"+"$i,${i + i}".trimIndent()) //"\n" + String.valueOf(i) + "," + String.valueOf(i + i)

            try {
                //saving the file into devices
                val out: FileOutputStream = context.openFileOutput("data.csv", Context.MODE_PRIVATE)
                out.write(data.toString().toByteArray())
                out.close()

                //exporting
                //context = getApplicationcontext();
                val filelocation: File = File(context.getFilesDir(), "data.csv")
                val path = FileProvider.getUriForFile(context, "be.volders.integratedproject2020.fileprovider", filelocation)
                val fileIntent = Intent(Intent.ACTION_SEND)
                fileIntent.type = "text/csv"
                fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data")
                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                fileIntent.putExtra(Intent.EXTRA_STREAM, path)
                context.startActivity(Intent.createChooser(fileIntent, "Send mail"))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }
