package com.example.visualdsa

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.ArrayAdapter
import android.widget.ListView
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.ActionBar
import android.widget.TextView

import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.content.Intent

import android.R.attr.button
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ListAdapter


class MainActivity : AppCompatActivity() {
    val topic = arrayOf<String>("Search Algorithms", "Sorting Algorithms", "Maze Runner", "Graph Algorithms",
        "Tree Algorithms")
    val description = arrayOf<String>(
        "C programming is considered as the base for other programming languages",
        "C++ is an object-oriented programming language.",
        ".NET is a framework which is used to develop software applications.",
        "Java is a programming language and a platform.",
        ".NET is a framework which is used to develop software applications."
    )
    val imageId = arrayOf<Int>(
        R.drawable.android_logo,R.drawable.search,R.drawable.graph1,R.drawable.graph1,
        R.drawable.tree,
    )
    lateinit var listView : ListView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)
        val myListAdapter = MyListAdapter(this,topic,description,imageId)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener(){adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            @Override
            fun onItemClick() {
                when (position) {
                    0 -> {
                        val intent = Intent(this, searchingAlgoActivity::class.java);
                        startActivity(intent);
                    }
                    1 -> {
                        val intent = Intent(this, sortingAlgoActivity::class.java);
                        startActivity(intent);
                    }
                    2 -> {
                        val intent = Intent(this, mazeActivity::class.java);
                        startActivity(intent);
                    }
                    3 -> {
                        val intent = Intent(this, graphAlgoActivity::class.java);
                        startActivity(intent);
                    }
                    4 -> {
                        val intent = Intent(this, treeAlgoActivity::class.java);
                        startActivity(intent);
                    }
                }
            }
            onItemClick()
        }
        supportActionBar?.apply {
            // show custom title in action bar
            customView = actionBarCustomTitle()
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
        }

    }
    private fun actionBarCustomTitle():TextView{
        return TextView(this).apply {
            text = "Welcome to VisualDSA"

            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            // center align the text view/ action bar title
            params.gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = params
            setTextSize(25F)
        }
    }
}