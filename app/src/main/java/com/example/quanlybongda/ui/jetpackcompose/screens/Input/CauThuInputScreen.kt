package com.example.quanlybongda.ui.jetpackcompose.screens.Input

import android.widget.Toast
import com.example.quanlybongda.ui.jetpackcompose.screens.InputTextField
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDatePicker
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import com.example.quanlybongda.ui.jetpackcompose.screens.convertLocalDateTimeToMillis
import com.example.quanlybongda.ui.jetpackcompose.screens.convertMillisToLocalDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush

@Composable
fun CauThuInputScreen(
    maDoi: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val context = LocalContext.current;
    val coroutineScope = rememberCoroutineScope()
    var loaiCTOptions by remember { mutableStateOf(listOf<OptionValue>()) }

    var tenCT by remember { mutableStateOf("") }
    var ngaySinh by remember { mutableStateOf(LocalDateTime.now()) }
    var loaiCT by remember { mutableStateOf(OptionValue(0, "")) }
    var ghiChu by remember { mutableStateOf("") }
    var soAo by remember { mutableStateOf("") }
    val onClick = {
        coroutineScope.launch {
            if (soAo.toIntOrNull() == null) {
                Toast.makeText(context, "WARNING: soAo không hợp lệ", Toast.LENGTH_SHORT).show();
                return@launch;
            }
            viewModel.cauThuDAO.upsertCauThu(
                CauThu(
                    tenCT = tenCT,
                    ngaySinh = ngaySinh.toLocalDate(),
                    maLCT = loaiCT.value,
                    maDoi = maDoi,
                    ghiChu = ghiChu,
                    soAo = soAo.toInt(),
                )
            )
            delay(500);
            navController.popBackStack()
        }
    };

    LaunchedEffect(Unit) {
        val loaiCTs = viewModel.cauThuDAO.selectAllLoaiCT();
        loaiCTOptions = loaiCTs.map { OptionValue(value = it.maLCT, label = it.tenLCT) }
    }

    Scaffold(
        backgroundColor = Color(0xFF181928),
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
                InputTextField(tenCT, "Tên cầu thủ", { tenCT = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDatePicker("Ngày sinh",
                    convertLocalDateTimeToMillis(ngaySinh),
                    onDateSelected = {
                        if (it != null)
                            ngaySinh = convertMillisToLocalDateTime(it)
                    },
                    onDismiss = {}
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu("Loại cầu thủ", loaiCTOptions, loaiCT, { loaiCT = it });
                Spacer(modifier = Modifier.height(16.dp))

                InputTextField(ghiChu, "Ghi chú", { ghiChu = it }, modifier = Modifier.height(100.dp))
                Spacer(modifier = Modifier.height(32.dp))

                InputTextField(soAo, "Số áo", { soAo = it })
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
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
        CauThuInputScreen(1, rememberNavController())
    }
}