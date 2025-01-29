package com.vipul.docscannerapp.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipul.docscannerapp.data.models.PdfEntity
import com.vipul.docscannerapp.data.repository.PdfRepository
import com.vipul.docscannerapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PdfViewModel(application: Application) : ViewModel() {

    private val pdfRepository = PdfRepository(application)
    var isSplashScreen: Boolean by mutableStateOf(false)
    var showRenameDialog: Boolean by mutableStateOf(false)
    var loadingDialog: Boolean by mutableStateOf(false)
    var isDarkMode: Boolean by mutableStateOf(false)

    private val _pdfStatFlow = MutableStateFlow<Resource<List<PdfEntity>>>(Resource.Idle)
    val pdfStatFlow: StateFlow<Resource<List<PdfEntity>>>
        get() = _pdfStatFlow

    private val _message = Channel<Resource<String>>()
    val message = _message.receiveAsFlow()

    var currentPdfEntity: PdfEntity? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            delay(2000)
            isSplashScreen = false
        }

        viewModelScope.launch(Dispatchers.IO) {
            _pdfStatFlow.emit(Resource.Loading)
            delay(2000)
            pdfRepository.getPdfList().catch {
                _pdfStatFlow.emit(Resource.Error(it.message ?: "Something went wrong!"))
                it.printStackTrace()
            }.collect {
                _pdfStatFlow.emit(Resource.Success(it))
            }
        }

        viewModelScope.launch {
            pdfStatFlow.collect {
                when (it) {
                    is Resource.Error -> {
                        loadingDialog = false
                    }

                    Resource.Idle -> {

                    }

                    Resource.Loading -> {
                        loadingDialog = true
                    }

                    is Resource.Success -> {
                        loadingDialog = false
                    }
                }
            }
        }
    }


    fun insertPdf(pdfEntity: PdfEntity) = viewModelScope.launch(Dispatchers.IO) {
        try {
            // if circular progress bar is use on screen
//            _pdfStatFlow.emit(Resource.Loading)

            //  if loading dialog use
            loadingDialog = true

            val result = pdfRepository.insertPdf(pdfEntity)
            if (result.toInt() != -1) {
                _message.send(Resource.Success("Pdf inserted successfully"))
            } else {
                _message.send(Resource.Error("Something went wrong!"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _message.send(Resource.Error(e.message ?: "Something went wrong!"))
        }
    }

    fun deletePdf(pdfEntity: PdfEntity) = viewModelScope.launch(Dispatchers.IO) {
        try {
            // if circular progress bar is use on screen
            _pdfStatFlow.emit(Resource.Loading)
            pdfRepository.deletePdf(pdfEntity)
            _message.send(Resource.Success("Pdf deleted successfully"))
        } catch (e: Exception) {
            e.printStackTrace()
            _message.send(Resource.Error(e.message ?: "Something went wrong!"))
        }
    }

    fun updatePdf(pdfEntity: PdfEntity) = viewModelScope.launch(Dispatchers.IO) {
        try {
            // if circular progress bar is use on screen
            _pdfStatFlow.emit(Resource.Loading)
            val result = pdfRepository.updatePdf(pdfEntity)
            _message.send(Resource.Success("Pdf updated successfully"))
        } catch (e: Exception) {
            e.printStackTrace()
            _message.send(Resource.Error(e.message ?: "Something went wrong!"))
        }
    }


}