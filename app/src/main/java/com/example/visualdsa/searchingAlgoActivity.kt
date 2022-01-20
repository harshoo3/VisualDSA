package com.example.visualdsa

import android.R.id
import android.content.ClipData
import android.content.ClipDescription
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.appcompat.app.ActionBar.LayoutParams
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import java.util.Timer import kotlin.concurrent.schedule
import androidx.core.content.ContentProviderCompat.requireContext
import android.widget.TableLayout

import android.widget.TextView
import kotlinx.coroutines.*
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
    val red:Int = Color.parseColor("#FF0000")
    val blue:Int = Color.parseColor("#0000FF")
    val yellow:Int = Color.parseColor("#FCF4A3")
    val brown:Int = Color.parseColor("#8A3324")
    var colorArray= Array(size) { themeColor }
    var speedArr = arrayOf("1x","0.25x","0.5x","0.75x","1.25x","1.5x", "1.75x","2x","4x")
    var searchAlgoArr = arrayOf("Linear Search","Binary Search")
    var algoInUse:Int = 0
    var algoRunning: Boolean= false
    var algoFinished:Boolean = false
    var algoPaused: Boolean = false
    var speedInUSe: Double = 1.0
    var speed:Long = 1000

//    val slider:Slider = findViewById(R.id.slider)
//    val button: Button = findViewById(R.id.randomize)
//    val button2: Button = findViewById(R.id.start)

    private val buttons: MutableList<MutableList<Button>> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
//        speedMap["1x"]= 1
        for(i in 0..size-1){
            println(colorArray[i])
        }
        println(themeColor)
        println("hi")
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
        val button: Button = findViewById(R.id.randomize)
        val button2: Button = findViewById(R.id.start)
        var spinner:Spinner = findViewById(R.id.spinner)
        var spinner2:Spinner = findViewById(R.id.spinner2)
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

        button.setOnClickListener {
            if(algoRunning){
                algoFinished = true
            }
            else if(algoFinished) Toast.makeText(this,"Please Reset.",Toast.LENGTH_LONG).show()
            // Code here executes on main thread after user presses button
            else if(algoInUse==1) Toast.makeText(this,"Binary Search requires the elements to be sorted.",Toast.LENGTH_LONG).show()
            else standardSetup()
//            standardSetup()
        }

        button2.setOnClickListener {
            // Code here executes on main thread after user presses button
            if(algoPaused){
                algoPaused = false
                button2.text="Pause"
            }else if(algoRunning){
                //pause functionality
                algoPaused = true
                button2.text = "Resume"
            }else if(algoFinished){
                setUpWithoutShuffling()
            }
            else if(check){
                button2.text = "Pause"
                button.text = "Stop"
                slider.isEnabled = false
                spinner.isEnabled = false
                if(algoInUse==1) binarySearch()
                else linearSearch()
            }else{
                Toast.makeText(this,"Select an element in the array first.",Toast.LENGTH_LONG).show()
            }
        }

        createButtonScreen(size)
        makeDropDown(searchAlgoArr,spinner.id)
        makeDropDown(speedArr,spinner2.id)
        standardSetup()
    }


    private fun holdFunctionality(){
        for(i in 0..size-1){
            for(j in 0..orderArray[i]){
                val btn=buttons[i][j]
                btn.setOnClickListener {
                    Toast.makeText(this,"length=${orderArray[i]+1}",Toast.LENGTH_SHORT).show()
                    if(!algoRunning){
                        if(check){
                            colorFollowingButtons(selected,0,orderArray[selected],themeColor)
                        }
                        selected = i
                        check = true
                        colorSelectedRow()

                    }
                }
                btn.setOnLongClickListener {
                    if(algoRunning){
                        Toast.makeText(this,"Drag and Drop not allowed while Algorithm is running",Toast.LENGTH_SHORT).show()
                        return@setOnLongClickListener true
                    }
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
//                            try{
                                buttonIdMap[destination.id]?.let { swap(dragData, it) }
//                                swap(buttonIdMap[destination.id]!!,dragData);
                                if(check){
                                    setUpWithoutShuffling(true)
                                    if(selected==buttonIdMap[destination.id] || selected==dragData){

                                        if(selected == buttonIdMap[destination.id]!!){
                                            selected = dragData
                                            println(orderArray[selected])
                                            colorSelectedRow()
                                        }else{
                                            selected = buttonIdMap[destination.id]!!
                                            colorSelectedRow()
                                        }
                                    }else{
                                        colorSelectedRow()
                                    }
                                }else setUpWithoutShuffling()
//                            }catch(e:SomeException){
//                                setUpWithoutShuffling()
//                                colorSelectedRow()
//                            }


                            true
                        }
                        DragEvent.ACTION_DRAG_ENDED->{
                            if(!dragEvent.result) {
                                setUpWithoutShuffling(true)
                                colorSelectedRow()
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
    private fun pauseFunctionality(){
        GlobalScope.launch {
            while(algoPaused){
                if(algoFinished){
                    break
                }
                delay(100)
            }
        }
    }
    private fun linearSearch(){
        GlobalScope.launch {
            algoRunning = true
            for(i in 0..size-1){
                while(algoPaused){
                    if(algoFinished){
                        break
                    }
                    delay(100)
                }
                if(algoFinished) {
                    algoFinishedFunctionality()
                    break
                }
                colorFollowingButtons(i,0,orderArray[i],red)
                delay(speed)
                if(orderArray[selected]==orderArray[i]) {
                    colorFollowingButtons(i, 0, orderArray[i], green)
                    algoFinishedFunctionality()
                    break
                }
            }
        }

    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
//        // If you don't have res/menu, just create a directory named "menu" inside res
//        menuInflater.inflate(R.menu.search_algo_help, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    // handle button activities
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.mybutton) {
//            // do something here
//        }
//        return super.onOptionsItemSelected(item)
//    }
    private fun binarySearch(){
        GlobalScope.launch {
            var left:Int = 0
            var right:Int = size-1
            algoRunning=true
            while(left<=right){
                while(algoPaused){
                    if(algoFinished){
                        break
                    }
                    delay(100)
                }
                if(algoFinished) {
                    algoFinishedFunctionality()
                    break
                }
                var mid:Int = left+(right-left)/2
                var leftColor = colorArray[left]
                var rightColor = colorArray[right]
                colorFollowingButtons(left,0,orderArray[left],yellow)
                colorFollowingButtons(right,0,orderArray[right],brown)
                delay(speed)
                colorFollowingButtons(left,0,orderArray[left],leftColor)
                colorFollowingButtons(right,0,orderArray[right],rightColor)
                colorFollowingButtons(mid,0,orderArray[mid],red)
                delay(speed)
                if(orderArray[mid]==orderArray[selected]){
                    colorFollowingButtons(mid,0,orderArray[mid],green)
                    algoFinishedFunctionality()
                    break
                    //found
                }else if(orderArray[mid]>orderArray[selected]){
                    right = mid-1
                }else{
                    left = mid+1
                }
            }
        }
    }
    private fun algoFinishedFunctionality(){
        val button: Button = findViewById(R.id.randomize)
        val button2: Button = findViewById(R.id.start)
        algoRunning = false
        algoPaused = false
        algoFinished = true
        button.text = "Randomise"
        button2.text = "Reset"
    }
    private fun resetFunctionality(keep: Boolean = false){
        val slider:Slider = findViewById(R.id.slider)
        val button: Button = findViewById(R.id.randomize)
        val button2: Button = findViewById(R.id.start)
        var spinner:Spinner = findViewById(R.id.spinner)
        button.text = "Randomise"
        algoRunning = false
        algoPaused = false
        slider.isEnabled = true
        algoFinished = false
        spinner.isEnabled = true
        button2.text = "Start"
        if(keep) return
        selected = -1
        check = false
    }
    private fun colorSelectedRow(){
        if(check) {
            colorFollowingButtons(selected, 0, orderArray[selected], blue)
        }
    }
    private fun standardSetup(keep:Boolean= false){
        resetFunctionality(keep)
        destroyButtons(keep)
        createButtonScreen(size)
        createShuffledArrays(size)
        colorButtonScreen(size)
        holdFunctionality()
    }
    private fun setUpWithoutShuffling(keep: Boolean=false){
        resetFunctionality(keep)
        destroyButtons(keep)
        createButtonScreen(size)
        colorButtonScreen(size)
        holdFunctionality()
    }
    private fun sortedSetup(keep:Boolean = false){
        Toast.makeText(this,"Binary search requires the elements to be sorted.",Toast.LENGTH_LONG).show()
        orderArray = Array(size, { i -> i * 1 })
        colorArray = Array(size){themeColor}
        setUpWithoutShuffling(keep)
    }
    private fun swap(i1:Int,i2:Int){
        var temp=orderArray[i1]
        orderArray[i1]=orderArray[i2]
        orderArray[i2]=temp
    }
    private fun createShuffledArrays(size: Int){
        orderArray = Array(size, { i -> i * 1 })
        colorArray = Array(size){themeColor}
        orderArray.shuffle()
    }
    private fun colorButtonScreen(size:Int){
        for(i in 0..size-1){
            colorFollowingButtons(i,0,orderArray[i],themeColor)
            colorFollowingButtons(i,orderArray[i]+1,size-1,black)
        }
    }
    private fun colorFollowingButtons(row:Int,left:Int, right:Int,color:Int){
        for(j in left..right){
            buttons[row][j].setBackgroundColor(color)
        }
        if(color!=black) colorArray[row]=color
    }
    private fun createButtonScreen(size: Int) {

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
    private fun destroyButtons(keep: Boolean=false){
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
    private fun makeDropDown(arr:Array<*>,id: Int){
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
                    speed= (1000*speedInUSe).toLong()
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


