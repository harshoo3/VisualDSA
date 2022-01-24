package com.example.visualdsa

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.*
import android.widget.*
import androidx.core.view.size
import com.google.android.material.slider.Slider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class sortingAlgoActivity : AppCompatActivity() {
    val themeColor:Int = Color.parseColor("#FFBB86FC")
    val black:Int = Color.parseColor("#000000")
    val green:Int = Color.parseColor("#00FF00")
    val red:Int = Color.parseColor("#FF0000")
    val blue:Int = Color.parseColor("#0000FF")
    val yellow:Int = Color.parseColor("#FCF4A3")
    val brown:Int = Color.parseColor("#8A3324")
    var speedMap = mutableMapOf<String,Double>()
    var speedArr = arrayOf("1x","0.25x","0.5x","0.75x","1.25x","1.5x","2x","4x","8x")
    var sortAlgoArr = arrayOf("Bubble Sort","Selection Sort","Insertion Sort","Merge Sort","Quick Sort","Heap Sort")
    var algoMap= mutableMapOf<String,Int>("Bubble Sort" to 0,"Selection Sort" to 1,"Insertion Sort" to 2,"Merge Sort" to 3,"Quick Sort" to 4,"Heap Sort" to 5)
    var buttonIdMap= mutableMapOf<Int,Int>()
    var algoInUse:Int = 0
    var algoRunning: Boolean= false
    var algoFinished:Boolean = false
    var algoPaused: Boolean = false
    var speedInUSe: Double = 0.5
    var speed:Long = 1000
    var size:Int = 6
    var colorArray= Array(size) { themeColor }
    var orderArray = Array(size, { i -> i * 1 })
    var notesArr = arrayOf("Note: You can customize the order of elements by dragging one element to another.", "Note: You can increase or decrease the execution speed using the above dropdown.","Note: You can press the ? icon above to know which color denotes what.")
    private val buttons: MutableList<MutableList<Button>> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorting_algo)
        for(i in 0..speedArr.size-1){
            speedMap[speedArr[i]]=1/((speedArr[i].subSequence(0,speedArr[i].length-1).toString().toDouble())*3)
            println(speedMap[speedArr[i]])
        }
        speedInUSe = speedMap["1x"]!!
        val slider = findViewById<Slider>(R.id.slider_sort)
        val button: Button = findViewById(R.id.randomize_sort)
        val button2: Button = findViewById(R.id.start_sort)
        var spinner: Spinner = findViewById(R.id.spinner_sort1)
        var spinner2: Spinner = findViewById(R.id.spinner_sort2)
        val notes:TextView = findViewById(R.id.notes_sort)

        notes.setText(notesArr[(0..notesArr.size-1).random()])
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
            standardSetup()
//            if(algoInUse==1) sortedSetup()
//            else
        }
        button.textSize = 16F
        button.setOnClickListener {
            if(algoRunning){
                algoFinished = true
            }
            else if(algoFinished) Toast.makeText(this,"Please Reset.", Toast.LENGTH_LONG).show()
            // Code here executes on main thread after user presses button
//            else if(algoInUse==1) Toast.makeText(this,"Binary Search requires the elements to be sorted.",
//                Toast.LENGTH_LONG).show()
            else standardSetup()
        }
        button2.textSize = 16F
        button2.setBackgroundColor(green)
        button2.setOnClickListener {
            // Code here executes on main thread after user presses button
            if(algoPaused){
                algoPaused = false
                button2.setBackgroundColor(themeColor)
                button2.text="Pause"
            }else if(algoRunning){
                //pause functionality
                algoPaused = true
                button2.setBackgroundColor(green)
                button2.text = "Resume"
            }else if(algoFinished){
                resetFunctionality()
                setUpWithoutShuffling()
            }
            else{
                button2.text = "Pause"
                button.text = "Stop"
                slider.isEnabled = false
                spinner.isEnabled = false
                button2.setBackgroundColor(themeColor)
                button.setBackgroundColor(red)
                when(algoInUse){
                    0->bubbleSort()
                    1->selectionSort()
                    2->insertionSort()
                    3->mergeSort()
                    4->quickSort()
                    else-> heapSort()
                }
            }
        }
        createButtonScreen(size)
        makeDropDown(sortAlgoArr,spinner.id)
        makeDropDown(speedArr,spinner2.id)
        standardSetup()
    }
    private fun holdFunctionality(){
        for(i in 0..size-1){
            for(j in 0..orderArray[i]){
                val btn=buttons[i][j]
                btn.setOnClickListener {
                    Toast.makeText(this,"length=${orderArray[i]+1}",Toast.LENGTH_SHORT).show()
                }
                btn.setOnLongClickListener {
                    if(algoRunning){
                        Toast.makeText(this,"Drag and Drop not allowed while Algorithm is running",Toast.LENGTH_SHORT).show()
                        return@setOnLongClickListener true
                    }
                    val clipInt="$i"
                    val item= ClipData.Item(clipInt)
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
                            buttonIdMap[destination.id]?.let { swapDrop(dragData, it);swapColor(dragData,it) }
//                                swap(buttonIdMap[destination.id]!!,dragData);
                            setUpWithoutShuffling(true)
//                            }catch(e:SomeException){
//                                setUpWithoutShuffling()
//                                colorSelectedRow()
//                            }


                            true
                        }
                        DragEvent.ACTION_DRAG_ENDED->{
                            if(!dragEvent.result) {
                                setUpWithoutShuffling(true)
//                                colorSelectedRow()
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
    private fun bubbleSort(){
        GlobalScope.launch(Dispatchers.Main) {
            algoRunning=true
            var check:Boolean=true
            for(i in 0..size-1){
                for(j in 0..size-i-2){
                    while(algoPaused){
                        if(algoFinished){
                            check=false
                            break
                        }
                        delay(100)
                    }
                    if(algoFinished){
                        check=false
                        break
                    }
                    colorFollowingButtons(j,0,orderArray[j],blue)
                    delay(speed)
                    colorFollowingButtons(j+1,0,orderArray[j+1],red)
                    delay(speed)
                    if(orderArray[j]>orderArray[j+1]){
                        swapDrop(j,j+1)
                        swapColor(j,j+1)
                        setUpCurrentScreen(true)
                    }
                    colorFollowingButtons(j+1,0,orderArray[j+1],blue)
                    colorFollowingButtons(j,0,orderArray[j],red)
//                    delay(speed)
                }
                if(!check) break
                println("yolo")
                colorFollowingButtons(size-i-1,0,orderArray[size-1-i],green)
                for(j in 0..size-i-2){
                    colorFollowingButtons(j,0,orderArray[j],themeColor)
                }
                delay(speed)
            }
            algoFinishedFunctionality()
        }

    }
    private fun selectionSort(){

        GlobalScope.launch(Dispatchers.Main) {
            algoRunning=true
            var check:Boolean=true
            for (i in 0..size-1)
            {
                // Find the minimum element in unsorted array
                var minIndex:Int = i
                colorFollowingButtons(minIndex,0,orderArray[minIndex],yellow)
                delay(speed)
                for (j in i+1..size-1){
                    while(algoPaused){
                        if(algoFinished){
                            check=false
                            break
                        }
                        delay(100)
                    }
                    if(algoFinished){
                        check=false
                        break
                    }
                    colorFollowingButtons(j,0,orderArray[j],blue)
                    delay(speed)
                    if (orderArray[j] < orderArray[minIndex]){
                        colorFollowingButtons(minIndex,0,orderArray[minIndex],red)
                        minIndex = j
                        colorFollowingButtons(minIndex,0,orderArray[minIndex],yellow)
                        delay(speed)
                    }else{
                        colorFollowingButtons(j,0,orderArray[j],red)
                        delay(speed)
                    }
                }
                if(!check) break
                // Swap the found minimum element with the first element
                swapDrop(minIndex,i)
                swapColor(minIndex,i)
                setUpCurrentScreen(true)
                delay(speed)
                for(j in i+1..size-1){
                    colorFollowingButtons(j,0,orderArray[j],themeColor)
                }
                colorFollowingButtons(i,0,orderArray[i],green)
                delay(speed)
            }
            algoFinishedFunctionality()
        }

    }
    private fun insertionSort(){
        GlobalScope.launch(Dispatchers.Main) {
            algoRunning=true
            var check:Boolean=true
            for (i in 1..size-1) {
                while(algoPaused){
                    if(algoFinished){
                        check=false
                        break
                    }
                    delay(100)
                }
                if(algoFinished){
//                    check=false
//                    createButtonScreen(size)
                    break
                }
                var key: Int = orderArray[i]
                Toast.makeText(this@sortingAlgoActivity,"Key = ${key+1}",Toast.LENGTH_SHORT).show()
                var j: Int = i - 1
                colorFollowingButtons(i,0,orderArray[i],yellow)
                delay(speed)
                while (j >= 0 && orderArray[j] > key) {
                    while(algoPaused){
                        if(algoFinished){
                            check=false
                            break
                        }
                        delay(100)
                    }
                    if(algoFinished){
                        check=false
//                        createButtonScreen(size)
                        break
                    }
                    colorFollowingButtons(j,0,orderArray[j],red)
                    delay(speed)
                    orderArray[j + 1] = orderArray[j]
//                    setUpCurrentScreen(true)
                    colorFollowingButtons(j+1,0,orderArray[j+1],blue)
                    colorFollowingButtons(j+1,orderArray[j+1]+1,size-1,black)
//                    createButtonScreen(size)
                    setUpCurrentScreen(true)
                    delay(speed)
                    j-=1
                }
                orderArray[j + 1] = key
                if(!check) {
                    createShuffledArrays(size)
                    break
                }
                for(k in 0..i){
                    if(orderArray[k]==k || k==j+1){
                        colorFollowingButtons(k,0,orderArray[k],green)
                        colorFollowingButtons(k,orderArray[k]+1,size-1,black)
                        delay(speed)
                    }
                    if(orderArray[k]!=k){
                        colorFollowingButtons(k,0,orderArray[k],themeColor)
                        colorFollowingButtons(k,orderArray[k]+1,size-1,black)
//                        delay(speed)
                    }
                }
//                delay(speed)
            }
            algoFinishedFunctionality()
//            destroyButtons()
//            createButtonScreen(size)
//            createShuffledArrays(size)
//            colorButtonScreen(size)
//            holdFunctionality()
        }
    }
    private fun mergeSort(){

    }
    private fun quickSort(){

    }
    private fun heapSort(){

    }


    private fun algoFinishedFunctionality(){
        val button: Button = findViewById(R.id.randomize_sort)
        val button2: Button = findViewById(R.id.start_sort)
        algoRunning = false
        algoPaused = false
        algoFinished = true
        button.text = "Randomise"
        button2.text = "Reset"
        button.setBackgroundColor(themeColor)
        button2.setBackgroundColor(themeColor)
    }
    private fun resetFunctionality(){
        val slider:Slider = findViewById(R.id.slider_sort)
        val button: Button = findViewById(R.id.randomize_sort)
        val button2: Button = findViewById(R.id.start_sort)
        var spinner:Spinner = findViewById(R.id.spinner_sort1)
        button.text = "Randomise"
        algoRunning = false
        algoPaused = false
        slider.isEnabled = true
        algoFinished = false
        spinner.isEnabled = true
        button2.text = "Start"
        button.setBackgroundColor(themeColor)
        button2.setBackgroundColor(green)
    }
    private fun standardSetup(keep:Boolean=false){
        resetFunctionality()
        destroyButtons(keep)
        createButtonScreen(size)
        createShuffledArrays(size)
        colorButtonScreen(size)
        holdFunctionality()
    }
    private fun setUpWithoutShuffling(keep:Boolean=false){
//        resetFunctionality()
        destroyButtons(keep)
        createButtonScreen(size)
        colorButtonScreen(size)
        holdFunctionality()
    }
    private fun setUpCurrentScreen(keep: Boolean=false){
        destroyButtons(true)
        createButtonScreen(size)
        colorCurrentButtonScreen(size)
        holdFunctionality()
    }
    private fun swapDrop(i1:Int,i2:Int){
        var temp=orderArray[i1]
        orderArray[i1]=orderArray[i2]
        orderArray[i2]=temp
    }
    private fun swapColor(i1:Int,i2:Int){
        var temp=colorArray[i1]
        colorArray[i1]=colorArray[i2]
        colorArray[i2]=temp
    }
    private fun createShuffledArrays(size: Int){

        orderArray = Array(size) { i -> i * 1 }
        colorArray = Array(size){themeColor}
        orderArray.shuffle()
    }
    private fun colorButtonScreen(size:Int){
        for(i in 0..size-1){
            colorFollowingButtons(i,0,orderArray[i],themeColor)
            colorFollowingButtons(i,orderArray[i]+1,size-1,black)
        }
    }
    private fun colorCurrentButtonScreen(size:Int){
        for(i in 0..size-1){
            colorFollowingButtons(i,0,orderArray[i],colorArray[i])
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

        var layoutid = resources.getIdentifier("col1", "id", packageName)
        val layout=findViewById<LinearLayout>(layoutid)

        val buttonScreen = LinearLayout(this)
        buttonScreen.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        buttonScreen.orientation = LinearLayout.VERTICAL

        var buttonScreenid = resources.getIdentifier("buttonScreen2", "id", packageName)
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
    private fun destroyButtons(keep:Boolean=false){
        var id = resources.getIdentifier("buttonScreen2", "id", packageName)
        val buttonScreen =findViewById<LinearLayout>(id)
        (buttonScreen.getParent() as ViewGroup).removeView(buttonScreen)
        buttons.removeAll(buttons)
        if(keep) return
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
                if(id == R.id.spinner_sort1){
                    algoInUse= algoMap[item]!!
                }else{
                    speedInUSe = speedMap[item]!!
                    speed= (1000*speedInUSe).toLong()
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }
}