package screen
import data.Result
import ChatMessageItem
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import viewModel.MessageViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import data.Users
import eu.tutorials.chatroomapp.data.Message
import viewModel.MainViewModel

@Composable
fun ChatScreen(
    roomId: String,
    viewModel: MainViewModel,
    messageViewModel: MessageViewModel,
    navController: NavController
) {
//    messageViewModel.setRoomId(roomId)
//    val fetchedMessages = messageViewModel.messages.value
    val listState = rememberLazyListState()
    val text = remember { mutableStateOf("") }
    val fetchedMessages by messageViewModel.messages.observeAsState(emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f)
        ) {
            items(fetchedMessages) { item ->
                ChatMessageItem(item.copy(isSentByCurrentUser
                = item.senderId == messageViewModel.currentUser.value?.email))
            }

        }
        LaunchedEffect(fetchedMessages) {
            if (fetchedMessages.isNotEmpty()) {
                listState.animateScrollToItem(index = fetchedMessages.lastIndex)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                label = {Text("Enter your message")},
                value = text.value,
                onValueChange = { text.value = it },
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )

            IconButton(
                onClick = {
//                    messageViewModel.fetchMessages()

                    messageViewModel.fetchCurrentUser()
                    val currentUserData = messageViewModel.currentUser.value
//                    Log.d("UserData", "ChatScreen: $currentUserData")
                    if (currentUserData != null) {

                        messageViewModel.sendMessage(Message(senderFirstName = currentUserData.firstName, text = text.value, isSentByCurrentUser = false, senderId = currentUserData.email))
//                        Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show()

                    }
                    if (text.value.isNotEmpty()) {

                        text.value = ""
//                        messageViewModel.setRoomId(roomId)
                        messageViewModel.fetchMessages()
                    }

                }
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }
    }

}


