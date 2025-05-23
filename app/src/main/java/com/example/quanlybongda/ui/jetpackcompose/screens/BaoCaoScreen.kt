package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quanlybongda.R

// Thêm import cần thiết để lấy chiều cao thanh trạng thái
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars

// Màu sắc
val standingsScreenBackground = Color(0xFF0D0D12)
val standingsContentBg = Color(0xFF181A20)
val standingsCardBg = Color(0xFF222232)
val standingsTextWhite = Color.White
val standingsTextAccent = Color(0xFFD1B4FF)
val standingsTextMuted = Color(0xFFA0A3BD)

@Composable
fun BaoCao() {
    val context = LocalContext.current

    // Lấy chiều cao của thanh trạng thái
    val density = LocalDensity.current
    val statusBarHeight = with(density) {
        WindowInsets.statusBars.getTop(density).toDp()
    }

    // Khoảng cách bổ sung giữa thanh trạng thái và giao diện
    val additionalSpacing = 8.dp

    Scaffold(
        backgroundColor = standingsScreenBackground,
        topBar = {
            StandingsTopAppBar(statusBarHeight = statusBarHeight + additionalSpacing)
        },
        bottomBar = {
            StandingsBottomNavigationBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = statusBarHeight + additionalSpacing) // Đẩy nội dung xuống dưới thanh trạng thái + khoảng cách bổ sung
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.football_stadium)
                    .crossfade(true)
                    .build(),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.3f
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

                    val teams = listOf(
                        Team("Arsenal", R.drawable.arsenal_logo, 3, 0, 0, 9, League.CHAMPIONS),
                        Team("Man City", R.drawable.mancity_logo, 2, 1, 0, 7, League.CHAMPIONS),
                        Team("Leeds United", R.drawable.leeds_united_logo, 2, 1, 0, 7, League.CHAMPIONS),
                        Team("Tottenham", R.drawable.tottenham_logo, 1, 2, 0, 5, League.EUROPA),
                        Team("Brighton", R.drawable.brighton_logo, 1, 1, 1, 4, League.EUROPA)
                    )

                    teams.forEachIndexed { index, team ->
                        StandingsListRow(context = context, team = team)
                        if (index < teams.lastIndex) {
                            Divider(
                                color = standingsTextMuted.copy(alpha = 0.2f),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
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
fun StandingsTopAppBar(statusBarHeight: androidx.compose.ui.unit.Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = statusBarHeight, bottom = 8.dp), // Đẩy tiêu đề xuống dưới thanh trạng thái
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Standings",
            color = standingsTextWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
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
            model = team.logoResId,
            contentDescription = "${team.name} Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(20.dp)
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
    val logoResId: Int,
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