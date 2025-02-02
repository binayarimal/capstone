package com.example.mobileapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class events : AppCompatActivity() {
    var addEventButton: Button? = null
    var db: databaseHelper? = null
    var dbDel: databaseHelper? = null
    fun deleteEvent(eventId: Int) {
        dbDel = databaseHelper(this)
        val delVal = dbDel!!.deleteEvent(eventId)
        if (delVal) {
            Toast.makeText(this@events, "Event Deleted", Toast.LENGTH_LONG).show()
            startActivity(intent)
        } else {
            Toast.makeText(this@events, "Event Deletion Unsuccessful", Toast.LENGTH_LONG).show()
        }
    }

    fun setDateAttr(dateView: TextView, eventDate: String?) {
        val dateParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //dynamically styling date column
        dateParams.gravity = Gravity.START
        dateParams.height = 120
        dateParams.width = 200
        dateParams.setMargins(5, 5, 10, 5)
        dateView.layoutParams = dateParams
        dateView.textSize = 12f
        dateView.text = eventDate
        dateView.id = View.generateViewId()
    }

    fun setEventAttr(eventView: TextView, eventData: String?) {
        val eventParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        //Dynamically Styling event column
        eventParams.gravity = Gravity.START
        eventParams.height = 120
        eventParams.width = 500
        eventParams.setMargins(5, 5, 5, 5)
        eventView.layoutParams = eventParams
        eventView.textSize = 15f
        eventView.text = eventData
        eventView.id = View.generateViewId()
    }

    fun setDelBtnAttr(del: Button) {
        val delParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //Dynamically Styling delete button
        delParams.gravity = Gravity.START
        delParams.height = 120
        delParams.width = 150
        delParams.setMargins(5, 5, 5, 5)
        del.layoutParams = delParams
        del.textSize = 10f
        del.text = "X"
        del.id = View.generateViewId()
        del.setBackgroundColor(Color.rgb(255, 69, 0))
        del.setTextColor(Color.WHITE)
    }

    fun setEditBtnAttr(edit: Button) {
        val editParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //Dynamically Styling edit button
        editParams.gravity = Gravity.START
        editParams.height = 120
        editParams.width = 150
        editParams.setMargins(5, 5, 5, 5)
        edit.layoutParams = editParams
        edit.textSize = 10f
        edit.text = "EDIT"
        edit.id = View.generateViewId()
        edit.setBackgroundColor(Color.rgb(70, 130, 180))
        edit.setTextColor(Color.WHITE)
    }
    private fun sortEventsByDate(events: MutableList<Triple<Int, String, String>>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (i in 0 until events.size - 1) {
            for (j in 0 until events.size - i - 1) {
                val date1 = try { dateFormat.parse(events[j].third) } catch (e: Exception) { null }
                val date2 = try { dateFormat.parse(events[j + 1].third) } catch (e: Exception) { null }

                if (date1 != null && date2 != null && date1.after(date2)) {
                    events[j] = events[j + 1].also { events[j + 1] = events[j] } // Swap elements
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        db = databaseHelper(this)
        val res = db!!.getEvents()
        val myRoot = findViewById<View>(R.id.linear_parent) as LinearLayout
        // iterating events in database
        val eventList = mutableListOf<Triple<Int, String, String>>() // (ID, Event, Date)
        while (res.moveToNext()) {
            @SuppressLint("Range") val eventData = res.getString(res.getColumnIndex("event"))
            @SuppressLint("Range") val eventId = res.getInt(1)
            @SuppressLint("Range") val eventDate = res.getString(res.getColumnIndex("date"))

            eventList.add(Triple(eventId, eventData, eventDate))

        }

        sortEventsByDate(eventList)

        for ((eventId, eventData, eventDate) in eventList) {

            val dateView = TextView(this)
            setDateAttr(dateView, eventDate)
            val eventView = TextView(this)
            setEventAttr(eventView, eventData)
            val del = Button(this)
            setDelBtnAttr(del)
            del.setOnClickListener { deleteEvent(eventId) }
            val edit = Button(this)
            setEditBtnAttr(edit)
            edit.setOnClickListener {
                val editScreen = Intent(applicationContext, edit_event::class.java)
                editScreen.putExtra("key", eventId)
                startActivity(editScreen)
            }

            //Assigning views to layout
            val layoutChild = LinearLayout(this)
            layoutChild.orientation = LinearLayout.HORIZONTAL
            layoutChild.addView(dateView)
            layoutChild.addView(eventView)
            layoutChild.addView(edit)
            layoutChild.addView(del)
            myRoot.addView(layoutChild)

            addEventButton = findViewById(R.id.add_event_view)
            addEventButton?.setOnClickListener(
                View.OnClickListener
                //navigate to add event page
                {
                    val intent = Intent(applicationContext, add_event::class.java)
                    startActivity(intent)
                })
    }   }
}