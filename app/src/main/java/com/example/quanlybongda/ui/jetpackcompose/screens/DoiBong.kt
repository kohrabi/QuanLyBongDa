///*
//package com.example.quanlybongda.ui.jetpackcompose.screens
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//// Data class for Football Team information
//data class FootballTeam(
//    val name: String,
//    val yard: String
//)
//
//// Main Composable for the Football Team Screen
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FootballTeamScreen() {
//    // List of football teams (sample data)
//    val teams = remember {
//        listOf(
//            FootballTeam(name = "TEAM NAME", yard = "YARD"),
//            FootballTeam(name = "TEAM NAME", yard = "YARD"),
//            FootballTeam(name = "TEAM NAME", yard = "YARD")
//        )
//    }
//
//    // Main screen layout
//    Scaffold(
//        containerColor = MaterialTheme.colorScheme.background
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(MaterialTheme.colorScheme.background)
//                .padding(horizontal = 16.dp), // Padding ngang cho toàn bộ nội dung
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Header with Add and Search icons
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp), // Khoảng cách từ trên xuống
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Thêm đội bóng",
//                    tint = Color.White,
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clickable { */
///* Handle add team click *//*
// }
//                )
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Tìm kiếm",
//                    tint = Color.Gray,
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clickable { */
///* Handle search click *//*
// }
//                )
//            }
//
//            // Spacer to push the title down (matching the image)
//            Spacer(modifier = Modifier.height(80.dp)) // Khoảng cách lớn để đẩy tiêu đề xuống
//
//            // Title "FOOTBALL TEAM"
//            Text(
//                text = "FOOTBALL TEAM",
//                color = MaterialTheme.colorScheme.onBackground,
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentWidth(Alignment.CenterHorizontally)
//            )
//
//            // Spacer between title and team list
//            Spacer(modifier = Modifier.height(24.dp)) // Khoảng cách giữa tiêu đề và danh sách
//
//            // List of Team Cards
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các card
//            ) {
//                items(teams) { team ->
//                    TeamCard(team = team)
//                }
//            }
//        }
//    }
//}
//
//// Composable for a single Team Card
//@Composable
//fun TeamCard(team: FootballTeam) {
//    Card(
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface
//        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = team.name,
//                color = MaterialTheme.colorScheme.onSurface,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.SemiBold
//            )
//            Text(
//                text = team.yard,
//                color = Color.LightGray,
//                fontSize = 16.sp
//            )
//        }
//    }
//}
//
//// Preview function for Android Studio
//@Preview(showBackground = true, widthDp = 360, heightDp = 720)
//@Composable
//fun PreviewFootballTeamScreen() {
//    FootballAppTheme {
//        FootballTeamScreen()
//    }
//}
//
//// This is typically in your MainActivity.kt (optional, remove if already defined elsewhere)
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            FootballAppTheme {
//                FootballTeamScreen()
//            }
//        }
//    }
//}
//*/














package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme

// Data class for Football Team information
data class FootballTeam(
    val name: String,
    val yard: String
)

// Main Composable for the Football Team Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoiBong() { // Đổi tên từ FootballTeamScreen thành DoiBong
    // List of football teams (sample data)
    val teams = remember {
        listOf(
            FootballTeam(name = "TEAM NAME", yard = "YARD"),
            FootballTeam(name = "TEAM NAME", yard = "YARD"),
            FootballTeam(name = "TEAM NAME", yard = "YARD")
        )
    }

    // Main screen layout
    Scaffold(
        containerColor = Color(0xFF121212) // Đặt màu nền trực tiếp, đồng nhất với các màn hình khác
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF121212)) // Đặt màu nền trực tiếp
                .padding(horizontal = 16.dp), // Padding ngang cho toàn bộ nội dung
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with Add and Search icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Khoảng cách từ trên xuống
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Thêm đội bóng",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* Handle add team click */ }
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* Handle search click */ }
                )
            }

            // Spacer to push the title down (matching the image)
            Spacer(modifier = Modifier.height(80.dp)) // Khoảng cách lớn để đẩy tiêu đề xuống

            // Title "FOOTBALL TEAM"
            Text(
                text = "FOOTBALL TEAM",
                color = Color.White, // Thay MaterialTheme.colorScheme.onBackground
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            // Spacer between title and team list
            Spacer(modifier = Modifier.height(24.dp)) // Khoảng cách giữa tiêu đề và danh sách

            // List of Team Cards
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các card
            ) {
                items(teams) { team ->
                    TeamCard(team = team)
                }
            }
        }
    }
}

// Composable for a single Team Card
@Composable
fun TeamCard(team: FootballTeam) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E) // Thay MaterialTheme.colorScheme.surface, đồng nhất với MuaGiai.kt
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = team.name,
                color = Color.White, // Thay MaterialTheme.colorScheme.onSurface
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = team.yard,
                color = Color.LightGray,
                fontSize = 16.sp
            )
        }
    }
}

// Preview function for Android Studio
@Preview(showBackground = true, widthDp = 360, heightDp = 720, backgroundColor = 0xFF121212)
@Composable
fun PreviewDoiBong() { // Đổi tên từ PreviewFootballTeamScreen thành PreviewDoiBong
    QuanLyBongDaTheme {
        DoiBong()
    }
}