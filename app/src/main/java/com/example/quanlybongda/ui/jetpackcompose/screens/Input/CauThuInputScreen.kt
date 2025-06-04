package com.example.quanlybongda.ui.jetpackcompose.screens.Input

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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.ui.jetpackcompose.screens.AppTopBar
import com.example.quanlybongda.ui.jetpackcompose.screens.CauThuScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDatePicker
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.InputIntField
import com.example.quanlybongda.ui.jetpackcompose.screens.InputTextField
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import com.example.quanlybongda.ui.jetpackcompose.screens.convertLocalDateTimeToMillis
import com.example.quanlybongda.ui.jetpackcompose.screens.convertLocalDateToMillis
import com.example.quanlybongda.ui.jetpackcompose.screens.convertMillisToLocalDate
import com.example.quanlybongda.ui.jetpackcompose.screens.convertMillisToLocalDateTime
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CauThuInputScreen(
    cauThu: CauThu = CauThu(0, "", LocalDate.now(), "", 0, 0, 0),
    maDoi: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current;
    val coroutineScope = rememberCoroutineScope()

    var submitted by remember { mutableStateOf(false) }
    var clicked by remember { mutableStateOf(false) }

    var loaiCTOptions by remember { mutableStateOf(listOf<OptionValue>()) }

    var tenCT by remember { mutableStateOf(cauThu.tenCT) }
    var ngaySinh by remember { mutableStateOf(cauThu.ngaySinh) }
    var loaiCT by remember { mutableStateOf(OptionValue.DEFAULT) }
    var ghiChu by remember { mutableStateOf(cauThu.ghiChu) }
    var soAo by remember { mutableStateOf<Int?>(cauThu.soAo) }
    var tuoiMin by remember { mutableStateOf(0L) }
    var tuoiMax by remember { mutableStateOf(90L) }
    val onClick = {
        clicked = true;
        coroutineScope.launch {
            if (submitted)
                return@launch;
            if (loaiCT.value == null ||
                soAo == null)
                return@launch;
            val ngaySinhMin = LocalDate.now().minusYears(tuoiMax);
            val ngaySinhMax = LocalDate.now().minusYears(tuoiMin);
            if (ngaySinh.isBefore(ngaySinhMin) || ngaySinh.isAfter(ngaySinhMax)) {
                Toast.makeText(context, "Ngày sinh phải lớn hơn ${ngaySinhMin} " +
                        "và bé hơn ${ngaySinhMax}", Toast.LENGTH_SHORT).show();
            }
            viewModel.cauThuDAO.upsertCauThu(
                CauThu(
                    maCT = cauThu.maCT,
                    tenCT = tenCT,
                    ngaySinh = ngaySinh,
                    maLCT = loaiCT.value!!,
                    maDoi = maDoi,
                    ghiChu = ghiChu,
                    soAo = soAo!!,
                )
            )
            submitted = true;
            delay(500);
            navController.popBackStack()
        }
    };

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {

            val loaiCTs = viewModel.cauThuDAO.selectAllLoaiCT();
            loaiCTOptions = loaiCTs.map { OptionValue(value = it.maLCT, label = it.tenLCT) }

            loaiCT = loaiCTOptions.find { it.value == cauThu.maLCT } ?: OptionValue.DEFAULT;
            tuoiMin = viewModel.thamSoDAO.selectThamSo("tuoiMin")!!.giaTri.toLong();
            tuoiMax = viewModel.thamSoDAO.selectThamSo("tuoiMax")!!.giaTri.toLong();
        }
    }

    Scaffold(
        containerColor = DarkColorScheme.background,
        topBar = {
            AppTopBar(
                title = "Nhập cầu thủ",
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
                InputTextField(
                    value = tenCT,
                    label = "Tên cầu thủ",
                    onValueChange = { tenCT = it },
                    isError = tenCT.isEmpty() && clicked,
                    errorMessage = "Tên cầu thủ trống"
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputDatePicker(
                    label = "Ngày sinh",
                    value = convertLocalDateToMillis(ngaySinh),
                    onDateSelected = {
                        if (it != null) {
                            ngaySinh = convertMillisToLocalDate(it)
                            val ngaySinhMin = LocalDate.now().minusYears(tuoiMax);
                            val ngaySinhMax = LocalDate.now().minusYears(tuoiMin);
                            if (ngaySinh.isBefore(ngaySinhMin) || ngaySinh.isAfter(ngaySinhMax)) {
                                Toast.makeText(context, "Ngày sinh phải lớn hơn ${ngaySinhMin} " +
                                        "và bé hơn ${ngaySinhMax}", Toast.LENGTH_SHORT).show();
                            }
                            ngaySinh = ngaySinh.coerceIn(ngaySinhMin, ngaySinhMax);
                        }
                    },
                    onDismiss = {}
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    label = "Loại cầu thủ",
                    options = loaiCTOptions,
                    selectedOption = loaiCT,
                    onOptionSelected = { loaiCT = it },
                    showEmptyError = clicked);
                Spacer(modifier = Modifier.height(16.dp))

                InputTextField(
                    value = ghiChu,
                    label = "Ghi chú",
                    onValueChange = { ghiChu = it },
                    isError = ghiChu.isEmpty() && clicked,
                    errorMessage = "Ghi chú trống",
                    modifier = Modifier.height(100.dp))
                Spacer(modifier = Modifier.height(32.dp))

                InputIntField(
                    value = soAo,
                    label = "Số áo",
                    onValueChange = { soAo = it },

                )
                Spacer(modifier = Modifier.height(32.dp))

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
fun SignInScreen3Preview() {
    MaterialTheme {
        CauThuInputScreen(maDoi = 1, navController = rememberNavController())
    }
}