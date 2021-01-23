package be.volders.integratedproject2020.Students

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import be.volders.integratedproject2020.DatabaseHelpe
import be.volders.integratedproject2020.Model.Student
import be.volders.integratedproject2020.R
import be.volders.integratedproject2020.StudentDetails.StudentDetailsActivity


class StudentAdapter(context:Context, private val students: List<Student>) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    var databaseHelper: DatabaseHelpe? = DatabaseHelpe(context)
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

        if (databaseHelper?.getSuspiciousOrNot(student.snumber) == true) {
            holder.tvName.setTextColor(Color.RED)
            holder.tvLastName.setTextColor(Color.RED)
            holder.tvSnumber.setTextColor(Color.RED)
        }

        // click listener op item
        holder.itemView.setOnClickListener{
            val intent = Intent(context, StudentDetailsActivity::class.java)
            intent.putExtra("studentSnr",student.snumber)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLastName: TextView = itemView.findViewById(R.id.tvFirstname)
        var tvName: TextView = itemView.findViewById(R.id.tvDatum)
        var tvSnumber: TextView = itemView.findViewById(R.id.tvSnummer)
    }
}