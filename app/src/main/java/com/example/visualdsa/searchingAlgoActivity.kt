package com.example.visualdsa

import android.R.id
import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.core.view.marginLeft
import java.lang.Exception
import com.google.android.material.slider.Slider





class searchingAlgoActivity : AppCompatActivity() {
//    lateinit var option: Spinner
//    lateinit var result: TextView
//    val searchAlgorithmList = resources.getStringArray(R.array.searchAlgorithmList)
    var size:Int = 5
//    var orderArray = Array(5,{1,2,3,4,5})
    var themeColor:Int = Color.parseColor("#FFBB86FC")
    var black:Int = Color.parseColor("#000000")
    private val buttons: MutableList<MutableList<Button>> = ArrayList()
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


        val slider = findViewById<Slider>(R.id.slider)
        slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })

        slider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            destroyButtons()
            size = value.toInt()
            createButtonScreen(size)
            createShuffledArrays(size)
        }

        val button: Button = findViewById(R.id.randomize)
        button.setOnClickListener {
            // Code here executes on main thread after user presses button
            createShuffledArrays(size)
        }

//        slider.addOnSliderTouchListener(touchListener)
        makeDropDown(arrayOf("Linear Search","Binary Search"),R.id.spinner)
        makeDropDown(arrayOf("1x","0.25x","0.5x","0.75x","1.25x","1.5x", "1.75x","2x"),R.id.spinner2)
        createButtonScreen(size)
        createShuffledArrays(size)

    }
    fun createShuffledArrays(size: Int){
        val orderArray = Array(size, { i -> i * 1 })
        orderArray.shuffle()
        colorButtonScreen(orderArray,size)
    }
    fun colorButtonScreen(orderArray:Array<Int>,size:Int){
        for(i in 0..size-1){
            colorFollowingButtons(orderArray,i,0,orderArray[i],themeColor)
            colorFollowingButtons(orderArray,i,orderArray[i]+1,size-1,black)
        }
    }
    fun colorFollowingButtons(orderArray:Array<Int>,row:Int,left:Int, right:Int,color:Int){
        for(j in left..right){
            buttons[row][j].setBackgroundColor(color)
        }
    }
    fun createButtonScreen(size: Int) {

        var screenid = resources.getIdentifier("col0", "id", packageName)
        val screen=findViewById<LinearLayout>(screenid)


        val buttonScreen = LinearLayout(this)
        buttonScreen.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        buttonScreen.orientation = LinearLayout.VERTICAL
        var buttonScreenid = resources.getIdentifier("buttonScreen", "id", packageName)
        buttonScreen.id=buttonScreenid
        screen.addView(buttonScreen)

        for (i in 1..size) {

            val arr = LinearLayout(this)
            arr.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,1.0f
            )
            arr.orientation = LinearLayout.HORIZONTAL
            val buttonrow: MutableList<Button> = ArrayList()
            for (j in 1..size) {
                val button = Button(this)
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
                )
                buttonrow.add(button)
                arr.addView(button)
            }
            buttons.add(buttonrow)
            buttonScreen.addView(arr)
        }
    }
    fun destroyButtons(){
        var id = resources.getIdentifier("buttonScreen", "id", packageName)
        val buttonScreen =findViewById<LinearLayout>(id)
        (buttonScreen.getParent() as ViewGroup).removeView(buttonScreen)
        buttons.removeAll(buttons)
    }
    fun makeDropDown(arr:Array<*>,id: Int){

        val spinner = findViewById<Spinner>(id)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arr
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


