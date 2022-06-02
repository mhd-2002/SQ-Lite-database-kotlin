package com.example.sqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class StudentAdapter(private val context: Context) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var stdList: ArrayList<StudentModel> = ArrayList()
    private var onClickItem: ((StudentModel) -> Unit)? = null
    private var onClickDeleteItem: ((StudentModel) -> Unit)? = null

    fun addItem(items: ArrayList<StudentModel>) {
        this.stdList = items

    }

    fun setonClickItem(callback: (StudentModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setonDeleteClickItem(callback: (StudentModel) -> Unit) {
        this.onClickDeleteItem = callback
    }

    class StudentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val id = view.findViewById<TextView>(R.id.tv_id)
        private val name = view.findViewById<TextView>(R.id.tv_name)
        private val email = view.findViewById<TextView>(R.id.tv_email)
         val bt_delete = view.findViewById<Button>(R.id.bt_delete)

        fun bindView(std: StudentModel) {
            id.text = std.id.toString()
            name.text = std.name
            email.text = std.email

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudentViewHolder(
        LayoutInflater.from(context).inflate(R.layout.card_items_students, parent, false)
    )

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener { onClickItem?.invoke(std)}
        holder.bt_delete.setOnClickListener { onClickDeleteItem?.invoke(std) }

    }

    override fun getItemCount(): Int = stdList.size

}