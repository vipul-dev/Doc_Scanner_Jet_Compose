package com.vipul.docscannerapp.data.repository

import android.app.Application
import com.vipul.docscannerapp.data.database.PdfDatabase
import com.vipul.docscannerapp.data.models.PdfEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class PdfRepository(application: Application) {
    private val pdfDao = PdfDatabase.getDatabase(application).pdfDao


    suspend fun insertPdf(pdfEntity: PdfEntity): Long {
        return pdfDao.insertPdf(pdfEntity)
    }

    suspend fun updatePdf(pdfEntity: PdfEntity): Int {
        return pdfDao.updatePdf(pdfEntity)
    }

    suspend fun deletePdf(pdfEntity: PdfEntity): Int {
        return pdfDao.deletePdf(pdfEntity)
    }

    fun getPdfList() = pdfDao.getAllPdf().flowOn(Dispatchers.IO)

}