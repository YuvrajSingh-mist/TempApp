package com.example.chatapp

import android.os.Bundle
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.ui.theme.ChatAppTheme
import viewModel.MainViewModel
import viewModel.MessageViewModel
import viewModel.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val modelViewModel : MainViewModel = viewModel()
            val messageViewModel : MessageViewModel = viewModel()
            ChatAppTheme {
              setContent { 
                  Navigation(viewModel = modelViewModel, messageViewModel = messageViewModel, navController = navController)
              }
            }
        }
    }
}

