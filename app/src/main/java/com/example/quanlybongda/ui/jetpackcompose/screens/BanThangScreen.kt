package com.example.quanlybongda.ui.jetpackcompose.screens

// ... (Các import của bạn giữ nguyên)
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.BanThang
import com.example.quanlybongda.Database.Schema.LichThiDau
import com.example.quanlybongda.R
import com.example.quanlybongda.ui.theme.DarkColorScheme
import kotlinx.coroutines.launch

// ... (Các biến màu sắc của bạn giữ nguyên)
val darkScreenBackground = Color(0xFF1E1E2C)
val scoreColor = Color(0xFFE0FF00)
val fullTimeColor = Color(0xFF4CFF89)
val textWhiteColor = Color.White
val textMutedColor = Color(0xFFA0A3BD)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BanThangScreen(
    maTD: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var lichThiDau by remember { mutableStateOf<LichThiDau?>(null) }
    var tiSoDoiMot by remember { mutableStateOf(0) }
    var tiSoDoiHai by remember { mutableStateOf(0) }
    var banThangs by remember { mutableStateOf(listOf<BanThang>()) }
    val user by viewModel.user.collectAsState()
    var isEditable by remember { mutableStateOf(false) }

    // ✅ SỬA LỖI: Khối LaunchedEffect được viết lại để xử lý null an toàn
    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            // Bước 1: Lấy thông tin trận đấu và kiểm tra null ngay lập tức
            val fetchedLichThiDau = viewModel.lichThiDauDAO.selectLichThiDauMaTD(maTD)

            // Bước 2: Chỉ thực hiện logic còn lại nếu trận đấu tồn tại (không null)
            fetchedLichThiDau?.let { ltd ->
                // Cập nhật state cho giao diện
                lichThiDau = ltd

                // Tính toán tỷ số một cách an toàn
                tiSoDoiMot = viewModel.banThangDAO.selectSoBanThangTranDauDoi(maTD, ltd.doiMot) +
                        viewModel.banThangDAO.selectSoBanThangPhanLuoiTranDauDoi(maTD, ltd.doiHai)
                tiSoDoiHai = viewModel.banThangDAO.selectSoBanThangTranDauDoi(maTD, ltd.doiHai) +
                        viewModel.banThangDAO.selectSoBanThangPhanLuoiTranDauDoi(maTD, ltd.doiMot)

                // Lấy các danh sách liên quan
                val banThangsTemp = viewModel.banThangDAO.selectBanThang(maTD)
                val loaiBTs = viewModel.banThangDAO.selectAllLoaiBT()
                val cauThus = viewModel.cauThuDAO.selectCauThuTGTD(maTD)

                // Bước 3: Xử lý danh sách bàn thắng một cách an toàn, tránh dùng `!!`
                val processedBanThangs = banThangsTemp.map { banThang ->
                    // Tìm cầu thủ, nếu không thấy sẽ là null, không crash
                    val cauThu = cauThus.find { it.maCT == banThang.maCT }
                    // Tìm loại bàn thắng
                    val loaiBT = loaiBTs.find { it.maLBT == banThang.maLBT }

                    // Gán tên và bên (side) cho bàn thắng nếu tìm thấy cầu thủ
                    cauThu?.let { ct ->
                        banThang.tenCT = ct.tenCT
                        banThang.side = if (ct.maDoi == ltd.doiMot) "L" else "R"
                    } ?: run {
                        // Nếu không tìm thấy cầu thủ, gán giá trị mặc định
                        banThang.tenCT = "Neto"
                        banThang.side = "L"
                    }

                    // Gán tên loại bàn thắng nếu tìm thấy
                    loaiBT?.let { lbt ->
                        banThang.tenLBT = lbt.tenLBT
                    } ?: run {
                        banThang.tenLBT = "Bàn thắng"
                    }

                    banThang // Trả về đối tượng banThang đã được cập nhật
                }

                // Cập nhật state cuối cùng cho danh sách bàn thắng
                banThangs = processedBanThangs
            }
        }
    }


    LaunchedEffect(user) {
        if (user == null)
            return@LaunchedEffect;
        viewModel.viewModelScope.launch {
            isEditable = viewModel.checkPageEditable(user!!.groupId, "banthang");
        }
    }

    Scaffold(
        // ... (Phần còn lại của Scaffold giữ nguyên)
        containerColor = DarkColorScheme.background,
        topBar = {
            AppTopBar(
                title = "Báo cáo",
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            if (isEditable)
                AddFloatingButton("Tạo bàn thắng", onClick = { navController.navigate("banThangInput/${maTD}") })
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 12.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.football_stadium),
                contentDescription = "Background Stadium",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.3f
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Full Time",
                        color = fullTimeColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(R.drawable.arsenal_logo_4x),
                            contentDescription = "Team 1 Logo",
                            modifier = Modifier.size(70.dp)
                        )
                        Text(
                            text = "$tiSoDoiMot - $tiSoDoiHai",
                            color = scoreColor,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Image(
                            painter = rememberAsyncImagePainter(R.drawable.mancity_logo_4x),
                            contentDescription = "Team 2 Logo",
                            modifier = Modifier.size(70.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Statistic Match",
                        color = textWhiteColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(banThangs) {
                    StatisticRowUpdated(side = it.side, player = it.tenCT, action = it.tenLBT, time = it.thoiDiem.toString(), modifier)
                }
            }
        }
    }
}

// ... (Composable StatisticRowUpdated và Preview giữ nguyên)
@Composable
fun StatisticRowUpdated(side: String, player: String, action: String, time: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = side,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.15f),
            textAlign = TextAlign.Center
        )
        Text(
            text = player,
            color = textWhiteColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier
                .weight(0.5f)
                .padding(start = 8.dp)
        )
        Text(
            text = action,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.15f),
            textAlign = TextAlign.Center
        )
        Text(
            text = time,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.2f),
            textAlign = TextAlign.End
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E2C)
@Composable
fun FinalScoreScreenPreview() {
    MaterialTheme {
        BanThangScreen(1, rememberNavController())
    }
}