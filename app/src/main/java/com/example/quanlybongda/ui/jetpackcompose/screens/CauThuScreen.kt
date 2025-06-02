package com.example.quanlybongda.ui.jetpackcompose.screens

// Thêm import cần thiết để lấy chiều cao thanh trạng thái
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple40
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkCardBackground
import kotlinx.coroutines.launch

// Main Composable for the Player List Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CauThuScreen(
    maDoi : Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var cauThus by remember { mutableStateOf(listOf<CauThu>()) }
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedValue by remember { mutableStateOf<CauThu?>(null) }

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            val doiBong = viewModel.doiBongDAO.selectDoiBongMaDoi(maDoi);
            cauThus = viewModel.cauThuDAO.selectCauThuDoiBong(maDoi);
            val loaiCTs = viewModel.cauThuDAO.selectAllLoaiCT();
            for (cauThu in cauThus) {
                cauThu.tenLCT = loaiCTs.find { it.maLCT == cauThu.maLCT }!!.tenLCT;
                cauThu.doiImageURL = doiBong?.imageURL ?: "";
            }
        }
    }

    DisposableEffect(snackbarHostState) {
        onDispose {
            if (selectedValue != null) {
                viewModel.viewModelScope.launch {
                    viewModel.cauThuDAO.deleteCauThu(selectedValue!!);
                    selectedValue = null;
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
        topBar = {
            AppTopBar(
                title = "Cầu thủ",
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = Purple80,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AddFloatingButton("Cầu thủ", onClick = { navController.navigate("cauThuInput/${maDoi}")})
        },
        containerColor = DarkColorScheme.background,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        // Danh sách cầu thủ
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .background(DarkColorScheme.background)
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cauThus) { cauThu ->
                SwipeToDeleteContainer(
                    item = cauThu,
                    onDelete = {
                        if (selectedValue != null) {
                            viewModel.viewModelScope.launch {
                                viewModel.cauThuDAO.deleteCauThu(selectedValue!!);
                                selectedValue = null;
                            }
                        }
                        selectedValue = cauThu;
                        val result = snackbarHostState
                            .showSnackbar(
                                message = "Deleted ${cauThu.tenCT}",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                return@SwipeToDeleteContainer false;
                            }
                            SnackbarResult.Dismissed -> {
                                viewModel.viewModelScope.launch {
                                    viewModel.cauThuDAO.deleteCauThu(selectedValue!!);
                                    selectedValue = null;
                                }
                                return@SwipeToDeleteContainer true;
                            }
                        }
                    },
                    onUpdate = {
                        navController.navigate("cauThuInput/${it.maDoi}");
                        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle;
                        savedStateHandle?.set("maCT", it.maCT);
                        savedStateHandle?.set("tenCT", it.tenCT);
                        savedStateHandle?.set("maLCT", it.maLCT);
                        savedStateHandle?.set("maDoi", it.maDoi);
                        savedStateHandle?.set("soAo", it.soAo);
                        savedStateHandle?.set("ghiChu", it.ghiChu);
                        savedStateHandle?.set("ngaySinh", it.ngaySinh);
                        savedStateHandle?.set("imageURL", it.imageURL);
                    },
                    content = {
                        PlayerCard(player = cauThu)
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                    backgroundModifier = Modifier.clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

// Composable for a single Player Card
@Composable
fun PlayerCard(player: CauThu, onClick : () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = darkCardBackground
        ),
        modifier = Modifier.clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Phần bên trái: Mã cầu thủ, Tên cầu thủ, Goals, Số bàn thắng
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(end = 16.dp), // Tăng khoảng cách với phần bên phải
                horizontalAlignment = Alignment.Start
            ) {
                // Mã cầu thủ (trong vòng tròn tím)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Purple40, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${player.soAo}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Tên cầu thủ
                Text(
                    text = player.tenCT,
                    style = TextStyle(
                        fontSize = 14.02.sp,
                        lineHeight = 24.53.sp,
                        fontWeight = FontWeight(500),
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.26.sp
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                AsyncImage(
                    model = player.doiImageURL,
                    contentDescription = "",
                    modifier = Modifier.size(64.dp)
                )
                //                // Nhãn "Goal"
                //                Text(
                //                    text = "Goal",
                //                    style = TextStyle(
                //                        fontSize = 8.76.sp,
                //                        lineHeight = 9.64.sp,
                //                        fontWeight = FontWeight(500),
                //                        color = Color(0xFF797979),
                //                        textAlign = TextAlign.Start
                //                    ),
                //                    modifier = Modifier.padding(bottom = 4.dp)
                //                )
                // Số bàn thắng (Goals)
                //                Text(
                //                    text = "${player.goals}",
                //                    style = TextStyle(
                //                        fontSize = 21.03.sp,
                //                        lineHeight = 14.71.sp,
                //                        fontWeight = FontWeight(600),
                //                        color = Color(0xFFD2B5FF),
                //                        textAlign = TextAlign.Start
                //                    )
                //                )
            }

            // Phần bên phải: Position, Birth day, Note
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 16.dp, top = 36.dp), // Tăng padding(start) và top để đẩy xuống dưới
                horizontalAlignment = Alignment.Start
            ) {
                // Vị trí (Player Type)
                Text(
                    text = player.tenLCT,
                    style = TextStyle(
                        fontSize = 14.02.sp,
                        lineHeight = 24.53.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.26.sp
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Ngày sinh (Birth day)
                Text(
                    text = DateConverter.LocalDateToString(player.ngaySinh),
                    style = TextStyle(
                        fontSize = 14.02.sp,
                        lineHeight = 24.53.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.26.sp
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Ghi chú (Note)
                Text(
                    text = player.ghiChu,
                    style = TextStyle(
                        fontSize = 14.02.sp,
                        lineHeight = 24.53.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.26.sp
                    )
                )
            }
        }
    }
}

// Preview function for Android Studio
@Preview(showBackground = true, widthDp = 360, heightDp = 720, backgroundColor = 0xFF1C1D2B)
@Composable
fun PreviewCauThu() {
    QuanLyBongDaTheme {
        CauThuScreen(1, rememberNavController())
    }
}