package be.volders.integratedproject2020.StudentDetails;

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import be.volders.integratedproject2020.Model.SignatureList
import be.volders.integratedproject2020.R


class DetailsListAdapter(context: Context, private val signatuurList: List<SignatureList>) :  RecyclerView.Adapter<DetailsListAdapter.ViewHolder>() {

    companion object {
        const val STUDENTLIST = 1
        const val STUDENTDETAILS = 2
    }
    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_student_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsListAdapter.ViewHolder, position: Int) {
        val signature = signatuurList[position]
        holder.tvSignature?.setImageBitmap(signature.imageByteArray)
        holder.tvroad?.text = signature.dbRoad
        holder.tvhousenumber?.text = signature.dbHouseNubmer.toString()
        holder.tvdatum?.text = signature.dbDatum.toString()
    }

    override fun getItemCount(): Int {
        return signatuurList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSignature: ImageView? = itemView.findViewById(R.id.imageView) as? ImageView
        var tvroad: TextView? = itemView.findViewById(R.id.tvStraat)
        var tvhousenumber: TextView? = itemView.findViewById(R.id.tvHuisnumer)
        var tvdatum: TextView? = itemView.findViewById(R.id.tvDatum)
        //var tvpostcode: TextView = itemView.findViewById(R.id.tvHuisnr)
        //var tvtown: TextView = itemView.findViewById(R.id.tvHuisnr)
        //var tvneibhourhood: TextView = itemView.findViewById(R.id.tvHuisnr)
        //var tvcountry: TextView = itemView.findViewById(R.id.tvHuisnr)
    }
}
