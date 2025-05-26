package com.example.quanlybongda.ui.jetpackcompose.screens.Input

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import com.example.quanlybongda.Database.Schema.Loai.SanNha
import com.example.quanlybongda.navigatePopUpTo
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDatePicker
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.InputTextField
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import com.example.quanlybongda.ui.jetpackcompose.screens.convertLocalDateTimeToMillis
import com.example.quanlybongda.ui.jetpackcompose.screens.convertMillisToLocalDateTime
import com.example.quanlybongda.ui.theme.darkContentBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.StackWalker.Option
import java.time.LocalDateTime


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LichThiDauInputScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
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
    var thoiGianDaThiDau by remember { mutableStateOf("") }
    val onClick = {
        coroutineScope.launch {
            if (currentMuaGiai == null) {
                Toast.makeText(context, "WARNING: muaGiai không hợp lệ", Toast.LENGTH_SHORT).show();
                navigatePopUpTo(navController, "muaGiai");
                return@launch;
            }
            if (thoiGianDaThiDau.toFloatOrNull() == null) {
                Toast.makeText(context, "WARNING: thoiGianDaThiDau không hợp lệ", Toast.LENGTH_SHORT).show();
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
        backgroundColor = darkContentBackground,
        modifier = modifier.padding(top = 24.dp)
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
                InputDropDownMenu("Đội một", doiBongOptions, doiMot, { doiMot = it });
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu("Đội hai", doiBongOptions, doiHai, { doiHai = it });
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu("Vòng thi đấu", vongTDOptions, vongTD, { vongTD = it  } )
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu("Đội Thắng", doiThangOptions, doiThang, { doiThang = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDatePicker("Ngày dự kiến",
                    value = convertLocalDateTimeToMillis(ngayGioDuKien),
                    onDateSelected = {
                        if (it != null) {
                            ngayGioDuKien = convertMillisToLocalDateTime(it);
                        }
                    },
                    onDismiss = {});
                Spacer(modifier = Modifier.height(16.dp))

                InputDatePicker("Ngày thực tế",
                    value = convertLocalDateTimeToMillis(ngayGioThucTe),
                    onDateSelected =  {
                        if (it != null) {
                            ngayGioThucTe = convertMillisToLocalDateTime(it);
                        }
                    },
                    onDismiss = {});
                Spacer(modifier = Modifier.height(16.dp))

                InputTextField(thoiGianDaThiDau, "Thời gian đã thi đấu", { thoiGianDaThiDau = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu("Trọng tài", trongTaiOptions, trongTai, { trongTai = it })
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
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