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
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "Searching", "Sorting", "Graph Algorithms",
            "Tree Algorithms"
        )

        // access the listView from xml file
        var mListView = findViewById<ListView>(R.id.userlist)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, users)
        mListView?.adapter = arrayAdapter
//        button.setOnClickListener(object : OnClickListener() {
//            fun onClick(viewClicked: View) {
//                val intent = Intent(viewClicked.getContext(), ActivityX::class.java)
//            }
//        })
        mListView.setOnItemClickListener(OnItemClickListener { arg0, view, position, id -> // When clicked, show a toast with the TextView text

            @Override
            fun onItemClick() {
                when (position) {
                    0->{val intent = Intent(this, searchingAlgoActivity::class.java);
                        startActivity(intent);
                        }
                    1->{val intent = Intent(this, sortingAlgoActivity::class.java);
                        startActivity(intent);
                    }
                    2->{val intent = Intent(this, graphAlgoActivity::class.java);
                        startActivity(intent);
                    }
                    3->{val intent = Intent(this, treeAlgoActivity::class.java);
                        startActivity(intent);
                    }
                }
            }
            onItemClick()
//            @Override
//            fun onItemClick() {
//                when (position) {
//                    0->{val intent = Intent(this, searchingAlgoActivity::class.java);
//                        startActivity(intent);
//                    }
//                    1->{val intent = Intent(this, sortingAlgoActivity::class.java);
//                        startActivity(intent);
//                    }
//                    2->{val intent = Intent(this, graphAlgoActivity::class.java);
//                        startActivity(intent);
//                    }
//                    3->{val intent = Intent(this, treeAlgoActivity::class.java);
//                        startActivity(intent);
//                    }
//
//                }
//            }

//            fun onClick() {
//                val intent = Intent(this, sortingAlgoActivity::class.java)
//                startActivity(intent)
//            }
//            onClick()
            Toast.makeText(
                applicationContext, (view as TextView).text,
                Toast.LENGTH_SHORT
            ).show()
        })
    }
}