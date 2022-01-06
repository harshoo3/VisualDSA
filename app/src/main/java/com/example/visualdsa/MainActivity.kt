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
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ListAdapter


class MainActivity : AppCompatActivity() {
    val topic = arrayOf<String>("Searching", "Sorting", "Graph Algorithms",
        "Tree Algorithms")
    val description = arrayOf<String>(
        "C programming is considered as the base for other programming languages",
        "C++ is an object-oriented programming language.",
        "Java is a programming language and a platform.",
        ".NET is a framework which is used to develop software applications."
    )
    val imageId = arrayOf<Int>(
        R.drawable.android_logo,R.drawable.android_logo,R.drawable.android_logo,
        R.drawable.android_logo
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
                        val intent = Intent(this, graphAlgoActivity::class.java);
                        startActivity(intent);
                    }
                    3 -> {
                        val intent = Intent(this, treeAlgoActivity::class.java);
                        startActivity(intent);
                    }
                }
            }
            onItemClick()
            Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()

        }

//
//        val arrayAdapter: ArrayAdapter<*>
//        val users = arrayOf(
//            "Searching", "Sorting", "Graph Algorithms",
//            "Tree Algorithms"
//        )
//
//        // access the listView from xml file
//        var mListView = findViewById<ListView>(R.id.userlist)
//        arrayAdapter = ArrayAdapter(this,
//            android.R.layout.simple_list_item_1, users)
//        mListView?.adapter = arrayAdapter
////        button.setOnClickListener(object : OnClickListener() {
////            fun onClick(viewClicked: View) {
////                val intent = Intent(viewClicked.getContext(), ActivityX::class.java)
////            }
////        })
//        mListView.setOnItemClickListener(OnItemClickListener { arg0, view, position, id -> // When clicked, show a toast with the TextView text
//
//            @Override
//            fun onItemClick() {
//                when (position) {
//                    0->{val intent = Intent(this, searchingAlgoActivity::class.java);
//                        startActivity(intent);
//                        }
//                    1->{val intent = Intent(this, sortingAlgoActivity::class.java);
//                        startActivity(intent);
//                    }
//                    2->{val intent = Intent(this, graphAlgoActivity::class.java);
//                        startActivity(intent);
//                    }
//                    3->{val intent = Intent(this, treeAlgoActivity::class.java);
//                        startActivity(intent);
//                    }
//                }
//            }
//            onItemClick()
////            @Override
////            fun onItemClick() {
////                when (position) {
////                    0->{val intent = Intent(this, searchingAlgoActivity::class.java);
////                        startActivity(intent);
////                    }
////                    1->{val intent = Intent(this, sortingAlgoActivity::class.java);
////                        startActivity(intent);
////                    }
////                    2->{val intent = Intent(this, graphAlgoActivity::class.java);
////                        startActivity(intent);
////                    }
////                    3->{val intent = Intent(this, treeAlgoActivity::class.java);
////                        startActivity(intent);
////                    }
////
////                }
////            }
//
////            fun onClick() {
////                val intent = Intent(this, sortingAlgoActivity::class.java)
////                startActivity(intent)
////            }
////            onClick()
//            Toast.makeText(
//                applicationContext, (view as TextView).text,
//                Toast.LENGTH_SHORT
//            ).show()
//        })
    }
}