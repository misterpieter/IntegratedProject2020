package be.volders.integratedproject2020.StudentDetails;
/*
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.volders.integratedproject2020.R

class DetailsListAdapter (context:Context, private val signatuurList: List<SignatuurList>) :  RecyclerView.Adapter<DetailsListAdapter.ViewHolder>() {

    val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsListAdapter.ViewHolder, position: Int) {
        val signature = signatuurList[position]
        //holder.tvSignature.text = signatuurList.
        holder.tvadresnaam.text = signature.adres
/*
        // click listener op item
        holder.itemView.setOnClickListener{
            //toast + redirect naar home (enkel als voorbeeld om naar een andere activity te gaan wanneer geklikt )
            Toast.makeText(context, "${signature} clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }*/
    }

    override fun getItemCount(): Int {
        return signatuurList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSignature: TextView = itemView.findViewById(R.id.tvStraat)
        var tvadresnaam: TextView = itemView.findViewById(R.id.tvHuisnr)
    }
}
class SignatuurList(
        var imageByteArray: ByteArray,
        var adres: String,
)*/