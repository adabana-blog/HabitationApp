package com.websarva.wings.android.HabitationApp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.StringBuilder

class DatabaseHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "habitation.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sb = StringBuilder()
        sb.append("CREATE TABLE habit(")
        sb.append(" habit_id INTEGER PRIMARY KEY,")
        sb.append(" name TEXT,")
        sb.append(" insert_date TEXT,")
        sb.append(" is_deleted BOOLEAN,")
        sb.append(" is_complete BOOLEAN")
        sb.append(");")
        var sql = sb.toString()
        db.execSQL(sql)

        sb.clear()
        sb.append("CREATE TABLE daily_habit_record(")
        sb.append(" date TEXT,")
        sb.append(" habit_id INTEGER,")
        sb.append(" is_checked BOOLEAN,")
        sb.append(" emoticon_id INTEGER,")
        sb.append(" update_date TEXT,")
        sb.append("PRIMARY KEY(date, habit_id)")
        sb.append(");")
        sql = sb.toString()
        db.execSQL(sql)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}