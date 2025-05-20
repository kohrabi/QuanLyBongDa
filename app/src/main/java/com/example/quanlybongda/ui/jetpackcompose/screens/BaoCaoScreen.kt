package com.example.quanlybongda.ui.jetpackcompose.screens

// Các import của bạn giữ nguyên, đảm bảo có:
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // Sẽ cần nếu dùng Image composable với drawable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Sử dụng AsyncImage của Coil
import coil.request.ImageRequest
import com.example.quanlybongda.R // Quan trọng: Import R để truy cập resources

// Màu sắc (giữ nguyên hoặc điều chỉnh)
val standingsScreenBackground = Color(0xFF0D0D12)
val standingsContentBg = Color(0xFF181A20)
val standingsCardBg = Color(0xFF222232)
val standingsTextWhite = Color.White
val standingsTextAccent = Color(0xFFD1B4FF)
val standingsTextMuted = Color(0xFFA0A3BD)

// KHÔNG CẦN URL PLACEHOLDER NỮA

@Composable
fun BaoCao() {
    val context = LocalContext.current

    Scaffold(
        backgroundColor = standingsScreenBackground,
        topBar = {
            StandingsTopAppBar()
        },
        bottomBar = {
            StandingsBottomNavigationBar()
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Ảnh nền từ drawable
            AsyncImage(
                // model = ImageRequest.Builder(context).data(R.drawable.your_stadium_background_drawable_name).crossfade(true).build(),
                // Hoặc đơn giản hơn nếu chỉ cần crossfade:
                model = R.drawable.football_stadium, // << THAY THẾ TÊN FILE DRAWABLE CỦA BẠN
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.3f, // Giữ nguyên hoặc điều chỉnh alpha
                // Thêm crossfade nếu muốn hiệu ứng mờ dần khi tải
                // Để dùng crossfade trực tiếp với AsyncImage, Coil phiên bản mới có thể hỗ trợ tham số crossfade
                // Hoặc giữ ImageRequest.Builder nếu bạn cần nhiều tùy chỉnh hơn:
                // model = ImageRequest.Builder(context)
                //    .data(R.drawable.stadium_background) // << THAY THẾ TÊN FILE DRAWABLE
                //    .crossfade(true)
                //    .build(),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(standingsContentBg.copy(alpha = 0.7f))
                        .padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Table Standings",
                            color = standingsTextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "See All",
                            color = standingsTextAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    StandingsListHeader()
                    Spacer(modifier = Modifier.height(12.dp))

                    // Dữ liệu các đội - THAY THẾ URL BẰNG RESOURCE ID
                    val teams = listOf(
                        Team("Arsenal", R.drawable.arsenal_logo, 3, 0, 0, 9, League.CHAMPIONS), // << THAY TÊN FILE
                        Team("Man City", R.drawable.mancity_logo, 2, 1, 0, 7, League.CHAMPIONS), // << THAY TÊN FILE
                        Team("Leeds United", R.drawable.leeds_united_logo, 2, 1, 0, 7, League.CHAMPIONS), // << THAY TÊN FILE
                        Team("Tottenham", R.drawable.tottenham_logo, 1, 2, 0, 5, League.EUROPA), // << THAY TÊN FILE
                        Team("Brighton", R.drawable.brighton_logo, 1, 1, 1, 4, League.EUROPA) // << THAY TÊN FILE
                    )

                    teams.forEachIndexed { index, team ->
                        StandingsListRow(context = context, team = team)
                        if (index < teams.lastIndex) {
                            Divider(color = standingsTextMuted.copy(alpha = 0.2f), thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    LeagueLegendStandings()
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StandingsTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Standings",
                color = standingsTextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun StandingsListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(6.dp + 8.dp + 20.dp + 8.dp))
        Text("Club", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(2.2f))
        Text("W", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("D", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("L", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("Poin", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
    }
}

@Composable
fun StandingsListRow(context: android.content.Context, team: Team) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
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
            // model = ImageRequest.Builder(context).data(team.logoResId).crossfade(true).build(),
            // Hoặc đơn giản hơn:
            model = team.logoResId, // << SỬ DỤNG RESOURCE ID TỪ TEAM
            contentDescription = "${team.name} Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(20.dp)
            // Thêm crossfade nếu muốn, tương tự như ảnh nền
            // model = ImageRequest.Builder(context)
            //    .data(team.logoResId)
            //    .crossfade(true)
            //    .build(),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = team.name,
            color = standingsTextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(2.2f)
        )
        Text("${team.w}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.d}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.l}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.points}", color = standingsTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
    }
}

@Composable
fun LeagueLegendStandings() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 12.dp)
    ) {
        Box(modifier = Modifier.size(6.dp).background(Color(0xFF5C7CFA), shape = CircleShape))
        Text(" UEFA Champions League", color = standingsTextMuted, fontSize = 10.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.size(6.dp).background(Color(0xFFFF9F1C), shape = CircleShape))
        Text(" Europa League", color = standingsTextMuted, fontSize = 10.sp)
    }
}

@Composable
fun StandingsBottomNavigationBar() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        "Home" to Icons.Filled.Home,
        "Standings" to Icons.Filled.List,
        "Schedule" to Icons.Filled.Schedule,
        "Profile" to Icons.Filled.Person
    )

    BottomNavigation(
        backgroundColor = standingsContentBg.copy(alpha = 0.9f),
        contentColor = standingsTextMuted
    ) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(item.second, contentDescription = item.first) },
                selected = selectedItem == index,
                onClick = { selectedItem = index /* TODO: Handle navigation */ },
                selectedContentColor = Color.White,
                unselectedContentColor = standingsTextMuted
            )
        }
    }
}

// Data class và Enum
data class Team(
    val name: String,
    val logoResId: Int, // << THAY ĐỔI TỪ String SANG Int
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
    MaterialTheme {
        BaoCao()
    }
}