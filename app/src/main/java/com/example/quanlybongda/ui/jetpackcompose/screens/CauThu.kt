package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlybongda.R
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme

// Thêm import cần thiết để lấy chiều cao thanh trạng thái
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars

// Data class for Player information
data class Player(
    val id: Int,
    val name: String,
    val goals: Int,
    val position: String,
    val birthDate: String,
    val notes: String
)

// Main Composable for the Player List Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CauThu() {
    // List of players (sample data)
    val players = remember {
        listOf(
            Player(id = 1, name = "Craig Vetrovs", goals = 24, position = "Forward", birthDate = "01/01/1990", notes = "Good performer"),
            Player(id = 2, name = "John Smith", goals = 18, position = "Midfielder", birthDate = "15/05/1992", notes = "Team captain"),
            Player(id = 3, name = "Mike Johnson", goals = 12, position = "Defender", birthDate = "22/09/1995", notes = "Strong defense")
        )
    }

    // Lấy chiều cao của thanh trạng thái
    val density = LocalDensity.current
    val statusBarHeight = with(density) {
        WindowInsets.statusBars.getTop(density).toDp()
    }

    // Khoảng cách bổ sung giữa thanh trạng thái và giao diện
    val additionalSpacing = 16.dp

    Scaffold(
        containerColor = Color(0xFF1C1D2B)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1C1D2B))
                .padding(top = statusBarHeight + additionalSpacing)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header với icon Back và Search
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* Handle back click */ }
                )
                Text(
                    text = "Cầu Thủ",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* Handle search click */ }
                )
            }

            // Danh sách cầu thủ
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(players) { player ->
                    PlayerCard(player = player)
                }
            }
        }
    }
}

// Composable for a single Player Card
@Composable
fun PlayerCard(player: Player) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF242539)
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
            verticalAlignment = Alignment.Top
        ) {
            // Phần bên trái: Mã cầu thủ, Tên cầu thủ, Goals, Số bàn thắng
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(end = 16.dp), // Tăng khoảng cách với phần bên phải
                horizontalAlignment = Alignment.Start
            ) {
                // Mã cầu thủ (trong vòng tròn tím)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF9C27B0), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${player.id}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Tên cầu thủ
                Text(
                    text = player.name,
                    style = TextStyle(
                        fontSize = 14.02.sp,
                        lineHeight = 24.53.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.26.sp
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Nhãn "Goal"
                Text(
                    text = "Goal",
                    style = TextStyle(
                        fontSize = 8.76.sp,
                        lineHeight = 9.64.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF797979),
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Số bàn thắng (Goals)
                Text(
                    text = "${player.goals}",
                    style = TextStyle(
                        fontSize = 21.03.sp,
                        lineHeight = 14.71.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFD2B5FF),
                        textAlign = TextAlign.Start
                    )
                )
            }

            // Phần bên phải: Position, Birth day, Note
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 16.dp, top = 36.dp), // Tăng padding(start) và top để đẩy xuống dưới
                horizontalAlignment = Alignment.Start
            ) {
                // Vị trí (Player Type)
                Text(
                    text = player.position,
                    style = TextStyle(
                        fontSize = 14.02.sp,
                        lineHeight = 24.53.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.26.sp
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Ngày sinh (Birth day)
                Text(
                    text = player.birthDate,
                    style = TextStyle(
                        fontSize = 14.02.sp,
                        lineHeight = 24.53.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.26.sp
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Ghi chú (Note)
                Text(
                    text = player.notes,
                    style = TextStyle(
                        fontSize = 14.02.sp,
                        lineHeight = 24.53.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.26.sp
                    )
                )
            }
        }
    }
}

// Preview function for Android Studio
@Preview(showBackground = true, widthDp = 360, heightDp = 720, backgroundColor = 0xFF1C1D2B)
@Composable
fun PreviewCauThu() {
    QuanLyBongDaTheme {
        CauThu()
    }
}