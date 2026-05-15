package com.example.nammanalaa.service

import com.example.nammanalaa.model.Report
import com.example.nammanalaa.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance("https://nammanala-613e6-default-rtdb.firebaseio.com/").reference

    suspend fun registerUser(user: User, password: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Authentication failed")
            val userWithUid = user.copy(uid = uid)
            
            val path = if (user.role == "Officer") "officers" else "farmer"
            db.child(path).child(uid).setValue(userWithUid).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Login failed")
            
            val user = getUserData(uid)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User profile not found in database. Please register again."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserData(uid: String): User? {
        val farmerTask = db.child("farmer").child(uid).get()
        try {
            val snapshot = farmerTask.await()
            if (snapshot.exists()) {
                return snapshot.getValue(User::class.java)?.copy(uid = uid, role = "Farmer")
            }
        } catch (e: Exception) {
            if (e.message?.contains("permission", ignoreCase = true) == true) {
                throw e 
            }
        }

        val officerTask = db.child("officers").child(uid).get()
        try {
            val snapshot = officerTask.await()
            if (snapshot.exists()) {
                return snapshot.getValue(User::class.java)?.copy(uid = uid, role = "Officer")
            }
        } catch (e: Exception) {
            if (e.message?.contains("permission", ignoreCase = true) == true) {
                throw e
            }
        }

        return null
    }

    suspend fun getOfficers(): List<User> {
        return try {
            val snapshot = db.child("officers").get().await()
            snapshot.children.mapNotNull { it.getValue(User::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getReportsForUser(email: String): List<Report> {
        return try {
            val snapshot = db.child("reports").get().await()
            snapshot.children.mapNotNull { it.getValue(Report::class.java) }
                .filter { it.email == email }
                .sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun submitReport(report: Report): Result<Unit> {
        return try {
            val reportRef = db.child("reports").push()
            val finalReport = report.copy(id = reportRef.key ?: "")
            reportRef.setValue(finalReport).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getReportsRef() = db.child("reports")
}
