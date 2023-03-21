package com.digitalaya.chat.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.digitalaya.chat.screen.ChangeKey
import com.digitalaya.chat.screen.ChatScreen
import com.digitalaya.chat.viewModel.ChatViewModel
import kotlin.system.exitProcess

@Composable
fun NavigationNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Chat") {
        composable("Chat") {
            ChatScreen(navController = navController, viewModel = ChatViewModel())
            BackHandler { exitProcess(1) }
        }

        composable("changeKey/{storeApiKey}") {
            ChangeKey(
                it.arguments?.getString("storeApiKey").toString(),
                navController = navController
            )
        }
    }
}
