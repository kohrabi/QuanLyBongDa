package com.example.goalscored // Hoặc package của bạn

// Import cho Jetpack Compose cơ bản
import androidx.compose.foundation.Image // Bạn có dùng Image ở đâu đó không? Nếu không thì có thể bỏ.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme // QUAN TRỌNG cho styling
import androidx.compose.material.Text
import androidx.compose.material.Scaffold // Nếu bạn dùng Scaffold (tôi sẽ thêm vào)
import androidx.compose.material.TopAppBar // Nếu bạn dùng TopAppBar
import androidx.compose.material.BottomNavigation // Nếu bạn dùng BottomNavigation
import androidx.compose.material.BottomNavigationItem // Nếu bạn dùng BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Ví dụ icon
import androidx.compose.material.icons.filled.Search   // Ví dụ icon
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.* // QUAN TRỌNG
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // QUAN TRỌNG cho Coil
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Import cho Coil (thư viện tải ảnh)
import coil.compose.AsyncImage // << IMPORT QUAN TRỌNG
import coil.request.ImageRequest // << IMPORT QUAN TRỌNG

// Màu sắc (giữ nguyên hoặc điều chỉnh)
val gsScreenBackground = Color(0xFF0A0F24)
val gsPlayerCardBackground = Color(0xFF1B2038) // Nền cho card Frenkie De Jong
val gsTeamRowBackground = Color(0xFF1B2038)   // Nền cho các TeamRow
val gsListBackground = Color(0xFF0A0F24)    // Nền cho Column chứa danh sách cầu thủ (nếu khác screenBg)
val gsTextColorWhite = Color.White
val gsTextColorMuted = Color(0xFFDADADA)
val gsScoreColor = Color(0xFFFF9E0D)
val gsFullTimeColor = Color(0xFF4CFF89)

// URL Placeholder - BẠN CẦN THAY THẾ
const val GS_BACKGROUND_URL = "https://i.imgur.com/your_background_football_stadium.png"
const val GS_FCB_LOGO_URL = "https://i.imgur.com/nWgD6YI.png"
const val GS_MANCITY_LOGO_URL = "https://i.imgur.com/yiLJgk9.png"


@Composable
fun GoalsScoredScreen() {
    val context = LocalContext.current

    // Sử dụng Scaffold để có cấu trúc TopBar và BottomBar (nếu cần)
    // Nếu màn hình này không có BottomBar, bạn có thể dùng Box hoặc Column thường.
    // Giả sử theo thiết kế gần nhất bạn muốn có TopBar cho "Goals Scored"
    Scaffold(
        backgroundColor = gsScreenBackground,
        topBar = { GoalsScoredTopBarContent() } // Tạo hàm riêng cho TopBar
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding) // Áp dụng padding từ Scaffold
                .fillMaxSize()
            // .background(gsScreenBackground) // Nền đã được đặt ở Scaffold
        ) {
            // Ảnh nền (nếu có)
            AsyncImage(
                model = ImageRequest.Builder(context).data(GS_BACKGROUND_URL).crossfade(true).build(),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.2f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp) // Padding ngang cho toàn bộ nội dung cuộn
            ) {
                // Khoảng cách từ mép trên (sau TopBar) xuống nội dung đầu tiên
                Spacer(modifier = Modifier.height(16.dp)) // Giảm bớt vì TopBar đã có padding

                PlayerInfoCard( // Đổi tên từ PlayerCard cho rõ hơn
                    context = context,
                    team = "Barcelona",
                    name = "Frenkie De Jong",
                    score = "9"
                    // playerImageUrl = "URL_ANH_FRENKIE_DE_JONG" // Nếu có ảnh cầu thủ
                )

                Spacer(modifier = Modifier.height(20.dp))

                TeamScoreRow( // Đổi tên từ TeamRow
                    context = context,
                    teamName = "Manchester City",
                    score = "2",
                    logoUrl = GS_MANCITY_LOGO_URL
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List of Players
                GoalScorerRow("Cooper Calzoni", "4") // Đổi tên từ PlayerRow
                GoalScorerRow("Alfredo Saris", "4")
                GoalScorerRow("Jakob Levin", "4")
                GoalScorerRow("Alfonso Kenter", "3")
                GoalScorerRow("Emerson Septimus", "3")
                GoalScorerRow("Brandon Vaccaro", "2")

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun GoalsScoredTopBarContent() { // Composable riêng cho nội dung TopBar
    // Bạn có thể dùng TopAppBar của Material nếu muốn hiệu ứng cuộn, title tự động, etc.
    // Ở đây làm Row đơn giản như code gốc của bạn
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp), // Padding cho TopBar
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* TODO: Handle back action */ }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
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
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { /* TODO: Handle search action */ }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = gsTextColorWhite
            )
        }
    }
}

@Composable
fun PlayerInfoCard(context: android.content.Context, team: String, name: String, score: String, playerImageUrl: String? = null) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp) // Giảm padding ngang của card để tổng thể không quá rộng
            .clip(RoundedCornerShape(20.dp))
            .background(gsPlayerCardBackground)
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(), // Row chiếm hết chiều rộng của Box
            horizontalArrangement = if (playerImageUrl != null) Arrangement.Start else Arrangement.Center
        ) {
            if (playerImageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(playerImageUrl).crossfade(true).build(),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                horizontalAlignment = if (playerImageUrl == null) Alignment.CenterHorizontally else Alignment.Start,
                modifier = if (playerImageUrl == null) Modifier.fillMaxWidth() else Modifier // Để Column tự co giãn nếu có ảnh
            ) {
                Text(text = team, color = gsTextColorWhite.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(text = name, color = gsTextColorWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 2.dp, bottom = 4.dp))
                Text(text = "Goals Score", color = gsTextColorMuted, fontSize = 11.sp)
                Text(text = score, color = gsScoreColor, fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

@Composable
fun TeamScoreRow(context: android.content.Context, teamName: String, score: String, logoUrl: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp), // Điều chỉnh padding cho TeamRow
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(logoUrl).crossfade(true).build(),
            contentDescription = "$teamName Logo",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Text(
            text = teamName,
            color = gsTextColorWhite,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 8.dp)
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
fun GoalScorerRow(name: String, goals: String) { // Đổi tên từ PlayerRow cho rõ nghĩa
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp), // Điều chỉnh padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // Placeholder avatar
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF414158))
        )
        Text(
            text = name,
            color = gsTextColorWhite,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 8.dp)
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
    MaterialTheme {
        GoalsScoredScreen()
    }
}