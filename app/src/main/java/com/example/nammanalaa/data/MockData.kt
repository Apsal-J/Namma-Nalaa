package com.example.nammanalaa.data

import com.example.nammanalaa.model.*

object MockData {
    val officers = listOf(
        OfficerContact("Dr. Suresh Kumar", "Executive Engineer", "9845012345", "Chitradurga Division"),
        OfficerContact("Er. Lakshmi Devi", "Assistant Engineer", "9845054321", "Hiriyur Sector"),
        OfficerContact("Mr. Rajesh Gowda", "Section Officer", "9845098765", "Vani Vilas Area"),
        OfficerContact("Ms. Kavitha M.", "Junior Engineer", "9845022334", "Marikanave Zone")
    )

    val farmerIssues = listOf(
        Issue("101", "Ramesh Gowda", "Hiriyur Sector 4", "Breakage", "Main wall cracked at point B4. Water is seeping into nearby fields.", "High", "Pending", "2023-11-01"),
        Issue("102", "Ramesh Gowda", "Hiriyur Sector 2", "Blockage", "Heavy silt and plastic waste blocking the flow near the bridge.", "Medium", "In Progress", "2023-10-28")
    )

    val allIssues = listOf(
        Issue("101", "Ramesh Gowda", "Hiriyur Sector 4", "Breakage", "Main wall cracked at point B4", "High", "Pending", "2023-11-01"),
        Issue("102", "Ramesh Gowda", "Hiriyur Sector 2", "Blockage", "Heavy silt blocking the flow", "Medium", "In Progress", "2023-10-28"),
        Issue("103", "Siddappa", "Marikanave Zone A", "Leak", "Small leak near the gate valve.", "Low", "Resolved", "2023-10-25"),
        Issue("104", "Mallesh", "Challakere East", "Breakage", "Canal lining damaged by tree roots.", "High", "Pending", "2023-11-02"),
        Issue("105", "Raju K.", "Vani Vilas Area", "Blockage", "Animal carcass blocking the small distributary.", "Critical", "Resolved", "2023-10-30")
    )

    val notifications = listOf(
        Notification(1, "Status Update", "Your report #102 has been moved to 'In Progress'.", "10 mins ago"),
        Notification(2, "Water Advisory", "Water level expected to rise by 0.5m tomorrow.", "1 hour ago"),
        Notification(3, "Maintenance Notice", "Gate 4 cleaning scheduled for next Monday.", "Yesterday")
    )

    val feedItems = listOf(
        FeedItem(1, "Marikanave", "New lining work started in Zone A.", "2h ago", "Officer Kumar"),
        FeedItem(2, "Vani Vilas", "Community cleaning drive successful.", "5h ago", "Farmer Raju")
    )
}
