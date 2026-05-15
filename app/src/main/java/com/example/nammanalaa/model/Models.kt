package com.example.nammanalaa.model

import com.google.firebase.firestore.PropertyName as FirestoreName
import com.google.firebase.database.PropertyName as DatabaseName

data class User(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    @get:FirestoreName("phone number") @set:FirestoreName("phone number")
    @get:DatabaseName("phone number") @set:DatabaseName("phone number")
    var phno: Long = 0,
    var gender: String = "",
    var age: Long = 0,
    var city: String = "",
    var address: String = "",
    var role: String = "Farmer", // "Farmer" or "Officer"
    var designation: String = "" // For Officers
)

data class Report(
    val id: String = "",
    val farmerName: String = "",
    val email: String = "",
    val phno: String = "",
    val address: String = "",
    val issueType: String = "",
    val area: String = "",
    val photoUrl: String = "", // Cloudinary URL
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)

data class OfficerContact(
    val name: String,
    val role: String,
    val phno: String,
    val division: String
)

data class Issue(
    val id: String,
    val farmerName: String,
    val area: String,
    val type: String,
    val description: String,
    val priority: String,
    val status: String,
    val date: String
)

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val time: String
)

data class FeedItem(
    val id: Int,
    val area: String,
    val message: String,
    val time: String,
    val postedBy: String
)

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)
