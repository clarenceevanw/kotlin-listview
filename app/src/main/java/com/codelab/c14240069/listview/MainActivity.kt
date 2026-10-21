package com.codelab.c14240069.listview

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var data = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        data.addAll(listOf("1","2","3","4","5"))

        val lvAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            data
        )

        val _lv1 = findViewById<ListView>(R.id.lv1)
        _lv1.adapter = lvAdapter

        _lv1.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(
                this,
                data[position],
                Toast.LENGTH_SHORT
            ).show()
        }

        val gestureDetector = GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val position = _lv1.pointToPosition(e.x.toInt(), e.y.toInt())
                    if (position != ListView.INVALID_POSITION) {
                        val selectedItem = data[position]
                        showActionDialog(position, selectedItem, data, lvAdapter)
                    }
                    return true
                }
            }
        )
        
        _lv1.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }

        val _btnTambah = findViewById<Button>(R.id.btnTambah)
        _btnTambah.setOnClickListener {
            var dtAkhir = Integer.parseInt(data.get(data.size - 1)) + 1
            data.add("$dtAkhir")
            lvAdapter.notifyDataSetChanged()
        }
    }

    private fun showActionDialog(
        position: Int,
        selectedItem: String,
        data: MutableList<String>,
        adapter: ArrayAdapter<String>
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ITEM $selectedItem")
        builder.setMessage("Pilih tindakan yang ingin dilakukan")

        builder.setPositiveButton("Update") {_,_ ->

        }

        builder.setNegativeButton("Hapus") {_, _ ->
            data.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(
                this,
                "Hapus item $selectedItem",
                Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNeutralButton("Batal") {dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }
}