package com.example.fileexplorer

import java.io.File

object FileUtils {
    fun moveFile(source: File, targetDir: File): Boolean {
        return try {
            val target = File(targetDir, source.name)
            source.renameTo(target)
        } catch (e: Exception) {
            false
        }
    }
}