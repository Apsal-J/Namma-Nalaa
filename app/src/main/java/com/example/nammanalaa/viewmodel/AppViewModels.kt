package com.example.nammanalaa.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammanalaa.model.*
import com.example.nammanalaa.service.FirebaseService
import kotlinx.coroutines.launch

class MainViewModel(private val firebaseService: FirebaseService) : ViewModel() {
    val user = mutableStateOf<User?>(null)
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    val waterFeed = mutableStateOf<List<FeedItem>>(emptyList())
    val maintenanceSections = mutableStateOf<List<MaintenanceSection>>(emptyList())
    val siltAlerts = mutableStateOf<List<SiltAlert>>(emptyList())
    val recentReports = mutableStateOf<List<Report>>(emptyList())
    val officers = mutableStateOf<List<User>>(emptyList())

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val result = firebaseService.login(email, password)
            if (result.isSuccess) {
                user.value = result.getOrNull()
                fetchAppData()
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
                fetchAppData()
                onSuccess()
            } else {
                error.value = result.exceptionOrNull()?.message
            }
            isLoading.value = false
        }
    }

    fun fetchAppData() {
        viewModelScope.launch {
            waterFeed.value = firebaseService.getWaterFeed()
            maintenanceSections.value = firebaseService.getMaintenanceSections()
            siltAlerts.value = firebaseService.getSiltAlerts()
        }
    }

    fun fetchFarmerHomeData() {
        val currentUser = user.value ?: return
        viewModelScope.launch {
            recentReports.value = firebaseService.getReports(currentUser.uid)
            officers.value = firebaseService.getOfficers()
        }
    }

    fun postWaterUpdate(village: String, message: String) {
        val currentUser = user.value ?: return
        viewModelScope.launch {
            val item = FeedItem(
                villageName = village,
                updateMessage = message,
                postedBy = currentUser.name
            )
            firebaseService.postFeedUpdate(item)
            fetchAppData()
        }
    }

    fun postSiltAlert(area: String, description: String, severity: String) {
        val currentUser = user.value ?: return
        viewModelScope.launch {
            val alert = SiltAlert(
                area = area,
                description = description,
                severity = severity,
                postedBy = currentUser.name
            )
            firebaseService.postSiltAlert(alert)
            fetchAppData()
        }
    }
}

class ReportViewModel(private val firebaseService: FirebaseService) : ViewModel() {
    val isSubmitting = mutableStateOf(false)
    val submissionSuccess = mutableStateOf(false)

    fun submitReport(user: User, type: String, area: String, address: String, imageUri: Uri?) {
        viewModelScope.launch {
            isSubmitting.value = true
            val report = Report(
                issueType = type,
                area = area,
                address = address,
                timestamp = System.currentTimeMillis(),
                status = "Pending"
            )
            val result = firebaseService.submitReport(user.uid, report)
            submissionSuccess.value = result.isSuccess
            isSubmitting.value = false
        }
    }
}
