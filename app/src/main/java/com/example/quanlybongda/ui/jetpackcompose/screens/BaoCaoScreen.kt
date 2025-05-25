package com.example.quanlybongda.ui.jetpackcompose.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.ReturnTypes.BangXepHangNgay
import com.example.quanlybongda.R
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import java.time.LocalDate

// Thêm import cần thiết để lấy chiều cao thanh trạng thái
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape

// Màu sắc
val standingsScreenBackground = Color(0xFF0D0D12)
val standingsContentBg = Color(0xFF181A20)
val standingsCardBg = Color(0xFF222232)
val standingsTextWhite = Color.White
val standingsTextAccent = Color(0xFFD1B4FF)
val standingsTextMuted = Color(0xFFA0A3BD)

@Composable
fun BaoCaoScreen(
    appController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var teams by remember { mutableStateOf(listOf<BangXepHangNgay>()) }

    LaunchedEffect(Unit) {
        teams = viewModel.selectBXHDoiNgay(LocalDate.of(2025, 5, 11))
    }

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
                .padding(top = statusBarHeight + additionalSpacing)
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp)
            ) {
                item {
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
                    }
                }

                itemsIndexed(teams, key = { _, it -> it.maDoi }) { index, team ->
                    StandingsListRow(context = context, team = team)
                    if (index < teams.lastIndex) {
                        Divider(
                            color = standingsTextMuted.copy(alpha = 0.2f),
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    LeagueLegendStandings()
                }
            }
        }
    }
}

@Composable
fun StandingsTopAppBar(statusBarHeight: androidx.compose.ui.unit.Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = statusBarHeight, bottom = 8.dp),
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
fun StandingsListHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(6.dp + 8.dp + 20.dp + 8.dp))
        Text("Club", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(2.2f))
        Text("W", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("D", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("L", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("Point", color = standingsTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
    }
}

@Composable
fun StandingsListRow(context: android.content.Context, team: BangXepHangNgay, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Box(
            modifier = modifier
                .size(6.dp)
                .background(
                    color = if (true) Color(0xFF5C7CFA) else Color(0xFFFF9F1C),
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = team.tenDoi,
            color = standingsTextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(2.2f)
        )
        Text("${team.soTranThang}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.soTranHoa}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.soTranThua}", color = standingsTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.hieuSo}", color = standingsTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
    }
}

@Composable
fun LeagueLegendStandings(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 12.dp)
    ) {
        Box(modifier = Modifier.size(6.dp).background(Color(0xFF5C7CFA), shape = CircleShape))
        Text(" UEFA Champions League", color = standingsTextMuted, fontSize = 10.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.size(6.dp).background(Color(0xFFFF9F1C), shape = CircleShape))
        Text(" Europa League", color = standingsTextMuted, fontSize = 10.sp)
    }
}

@Composable
fun StandingsBottomNavigationBar(modifier: Modifier = Modifier) {
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

@Preview(showBackground = true, backgroundColor = 0xFF0D0D12)
@Composable
fun StandingsScreenPreview() {
    QuanLyBongDaTheme {
        BaoCaoScreen(rememberNavController())
    }
}