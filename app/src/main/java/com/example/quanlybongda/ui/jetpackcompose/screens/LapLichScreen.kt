package com.example.quanlybongda.ui.jetpackcompose.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.Database.Schema.LichThiDau
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.Services.Converters.LocalDateTimeConverter
import com.example.quanlybongda.Services.Data.Competition
import com.example.quanlybongda.Services.Data.Match
import com.example.quanlybongda.Services.FootballAPI
import com.example.quanlybongda.Services.FootballAPIViewModel
import com.example.quanlybongda.Services.LoadingState
import com.example.quanlybongda.Services.gson
import com.example.quanlybongda.homeRoute
import com.example.quanlybongda.navigatePopUpTo
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.darkCardBackground
import com.example.quanlybongda.ui.theme.darkTextMuted
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.FormatStyle
import java.util.Locale


// Màu sắc từ thiết kế
// Gradient cho thẻ chính - bạn có thể cần điều chỉnh lại các màu này cho chính xác với ảnh thiết kế (image_d0de8e.png)
// Thiết kế có vẻ chuyển từ xanh đương đậm ở góc trên trái sang tím đậm ở góc dưới phải.
val featuredCardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF3A4E99), Color(0xFF7B429E)) // Ví dụ: Xanh đậm -> Tím đậm
    // Hoặc thử 3 màu nếu bạn thấy có điểm chuyển ở giữa
    // colors = listOf(Color(0xFF2E3192), Color(0xFF4C2C80), Color(0xFF692971))
)
val textGreenAccent = Color(0xFF4CFF89)
val textScoreColor = Color(0xFFE0FF00) // Màu vàng chanh cho tỷ số, điều chỉnh lại cho khớp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LapLichScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
    apiViewModel: FootballAPIViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val currentMuaGiai by DatabaseViewModel.currentMuaGiai.collectAsState()
    val lichThiDaus by apiViewModel.matches.collectAsState()
    var doiBongs by remember { mutableStateOf(listOf<DoiBong>()) }
    val state = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedValue by remember { mutableStateOf<LichThiDau?>(null) }
    val user by viewModel.user.collectAsState()
    var isEditable by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        apiViewModel.loadMatches()
    }

    Scaffold(
        containerColor = DarkColorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
        topBar = {
            AppTopBar(
                title = "Lịch thi đấu",
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (isEditable)
                AddFloatingButton("Tạo lịch thi đấu", onClick = { navController.navigate("lichThiDauInput") })
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )  { innerPadding ->
        when (lichThiDaus) {
            is LoadingState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Purple80,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            is LoadingState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lỗi tải dữ liệu: ${(lichThiDaus as LoadingState.Error).message}",
                        color = Color.Red
                    )
                }
            }
            is LoadingState.Success -> {
                val result = (lichThiDaus as LoadingState.Success<List<Match>>).data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .padding(horizontal = 16.dp)
                        .background(DarkColorScheme.background),
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        // Tăng khoảng cách giữa TopAppBar và FeaturedMatchCardUpdated
                        Spacer(modifier = Modifier.height(30.dp)) // << SỬA: Tăng khoảng cách

                        if (result.isNotEmpty()) {
                            FeaturedMatch(
                                result[0],
                                onClick = {
//                                navController.navigate("banThang/${lichThiDau.id}")
                                }
                            )
                        }
                        // Tăng khoảng cách giữa FeaturedMatchCardUpdated và MatchScheduleHeader
                        Spacer(modifier = Modifier.height(60.dp)) // << SỬA: Tăng khoảng cách

                        MatchScheduleHeader()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    itemsIndexed(result) { index, lichThiDau ->
                        if (index == 0)
                            return@itemsIndexed;
                        MatchInfoRowNoLogos(
                            lichThiDau,
                            onClick = {
//                                navController.navigate("banThang/${lichThiDau.id}")
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    };
                }
            }
        }
    }
}

@Composable
fun FeaturedMatch(
    match: Match,
    onClick : () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(featuredCardGradient) // << SỬA: Sử dụng gradient mới
            .clickable { onClick() }
    ) {

        // Status gradient circle at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val statusColor = getStatusColor(match.status)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .scale(2.0f)
                    .offset(y = 24.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(statusColor, Color.Transparent),
//                            center = Offset(500.dp.value, 300.dp.value),
                            radius = 150f
                        )
                    )
            )
        }

        // Main content
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            // Area flag and venue information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Area flag
                match.area.flag?.let { flagUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(flagUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = "Country flag",
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Venue information
                if (match.venue != null) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Venue",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = match.venue,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Teams and score
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home team
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        match.homeTeam.crest,
                        contentDescription = "",
                        modifier = Modifier.size(64.dp),
                    )
                    Text(
                        text = match.homeTeam.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Score and status in the middle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    // Status indicator
                    StatusIndicator(status = match.status)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Date/time
                    Text(
                        text = (match.utcDate).format(java.time.format.DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    // Score (if match is not scheduled)
                    if (match.status != "SCHEDULED" && match.status != "TIMED") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${match.score.fullTime.home ?: "-"}",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = " - ",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${match.score.fullTime.away ?: "-"}",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Away team
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        match.awayTeam.crest,
                        contentDescription = "",
                        modifier = Modifier.size(64.dp),
                    )
                    Text(
                        text = match.awayTeam.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun TeamDisplayUpdated( // Đổi tên và sửa đổi
    teamName: String,
    teamImageURL: String,
    scorers: String,
    horizontalAlignment: Alignment.Horizontal,
    textAlign: TextAlign
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .widthIn(min = 80.dp, max = 120.dp) // Đặt giới hạn chiều rộng cho cột này
        // .padding(top = 8.dp) // Thêm padding top để đẩy tên đội xuống thẳng hàng tỷ số
    ) {
        AsyncImage(
            model = teamImageURL,
            contentDescription = "",
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp)) // << SỬA: Thêm Spacer
        Text(
            text = teamName,
            color = Color.White,
            fontSize = 24.sp, // << SỬA: Tăng kích thước tên đội
            fontWeight = FontWeight.Bold,
            textAlign = textAlign,
            maxLines = 1
        )
        // Spacer để đẩy tên cầu thủ xuống một chút so với tên đội
//        Spacer(modifier = Modifier.height(8.dp)) // << SỬA: Thêm Spacer
//        Text(
//            text = scorers, // scorers giờ đây có thể chứa \n để xuống dòng
//            color = Color.White.copy(alpha = 0.85f),
//            fontSize = 11.sp, // << SỬA: Tăng nhẹ font chữ người ghi bàn
//            textAlign = textAlign,
//            lineHeight = 14.sp // Tăng lineHeight để các dòng không quá sát nhau
//        )
    }
}

// --- Các Composable AppTopBar, MatchScheduleHeader, MatchInfoRowNoLogos, AppBottomNavigationBar ---
// --- và hàm Preview giữ nguyên như phiên bản trước. Bạn copy chúng vào đây. ---

@Composable
fun MatchScheduleHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Match Schedule", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
//        Text("See All", color = darkTextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MatchInfoRowNoLogos(
    match: Match,
    onClick : () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(darkCardBackground)
            .clickable { onClick() }
    ) {

        // Status gradient circle at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val statusColor = getStatusColor(match.status)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .scale(2.0f)
                    .offset(y = 24.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(statusColor, Color.Transparent),
//                            center = Offset(500.dp.value, 300.dp.value),
                            radius = 150f
                        )
                    )
            )
        }

        // Main content
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            // Area flag and venue information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Area flag
                match.area.flag?.let { flagUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(flagUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = "Country flag",
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Venue information
                if (match.venue != null) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Venue",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = match.venue,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Teams and score
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home team
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        match.homeTeam.crest,
                        contentDescription = "",
                        modifier = Modifier.size(64.dp),
                    )
                    Text(
                        text = match.homeTeam.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Score and status in the middle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    // Status indicator
                    StatusIndicator(status = match.status)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Date/time
                    Text(
                        text = (match.utcDate).format(java.time.format.DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    // Score (if match is not scheduled)
                    if (match.status != "SCHEDULED" && match.status != "TIMED") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${match.score.fullTime.home ?: "-"}",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = " - ",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${match.score.fullTime.away ?: "-"}",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Away team
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        match.awayTeam.crest,
                        contentDescription = "",
                        modifier = Modifier.size(64.dp),
                    )
                    Text(
                        text = match.awayTeam.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun StatusIndicator(status: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(getStatusColor(status))
    ) {
        when (status) {
            "IN_PLAY", "PAUSED", "EXTRA_TIME", "PENALTY_SHOOTOUT" -> {
                // Live indicator
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Live",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            "FINISHED" -> {
                // Finished indicator
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Finished",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            "SUSPENDED", "POSTPONED", "CANCELLED" -> {
                // Suspended/postponed indicator
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Suspended",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            "SCHEDULED", "TIMED" -> {
                // Scheduled indicator
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Scheduled",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            "AWARDED" -> {
                // Awarded indicator
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Awarded",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// Function to get color based on match status
fun getStatusColor(status: String): Color {
    return when (status) {
        "IN_PLAY", "PAUSED" -> Color(0xFF4CAF50) // Green for live matches
        "EXTRA_TIME", "PENALTY_SHOOTOUT" -> Color(0xFFFF9800) // Orange for extra time/penalties
        "FINISHED" -> Color(0xFF2196F3) // Blue for finished
        "SUSPENDED", "POSTPONED" -> Color(0xFFFFC107) // Amber for suspended/postponed
        "CANCELLED" -> Color(0xFFF44336) // Red for cancelled
        "AWARDED" -> Color(0xFFE91E63) // Pink for awarded
        else -> Color(0xFF9E9E9E) // Grey for scheduled/timed/default
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun LapLichScreenPreview() {
    MaterialTheme {
        LapLichScreen(rememberNavController())
    }
}