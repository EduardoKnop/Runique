package com.example.runique

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.auth.presentation.intro.IntroScreen
import com.example.auth.presentation.register.RegisterScreen
import com.example.auth.presentation.register.RegisterScreenRoot
import com.example.core.presentation.designsystem.CrossIcon
import com.example.core.presentation.designsystem.RuniqueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RuniqueTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreenRoot(
                        onSignInClick = {
                            Log.d("TAG", "onCreate: Logged In")
                        },
                        onSuccessfulRegistration = {},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}