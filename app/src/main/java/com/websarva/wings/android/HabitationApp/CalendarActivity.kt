package com.websarva.wings.android.HabitationApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class CalendarActivity : AppCompatActivity() {
//ToDo 初期画面はすべての習慣を表示
    private var _habitId = -1
    private var _habitName = ""
    private val _helper = DatabaseHelper(this@CalendarActivity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = _helper.writableDatabase
        //テスト用に習慣リストを作成 START
        val deleteSql = "DELETE FROM habit"
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
        //テスト用に習慣リストを作成 END

        val sql = "SELECT * FROM habit"
        val cursor = db.rawQuery(sql, null)
        var habitList = mutableListOf<String>()
        while(cursor.moveToNext()){
            val indexName = cursor.getColumnIndex("name")
            habitList.add(cursor.getString(indexName))
        }
        val adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        for(name in habitList){
            adapter.add(name)
        }
        val spinner = findViewById<Spinner>(R.id.spHabit)
        spinner.adapter = adapter

        //現在の年月日と今月の末日を取得
        val c = Calendar.getInstance()
        createCalender(c)

        val listener = ButtonListener()
        val btPreviousMonth = findViewById<Button>(R.id.btPreviousMonth)
        btPreviousMonth.setOnClickListener(listener)
        val btNextMonth = findViewById<Button>(R.id.btNextMonth)
        btNextMonth.setOnClickListener(listener)
        val lvCalendar = findViewById<ListView>(R.id.lvCalendar)
        lvCalendar.onItemClickListener = ListItemClickListner()
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    private inner class ButtonListener: View.OnClickListener{
        override fun onClick(v: View) {
            val input = findViewById<TextView>(R.id.tvTitle).text.toString()
            val c = Calendar.getInstance()
            val (year,month) = input.split("/")
            c.set(year.toInt(),month.toInt()-1,1)

            when(v.id){
                R.id.btPreviousMonth -> {
                    c.add(Calendar.MONTH,-1)
                    createCalender(c)
                }
                R.id.btNextMonth ->{
                    c.add(Calendar.MONTH,1)
                    createCalender(c)
                }
            }
        }
    }

    private inner class ListItemClickListner: AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            val tvTitle = findViewById<TextView>(R.id.tvTitle)
            val year = tvTitle.text.toString().split("/")[0]
            val month = tvTitle.text.toString().split("/")[1]

            val calendarItem = parent.getItemAtPosition(position) as CalendarItems
            val date =calendarItem.date
            val dayOfWeek = calendarItem.dayOfWeek
            val checkId = calendarItem.checkId
            val emoticonId = calendarItem.emoticonId

            val habitId = findViewById<Spinner>(R.id.spHabit).selectedItemId

            val intent = Intent(applicationContext,DailyCheckActivity::class.java)
            intent.putExtra("year",year)
            intent.putExtra("month",month)
            intent.putExtra("date",date)
            intent.putExtra("dayOfWeek",dayOfWeek)
            intent.putExtra("checkId",checkId)
            intent.putExtra("emoticonId",emoticonId)
            startActivity(intent)
        }
    }

    private fun createCalender(c:Calendar){
        val year = c.get(Calendar.YEAR).toString()
        val month = (c.get(Calendar.MONTH) + 1).toString()
        val minimumDay = c.getActualMinimum(Calendar.DAY_OF_MONTH)
        val maximumDay = c.getActualMaximum(Calendar.DAY_OF_MONTH)

        //タイトルのテキストを設定
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = getString(R.string.selectedMonth,year,month)

        //カレンダー部の作成処理
        val calendarList = mutableListOf<CalendarItems>()

        var date = ""
        var dayOfWeek = ""
        var checkId = 0
        var emoticonId = 0

        c.set(Calendar.DATE,minimumDay)
        for(i in 0..maximumDay-1){
            date  = c.get(Calendar.DATE).toString()
            dayOfWeek = when(c.get(Calendar.DAY_OF_WEEK)) {
                1 -> "日"
                2 -> "月"
                3 -> "火"
                4 -> "水"
                5 -> "木"
                6 -> "金"
                7 -> "土"
                else -> ""
            }

            /*TODO 既に登録されているデータを参照して表示する
            checkId = 1
            emoticonId = 1
             */
            val calendarItems = CalendarItems(date,dayOfWeek,checkId,emoticonId)
            calendarList.add(calendarItems)
            c.add(Calendar.DATE,1)
        }

        val adapter = CalendarItemListAdapter(this,calendarList)
        /*
        TODO         土日祝日の色を変える,グリッドビューにする
        */
        val lvCalendar = findViewById<ListView>(R.id.lvCalendar)
        lvCalendar.adapter = adapter
    }
}
