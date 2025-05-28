package com.example.quanlybongda.ui.jetpackcompose.screens

// Thêm import cần thiết để lấy chiều cao thanh trạng thái
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkContentBackground
import com.example.quanlybongda.ui.theme.darkTextMuted
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaoCaoScreen(
    appController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    var teams by remember { mutableStateOf(listOf<BangXepHangNgay>()) }
    var ngayBaoCao by remember { mutableStateOf(LocalDate.of(2025, 5, 11)) }

    LaunchedEffect(ngayBaoCao) {
        teams = viewModel.selectBXHDoiNgay(ngayBaoCao)
    }

    Scaffold(
        containerColor = DarkColorScheme.background,
        topBar = {
            AppTopBar(
                title = "Báo cáo",
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
//                .padding(top = statusBarHeight + additionalSpacing)
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
                    InputDatePicker("Ngày báo cáo",
                        value = convertLocalDateToMillis(ngayBaoCao),
                        onDateSelected =  {
                            if (it != null) {
                                ngayBaoCao = convertMillisToLocalDate(it);
                            }
                        },
                        onDismiss = {});
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(darkContentBackground.copy(alpha = 0.7f))
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Table Standings",
                                color = com.example.quanlybongda.ui.theme.darkTextWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
//                            Text(
//                                text = "See All",
//                                color = standingsTextAccent,
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Bold
//                            )
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
                            color = darkTextMuted.copy(alpha = 0.2f),
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
            color = com.example.quanlybongda.ui.theme.darkTextWhite,
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
        Text("Club", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(2.2f))
        Text("W", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("D", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("L", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("Point", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
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
            color = com.example.quanlybongda.ui.theme.darkTextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(2.2f)
        )
        Text("${team.soTranThang}", color = com.example.quanlybongda.ui.theme.darkTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.soTranHoa}", color = com.example.quanlybongda.ui.theme.darkTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.soTranThua}", color = com.example.quanlybongda.ui.theme.darkTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.hieuSo}", color = com.example.quanlybongda.ui.theme.darkTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
    }
}

@Composable
fun LeagueLegendStandings(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 12.dp)
    ) {
        Box(modifier = Modifier.size(6.dp).background(Color(0xFF5C7CFA), shape = CircleShape))
        Text(" UEFA Champions League", color = darkTextMuted, fontSize = 10.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier.size(6.dp).background(Color(0xFFFF9F1C), shape = CircleShape))
        Text(" Europa League", color = darkTextMuted, fontSize = 10.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D12)
@Composable
fun StandingsScreenPreview() {
    QuanLyBongDaTheme {
        BaoCaoScreen(rememberNavController())
    }
}