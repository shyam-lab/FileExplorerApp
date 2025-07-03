package com.example.fileexplorer

import android.os.Bundle
import android.os.Environment
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter
    private var currentPath: File = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = FileAdapter(currentPath.listFiles()?.toList() ?: emptyList(),
            onItemClick = { file ->
                if (file.isDirectory) {
                    currentPath = file
                    adapter.updateFiles(file.listFiles()?.toList() ?: emptyList())
                } else {
                    Toast.makeText(this, "Selected: ${file.name}", Toast.LENGTH_SHORT).show()
                }
            },
            onFileDrop = { source, target ->
                val success = FileUtils.moveFile(source, target)
                if (success) {
                    adapter.updateFiles(currentPath.listFiles()?.toList() ?: emptyList())
                    Toast.makeText(this, "Moved ${source.name}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        recyclerView.adapter = adapter
        recyclerView.setOnDragListener { _, _ -> true }
    }
}