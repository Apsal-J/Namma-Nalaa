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
    var city: String = "",
    var address: String = "",
    var role: String = "Farmer", // "Farmer" or "Officer"
    var designation: String = ""
)

data class WaterUpdate(
    val id: String = "",
    val villageName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val postedBy: String = ""
)

data class FeedItem(
    val id: String = "",
    val villageName: String = "",
    val updateMessage: String = "",
    val time: String = "",
    val postedBy: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class MaintenanceSection(
    val id: String = "",
    val sectionName: String = "",
    val status: String = "Pending", // Pending, In Progress, Completed
    val lastUpdated: Long = System.currentTimeMillis(),
    val history: List<MaintenanceHistoryRecord> = emptyList()
)

data class MaintenanceHistoryRecord(
    val id: String = "",
    val date: String = "",
    val description: String = "",
    val status: String = ""
)

data class SiltAlert(
    val id: String = "",
    val area: String = "",
    val description: String = "",
    val severity: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val postedBy: String = ""
)

data class OfficerContact(
    val name: String = "",
    val designation: String = "",
    val phone: String = "",
    val division: String = ""
)

data class Issue(
    val id: String = "",
    val reporter: String = "",
    val area: String = "",
    val type: String = "",
    val description: String = "",
    val priority: String = "",
    val status: String = "",
    val date: String = ""
)

data class Report(
    val id: String = "",
    val issueType: String = "",
    val status: String = "Pending",
    val area: String = "",
    val address: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class Notification(
    val id: Int = 0,
    val title: String = "",
    val message: String = "",
    val time: String = ""
)
