package com.example.kursmobile.data.database

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object DatabasePrepopulator {

    suspend fun copyDatabaseFromAssets(
        context: Context,
        dbName: String = "cities.db"
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val destFile = File(context.filesDir, dbName)

            if (destFile.exists() && destFile.length() > 0) {
                return@withContext true
            }

            context.assets.open("databases/$dbName").use { inputStream ->
                FileOutputStream(destFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}