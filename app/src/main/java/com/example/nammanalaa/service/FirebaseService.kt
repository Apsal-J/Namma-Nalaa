package com.example.nammanalaa.service

import com.example.nammanalaa.model.*
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

    // --- Water Status Feed ---
    suspend fun getWaterFeed(): List<FeedItem> {
        return try {
            val snapshot = db.child("water_feed").get().await()
            snapshot.children.mapNotNull { it.getValue(FeedItem::class.java) }
                .sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun postFeedUpdate(feedItem: FeedItem): Result<Unit> {
        return try {
            val ref = db.child("water_feed").push()
            val itemWithId = feedItem.copy(id = ref.key ?: "")
            ref.setValue(itemWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Maintenance Tracker ---
    suspend fun getMaintenanceSections(): List<MaintenanceSection> {
        return try {
            val snapshot = db.child("maintenance").get().await()
            snapshot.children.mapNotNull { it.getValue(MaintenanceSection::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // --- Silt Alert System ---
    suspend fun getSiltAlerts(): List<SiltAlert> {
        return try {
            val snapshot = db.child("silt_alerts").get().await()
            snapshot.children.mapNotNull { it.getValue(SiltAlert::class.java) }
                .sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun postSiltAlert(alert: SiltAlert): Result<Unit> {
        return try {
            val ref = db.child("silt_alerts").push()
            val alertWithId = alert.copy(id = ref.key ?: "")
            ref.setValue(alertWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Reports ---
    suspend fun getReports(uid: String): List<Report> {
        return try {
            val snapshot = db.child("reports").child(uid).get().await()
            snapshot.children.mapNotNull { it.getValue(Report::class.java) }
                .sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun submitReport(uid: String, report: Report): Result<Unit> {
        return try {
            val ref = db.child("reports").child(uid).push()
            val reportWithId = report.copy(id = ref.key ?: "")
            ref.setValue(reportWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Officers ---
    suspend fun getOfficers(): List<User> {
        return try {
            val snapshot = db.child("officers").get().await()
            snapshot.children.mapNotNull { it.getValue(User::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
