package com.example.visualdsa

import android.R.id
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.appcompat.app.ActionBar.LayoutParams
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import android.widget.TableLayout

import android.widget.TextView

import android.widget.LinearLayout
import java.lang.Exception


class searchingAlgoActivity : AppCompatActivity() {
//    lateinit var option: Spinner
//    lateinit var result: TextView
//    val searchAlgorithmList = resources.getStringArray(R.array.searchAlgorithmList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching_algo)

        supportActionBar?.apply {
            // show custom title in action bar
            customView = actionBarCustomTitle()
            displayOptions = DISPLAY_SHOW_CUSTOM

            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)

        }
        val buttonsNumber = 5 // Put here your number of buttons

        val col0 = findViewById<View>(R.id.col0) as LinearLayout

        for (i in 0 until buttonsNumber) {
            try {
                val newButton = Button(this)
                newButton.id = id::class.java.getField("b$i").getInt(null)
                // Since API Level 17, you can also use View.generateViewId()
                newButton.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 20, 1F)
                newButton.text = "hello"
                newButton.setBackgroundColor(4)
                col0.addView(newButton)
            } catch (e: Exception) {
                // Unknown button id !
                // We skip it
            }
        }

//        val v0 = findViewById<View>(R.id.v0) as TextView
//        v0.layoutParams =
//            TableLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, 100,
//                (buttonsNumber - 1).toFloat()
//            )
        var options = arrayOf("Linear Search","Binary Search")
        val spinner = findViewById<Spinner>(R.id.spinner)
    val adapter = ArrayAdapter(
        this,
        android.R.layout.simple_list_item_1,
        options
    )
        spinner.adapter=adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                arg2: Int,
                arg3: Long
            ) {
                // Do what you want
                val items = spinner.selectedItem.toString()

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun actionBarCustomTitle():TextView{
        return TextView(this).apply {
            text = "Search Algorithms"
//            setBackgroundColor(5)
//            titleColor =
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            // center align the text view/ action bar title
            params.gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = params
            setTextSize(25F)
        }
    }
}


