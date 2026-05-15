package com.example.nammanalaa.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammanalaa.model.*
import com.example.nammanalaa.service.CloudinaryService
import com.example.nammanalaa.service.FirebaseService
import kotlinx.coroutines.launch

class MainViewModel(private val firebaseService: FirebaseService) : ViewModel() {
    val user = mutableStateOf<User?>(null)
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    
    val officers = mutableStateOf<List<User>>(emptyList())
    val recentReports = mutableStateOf<List<Report>>(emptyList())

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val result = firebaseService.login(email, password)
            if (result.isSuccess) {
                user.value = result.getOrNull()
                fetchFarmerHomeData()
                onSuccess()
            } else {
                error.value = result.exceptionOrNull()?.message
            }
            isLoading.value = false
        }
    }

    fun register(userData: User, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val result = firebaseService.registerUser(userData, password)
            if (result.isSuccess) {
                user.value = userData
                fetchFarmerHomeData()
                onSuccess()
            } else {
                error.value = result.exceptionOrNull()?.message
            }
            isLoading.value = false
        }
    }

    fun fetchFarmerHomeData() {
        val currentUser = user.value ?: return
        if (currentUser.role == "Farmer") {
            viewModelScope.launch {
                officers.value = firebaseService.getOfficers()
                recentReports.value = firebaseService.getReportsForUser(currentUser.email)
            }
        }
    }
}

class ReportViewModel(
    private val firebaseService: FirebaseService,
    private val cloudinaryService: CloudinaryService
) : ViewModel() {
    val isSubmitting = mutableStateOf(false)
    val submissionSuccess = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    fun submitReport(
        user: User,
        issueType: String,
        area: String,
        address: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            isSubmitting.value = true
            try {
                var photoUrl = ""
                if (imageUri != null) {
                    // Upload to Cloudinary
                    photoUrl = cloudinaryService.uploadImage(imageUri) ?: throw Exception("Image upload failed")
                }

                val report = Report(
                    farmerName = user.name,
                    email = user.email,
                    phno = user.phno.toString(),
                    address = address,
                    issueType = issueType,
                    area = area,
                    photoUrl = photoUrl, // Cloudinary link stored here
                    timestamp = System.currentTimeMillis(),
                    status = "Pending"
                )

                // Save report with Cloudinary link to Firebase RTDB
                val result = firebaseService.submitReport(report)
                if (result.isSuccess) {
                    submissionSuccess.value = true
                } else {
                    error.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                error.value = e.message
            }
            isSubmitting.value = false
        }
    }
}
