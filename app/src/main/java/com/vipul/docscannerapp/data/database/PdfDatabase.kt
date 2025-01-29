package com.vipul.docscannerapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vipul.docscannerapp.data.dao.PdfDao
import com.vipul.docscannerapp.data.models.PdfEntity
import com.vipul.docscannerapp.utils.converter.DateTypeConverter

@Database(entities = [PdfEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class PdfDatabase : RoomDatabase() {
    abstract val pdfDao: PdfDao

    companion object {
        @Volatile
        private var INSTANCE: PdfDatabase? = null

        fun getDatabase(context: Context): PdfDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    PdfDatabase::class.java,
                    "pdf_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
