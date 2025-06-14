package com.example.quanlybongda.ui.jetpackcompose.screens

// Thêm import cần thiết để lấy chiều cao thanh trạng thái
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.ReturnTypes.BangXepHangNgay
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.R
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkContentBackground
import com.example.quanlybongda.ui.theme.darkTextMuted
import com.example.quanlybongda.ui.theme.darkTextWhite
import kotlinx.coroutines.launch
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaoCaoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    var teams by remember { mutableStateOf(listOf<BangXepHangNgay>()) }
    val muaGiai by DatabaseViewModel.currentMuaGiai.collectAsState()
//    var ngayBaoCao by remember { mutableStateOf(LocalDate.of(2025, 5, 11)) }
//
//    LaunchedEffect(ngayBaoCao) {
//        teams = viewModel.selectBXHDoiNgay(ngayBaoCao).sortedByDescending { it.hieuSo }
//    }
    var muaGiaiOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var selectedMuaGiai by remember { mutableStateOf(OptionValue(null, "Vui lòng chọn mùa giải")) }

    LaunchedEffect(Unit) {
        if (muaGiai != null)
            selectedMuaGiai = OptionValue(muaGiai!!.maMG, muaGiai!!.tenMG);
        viewModel.viewModelScope.launch {
            muaGiaiOptions = viewModel.muaGiaiDAO.selectAllMuaGiai().map { OptionValue(it.maMG, it.tenMG) };
        }
    }

    LaunchedEffect(selectedMuaGiai) {
        viewModel.viewModelScope.launch {
            if (selectedMuaGiai.value != null) {
                teams = viewModel.selectBXHDoiMuaGiai(selectedMuaGiai.value!!).sortedByDescending { it.hieuSo };
            }
        }
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
            Image(
                painter = painterResource(R.drawable.football_stadium),
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
                    InputDropDownMenu(
                        label = "Mùa giải",
                        options = muaGiaiOptions,
                        selectedOption = selectedMuaGiai,
                        onOptionSelected = {
                            selectedMuaGiai = it
                        },
                        showEmptyError = selectedMuaGiai.value == null,
                    )
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
                                color = darkTextWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        StandingsListHeader()
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                itemsIndexed(teams) { index, team ->
                    StandingsListRow(team = team)
//                    if (index < teams.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 0.5.dp,
                            color = darkTextMuted.copy(alpha = 0.2f)
                        )
//                    }
                }

//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    LeagueLegendStandings()
//                }
            }
        }
    }
}

@Composable
fun StandingsTopAppBar(statusBarHeight: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = statusBarHeight, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Standings",
            color = darkTextWhite,
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
//        Spacer(modifier = Modifier.width(6.dp + 8.dp))
        Text("Logo", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(1.0f))
        Text("Club", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(2.2f))
        Text("W", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("D", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("L", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("Point", color = darkTextMuted, fontSize = 11.sp, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
    }
}

@Composable
fun StandingsListRow(team: BangXepHangNgay, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
//        Box(
//            modifier = modifier
//                .size(6.dp)
//                .background(
//                    color = if (true) Color(0xFF5C7CFA) else Color(0xFFFF9F1C),
//                    shape = CircleShape
//                )
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            model = team.imageURL,
            contentDescription = "",
            modifier = Modifier.size(32.dp).weight(1.0f)
        )
        Text(
            text = team.tenDoi,
            color = darkTextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(2.2f)
        )
        Text("${team.soTranThang}", color = darkTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.soTranHoa}", color = darkTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.soTranThua}", color = darkTextWhite, fontSize = 13.sp, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
        Text("${team.hieuSo}", color = darkTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
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