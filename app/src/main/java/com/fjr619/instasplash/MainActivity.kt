package com.fjr619.instasplash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fjr619.instasplash.domain.model.NetworkStatus
import com.fjr619.instasplash.domain.repository.NetworkConnectivityObserver
import com.fjr619.instasplash.presentation.MainViewModel
import com.fjr619.instasplash.presentation.components.NetworkStatusBar
import com.fjr619.instasplash.presentation.screens.NavGraphs
import com.fjr619.instasplash.presentation.theme.InstaSplashTheme
import com.fjr619.instasplash.presentation.util.snackbar.AppSnackbar
import com.fjr619.instasplash.presentation.util.snackbar.ProvideSnackbarController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            InstaSplashTheme {

                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                val status by mainViewModel.networkStatus.collectAsStateWithLifecycle()
                var showMessageBar by rememberSaveable { mutableStateOf(false) }
                var message by rememberSaveable { mutableStateOf("") }
                var backgroundColor by remember { mutableStateOf(Color.Red) }

                //TODO improvement will data class
                LaunchedEffect(key1 = status) {
                    when (status) {
                        NetworkStatus.Connected -> {
                            message = "Connected to Internet"
                            backgroundColor = Color.Green
                            delay(timeMillis = 2000)
                            showMessageBar = false
                        }

                        NetworkStatus.Disconnected -> {
                            showMessageBar = true
                            message = "No Internet Connection"
                            backgroundColor = Color.Red
                        }
                    }
                }

                ProvideSnackbarController(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState) { data ->
                                AppSnackbar(data = data)
                            }
                        },
                        bottomBar = {
                            NetworkStatusBar(
                                showMessageBar = showMessageBar,
                                message = message,
                                backgroundColor = backgroundColor
                            )

                        },
                    ) {
                        DestinationsNavHost(
                            modifier = Modifier,
                            navGraph = NavGraphs.root,
                            dependenciesContainerBuilder = {
                                dependency(snackbarHostState)
                            }
                        )
                    }
                }
            }
        }
    }
}