package viewModel

import android.util.Log
import data.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import data.ChatRoomListData
import data.MessageRepository
import data.Users
import data.ViewRepository
import eu.tutorials.chatroomapp.data.Message
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel(){

    var messageRepository: MessageRepository
    var viewRepository: ViewRepository
    init {
        messageRepository = MessageRepository(FirebaseAuth.getInstance(), Injection.instance())
        viewRepository = ViewRepository(FirebaseAuth.getInstance(), Injection.instance())
        fetchCurrentUser()
    }
    private val _currentUser : MutableLiveData<Users> = MutableLiveData()
    private val _roomId: MutableLiveData<String> = MutableLiveData()

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

//    private var _messages = MutableLiveData<List<Message>>()
//    private var _msgSent: MutableLiveData<Boolean> = MutableLiveData()

//    val msgSent: LiveData<Boolean> = _msgSent
//    var messagesList: LiveData<List<Message>> = _messages
    val roomId: LiveData<String> = _roomId
    val currentUser : LiveData<Users> = _currentUser

    fun fetchCurrentUser(){
        viewModelScope.launch {
            when (val currentUserData = viewRepository.getCurrentUser()) {
                is Result.Success -> {
                    _currentUser.value = currentUserData.data
                }

                is Result.Error -> {
                    _currentUser.value = null
                }
            }
        }

    }
    fun setRoomId(roomId: String){
        _roomId.value = roomId
    }
    fun getRoomId(): String? {
        return roomId.value
    }

     fun sendMessage(message: Message){
         viewModelScope.launch {
             _roomId.value?.let { messageRepository.sendMessage(it, message) }
         }
    }

    fun fetchMessages() {
        viewModelScope.launch {
            _roomId.value?.let { roomId ->  //observeForever for conversion from Flow to LiveData
                messageRepository.getChatMessages(roomId = roomId).asLiveData().observeForever { messages ->
                    _messages.value = messages
                    Log.d("fetchMessages", "Fetched messages: ${messages}")
                }
            }
            Log.d("roomIdInside", "${_roomId.value}")
        }
    }



}