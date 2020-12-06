package be.volders.integratedproject2020.Students

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import be.volders.integratedproject2020.MainActivity
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.R


class StudentAdapter(context:Context, private val students: List<Student>) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        holder.tvLastName.text = student.lastname
        holder.tvName.text = student.name
        holder.tvSnumber.text = student.snumber
        holder.tvPassword.text = student.password

        // click listener op item
        holder.itemView.setOnClickListener{
            //toast + redirect naar home (enkel als voorbeeld om naar een andere activity te gaan wanneer geklikt )
            Toast.makeText(context, "${student} clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLastName: TextView = itemView.findViewById(R.id.tvLastName)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvSnumber: TextView = itemView.findViewById(R.id.tvSnumber)
        var tvPassword: TextView = itemView.findViewById(R.id.tvPassword)
    }
}