package com.example.quanlybongda.ui.jetpackcompose.screens // Giữ nguyên package của bạn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size

import com.example.quanlybongda.R // << QUAN TRỌNG: Import lớp R của dự án bạn

// Màu sắc dựa trên thiết kế image_5022a5.png
val darkScreenBackground = Color(0xFF1E1E2C)
val scoreColor = Color(0xFFE0FF00)
val fullTimeColor = Color(0xFF4CFF89)
val textWhiteColor = Color.White
val textMutedColor = Color(0xFFA0A3BD)

// URL Placeholder - BẠN CẦN THAY THẾ BẰNG URL THỰC TẾ
const val BACKGROUND_IMAGE_URL = "https://i.imgur.com/your_background_image.png"
const val FCB_LOGO_URL = "https://i.imgur.com/nWgD6YI.png"
const val MANCITY_LOGO_URL1 = "https://i.imgur.com/yiLJgk9.png"


@Composable
fun FinalScoreScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkScreenBackground)
    ) {
        Image(
            painter = rememberAsyncImagePainter(BACKGROUND_IMAGE_URL),
            contentDescription = "Background Stadium",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* TODO: Handle back action */ }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textWhiteColor
                    )
                }
                Text(
                    text = "Final Score",
                    color = textWhiteColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = { /* TODO: Handle info action */ }) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Information",
                        tint = textWhiteColor
                    )
                }
            }

            // Khoảng cách giữa TopBar và Full Time
            Spacer(modifier = Modifier.height(30.dp)) // << SỬA: Tăng khoảng cách

            // Full Time
            Text(
                text = "Full Time",
                color = fullTimeColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Khoảng cách giữa Full Time và Khu vực tỷ số
            Spacer(modifier = Modifier.height(20.dp)) // << SỬA: Tăng khoảng cách

            // Team Logos and Score
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // << SỬA: Thêm padding dọc để "to hơn"
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.logo_barca),
                    contentDescription = "FCB Logo",
                    modifier = Modifier.size(70.dp) // << SỬA: Có thể tăng kích thước logo nếu muốn vùng này to hơn
                )
                Text(
                    text = "2 - 2",
                    color = scoreColor,
                    fontSize = 40.sp,    // << SỬA: Tăng kích thước tỷ số nếu muốn
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.logo_barca),
                    contentDescription = "ManCity Logo",
                    modifier = Modifier.size(70.dp) // << SỬA: Có thể tăng kích thước logo
                )
            }

            // Khoảng cách giữa Khu vực tỷ số và Statistic Title
            Spacer(modifier = Modifier.height(32.dp)) // << SỬA: Tăng khoảng cách

            // Statistic Title
            Text(
                text = "Statistic Match",
                color = textWhiteColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Match statistics list
            val stats = listOf(
                Triple("L", "Depay", "66’"),
                Triple("R", "Depay", "73’"),
                Triple("R", "Depay", "76’"), // Đổi "L" thành "R" cho giống ảnh thiết kế
                Triple("L", "Depay", "79’"),
                Triple("R", "Depay", "89’")
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                stats.forEach { (side, player, time) ->
                    // Sử dụng một action placeholder, bạn có thể thay đổi nếu cần
                    StatisticRowUpdated(side = side, player = player, action = "A", time = time)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatisticRowUpdated(side: String, player: String, action: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp), // Tăng padding dọc cho mỗi dòng thống kê
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sử dụng weight để chia đều không gian hơn, thay vì width cố định cho tất cả trừ player
        // Hoặc đặt width cố định nhưng đảm bảo tổng không vượt quá và căn chỉnh đúng
        Text(
            text = side,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.15f), // Cột Side
            textAlign = TextAlign.Center
        )
        Text(
            text = player,
            color = textWhiteColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.weight(0.5f).padding(start = 8.dp) // Cột Player, chiếm nhiều không gian nhất
        )
        Text(
            text = action,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.15f), // Cột Action
            textAlign = TextAlign.Center
        )
        Text(
            text = time,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.2f), // Cột Time
            textAlign = TextAlign.End
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF1E1E2C)
@Composable
fun FinalScoreScreenPreview() {
    MaterialTheme {
        FinalScoreScreen()
    }
}