package com.example.quanlybongda.ui.jetpackcompose.screens.Input

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.Database.Schema.LichThiDau
import com.example.quanlybongda.navigatePopUpTo
import com.example.quanlybongda.ui.jetpackcompose.screens.AppTopBar
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDatePicker
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.InputFloatField
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import com.example.quanlybongda.ui.jetpackcompose.screens.convertLocalDateTimeToMillis
import com.example.quanlybongda.ui.jetpackcompose.screens.convertMillisToLocalDateTime
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LichThiDauInputScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current;
    val coroutineScope = rememberCoroutineScope()
    val currentMuaGiai by DatabaseViewModel.currentMuaGiai.collectAsState()
    var doiBongs by remember { mutableStateOf(listOf<DoiBong>()) }
    var doiBongOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var vongTDOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var doiThangOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var trongTaiOptions by remember { mutableStateOf(listOf<OptionValue>()) }

    var vongTD by remember { mutableStateOf(OptionValue.DEFAULT) }
    var doiMot by remember { mutableStateOf(OptionValue.DEFAULT) }
    var doiHai by remember { mutableStateOf(OptionValue.DEFAULT) }
    var doiThang by remember { mutableStateOf(OptionValue.DEFAULT) }

    var ngayGioDuKien by remember { mutableStateOf(LocalDateTime.now()) }
    var ngayGioThucTe by remember { mutableStateOf(LocalDateTime.now()) }
    var trongTai by remember { mutableStateOf(OptionValue.DEFAULT) }
    var thoiGianDaThiDau by remember { mutableStateOf(0.0f) }
    val onClick = {
        coroutineScope.launch {
            if (currentMuaGiai == null) {
                Toast.makeText(context, "WARNING: muaGiai không hợp lệ", Toast.LENGTH_SHORT).show();
                navigatePopUpTo(navController, "muaGiai");
                return@launch;
            }
            viewModel.lichThiDauDAO.upsertLichThiDau(
                LichThiDau(
                    maMG = currentMuaGiai!!.maMG,
                    maVTD = vongTD.value,
                    maSan = doiBongs.find { it.maDoi == doiMot.value }!!.maSan,
                    doiMot = doiMot.value,
                    doiHai = doiHai.value,
                    doiThang = (if (doiThang.value == 0) null else doiThang.value),
                    ngayGioDuKien = ngayGioDuKien,
                    ngayGioThucTe = ngayGioThucTe,
                    thoiGianDaThiDau = thoiGianDaThiDau.toFloat(),
                    maTT = trongTai.value,
                )
            )
            delay(500);
            navController.popBackStack()
        }
    };

    LaunchedEffect(Unit) {
        doiBongs = viewModel.doiBongDAO.selectAllDoiBong();
        trongTaiOptions = viewModel.lichThiDauDAO.selectAllTrongTai().map { OptionValue(value = it.maMG, label = it.tenTT) };
        doiBongOptions = doiBongs.map { OptionValue(value = it.maDoi!!, label = it.tenDoi) };
        vongTDOptions = viewModel.lichThiDauDAO.selectAllVongTD().map { OptionValue(it.maVTD, it.tenVTD) };
    }

    LaunchedEffect(doiMot, doiHai) {
        val options = mutableListOf<OptionValue>();
        options.add(OptionValue(0, "Hòa"));
        if (doiMot.value > 0)
            options.add(doiMot);
        if (doiHai.value > 0)
            options.add(doiHai);
        doiThangOptions = options;
    }

    Scaffold(
        containerColor = DarkColorScheme.background,
        topBar = {
            AppTopBar(
                title = "Nhập lịch thi đấu",
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
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerScaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(innerScaffoldPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                // Username
                InputDropDownMenu(
                    name = "Đội một",
                    options = doiBongOptions,
                    selectedOption = doiMot,
                    onOptionSelected = { doiMot = it });
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    name = "Đội hai",
                    options = doiBongOptions,
                    selectedOption = doiHai,
                    onOptionSelected = { doiHai = it });
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    name = "Vòng thi đấu",
                    options = vongTDOptions,
                    selectedOption = vongTD,
                    onOptionSelected = { vongTD = it  })
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    name = "Đội Thắng",
                    options = doiThangOptions,
                    selectedOption = doiThang,
                    onOptionSelected = { doiThang = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDatePicker(
                    label = "Ngày dự kiến",
                    value = convertLocalDateTimeToMillis(ngayGioDuKien),
                    onDateSelected = {
                        if (it != null) {
                            ngayGioDuKien = convertMillisToLocalDateTime(it);
                        }
                    },
                    onDismiss = {});
                Spacer(modifier = Modifier.height(16.dp))

                InputDatePicker(
                    label = "Ngày thực tế",
                    value = convertLocalDateTimeToMillis(ngayGioThucTe),
                    onDateSelected =  {
                        if (it != null) {
                            ngayGioThucTe = convertMillisToLocalDateTime(it);
                        }
                    },
                    onDismiss = {});
                Spacer(modifier = Modifier.height(16.dp))

                InputFloatField(
                    value = thoiGianDaThiDau,
                    label = "Thời gian đã thi đấu",
                    onValueChange = { thoiGianDaThiDau = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    name = "Trọng tài",
                    options = trongTaiOptions,
                    selectedOption = trongTai,
                    onOptionSelected = { trongTai = it })
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val buttonModifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                    Button(
                        onClick = { navController.popBackStack() },
                        shape = RoundedCornerShape(7.dp),
                        contentPadding = PaddingValues(),
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(
                                                0xFF4568DC
                                            ), Color(0xFFB06AB3)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Cancel", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Button(
                        onClick = { onClick() },
                        shape = RoundedCornerShape(7.dp),
                        contentPadding = PaddingValues(),
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(
                                                0xFFDC456F
                                            ), Color(0xFFB06AB3)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Finish", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
fun LichThiDauInputScreenPreview() {
    MaterialTheme {
        LichThiDauInputScreen(rememberNavController())
    }
}