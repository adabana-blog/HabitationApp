package com.websarva.wings.android.HabitationApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import java.lang.StringBuilder
import java.util.*

class DailyCheckActivity : AppCompatActivity() {

    private val _helper = DatabaseHelper(this@DailyCheckActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_check)

        val db = _helper.writableDatabase
        val year:String
        val month:String
        val date:String

        val intent = getIntent()
        val action = intent.action
        if (action != null){
            //テスト用に習慣リストを作成 START
            var deleteSql = "DELETE FROM habit"
            var stmt = db.compileStatement(deleteSql)
            stmt.executeUpdateDelete()

            val sqlInsertList = mutableListOf<String>()
            sqlInsertList.add("INSERT INTO habit (habit_id, name, insert_date, is_complete, is_deleted) VALUES (1, 'テスト1', '2019/12/10', 0, 0)")
            sqlInsertList.add("INSERT INTO habit (habit_id, name, insert_date, is_complete, is_deleted) VALUES (2, 'テスト2', '2019/12/11', 0, 0)")
            sqlInsertList.add("INSERT INTO habit (habit_id, name, insert_date, is_complete, is_deleted) VALUES (3, 'テスト3', '2019/12/12', 0, 0)")
            for (sql in sqlInsertList){
                stmt = db.compileStatement(sql)
                stmt.executeInsert()
            }
            deleteSql = "DELETE FROM daily_habit_record"
            stmt = db.compileStatement(deleteSql)
            stmt.executeUpdateDelete()

            val sqlInsertList2 = mutableListOf<String>()
            sqlInsertList2.add("INSERT INTO daily_habit_record (date, habit_id, is_checked, emoticon_id, update_date) VALUES ('2019/12/10', 1, 0, 1,'20180101')")
            sqlInsertList2.add("INSERT INTO daily_habit_record (date, habit_id, is_checked, emoticon_id, update_date) VALUES ('2019/12/11', 2, 1, 2,'20180101')")
            sqlInsertList2.add("INSERT INTO daily_habit_record (date, habit_id, is_checked, emoticon_id, update_date) VALUES ('2019/12/12', 3, 0, 3,'20180101')")
                for (sql in sqlInsertList2) {
                    stmt = db.compileStatement(sql)
                    stmt.executeInsert()
                }
            //テスト用に習慣リストを作成 END

            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR).toString()
            month = (c.get(Calendar.MONTH) + 1).toString()
            date = c.get(Calendar.DATE).toString()
            val tvDate = findViewById<TextView>(R.id.tvDate)
            //string.xml側でプレースホルダーを使用し文字を設定する
            tvDate.text = getString(R.string.selectedDate,year,month,date)
        }
        else{
            year = intent.getStringExtra("year")!!
            month = intent.getStringExtra("month")!!
            date = intent.getStringExtra("date")!!
            val tvDate = findViewById<TextView>(R.id.tvDate)
            tvDate.text = getString(R.string.selectedDate,year,month,date)
        }

        val sb = StringBuilder()
        sb.append(" SELECT ")
        sb.append("     habit.name ")
        sb.append("     ,daily_habit_record.is_checked ")
        sb.append("     ,daily_habit_record.emoticon_id ")
        sb.append(" FROM ")
        sb.append("     habit ")
        sb.append(" LEFT OUTER JOIN ")
        sb.append("     daily_habit_record ")
        sb.append(" ON ")
        sb.append("     habit.habit_id = daily_habit_record.habit_id ")
        sb.append(" AND daily_habit_record.date = ? ")
        sb.append(" WHERE ")
        sb.append("     habit.is_complete = 0 ")
        sb.append(" AND habit.is_deleted = 0 ")

        val sql = sb.toString()
        val selectedDate = String.format("%s/%s/%s",year,month,date)
        val param = arrayOf(selectedDate)
        val habitCursor = db.rawQuery(sql,param)


        val habitList = mutableListOf<DailyHabitItems>()
        var index = 0

        while (habitCursor.moveToNext()){
            val nameIndex = habitCursor.getColumnIndex("name")
            val checkIdIndex = habitCursor.getColumnIndex("is_checked")
            val emoticonIdIndex = habitCursor.getColumnIndex("emoticon_id")

            val name = habitCursor.getString(nameIndex)
            val checkId = when(habitCursor.getString(checkIdIndex)){
                null -> 0
                else -> habitCursor.getString(checkIdIndex).toInt()
            }
            val emoticonId = when(habitCursor.getString(emoticonIdIndex)){
                null -> 0
                else -> habitCursor.getString(emoticonIdIndex).toInt()
            }
            //TODO 以下を習慣名、チェックボックス、顔文字を表示する処理に書き換える
            Log.d("TAG",name + "/" + checkId + "/" +emoticonId)
            val dailyHabitItems = DailyHabitItems(name,checkId,emoticonId)
            habitList.add(dailyHabitItems)
        }

        val adapter = DailyHabitListAdapter(this,habitList)
        val lvDailyHabitList = findViewById<ListView>(R.id.lvDailyHabitList)
        lvDailyHabitList.adapter = adapter

        val buttonListener = ButtonListener()
        val btBack = findViewById<Button>(R.id.btBack)
        btBack.setOnClickListener(buttonListener)
    }

    private inner class ButtonListener: View.OnClickListener{
        override fun onClick(v: View) {
            val intent = Intent(applicationContext,CalendarActivity::class.java)
            startActivity(intent)
        }
    }
}
