package viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import data.ChatRoomListData
import data.MessageRepository
import eu.tutorials.chatroomapp.data.Message

class MessageViewModel : ViewModel(){

    lateinit var messageRepository: MessageRepository
    init {

        messageRepository = MessageRepository(FirebaseAuth.getInstance(), Injection.instance())

    }

    private val _roomId: MutableLiveData<String> = MutableLiveData()
    val roomId: LiveData<String> = _roomId

    fun setRoomId(roomId: String){
        _roomId.value = roomId
    }

    suspend fun sendMessage(message: Message){
        _roomId.value?.let { messageRepository.sendMessage(it, message) }
    }


}