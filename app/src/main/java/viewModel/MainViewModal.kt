package viewModel

import data.Result
import Injection
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import data.ChatRoomListData
import data.ViewRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _authResult = MutableLiveData<Result<Boolean>>()
    private val _showDialog = mutableStateOf(false)

    private val _roomName = mutableStateOf("")
    private val _rooms = MutableLiveData<List<ChatRoomListData>>()
    val rooms: LiveData<List<ChatRoomListData>> get() = _rooms
    val showDialog: State<Boolean> = _showDialog
    val roomName: State<String> = _roomName
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    var repository: ViewRepository

    init {
        //DI
        repository = ViewRepository(FirebaseAuth.getInstance(), Injection.instance())
//        Log.d("Check", "Hi")
//        loadRooms()
    }

    fun showDialogUpdateState(
        newshowDialogstate: Boolean
    ) {
        _showDialog.value = newshowDialogstate
    }

    fun roomNameUpdateState(
        newRoomName: String
    ) {
        _roomName.value = newRoomName
    }




    fun signUp(email: String, password: String, firstname: String, lastname: String) {

        viewModelScope.launch {
            _authResult.value = repository.signUp(email, password, firstname, lastname)
        }
    }

    fun signIn(email: String, password: String) {

        viewModelScope.launch {
            _authResult.value = repository.signIn(email, password)
        }

    }


    fun createRoom(name: String) {
        viewModelScope.launch {
            repository.createRoom(name)
        }
    }

//    @Composable
    fun loadRooms() {
//        var errorMessageState by remember {
//            mutableStateOf("")
//        }
        viewModelScope.launch {
            when (val result = repository.getRooms()) {
                is Result.Success -> _rooms.value = result.data
                is Result.Error -> {
//                    errorMessageState = "Couldn't locate the room. Please try again. Error: $${result.exception.message}"
                }
            }

        }
        Log.d("getRooms", "Fetched room: ${_rooms.value}")
//        if (errorMessageState.isNotEmpty()) {
//            Text(text = errorMessageState)
//        }
    }


}