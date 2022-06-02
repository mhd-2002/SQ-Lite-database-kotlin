package com.example.sqlite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.databinding.ActivityMainBinding
import java.nio.file.attribute.AclEntry

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var sqLiteHelper: SQLiteHelper
    private var adapter: StudentAdapter? = null
    private var std: StudentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycleView()

        sqLiteHelper = SQLiteHelper(this)

        binding.btAdd.setOnClickListener { addStudent() }
        binding.btView.setOnClickListener { getStudents() }

        adapter?.setonClickItem {

            binding.edName.setText(it.name)
            binding.edEmail.setText(it.email)
            std = it

        }
        binding.btUpdate.setOnClickListener { std?.let { it1 -> updateStudents(it1) } }

        adapter?.setonDeleteClickItem {
            deleteStudent(it.id)

        }
    }

    private fun deleteStudent(id: Int) {
        if (id == null) return

        AlertDialog.Builder(this)
            .setMessage("Are you sure?")
            .setCancelable(true)
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton("YES") { dialog, _ ->
                sqLiteHelper.deleteStudentsById(id)
                getStudents()
                dialog.dismiss()
            }.create().show()

    }

    private fun updateStudents(std: StudentModel) {
        val name = binding.edName.text.toString()
        val email = binding.edEmail.text.toString()

        if (name == std.name && email == std.email) {
            Toast.makeText(this, "No differences", Toast.LENGTH_LONG).show()
            clearEditTexts()
            return
        }

        val std = StudentModel(id = std.id, name = name, email = email)
        val status = sqLiteHelper.updateStudent(std)
        if (status > -1) {
            clearEditTexts()
            getStudents()
        } else {
            Toast.makeText(this, "Update Failed..", Toast.LENGTH_LONG).show()
        }

    }

    private fun initRecycleView() {

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(this)
        binding.recyclerView.adapter = adapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getStudents() {
        val stdList = sqLiteHelper.getAllStudents()
        adapter?.addItem(stdList)
        adapter?.notifyDataSetChanged()

    }

    private fun addStudent() {
        val name = binding.edName.text.toString()
        val email = binding.edEmail.text.toString()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please Enter Required Field", Toast.LENGTH_LONG).show()
        } else {
            val std = StudentModel(name = name, email = email)
            val status = sqLiteHelper.insertStudent(std)

            if (status > -1) {
                Toast.makeText(this, "Student Added...", Toast.LENGTH_LONG).show()
                clearEditTexts()
                getStudents()
            } else {
                Toast.makeText(this, "Student Add has failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearEditTexts() {
        binding.edName.setText("")
        binding.edEmail.setText("")
        binding.edName.requestFocus()

    }
}