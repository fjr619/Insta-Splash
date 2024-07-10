package com.fjr619.instasplash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fjr619.instasplash.presentation.screens.NavGraphs
import com.fjr619.instasplash.presentation.theme.InstaSplashTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstaSplashTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}