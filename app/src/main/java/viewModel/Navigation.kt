package viewModel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screen.ChatRoomListScreen
import screen.ChatScreen
import screen.LoginScreen
import screen.Screen
import screen.SignUpScreen

@Composable
fun Navigation(
    messageViewModel: MessageViewModel,
    viewModel: MainViewModel,
    navController: NavController
){
    NavHost(navController = navController as NavHostController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route){
            LoginScreen(viewModel, navController) {

                navController.navigate(Screen.ChatRoomsScreen.route)
            }
        }
        composable(Screen.SignupScreen.route){
            SignUpScreen(viewModel = viewModel, navController = navController)

        }

        composable(Screen.ChatRoomsScreen.route){
            ChatRoomListScreen(viewModel = viewModel, navController = navController)

        }
        composable(Screen.ChatScreen.route + "/{roomId}"){

            val roomId: String = it
                .arguments?.getString("roomId") ?: ""
            Log.d("roomId", roomId)
            messageViewModel.setRoomId(roomId)
            messageViewModel.fetchMessages()
            ChatScreen(navController = navController, roomId = roomId, viewModel = viewModel, messageViewModel = messageViewModel)

        }
    }
}