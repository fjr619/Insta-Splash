package com.fjr619.instasplash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fjr619.instasplash.presentation.screens.NavGraphs
import com.fjr619.instasplash.presentation.theme.InstaSplashTheme
import com.fjr619.instasplash.presentation.util.snackbar.AppSnackbar
import com.fjr619.instasplash.presentation.util.snackbar.LocalSnackbarController
import com.fjr619.instasplash.presentation.util.snackbar.ProvideSnackbarController
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            InstaSplashTheme {

                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                ProvideSnackbarController(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState) { data ->
                                AppSnackbar(data = data)
                            }
                        }
                    ) {
                        DestinationsNavHost(navGraph = NavGraphs.root)
                    }
                    
                }

            }
        }
    }
}