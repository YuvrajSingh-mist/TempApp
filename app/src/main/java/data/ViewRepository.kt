package data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import viewModel.MainViewModel

class ViewRepository(

    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            saveUserToFirestore(Users(firstName, lastName, email))
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    private suspend fun saveUserToFirestore(user: Users) {
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun signIn(
        email: String,
        password: String,

        ): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun createRoom(name: String): Result<Boolean> =
        try {

            val room = ChatRoomListData(name = name)
            firestore.collection("rooms").add(room).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getRooms(): Result<List<ChatRoomListData>> = try {
        val querySnapshot = firestore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(ChatRoomListData::class.java)!!.copy(id = document.id)
        }
        Result.Success(rooms)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getCurrentUser() : Result<Users> = try{
        val email = auth.currentUser?.email
        if(email!= null){
            val querySnapshot = firestore.collection("users").document(email).get().await()
            val user = querySnapshot.toObject(Users::class.java)!!.copy()
            Result.Success(user)
            }
        else{
            Result.Error(Exception("Couldn't fetch the current user's email id"))
        }
        }
    catch (e: Exception) {
        Result.Error(Exception("User(s) was not found."))
    }
}
