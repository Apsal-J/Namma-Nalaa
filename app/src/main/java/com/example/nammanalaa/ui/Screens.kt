package com.example.nammanalaa.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.nammanalaa.model.*
import com.example.nammanalaa.viewmodel.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// Theme Colors
val PrimaryGreen = Color(0xFF2E7D32)
val LightGreen = Color(0xFFE8F5E9)
val Orange = Color(0xFFF57C00)
val BackgroundGray = Color(0xFFF8F9FA)
val AppGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1B5E20), Color(0xFF2E7D32), Color(0xFF4CAF50))
)

// --- Splash Screen ---
@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(AppGradient), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.WaterDrop, null, modifier = Modifier.size(120.dp), tint = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Namma-Nala", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("Smart Canal Health Monitor", style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.8f))
        }
    }
}

// --- Login Screen ---
@Composable
fun LoginScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(28.dp), verticalArrangement = Arrangement.Center) {
        Text("Welcome Back", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = PrimaryGreen)
        Text("Sign in to continue", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = email, 
            onValueChange = { email = it }, 
            label = { Text("Email") }, 
            modifier = Modifier.fillMaxWidth(), 
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Email, null, tint = PrimaryGreen) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password, 
            onValueChange = { password = it }, 
            label = { Text("Password") }, 
            visualTransformation = PasswordVisualTransformation(), 
            modifier = Modifier.fillMaxWidth(), 
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = PrimaryGreen) }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { mainViewModel.login(email, password) { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } } } },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(16.dp),
            enabled = !mainViewModel.isLoading.value
        ) {
            if (mainViewModel.isLoading.value) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp)) 
            else Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigate(Screen.Signup.route) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Don't have an account? Create one", color = PrimaryGreen, fontWeight = FontWeight.Medium)
        }
        mainViewModel.error.value?.let { Text(it, color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) }
    }
}

// --- Signup Screen ---
@Composable
fun SignupScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phno by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedRole by remember { mutableIntStateOf(0) }
    val roles = listOf("Farmer", "Officer")

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp).verticalScroll(rememberScrollState())) {
        IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = PrimaryGreen) }
        Text("Create Account", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Spacer(modifier = Modifier.height(24.dp))
        TabRow(selectedTabIndex = selectedRole, containerColor = LightGreen, contentColor = PrimaryGreen, modifier = Modifier.clip(RoundedCornerShape(12.dp))) {
            roles.forEachIndexed { index, title ->
                Tab(selected = selectedRole == index, onClick = { selectedRole = index }, text = { Text(title, fontWeight = FontWeight.Bold) })
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = phno, onValueChange = { phno = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                val user = User(name = name, email = email, phno = phno.toLongOrNull() ?: 0L, city = city, address = address, role = roles[selectedRole])
                mainViewModel.register(user, password) { navController.navigate(Screen.Dashboard.route) }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(16.dp)
        ) { Text("Create Account", fontWeight = FontWeight.Bold) }
    }
}

// --- Home (Dashboard) Screen ---
@Composable
fun DashboardScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val user = mainViewModel.user.value ?: return
    LaunchedEffect(Unit) { mainViewModel.fetchFarmerHomeData() }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(BackgroundGray).verticalScroll(rememberScrollState())) {
            // Header
            Box(modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)).background(AppGradient).padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Welcome,", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.titleMedium)
                        Text(user.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${user.city}, ${user.address}", color = Color.White, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Surface(modifier = Modifier.size(64.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.2f), border = BorderStroke(2.dp, Color.White.copy(alpha = 0.5f))) {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(12.dp))
                    }
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                // Quick Action
                Card(
                    onClick = { navController.navigate(Screen.ReportIssue.route) },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryGreen)
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AddCircle, null, tint = Color.White, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Report New Canal Issue", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Recent Reports
                Text("My Recent Reports", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                if (mainViewModel.recentReports.value.isEmpty()) {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Text("No reports found yet.", modifier = Modifier.padding(24.dp), color = Color.Gray)
                    }
                } else {
                    mainViewModel.recentReports.value.take(3).forEach { report ->
                        ReportItemCard(report)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Officer Contacts
                Text("Field Officer Contacts", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                if (mainViewModel.officers.value.isEmpty()) {
                    Text("Updating contact list...", color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                } else {
                    mainViewModel.officers.value.forEach { officer ->
                        OfficerContactCard(officer)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ReportItemCard(report: Report) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(report.issueType, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = PrimaryGreen)
                Surface(color = if(report.status == "Pending") Orange.copy(alpha = 0.1f) else PrimaryGreen.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                    Text(report.status, color = if(report.status == "Pending") Orange else PrimaryGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${report.area}, ${report.address}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(report.timestamp)), style = MaterialTheme.typography.labelSmall, color = Color.LightGray, modifier = Modifier.align(Alignment.End))
        }
    }
}

@Composable
fun OfficerContactCard(officer: User) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = LightGreen) {
                    Icon(Icons.Default.SupportAgent, null, modifier = Modifier.padding(12.dp), tint = PrimaryGreen)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(officer.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(officer.designation.ifEmpty { "Nala Field Officer" }, color = PrimaryGreen, style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = { /* Call Phone */ }, modifier = Modifier.background(LightGreen, CircleShape)) {
                    Icon(Icons.Default.Call, null, tint = PrimaryGreen)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Icon(Icons.Default.Business, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Division: ${officer.city}", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
            }
        }
    }
}

// --- Report Issue Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueScreen(navController: NavHostController, mainViewModel: MainViewModel, reportViewModel: ReportViewModel) {
    var type by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri = it }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("New Issue Report", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(BackgroundGray).padding(24.dp).verticalScroll(rememberScrollState())) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("What is the issue? (e.g. Leak)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = area, onValueChange = { area = it }, label = { Text("Exact Area Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Additional Details") }, modifier = Modifier.fillMaxWidth(), minLines = 3, shape = RoundedCornerShape(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Evidence Photo", fontWeight = FontWeight.Bold, color = PrimaryGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(20.dp)).background(Color.White).border(2.dp, PrimaryGreen, RoundedCornerShape(20.dp)).clickable { launcher.launch("image/*") }, contentAlignment = Alignment.Center) {
                if (imageUri == null) Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddAPhoto, null, tint = PrimaryGreen, modifier = Modifier.size(40.dp))
                    Text("Select Photo from Gallery", color = PrimaryGreen, style = MaterialTheme.typography.bodySmall)
                } else Text("Photo Selected ✅", color = PrimaryGreen, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { mainViewModel.user.value?.let { reportViewModel.submitReport(it, type, area, it.address, imageUri) } },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(20.dp),
                enabled = !reportViewModel.isSubmitting.value
            ) {
                if (reportViewModel.isSubmitting.value) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp)) 
                else Text("Submit Official Report", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            if (reportViewModel.submissionSuccess.value) {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Report submitted successfully!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }
    }
}

// --- Profile Screen ---
@Composable
fun ProfileScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val user = mainViewModel.user.value ?: return
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(BackgroundGray).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(modifier = Modifier.size(120.dp), shape = CircleShape, color = LightGreen, border = BorderStroke(3.dp, PrimaryGreen)) {
                Icon(Icons.Default.Person, null, modifier = Modifier.padding(24.dp).fillMaxSize(), tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(user.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(user.role.uppercase(), color = PrimaryGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
            
            Spacer(modifier = Modifier.height(40.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileInfoRow(Icons.Default.Email, "Email Address", user.email)
                    ProfileInfoRow(Icons.Default.Phone, "Mobile Number", user.phno.toString())
                    ProfileInfoRow(Icons.Default.LocationCity, "Base City", user.city)
                    ProfileInfoRow(Icons.Default.Home, "Residential Address", user.address)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { navController.navigate(Screen.Login.route) { popUpTo(0) } }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)), shape = RoundedCornerShape(16.dp)) {
                Text("Logout Account", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = PrimaryGreen, modifier = Modifier.size(24.dp).background(LightGreen, CircleShape).padding(4.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            selected = currentRoute == Screen.Dashboard.route,
            onClick = { navController.navigate(Screen.Dashboard.route) { launchSingleTop = true } },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryGreen, selectedTextColor = PrimaryGreen, indicatorColor = LightGreen)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircle, null) },
            label = { Text("Report") },
            selected = currentRoute == Screen.ReportIssue.route,
            onClick = { navController.navigate(Screen.ReportIssue.route) { launchSingleTop = true } },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryGreen, selectedTextColor = PrimaryGreen, indicatorColor = LightGreen)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") },
            selected = currentRoute == Screen.Profile.route,
            onClick = { navController.navigate(Screen.Profile.route) { launchSingleTop = true } },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryGreen, selectedTextColor = PrimaryGreen, indicatorColor = LightGreen)
        )
    }
}
