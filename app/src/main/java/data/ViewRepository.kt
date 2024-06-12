package data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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

    fun signIn(
        email: String,
        password: String,

    ): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
}