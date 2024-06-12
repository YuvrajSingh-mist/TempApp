package viewModel

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screen.ChatRoomListScreen
import screen.LoginScreen
import screen.Screen
import screen.SignUpScreen

@Composable
fun Navigation(
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
            ChatRoomListScreen(viewModel = viewModel)

        }
    }
}