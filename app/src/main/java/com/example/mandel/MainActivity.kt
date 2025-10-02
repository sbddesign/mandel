package com.example.mandel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mandel.ui.screens.BlockedScreen
import com.example.mandel.ui.screens.EmailSignUpScreen
import com.example.mandel.ui.screens.HomeScreen
import com.example.mandel.ui.screens.WalletStartScreen
import com.example.mandel.ui.screens.WelcomeScreen
import com.example.mandel.ui.theme.MandelTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MandelTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          MandelNavigation()
        }
      }
    }
  }
}

@Composable
fun MandelNavigation() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = "wallet_start"
  ) {
    composable("wallet_start") {
      WalletStartScreen(
        onCreateWallet = {
          navController.navigate("email_signup")
        },
        onRestoreWallet = {
          // TODO: Navigate to restore wallet screen
        }
      )
    }

    composable("email_signup") {
      EmailSignUpScreen(
        onContinue = { email ->
          // Check if this is the success email
          if (email.lowercase() == "conor@spiral.xyz") {
            navController.navigate("welcome")
          } else {
            navController.navigate("blocked")
          }
        },
        onNavigateBack = {
          navController.popBackStack()
        }
      )
    }

    composable("blocked") {
      BlockedScreen(
        onBack = {
          navController.popBackStack()
        }
      )
    }

    composable("welcome") {
      WelcomeScreen(
        onContinue = {
          navController.navigate("home")
        }
      )
    }

    composable("home") {
      HomeScreen(
        onReceive = {
          // TODO: Navigate to receive screen
        },
        onSend = {
          // TODO: Navigate to send screen
        }
      )
    }
  }
}