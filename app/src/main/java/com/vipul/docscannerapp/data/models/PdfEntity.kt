package com.vipul.docscannerapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "pdf_table")
data class PdfEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pdfId")
    val id: String,
    val name: String,
    val size: String,
    val lastModifiedTime: Date
)
