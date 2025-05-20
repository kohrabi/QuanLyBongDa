package com.example.quanlybongda.ui.jetpackcompose.screens


import androidx.compose.foundation.Image // Giữ lại nếu bạn có dùng Image với painterResource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.* // Material 2 components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home // Icon cho BottomBar
import androidx.compose.material.icons.filled.List // Icon cho BottomBar (ví dụ cho Ranking/Standings)
import androidx.compose.material.icons.filled.Schedule // Icon cho BottomBar (ví dụ cho Schedule)
import androidx.compose.material.icons.filled.Person // Icon cho BottomBar
// Bỏ các import không cần thiết hoặc đã được thay thế
// import com.skydoves.landscapist.ImageOptions // Không dùng nữa
// import com.skydoves.landscapist.coil3.CoilImage // Không dùng nữa
import androidx.compose.runtime.* // Thêm lại các import cơ bản nếu thiếu
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Sử dụng AsyncImage của Coil

import coil.request.ImageRequest


// Màu sắc (giữ nguyên hoặc điều chỉnh)
val standingsScreenBackground = Color(0xFF0D0D12) // Màu nền tối hơn theo image_5afbae.png
val standingsContentBg = Color(0xFF181A20) // Màu nền cho phần nội dung chính
val standingsCardBg = Color(0xFF222232) // Màu nền cho thẻ bảng xếp hạng
val standingsTextWhite = Color.White
val standingsTextAccent = Color(0xFFD1B4FF)
val standingsTextMuted = Color(0xFFA0A3BD)

// URL Placeholder - BẠN CẦN THAY THẾ
const val BACKGROUND_IMAGE_URL_STANDINGS = "https://i.imgur.com/your_stadium_background.png" // Thay bằng ảnh nền sân vận động mờ
const val ARSENAL_LOGO_URL = "https://i.imgur.com/1ZQZ1Zm.png"
const val MANCITY_LOGO_URL = "https://i.imgur.com/HhJY7Zm.png"
const val LEEDS_LOGO_URL = "https://i.imgur.com/UKY1ZmX.png"
const val TOTTENHAM_LOGO_URL = "https://i.imgur.com/WfJH7Zm.png"
const val BRIGHTON_LOGO_URL = "https://i.imgur.com/3YkH7Zm.png"

@Composable
fun BaoCao() {
    val context = LocalContext.current

    Scaffold( // SỬ DỤNG SCAFFOLD
        backgroundColor = standingsScreenBackground, // Màu nền tối cho toàn bộ màn hình
        topBar = {
            StandingsTopAppBar() // TopAppBar chứa tiêu đề "Standings"
        },
        bottomBar = {
            StandingsBottomNavigationBar() // BottomBar chứa các icon điều hướng
        }
    ) { innerPadding -> // innerPadding được cung cấp bởi Scaffold
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) { // Box chứa ảnh nền và nội dung
            // Ảnh nền (nếu có)
            AsyncImage(
                model = ImageRequest.Builder(context).data(BACKGROUND_IMAGE_URL_STANDINGS).crossfade(true).build(),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.15f // Làm mờ ảnh nền đi nhiều
            )

            // Column nội dung chính, có thể cuộn
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp) // Padding ngang cho nội dung
                    .verticalScroll(rememberScrollState())
            ) {
                // Khoảng cách từ TopAppBar xuống nội dung
                Spacer(modifier = Modifier.height(24.dp)) // << TĂNG KHOẢNG CÁCH NÀY

                // Card container cho bảng xếp hạng
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)) // Bo góc cho card
                        .background(standingsContentBg.copy(alpha = 0.7f)) // Nền tối hơn, hơi trong suốt cho card
                        .padding(16.dp)
                ) {
                    // Header row: "Table Standings" và "See All"
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Table Standings",
                            color = standingsTextWhite,
                            fontSize = 16.sp, // Tăng font một chút
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "See All",
                            color = standingsTextAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Tăng khoảng cách

                    // Tiêu đề các cột của bảng
                    StandingsListHeader() // Đổi tên hàm cho rõ ràng

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dữ liệu các đội
                    val teams = listOf(
                        Team("Arsenal", ARSENAL_LOGO_URL, 3, 0, 0, 9, League.CHAMPIONS),
                        Team("Man City", MANCITY_LOGO_URL, 2, 1, 0, 7, League.CHAMPIONS),
                        Team("Leeds United", LEEDS_LOGO_URL, 2, 1, 0, 7, League.CHAMPIONS),
                        Team("Tottenham", TOTTENHAM_LOGO_URL, 1, 2, 0, 5, League.EUROPA),
                        Team("Brighton", BRIGHTON_LOGO_URL, 1, 1, 1, 4, League.EUROPA)
                    )

                    teams.forEachIndexed { index, team ->
                        StandingsListRow(context = context, team = team) // Đổi tên hàm
                        if (index < teams.lastIndex) { // Thêm đường kẻ mờ giữa các hàng
                            Divider(color = standingsTextMuted.copy(alpha = 0.2f), thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Chú thích (Legend)
                    LeagueLegendStandings() // Đổi tên hàm
                } // Kết thúc Card container
                Spacer(modifier = Modifier.height(16.dp)) // Khoảng trống ở cuối nội dung cuộn
            } // Kết thúc Column nội dung chính
        } // Kết thúc Box chứa ảnh nền và nội dung
    } // Kết thúc Scaffold
}

@Composable
fun StandingsTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Standings",
                color = standingsTextWhite,
                fontSize = 18.sp, // Có thể tăng nếu muốn
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, // Căn giữa tiêu đề
                modifier = Modifier.fillMaxWidth() // Cho Text chiếm hết chiều rộng để căn giữa hoạt động
            )
        },
        backgroundColor = Color.Transparent, // Nền trong suốt để thấy ảnh nền/màu nền của Scaffold
        elevation = 0.dp, // Không có bóng đổ
        modifier = Modifier.padding(top = 8.dp) // Đẩy TopAppBar xuống một chút nếu hệ thống tự thêm padding
    )
}

@Composable
fun StandingsListHeader() { // Đổi tên từ StandingsTableHeader
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp), // Padding dưới cho header
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Spacer để căn chỉnh với chấm tròn và logo ở các hàng dữ liệu
        Spacer(modifier = Modifier.width(6.dp + 8.dp + 20.dp + 8.dp)) // Kích thước chấm + padding + logo + padding
        Text("Club", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(2.2f)) // Điều chỉnh weight
        Text("W", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("D", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("L", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("Poin", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)

    }
}

@Composable

fun StandingsListRow(context: android.content.Context, team: Team) { // Đổi tên từ TeamDataRow
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp) // Giảm padding dọc chút
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    color = if (team.league == League.CHAMPIONS) Color(0xFF5C7CFA) else Color(0xFFFF9F1C),
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(context).data(team.logoUrl).crossfade(true).build(),
            contentDescription = "${team.name} Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(20.dp) // Kích thước logo
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = team.name,
            color = standingsTextWhite,
            fontSize = 13.sp, // Tăng một chút
            fontWeight = FontWeight.SemiBold, // Đậm hơn chút
            modifier = Modifier.weight(2.2f)
        )
        Text("${team.w}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.d}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.l}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.points}", color = standingsTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)

    }
}

@Composable

fun LeagueLegendStandings() { // Đổi tên từ LeagueLegend
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 12.dp) // Giảm padding top
    ) {
        Box(modifier = Modifier.size(6.dp).background(Color(0xFF5C7CFA), shape = CircleShape))
        Text(" UEFA Champions League", color = standingsTextMuted, fontSize = 10.sp) // Màu chữ phụ
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.size(6.dp).background(Color(0xFFFF9F1C), shape = CircleShape))
        Text(" Europa League", color = standingsTextMuted, fontSize = 10.sp) // Màu chữ phụ
    }
}

@Composable
fun StandingsBottomNavigationBar() { // Bottom Navigation Bar
    // Giả sử bạn muốn dùng Material 2 BottomNavigation
    var selectedItem by remember { mutableStateOf(0) } // 0 là Home, 1 là Standings (ví dụ)
    val items = listOf(
        "Home" to Icons.Filled.Home,
        "Standings" to Icons.Filled.List, // Icon cho Bảng xếp hạng
        "Schedule" to Icons.Filled.Schedule,
        "Profile" to Icons.Filled.Person
    )

    BottomNavigation(
        backgroundColor = standingsContentBg.copy(alpha = 0.9f), // Màu nền cho BottomNav
        contentColor = standingsTextMuted // Màu mặc định cho icon không được chọn
    ) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(item.second, contentDescription = item.first) },
                selected = selectedItem == index,
                onClick = { selectedItem = index /* TODO: Handle navigation */ },
                selectedContentColor = Color.White, // Màu icon được chọn
                unselectedContentColor = standingsTextMuted
            )
        }
    }
}


// Data class và Enum (giữ nguyên)
data class Team(
    val name: String,
    val logoUrl: String,
    val w: Int,
    val d: Int,
    val l: Int,
    val points: Int,
    val league: League
)
enum class League { CHAMPIONS, EUROPA }

@Preview(showBackground = true, backgroundColor = 0xFF0D0D12)
@Composable
fun BaoCaoScreenPreview() {
    MaterialTheme { // Sử dụng MaterialTheme hoặc theme của bạn
        BaoCao()

    }
}