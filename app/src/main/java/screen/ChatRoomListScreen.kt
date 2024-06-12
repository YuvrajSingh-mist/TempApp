package screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewModel.MainViewModel

@Composable
fun ChatRoomListScreen(
    viewModel: MainViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat Rooms",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display a list of chat rooms
        LazyColumn {

        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.showDialogUpdateState(true)
            },

            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Room")
            ShowRoomCreationDialogBox(viewModel)
        }
    }
}

@Composable
fun ShowRoomCreationDialogBox(viewModel: MainViewModel) {
    if (viewModel.showDialog.value) {
        AlertDialog(onDismissRequest = { viewModel.showDialogUpdateState(false)},
            title = { Text("Create a new room") },
            text = {

                OutlinedTextField(
                    value = viewModel.roomName.value,
                    onValueChange = {
                                    viewModel.roomNameUpdateState(it)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }, confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (viewModel.roomName.value.isNotBlank()) {
                                viewModel.showDialogUpdateState(false)

                            }
                        }
                    ) {
                        Text("Add")
                    }
                    Button(
                        onClick = {
                            viewModel.showDialogUpdateState(false)
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            })

    }
}


