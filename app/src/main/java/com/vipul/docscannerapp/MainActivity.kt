package com.vipul.docscannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vipul.docscannerapp.screens.home.HomeScreen
import com.vipul.docscannerapp.ui.theme.DocScannerAppTheme
import com.vipul.docscannerapp.viewmodels.PdfViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<PdfViewModel> {
        viewModelFactory {
            addInitializer(PdfViewModel::class) {
                PdfViewModel(application)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            splashScreen.setKeepOnScreenCondition { viewModel.isSplashScreen }
            DocScannerAppTheme(viewModel.isDarkMode,false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(viewModel)
                }
            }
        }
    }
}

