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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import kotlinx.coroutines.launch

// Main Composable for the Football Team Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoiBongScreen(
    navController : NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
) { // Đổi tên từ FootballTeamScreen thành DoiBong
    // List of football teams (sample data)
    val currentMuaGiai by viewModel.currentMuaGiai.collectAsState()
    var doiBongs by remember { mutableStateOf(listOf<DoiBong>()) }

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            if (currentMuaGiai == null) {
                doiBongs = viewModel.doiBongDAO.selectAllDoiBong();
                val sanNha = viewModel.sanNhaDAO.selectAllSanNha();
                for (doiBong in doiBongs) {
                    doiBong.tenSan = sanNha.find { doiBong.maSan == it.maSan }!!.tenSan;
                }
            }
            else {
                doiBongs = viewModel.doiBongDAO.selectDoiBongMuaGiai(currentMuaGiai!!.maMG);
                val sanNha = viewModel.sanNhaDAO.selectSanNhaMaMG(currentMuaGiai!!.maMG);
                for (doiBong in doiBongs) {
                    doiBong.tenSan = sanNha.find { doiBong.maSan == it.maSan }!!.tenSan;
                }

            }
        }
    }
    // Main screen layout
    Scaffold(
        floatingActionButton = {
            AddFloatingButton("Tạo đội bóng", onClick = { navController.navigate("doiBongInput") })
        },
        containerColor = Color(0xFF121212), // Đặt màu nền trực tiếp, đồng nhất với các màn hình khác
        modifier = modifier
    ) { it
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                items(doiBongs) { doiBong ->
                    TeamCard(
                        team = doiBong,
                        onClick = {
                            navController.navigate("cauThu/${doiBong.maDoi}");
                        })
                }
            }
        }
    }
}

// Composable for a single Team Card
@Composable
fun TeamCard(team: DoiBong, onClick : () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E) // Thay MaterialTheme.colorScheme.surface, đồng nhất với MuaGiai.kt
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = team.tenDoi,
                color = Color.White, // Thay MaterialTheme.colorScheme.onSurface
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = team.tenSan,
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
        DoiBongScreen(rememberNavController())
    }
}