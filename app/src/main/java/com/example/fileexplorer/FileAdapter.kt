package com.example.fileexplorer

import android.content.ClipData
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter(
    private var files: List<File>,
    private val onItemClick: (File) -> Unit,
    private val onFileDrop: (File, File) -> Unit
) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    fun updateFiles(newFiles: List<File>) {
        files = newFiles
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileName: TextView = view.findViewById(android.R.id.text1)

        init {
            view.setOnClickListener {
                val file = files[adapterPosition]
                onItemClick(file)
            }

            view.setOnLongClickListener {
                val clipData = ClipData.newPlainText("file", files[adapterPosition].absolutePath)
                it.startDragAndDrop(clipData, View.DragShadowBuilder(it), files[adapterPosition], 0)
                true
            }

            view.setOnDragListener { _, event ->
                if (event.action == DragEvent.ACTION_DROP) {
                    val source = event.localState as? File
                    val target = files[adapterPosition]
                    if (source != null && target.isDirectory) {
                        onFileDrop(source, target)
                    }
                    true
                } else true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = files.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fileName.text = files[position].name
    }
}