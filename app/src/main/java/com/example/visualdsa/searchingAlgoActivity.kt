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
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import java.lang.Exception
import com.google.android.material.slider.Slider





class searchingAlgoActivity : AppCompatActivity() {

    var size:Int = 6
    var orderArray = Array(size, { i -> i * 1 })
    var check:Boolean = false
    var selected:Int = -1
    var buttonIdMap= mutableMapOf<Int,Int>()
    var speedMap = mutableMapOf<String,Double>()
    val themeColor:Int = Color.parseColor("#FFBB86FC")
    val black:Int = Color.parseColor("#000000")
    val green:Int = Color.parseColor("#00FF00")
    var speedArr = arrayOf("1x","0.25x","0.5x","0.75x","1.25x","1.5x", "1.75x","2x","4x")
    var searchAlgoArr = arrayOf("Linear Search","Binary Search")
    var algoInUse:Int = 0
    var speedInUSe: Double = 1.0
    private val buttons: MutableList<MutableList<Button>> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
//        speedMap["1x"]= 1
        for(i in 0..speedArr.size-1){
            speedMap[speedArr[i]]=1/(speedArr[i].subSequence(0,speedArr[i].length-1).toString().toDouble())
            println(speedMap[speedArr[i]])
        }
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
            size = value.toInt()
            if(algoInUse==1) sortedSetup()
            else standardSetup()
//            standardSetup()
        }

        val button: Button = findViewById(R.id.randomize)
        button.setOnClickListener {
            // Code here executes on main thread after user presses button
            if(algoInUse==1) Toast.makeText(this,"Binary Search requires the elements to be sorted.",Toast.LENGTH_LONG).show()
            else standardSetup()
//            standardSetup()
        }
        val button2: Button = findViewById(R.id.start)
        button2.setOnClickListener {
            // Code here executes on main thread after user presses button
            if(check){

            }else{
                Toast.makeText(this,"Select an element in the array first.",Toast.LENGTH_LONG).show()
            }
        }
        createButtonScreen(size)
        makeDropDown(searchAlgoArr,R.id.spinner)
        makeDropDown(speedArr,R.id.spinner2)
        standardSetup()
    }

    fun holdFunctionality(){
        for(i in 0..size-1){
            for(j in 0..orderArray[i]){
                val btn=buttons[i][j]
                btn.setOnClickListener {
                    Toast.makeText(this,"length=${orderArray[i]+1}",Toast.LENGTH_SHORT).show()
                    if(check){
                        colorFollowingButtons(selected,0,orderArray[selected],themeColor)
                    }
                    selected = i
                    check = true
                    colorFollowingButtons(i,0,orderArray[i],green)
                }

                btn.setOnLongClickListener {
                    if(algoInUse==1){
                        Toast.makeText(this,"Drag and Drop not allowed in Binary Search mode",Toast.LENGTH_SHORT).show()
                        return@setOnLongClickListener true
                    }
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
                            view.invalidate()
                            true
                        }
                        DragEvent.ACTION_DRAG_LOCATION -> true
                        DragEvent.ACTION_DRAG_EXITED->{
                            view.invalidate()
                            true
                        }
                        DragEvent.ACTION_DROP->{
                            val item = dragEvent.clipData.getItemAt(0)
                            val dragData = item.text.toString().toInt()
                            val destination = view as Button
                            buttonIdMap[destination.id]?.let { swap(dragData, it) }
                            if(check){
                                setUpWithoutShuffling(true)
                                if(selected==buttonIdMap[destination.id] || selected==dragData){

                                    if(selected == buttonIdMap[destination.id]!!){
                                        selected = dragData
                                        println(orderArray[selected])
                                        colorFollowingButtons(selected,0,orderArray[selected],green)
                                    }else{
                                        selected = buttonIdMap[destination.id]!!
                                        colorFollowingButtons(selected,0,orderArray[selected],green)
                                    }
                                }else{
                                    colorFollowingButtons(selected,0,orderArray[selected],green)
                                }
                            }else setUpWithoutShuffling()
                            true
                        }
                        DragEvent.ACTION_DRAG_ENDED->{
                            if(!dragEvent.result) {
                                setUpWithoutShuffling(true)
                                colorFollowingButtons(selected,0,orderArray[selected],green)
                            }
                            true
                        }
                        else->{
                            setUpWithoutShuffling(true)
                            false
                        }
                    }
                }
                btn.setOnDragListener(dragListener)
            }
        }
    }
    fun linearSearch(){
//        for()
    }
    fun binarySearch(){

    }
    fun standardSetup(){
        destroyButtons()
        createButtonScreen(size)
        createShuffledArrays(size)
        colorButtonScreen(size)
        holdFunctionality()
    }
    fun setUpWithoutShuffling(keep: Boolean=false){
        destroyButtons(keep)
        createButtonScreen(size)
        colorButtonScreen(size)
        holdFunctionality()
    }
    fun sortedSetup(keep:Boolean = false){
        orderArray = Array(size, { i -> i * 1 })
        setUpWithoutShuffling(keep)
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

        var layoutid = resources.getIdentifier("col0", "id", packageName)
        val layout=findViewById<LinearLayout>(layoutid)

        val buttonScreen = LinearLayout(this)
        buttonScreen.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        buttonScreen.orientation = LinearLayout.VERTICAL

        var buttonScreenid = resources.getIdentifier("buttonScreen", "id", packageName)
        buttonScreen.id=buttonScreenid
        layout.addView(buttonScreen)
        for (i in 1..size) {
            val arr = LinearLayout(this)
            arr.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,1.0f
            )
            arr.orientation = LinearLayout.HORIZONTAL
            arr.setPadding(0,0,0,2)
            val buttonrow: MutableList<Button> = ArrayList()
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
                buttonIdMap[button.id] = i-1
                buttonrow.add(button)
                arr.addView(button)
//                println(button.id)
            }
            buttons.add(buttonrow)
            buttonScreen.addView(arr)
        }
    }
    fun destroyButtons(keep: Boolean=false){
        var id = resources.getIdentifier("buttonScreen", "id", packageName)
        val buttonScreen =findViewById<LinearLayout>(id)
        (buttonScreen.getParent() as ViewGroup).removeView(buttonScreen)
        buttons.removeAll(buttons)
        if(keep){
            return
        }
        check = false
        selected=-1
        buttonIdMap.clear()
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
                val item = spinner.selectedItem.toString()
                if(id == R.id.spinner){
                    if(item == "Binary Search"){
                        algoInUse = 1
                        sortedSetup()
                    }else{
                        algoInUse=0
                        standardSetup()
                    }
                }else{
                    speedInUSe = speedMap[item]!!
                }
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


