package com.example.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(var context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "student.db"
        const val DATABASE_VERSION = 2
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val TBL_student = "tbl_student"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblStudent = ("create table $TBL_student ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "email text)")
        db?.execSQL(createTblStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_student")
        onCreate(db)

    }

    fun insertStudent(std: StudentModel): Long {
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(NAME, std.name)
        contentValue.put(EMAIL, std.email)

        val success = db.insert(TBL_student, null, contentValue)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllStudents(): ArrayList<StudentModel> {
        val stdList: ArrayList<StudentModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_student"
        val db = this.readableDatabase

        val cursor: Cursor

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: Exception) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))

                val std = StudentModel(id = id, name = name, email = email)
                stdList.add(std)
            } while (cursor.moveToNext())

        }
        return stdList
    }

    fun updateStudent(std:StudentModel):Int{
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(ID , std.id)
        contentValue.put(NAME, std.name)
        contentValue.put(EMAIL, std.email)

       val success = db.update(TBL_student , contentValue , "id=" + std.id, null)
        db.close()
        return success
    }

    fun deleteStudentsById(id:Int): Int {
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(ID , id)

        val success = db.delete(TBL_student , "id=$id", null)
        db.close()

        return success
    }

}