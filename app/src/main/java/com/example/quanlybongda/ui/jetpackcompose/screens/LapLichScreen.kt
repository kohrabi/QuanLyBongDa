package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.Database.Schema.LichThiDau
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch


// Màu sắc từ thiết kế
val screenBgColor = Color(0xFF181A20)
val cardBgColor = Color(0xFF1F222A)
// Gradient cho thẻ chính - bạn có thể cần điều chỉnh lại các màu này cho chính xác với ảnh thiết kế (image_d0de8e.png)
// Thiết kế có vẻ chuyển từ xanh dương đậm ở góc trên trái sang tím đậm ở góc dưới phải.
val featuredCardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF3A4E99), Color(0xFF7B429E)) // Ví dụ: Xanh đậm -> Tím đậm
    // Hoặc thử 3 màu nếu bạn thấy có điểm chuyển ở giữa
    // colors = listOf(Color(0xFF2E3192), Color(0xFF4C2C80), Color(0xFF692971))
)
val textWhite = Color.White
val textSecondary = Color(0xFFA0A3BD)
val textGreenAccent = Color(0xFF4CFF89)
val textScoreColor = Color(0xFFE0FF00) // Màu vàng chanh cho tỷ số, điều chỉnh lại cho khớp

@Composable
fun LapLichScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var lichThiDaus by remember { mutableStateOf(listOf<LichThiDau>()) }
    var doiBongs by remember { mutableStateOf(listOf<DoiBong>()) }
    val state = rememberLazyListState()

    LaunchedEffect(Unit) {
        lichThiDaus = viewModel.lichThiDauDAO.selectAllLichThiDau();
        doiBongs = viewModel.doiBongDAO.selectAllDoiBong();
        for (lichThiDau in lichThiDaus) {
            lichThiDau.tenDoiMot = doiBongs.find { it.maDoi == lichThiDau.doiMot }!!.tenDoi;
            lichThiDau.tenDoiHai = doiBongs.find { it.maDoi == lichThiDau.doiHai }!!.tenDoi;
            if (lichThiDau.doiThang == null)
                lichThiDau.tenDoiThang = "Hòa";
            else
                lichThiDau.tenDoiThang = doiBongs.find { it.maDoi == lichThiDau.doiThang }!!.tenDoi;
        }
    }

    Scaffold(
        backgroundColor = screenBgColor,
        topBar = { AppTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("lichThiDauInput") },
                modifier = Modifier
            ) {
                Icon(Icons.Filled.Add, "Lập lịch")
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            item {
                // Tăng khoảng cách giữa TopAppBar và FeaturedMatchCardUpdated
                Spacer(modifier = Modifier.height(60.dp)) // << SỬA: Tăng khoảng cách

                FeaturedMatchCardUpdated(
                    team1Name = "Chelse",
                    team1Scorers = "De Jong 66’\nDepay 79’", // Giữ \n để xuống dòng tự nhiên
                    score = "2 - 2",
                    team2Name = "Leicester",
                    team2Scorers = "Alvarez 21’\nPalmer 70’" // Giữ \n
                )
                // Tăng khoảng cách giữa FeaturedMatchCardUpdated và MatchScheduleHeader
                Spacer(modifier = Modifier.height(60.dp)) // << SỬA: Tăng khoảng cách

                MatchScheduleHeader()
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(lichThiDaus) { lichThiDau ->
                MatchInfoRowNoLogos(
                    lichThiDau.tenDoiMot ?: "",
                    DateConverter.LocalDateTimeToString(lichThiDau.ngayGioThucTe),
                    lichThiDau.tenDoiHai ?: "",
                    onClick = {
                        navController.navigate("ghiNhan/${lichThiDau.maTD}");
                    })
                Spacer(modifier = Modifier.height(12.dp))
            };
        }
    }
}

@Composable
fun FeaturedMatchCardUpdated(
    team1Name: String,
    team1Scorers: String,
    score: String,
    team2Name: String,
    team2Scorers: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp) // Chiều cao thẻ
            .clip(RoundedCornerShape(20.dp))
            .background(featuredCardGradient) // << SỬA: Sử dụng gradient mới
            .padding(vertical = 16.dp, horizontal = 16.dp) // Tăng padding ngang một chút
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally // Căn giữa "FULL TIME"
        ) {
            Text(
                "FULL TIME",
                color = textGreenAccent,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
            )
            // Spacer(modifier = Modifier.height(10.dp)) // Bỏ Spacer này để Row dưới tự căn chỉnh

            // Row chứa tên đội và tỷ số, sẽ được căn giữa với nhau
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Cho Row này chiếm không gian còn lại để dễ căn giữa
                verticalAlignment = Alignment.CenterVertically, // Căn các item trong Row theo chiều dọc
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TeamDisplayUpdated(
                    teamName = team1Name,
                    scorers = team1Scorers,
                    horizontalAlignment = Alignment.Start, // Căn text của đội bên trong Column của đội
                    textAlign = TextAlign.Start
                )

                Text(
                    score,
                    color = textScoreColor,
                    fontSize = 38.sp,    // << SỬA: Tăng kích thước tỷ số hơn nữa
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                TeamDisplayUpdated(
                    teamName = team2Name,
                    scorers = team2Scorers,
                    horizontalAlignment = Alignment.End, // Căn text của đội bên trong Column của đội
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun TeamDisplayUpdated( // Đổi tên và sửa đổi
    teamName: String,
    scorers: String,
    horizontalAlignment: Alignment.Horizontal,
    textAlign: TextAlign
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .widthIn(min = 80.dp, max = 120.dp) // Đặt giới hạn chiều rộng cho cột này
        // .padding(top = 8.dp) // Thêm padding top để đẩy tên đội xuống thẳng hàng tỷ số
    ) {
        Text(
            text = teamName,
            color = textWhite,
            fontSize = 24.sp, // << SỬA: Tăng kích thước tên đội
            fontWeight = FontWeight.Bold,
            textAlign = textAlign,
            maxLines = 1
        )
        // Spacer để đẩy tên cầu thủ xuống một chút so với tên đội
        Spacer(modifier = Modifier.height(8.dp)) // << SỬA: Thêm Spacer
        Text(
            text = scorers, // scorers giờ đây có thể chứa \n để xuống dòng
            color = textWhite.copy(alpha = 0.85f),
            fontSize = 11.sp, // << SỬA: Tăng nhẹ font chữ người ghi bàn
            textAlign = textAlign,
            lineHeight = 14.sp // Tăng lineHeight để các dòng không quá sát nhau
        )
    }
}

// --- Các Composable AppTopBar, MatchScheduleHeader, MatchInfoRowNoLogos, AppBottomNavigationBar ---
// --- và hàm Preview giữ nguyên như phiên bản trước. Bạn copy chúng vào đây. ---

@Composable
fun AppTopBar() {
    TopAppBar(
        title = { },
        backgroundColor = screenBgColor,
        elevation = 0.dp,
//        navigationIcon = {
//            IconButton(onClick = { /* TODO */ }) {
//                Icon(Icons.Filled.Add, contentDescription = "Add", tint = textWhite)
//            }
//        },
//        actions = {
//            IconButton(onClick = { /* TODO */ }) {
//                Icon(Icons.Filled.Search, contentDescription = "Search", tint = textWhite)
//            }
//        },
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
fun MatchScheduleHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Match Schedule", color = textWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
//        Text("See All", color = textSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MatchInfoRowNoLogos(
    team1Name: String,
    matchDateTime: String,
    team2Name: String,
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBgColor)
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(team1Name, color = textWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
        Text(matchDateTime, color = textSecondary, fontSize = 10.sp, textAlign = TextAlign.Center, lineHeight = 12.sp, modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
        Text(team2Name, color = textWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
}

@Composable
fun AppBottomNavigationBar(context: android.content.Context) {
    BottomNavigation( // Sử dụng BottomNavigation của Material
        backgroundColor = Color(0xFF1C1C2A), // Màu nền từ thiết kế
        contentColor = Color(0xFF7F7F91) // Màu icon không được chọn
    ) {
        // selectedContentColor và unselectedContentColor cho icon và text (nếu có)
        val selectedColor = Color.White
        val unselectedColor = Color(0xFF7F7F91)

        // Giả sử bạn có một biến trạng thái để theo dõi mục đang được chọn
        var selectedItem by remember { mutableStateOf(0) } // 0 là Home

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 /* TODO: Navigate to Home */ },
            selectedContentColor = selectedColor,
            unselectedContentColor = unselectedColor
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Ranking") }, // Giả sử icon List cho Ranking
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 /* TODO: Navigate to Ranking */ },
            selectedContentColor = selectedColor,
            unselectedContentColor = unselectedColor
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Schedule, contentDescription = "Schedule") }, // Giả sử Schedule
            selected = selectedItem == 2,
            onClick = { selectedItem = 2 /* TODO: Navigate to Schedule */ },
            selectedContentColor = selectedColor,
            unselectedContentColor = unselectedColor
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            selected = selectedItem == 3,
            onClick = { selectedItem = 3 /* TODO: Navigate to Profile */ },
            selectedContentColor = selectedColor,
            unselectedContentColor = unselectedColor
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun LapLichScreenPreview() {
    MaterialTheme {
        LapLichScreen(rememberNavController())
    }
}