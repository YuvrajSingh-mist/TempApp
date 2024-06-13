package data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.chatroomapp.data.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageRepository(
    messageViewModel: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun sendMessage(roomId: String, message: Message): Result<Boolean> = try {
        firestore.collection("rooms").document(roomId)
            .collection("messages").add(message).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    fun getChatMessages(roomId: String): Flow<List<Message>> = callbackFlow {
        val subscription = firestore.collection("rooms").document(roomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                    Log.d("getChatMessages", "QuerySnapshot: $querySnapshot") // Log querySnapshot
                    val messages = it.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(Message::class.java)!!.copy()
                        } catch (e: Exception) {
                            Log.e("getChatMessages", "Error mapping document", e)
                            null
                        }
                    }
                    Log.d("getChatMessages", "Mapped Messages: $messages") // Log mapped messages
                    trySend(messages).isSuccess
                }
            }

        awaitClose { subscription.remove() }
    }

}