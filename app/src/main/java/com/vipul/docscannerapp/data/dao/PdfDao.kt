package com.vipul.docscannerapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vipul.docscannerapp.data.models.PdfEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PdfDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdf(pdf: PdfEntity): Long

    @Delete
    suspend fun deletePdf(pdf: PdfEntity): Int

    @Update
    suspend fun updatePdf(pdf: PdfEntity): Int

    @Query("SELECT * FROM pdf_table")
    fun getAllPdf(): Flow<List<PdfEntity>>
}