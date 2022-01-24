package com.example.visualdsa

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.slider.Slider
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*


class mazeActivity : AppCompatActivity() {
    val themeColor:Int = Color.parseColor("#FFBB86FC")
    val black:Int = Color.parseColor("#000000")
    val green:Int = Color.parseColor("#00FF00")
    val red:Int = Color.parseColor("#FF0000")
    val blue:Int = Color.parseColor("#0000FF")
    val white:Int = Color.parseColor("#FFFFFF")
    val yellow:Int = Color.parseColor("#FCF4A3")
    val brown:Int = Color.parseColor("#8A3324")
    var speedArr = arrayOf("1x","0.25x","0.5x","0.75x","1.25x","1.5x", "1.75x","2x","4x")
    var speedMap = mutableMapOf<String,Double>()
    var buttonIdMap= mutableMapOf<Int,Int>()
    var searchAlgoArr = arrayOf("BFS","DFS", "Dijkstra")
    var algoInUse:Int = 0
    var algoMap= mutableMapOf<String,Int>("BFS" to 0,"DFS" to 1,"Dijkstra" to 2)
    var algoRunning: Boolean= false
    var algoFinished:Boolean = false
    var algoPaused: Boolean = false
    var speedInUSe: Double = 1.0
    var speed:Long = 1000
    var size:Int = 6
    var n:Int = 4
    private var colorArray= Array(size) { IntArray(n) }
    private var ogColorArray= Array(size) { IntArray(n) }
    private val buttons: MutableList<MutableList<Button>> = ArrayList()
    var startSelected:Boolean = false
    var endSelected:Boolean = false
    var selectPressed:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)
        for(i in 0..speedArr.size-1){
            speedMap[speedArr[i]]=1/(speedArr[i].subSequence(0,speedArr[i].length-1).toString().toDouble())
        }
        val button: Button = findViewById(R.id.randomize_maze)
        val button2: Button = findViewById(R.id.start_maze)
        val button3: Button = findViewById(R.id.clear_maze)
        var spinner: Spinner = findViewById(R.id.spinner_maze1)
        var spinner2: Spinner = findViewById(R.id.spinner_maze2)
        val slider = findViewById<Slider>(R.id.slider_maze)
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
            n = (2*size)/3
            colorArray = Array(size) { IntArray(n) }
//            if(algoInUse==1) sortedSetup()
            standardSetup(size,n)
        }
        button.textSize = 16F
        button.setOnClickListener {
            if(algoRunning){
                algoFinished = true
            }
            else if(algoFinished) Toast.makeText(this,"Please Reset.", Toast.LENGTH_LONG).show()
            // Code here executes on main thread after user presses button
            else{
                randomize(size,n)
//                randomize(size,n,true)
            }
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
                setUpWithoutShuffling(size,n)
            }
            else if(startSelected && endSelected){
                button2.text = "Pause"
                button.text = "Stop"
                slider.isEnabled = false
                spinner.isEnabled = false
                button2.setBackgroundColor(themeColor)
                button.setBackgroundColor(red)
                when(algoInUse){
                    0->bfs()
                    1->dfs()
                    2->dijkstra()
                }
            }else if(selectPressed){
                Toast.makeText(this,"Select Start and End points first.",Toast.LENGTH_LONG).show()
            }else{
                selectPressed = true
                button2.text = "start"
            }
        }
        button3.setOnClickListener {
//            if(algoRunning){
//                algoFinished = true
//            }
//            else if(algoFinished) Toast.makeText(this,"Please Reset.", Toast.LENGTH_LONG).show()
//            // Code here executes on main thread after user presses button
//            else{
//                randomize(size,n)
////                randomize(size,n,true)
//            }
            colorButtonScreen(size,n)
        }
        makeDropDown(searchAlgoArr,spinner.id)
        makeDropDown(speedArr,spinner2.id)
        createButtonScreen(size,n)
        standardSetup(size,n)
    }
    private fun bfs(){}
    private fun dfs(){}
    private fun dijkstra(){}
    private fun buttonfunctionality(size: Int,n: Int){
        for(i in 0..size-1){
            for(j in 0..n-1){
                val btn=buttons[i][j]
//                var touchCheck:Boolean = false
                if(algoRunning || algoFinished) return
                btn.setOnClickListener {
                    if(!selectPressed) toggleColorButton(i,j,brown)
                    else if(!startSelected){
                        btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_location_on_24,0,0,0)
                        startSelected=true
                    }else if(!endSelected){
                        btn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_always_landscape_portrait,0,0,0)
                        endSelected=true
                    }
                }
            }
        }
    }
    private fun selectStartPoint(size:Int, n:Int){
        for(i in 0..size-1){
            for(j in 0..n-1){
                val btn=buttons[i][j]
//                var touchCheck:Boolean = false
                if(btn.hasFocus()) println("$i,$j")
                btn.setOnClickListener {
                    toggleColorButton(i,j,brown)
                }
            }
        }
    }
    private fun randomize(size: Int,n:Int,invert:Boolean=false){
        colorButtonScreen(size,n)
        var temp1 = Array(size){i->i+1}
        temp1.shuffle()
        var rand1:Int = (((size/2)-1)..(2*size/3)-1).random()
        var tempL:Int = (n/2)-1
        var tempR:Int = ((2*n)/3)-1
        for(i in 0..rand1-1){
            var temp2 = Array(n){i->i+1}
            temp2.shuffle()
            var rand2:Int = (tempL..tempR).random()
            for(j in 0..rand2-1){
                if(invert)  toggleColorButton(temp1[j]-1,temp2[i]-1,brown)
                else toggleColorButton(temp1[i]-1,temp2[j]-1,brown)
            }
        }
    }
    private fun toggleColorButton(i:Int,j:Int,color:Int){
        var setCol:Int =white
        if(colorArray[i][j]==white){
            setCol = color
        }
        colorFollowingButtons(i,j,j,setCol)
    }
    private fun colorButton(i:Int,j:Int,color:Int){
        colorFollowingButtons(i,j,j,color)
    }
    private fun standardSetup(size: Int,n:Int){
//        resetFunctionality(keep)
        destroyButtons()
        createButtonScreen(size,n)
        colorButtonScreen(size,n)
        buttonfunctionality(size,n)
    }
    private fun setUpWithoutShuffling(size: Int,n:Int){
//        resetFunctionality(keep)
        destroyButtons()
        createButtonScreen(size,n)
        colorCurrentButtonScreen(size,n)
        buttonfunctionality(size,n)
    }
    private fun createButtonScreen(size: Int,n:Int) {

        var layoutid = resources.getIdentifier("col2", "id", packageName)
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
            arr.setPadding(0,0,0,5)
            val buttonrow: MutableList<Button> = ArrayList()

            for (j in 1..n) {
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

//        if(keep){
//            return
//        }
//        check = false
//        selected=-1
//        buttonIdMap.clear()
    }
    private fun colorButtonScreen(size:Int,n:Int){
        for(i in 0..size-1){
            colorFollowingButtons(i,0,n-1,white)
        }
    }
    private fun colorCurrentButtonScreen(size:Int,n:Int){
        for(i in 0..size-1){
            for(j in 0..n-1){
                colorFollowingButtons(i,j,j,ogColorArray[i][j])
            }
        }
    }

    private fun colorFollowingButtons(row:Int, left:Int, right:Int, color:Int){
        val gd1 = GradientDrawable()
        gd1.setColor(color) // Changes this drawbale to use a single color instead of a gradient
        gd1.cornerRadius = 5f
//        gd1.setPadding(5,0,0,5)
        gd1.setStroke(2, yellow)
        for(j in left..right){
//            buttons[row][j].setBackgroundColor(color)
            buttons[row][j].setBackgroundDrawable(gd1)
            colorArray[row][j]=color
        }
//        if(color!=black) colorArray[row]=color
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
                if(id == R.id.spinner_maze1){
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