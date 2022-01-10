package com.example.visualdsa

import android.R.id
import android.content.ClipData
import android.content.ClipDescription
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
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
    var size:Int = 6
    var orderArray = Array(size, { i -> i * 1 })
    var map1= mutableMapOf<Int,Int>()
//    var orderArray = Array(5,{1,2,3,4,5})
    var idarray: MutableList<MutableList<Int>> = ArrayList()
    val themeColor:Int = Color.parseColor("#FFBB86FC")
    val black:Int = Color.parseColor("#000000")
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
            colorButtonScreen(size)
            holdFunctionality()
        }

        val button: Button = findViewById(R.id.randomize)
        button.setOnClickListener {
            // Code here executes on main thread after user presses button
            createShuffledArrays(size)
            colorButtonScreen(size)
        }

//        slider.addOnSliderTouchListener(touchListener)
        makeDropDown(arrayOf("Linear Search","Binary Search"),R.id.spinner)
        makeDropDown(arrayOf("1x","0.25x","0.5x","0.75x","1.25x","1.5x", "1.75x","2x"),R.id.spinner2)
        createButtonScreen(size)
        createShuffledArrays(size)
        colorButtonScreen(size)
        holdFunctionality()
    }
    fun holdFunctionality(){
        for(i in 0..size-1){
            for(j in 0..orderArray[i]){
                val btn=buttons[i][j]
//                btn.setOnTouchListener()

                btn.setOnClickListener {
                    Toast.makeText(this,"length=${btn.id}",Toast.LENGTH_LONG).show()
//                    return@setOnTouchListener false
                }
                btn.setOnLongClickListener {
                    Toast.makeText(this,"length=${orderArray[i]}",Toast.LENGTH_SHORT).show()
                    val clipInt="$i"
                    val item=ClipData.Item(clipInt)
                    val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    val data = ClipData(clipInt,mimeTypes,item)
                    val dragShadowBuilder = View.DragShadowBuilder(it)
                    it.startDragAndDrop(data,dragShadowBuilder,it,0)
                    it.visibility = View.INVISIBLE
                    for(k in 0..orderArray[i]){
                        buttons[i][k].visibility = View.INVISIBLE
                    }

                    return@setOnLongClickListener true
                }
                val dragListener = View.OnDragListener { view, dragEvent ->
                    when(dragEvent.action){
                        DragEvent.ACTION_DRAG_STARTED->{
                            dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                        }
                        DragEvent.ACTION_DRAG_ENTERED->{
//                            view.invalidate()
                            true
                        }
                        DragEvent.ACTION_DRAG_LOCATION -> true
                        DragEvent.ACTION_DRAG_EXITED->{
//                            view.invalidate()
                            true
                        }
                        DragEvent.ACTION_DROP->{
                            val item = dragEvent.clipData.getItemAt(0)
                            val dragData = item.text.toString().toInt()

//                            Toast.makeTex
//                            view.invalidate()
                            println(dragData)
                            val v=dragEvent.localState as View
                            val owner = v.parent as ViewGroup

//                            owner.removeView(v)
                            val destination = view as Button
                            println(map1[destination.id])
                            map1[destination.id]?.let { swap(dragData, it-1) }
                            destroyButtons()
                            createButtonScreen(size)
                            colorButtonScreen(size)
                            holdFunctionality()
//                            println(map1[view.id])
//                            destination.addView(v)
                            v.visibility = View.VISIBLE
                            true
                        }
                        DragEvent.ACTION_DRAG_ENDED->{
//                            view.invalidate()
                            for(k in 0..orderArray[i]){
                                buttons[i][k].visibility = View.VISIBLE
                            }
                            true
                        }
                        else -> false
                    }
                }
                btn.setOnDragListener(dragListener)
            }
        }
    }
    fun swap(i1:Int,i2:Int){
        var temp=orderArray[i1]
        orderArray[i1]=orderArray[i2]
        orderArray[i2]=temp
    }
    fun createShuffledArrays(size: Int){
        orderArray = Array(size, { i -> i * 1 })
        orderArray.shuffle()

    }
    fun colorButtonScreen(size:Int){
        for(i in 0..size-1){
            colorFollowingButtons(i,0,orderArray[i],themeColor)
            colorFollowingButtons(i,orderArray[i]+1,size-1,black)
        }
    }
    fun colorFollowingButtons(row:Int,left:Int, right:Int,color:Int){
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
        println(buttonScreen.id)
        screen.addView(buttonScreen)

        for (i in 1..size) {

            val arr = LinearLayout(this)
            arr.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,1.0f
            )
            arr.orientation = LinearLayout.HORIZONTAL
            val buttonrow: MutableList<Button> = ArrayList()
            var startIndex:Int = 1
            for (j in 1..size) {
                val button = Button(this)
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
                )
//                var buttonid = resources.getIdentifier("b_${i}_${j}", "id", packageName)
//                println(button.id)
                button.id=View.generateViewId()
                map1[button.id] = i
                buttonrow.add(button)
                arr.addView(button)
                println(button.id)
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
        map1.clear()
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


