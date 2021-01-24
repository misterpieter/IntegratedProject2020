package be.volders.integratedproject2020.StudentDetails;

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.Model.SignatureList
import be.volders.integratedproject2020.R
import java.lang.Exception


class DetailsListAdapter(context: Context, private val signatuurList: List<SignatureList>) :  RecyclerView.Adapter<DetailsListAdapter.ViewHolder>() {

    val context = context
    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_student_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsListAdapter.ViewHolder, position: Int) {
        val signature = signatuurList[position]

        holder.tvRemoveFlag?.isGone = true
        holder.tvRemoveFlag?.isClickable = false

        Log.d("signatureSusAtView", "Position: $position    and suspision is :  ${signature.dbSuspisious}")

        if (signature.dbSuspisious) {
            holder.tvRemoveFlag?.isGone = false
            holder.tvRemoveFlag?.isClickable = true

            holder.tvRemoveFlag?.setOnClickListener{
                try {
                    databaseHelper?.removeSuspicion(signature.dbsignatureId)
                    Toast.makeText(context, "Verdenking verwijderd", Toast.LENGTH_SHORT).show()

                    holder.tvRemoveFlag?.isGone = true
                    holder.tvRemoveFlag?.isClickable = false

                }catch (ex: Exception) {
                    Toast.makeText(context, "Kon verdenking niet verwijderen", Toast.LENGTH_SHORT).show()
                    Log.e("RemoveSuspicion", ex.stackTraceToString())
                }
            }
        }

        holder.tvSignature?.setImageBitmap(signature.imageByteArray)
        holder.tvroad?.text = signature.dbRoad
        holder.tvhousenumber?.text = signature.dbHouseNubmer.toString()
        holder.tvdatum?.text = signature.dbDatum.toString()
        holder.tvpostcode.text = signature.dbPostCode.toString()
        holder.tvtown.text = signature.dbTown
        holder.tvcountry.text = signature.dbCountry
    }


    override fun getItemCount(): Int {
        return signatuurList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRemoveFlag: Button? = itemView.findViewById(R.id.btnRemoveFlag)
        var tvSignature: ImageView? = itemView.findViewById(R.id.imageView) as? ImageView
        var tvroad: TextView? = itemView.findViewById(R.id.tvStraat)
        var tvhousenumber: TextView? = itemView.findViewById(R.id.tvHuisnumer)
        var tvdatum: TextView? = itemView.findViewById(R.id.tvDatum)
        var tvpostcode: TextView = itemView.findViewById(R.id.tvpostcode)
        var tvtown: TextView = itemView.findViewById(R.id.tvTown)
        //var tvneibhourhood: TextView = itemView.findViewById(R.id.tvHuisnr)
        var tvcountry: TextView = itemView.findViewById(R.id.tvCountry)
    }
}
