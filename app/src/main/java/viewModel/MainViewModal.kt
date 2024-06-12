package viewModel
import data.Result
import Injection
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import data.ViewRepository
import kotlinx.coroutines.launch

class MainViewModel :ViewModel() {

    private val _showDialog = mutableStateOf(false)
    private val _roomName = mutableStateOf("")

    val showDialog: State<Boolean> = _showDialog
    val roomName: State<String> = _roomName

    var respository: ViewRepository
    init {

        respository =  ViewRepository(FirebaseAuth.getInstance(), Injection.instance())
    }

    fun showDialogUpdateState(
        newshowDialogstate: Boolean
    ) {
        _showDialog.value = newshowDialogstate
    }

    fun roomNameUpdateState(
        newroomNameUpdateState: String
    ) {
        _roomName.value = newroomNameUpdateState
    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(email: String, password: String, firstname: String, lastname: String) {

        viewModelScope.launch {
            _authResult.value = respository.signUp(email, password, firstname, lastname)
        }
   }

    fun signIn(email: String, password: String) {

        viewModelScope.launch {
            _authResult.value = respository.signIn(email, password)
        }

    }

}