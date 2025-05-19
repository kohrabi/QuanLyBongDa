package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.Image // Giữ lại nếu bạn có dùng Image với painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme // Nên dùng MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Icon mũi tên quay lại
import androidx.compose.material.icons.filled.Search   // Icon kính lúp (Search)
// import androidx.compose.material.icons.filled.MoreVert // Không dùng MoreVert nữa
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // Cần cho Coil ImageRequest
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Sử dụng AsyncImage của Coil
import coil.request.ImageRequest // Sử dụng ImageRequest của Coil

// Màu sắc (bạn có thể tùy chỉnh cho khớp thiết kế)
val gsScreenBackground = Color(0xFF0A0F24)
val gsPlayerCardBackground = Color(0xFF1B2038)
val gsTextColorWhite = Color.White
val gsTextColorMuted = Color(0xFFDADADA)
val gsScoreColor = Color(0xFFFF9E0D)
val gsFullTimeColor = Color(0xFF4CFF89) // Màu từ code trước của bạn

// URL Placeholder - BẠN CẦN THAY THẾ
const val GS_BACKGROUND_URL = "https://i.imgur.com/your_background_football_stadium.png" // Thay bằng ảnh nền sân vận động mờ
const val GS_FCB_LOGO_URL = "https://i.imgur.com/nWgD6YI.png" // Placeholder logo Barca
const val GS_MANCITY_LOGO_URL = "https://i.imgur.com/yiLJgk9.png" // Placeholder logo ManCity

@Composable
fun GoalsScoredScreen() {
    val context = LocalContext.current // Lấy context cho Coil

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gsScreenBackground) // Màu nền chính cho toàn màn hình
    ) {
        // Ảnh nền (nếu có)
        AsyncImage(
            model = ImageRequest.Builder(context).data(GS_BACKGROUND_URL).crossfade(true).build(),
            contentDescription = "Background", // Thêm mô tả
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.2f // Làm mờ ảnh nền để nội dung nổi bật
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Cho phép cuộn toàn bộ nội dung
        ) {
            // Khoảng cách từ mép trên màn hình xuống TopBar
            Spacer(modifier = Modifier.height(24.dp)) // << ĐIỀU CHỈNH KHOẢNG CÁCH NÀY

            GoalsScoredTopBar() // TopBar đã được sửa

            Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa TopBar và PlayerCard

            PlayerCard(
                context = context, // Truyền context nếu PlayerCard dùng AsyncImage
                team = "Barcelona",
                name = "Frenkie De Jong",
                score = "9"
                // Thêm URL ảnh cầu thủ nếu PlayerCard hiển thị ảnh cầu thủ
                // playerImageUrl = "URL_ANH_FRENKIE_DE_JONG"
            )

            Spacer(modifier = Modifier.height(20.dp)) // Khoảng cách giữa PlayerCard và TeamRow

            TeamRow(
                context = context, // Truyền context
                teamName = "Manchester City",
                score = "2",
                logoUrl = GS_MANCITY_LOGO_URL // Sử dụng URL đã định nghĩa
            )

            Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách trước danh sách cầu thủ

            // List of Players
            // Bạn có thể tạo một data class cho Player và lặp qua danh sách
            PlayerRow("Cooper Calzoni", "4")
            PlayerRow("Alfredo Saris", "4")
            PlayerRow("Jakob Levin", "4")
            PlayerRow("Alfonso Kenter", "3")
            PlayerRow("Emerson Septimus", "3")
            PlayerRow("Brandon Vaccaro", "2")

            Spacer(modifier = Modifier.height(24.dp)) // Khoảng trống ở cuối
        }
    }
}

@Composable
fun GoalsScoredTopBar() { // Đổi tên để cụ thể hơn
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), // Giảm padding vertical của TopBar một chút
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* TODO: Handle back action */ }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack, // Sử dụng icon từ thư viện
                contentDescription = "Back",
                tint = gsTextColorWhite
            )
        }
        Text(
            text = "Goals Scored",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = gsTextColorWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f) // Để Text chiếm không gian còn lại và tự căn giữa
        )
        IconButton(onClick = { /* TODO: Handle search action */ }) {
            Icon(
                imageVector = Icons.Filled.Search, // << SỬA ICON THÀNH KÍNH LÚP
                contentDescription = "Search",   // << SỬA MÔ TẢ
                tint = gsTextColorWhite
            )
        }
    }
}

@Composable
fun PlayerCard(context: android.content.Context, team: String, name: String, score: String, playerImageUrl: String? = null) { // Thêm playerImageUrl tùy chọn
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp) // Giảm padding vertical
            .clip(RoundedCornerShape(20.dp)) // Bo góc lớn hơn một chút
            .background(gsPlayerCardBackground) // Màu nền cho card
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp), // Padding bên trong card
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) { // Sử dụng Row nếu muốn ảnh bên cạnh text
            // Ảnh cầu thủ (nếu có)
            if (playerImageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(playerImageUrl).crossfade(true).build(),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp) // Kích thước ảnh cầu thủ
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                horizontalAlignment = if (playerImageUrl == null) Alignment.CenterHorizontally else Alignment.Start,
                modifier = if (playerImageUrl == null) Modifier.fillMaxWidth() else Modifier
            ) {
                Text(
                    text = team,
                    color = gsTextColorWhite.copy(alpha = 0.8f), // Hơi mờ hơn
                    fontSize = 12.sp, // Nhỏ hơn
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = name,
                    color = gsTextColorWhite,
                    fontSize = 20.sp, // Giảm một chút
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                )
                Text(
                    text = "Goals Score",
                    color = gsTextColorMuted,
                    fontSize = 11.sp, // Nhỏ hơn
                )
                Text(
                    text = score,
                    color = gsScoreColor,
                    fontSize = 32.sp, // Tăng kích thước điểm số
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun TeamRow(context: android.content.Context, teamName: String, score: String, logoUrl: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp), // Điều chỉnh padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(logoUrl).crossfade(true).build(),
            contentDescription = "$teamName Logo", // Mô tả rõ hơn
            modifier = Modifier
                .size(36.dp) // Giảm kích thước logo một chút
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Text(
            text = teamName,
            color = gsTextColorWhite,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 8.dp) // Thêm padding end
        )
        Text(
            text = score,
            color = gsTextColorWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PlayerRow(name: String, goals: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 10.dp), // Tăng padding vertical
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Box tròn có thể là placeholder cho ảnh đại diện cầu thủ hoặc chỉ là dấu hiệu
        Box(
            modifier = Modifier
                .size(32.dp) // Giảm kích thước một chút
                .clip(CircleShape)
                .background(Color(0xFF414158)) // Giữ màu này hoặc thay đổi
        )
        Text(
            text = name,
            color = gsTextColorWhite,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 8.dp) // Thêm padding end
        )
        Text(
            text = goals,
            color = gsTextColorWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0F24)
@Composable
fun GoalsScoredScreenPreview() {
    MaterialTheme { // Nên bọc trong MaterialTheme hoặc Theme của bạn
        GoalsScoredScreen()
    }
}