package com.example.quanlybongda.ui.jetpackcompose.screens.Input

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
import com.example.quanlybongda.Database.Schema.BanThang
import com.example.quanlybongda.ui.jetpackcompose.screens.AppTopBar
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.InputFloatField
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BanThangInputScreen(
    maTD : Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current;
    val coroutineScope = rememberCoroutineScope()
    var doiBongOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var cauThuDoiMotOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var cauThuDoiHaiOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var loaiBTOptions by remember { mutableStateOf(listOf<OptionValue>()) }

    var doiBong by remember { mutableStateOf(OptionValue.DEFAULT) }
    var cauThu by remember { mutableStateOf(OptionValue.DEFAULT) }
    var thoiDiem by remember { mutableStateOf(0.0f) }
    var loaiBT by remember { mutableStateOf(OptionValue.DEFAULT) }
    val onClick = {
        coroutineScope.launch {
            viewModel.banThangDAO.upsertBanThang(BanThang(
                maTD = maTD,
                thoiDiem = thoiDiem,
                maCT = cauThu.value,
                maLBT = loaiBT.value,
                deleted = false,
            ))
            delay(500);
            navController.popBackStack()
        }
    };

    LaunchedEffect(Unit) {
        launch {
            loaiBTOptions = viewModel.banThangDAO.selectAllLoaiBT().map { OptionValue(it.maLBT, it.tenLBT) };
        }
        val lichThiDau = viewModel.lichThiDauDAO.selectLichThiDauMaTD(maTD);
        if (lichThiDau == null)
            return@LaunchedEffect;
        val doiBongs = mutableListOf<OptionValue>();
        launch {
            val doiMot = viewModel.doiBongDAO.selectDoiBongMaDoi(lichThiDau.doiMot)!!;
//          if (doiMot != null)
            doiBongs.add(OptionValue(doiMot.maDoi!!, doiMot.tenDoi));
            cauThuDoiMotOptions = viewModel.cauThuDAO.selectCauThuDoiBong(doiMot.maDoi).map { OptionValue(it.maCT!!, it.tenCT) };
        }
        launch {
            val doiHai = viewModel.doiBongDAO.selectDoiBongMaDoi(lichThiDau.doiHai)!!;
//          if (doiHai != null)
            doiBongs.add(OptionValue(doiHai.maDoi!!, doiHai.tenDoi));
            doiBongOptions = doiBongs;
            cauThuDoiHaiOptions = viewModel.cauThuDAO.selectCauThuDoiBong(doiHai.maDoi).map { OptionValue(it.maCT!!, it.tenCT) };
        }
    }

    Scaffold(
        containerColor = DarkColorScheme.background,
        topBar = {
            AppTopBar(
                title = "Nhập bàn thắng",
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
                InputDropDownMenu("Đội bóng", doiBongOptions, doiBong, { doiBong = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    name ="Cầu thủ",
                    options = (
                        if (doiBongOptions.size == 0)
                            listOf();
                        else if (doiBong.value == doiBongOptions[0].value)
                            cauThuDoiMotOptions
                        else cauThuDoiHaiOptions
                    ),
                    selectedOption =  cauThu,
                    onOptionSelected = { cauThu = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputFloatField(
                    value = thoiDiem,
                    label = "Thời điểm",
                    onValueChange = { thoiDiem = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    name = "Loại bàn thắng",
                    options = loaiBTOptions,
                    selectedOption = loaiBT,
                    onOptionSelected = { loaiBT = it })
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val buttonModifier = Modifier.weight(1f).height(48.dp)
                    Button(
                        onClick = { navController.popBackStack() },
                        shape = RoundedCornerShape(7.dp),
                        contentPadding = PaddingValues(),
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                brush = Brush.horizontalGradient(colors = listOf(Color(0xFF4568DC), Color(0xFFB06AB3)))
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
                            modifier = Modifier.fillMaxSize().background(
                                brush = Brush.horizontalGradient(colors = listOf(Color(0xFFDC456F), Color(0xFFB06AB3)))
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
fun BanThangInputScreenPreview() {
    MaterialTheme {
        BanThangInputScreen(1, rememberNavController())
    }
}