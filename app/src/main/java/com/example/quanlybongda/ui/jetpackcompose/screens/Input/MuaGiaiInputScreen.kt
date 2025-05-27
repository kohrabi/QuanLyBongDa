package com.example.quanlybongda.ui.jetpackcompose.screens.Input

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDatePicker
import com.example.quanlybongda.ui.jetpackcompose.screens.InputTextField
import com.example.quanlybongda.ui.jetpackcompose.screens.convertLocalDateTimeToMillis
import com.example.quanlybongda.ui.jetpackcompose.screens.convertLocalDateToMillis
import com.example.quanlybongda.ui.jetpackcompose.screens.convertMillisToLocalDate
import com.example.quanlybongda.ui.jetpackcompose.screens.convertMillisToLocalDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MuaGiaiInputScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val context = LocalContext.current;
    val coroutineScope = rememberCoroutineScope()
    var tenMG by remember { mutableStateOf("") }
    var ngayDienRa by remember { mutableStateOf(LocalDate.now()) }
    var ngayKetThuc by remember { mutableStateOf(LocalDate.now()) }
    var submitted by remember { mutableStateOf(false) }
    val onClick : () -> Unit = {
        if (!submitted) {
            coroutineScope.launch {
                viewModel.muaGiaiDAO.upsertDSMuaGiai(
                    MuaGiai(
                        tenMG = tenMG,
                        ngayDienRa = ngayDienRa,
                        ngayKetThuc = ngayKetThuc,
                    )
                )
                submitted = true;
                delay(500);
                navController.popBackStack()
            }
        }
    };

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
                // Username
                InputTextField(
                    value = tenMG,
                    label = "Tên mùa giải",
                    onValueChange = { tenMG = it });
                Spacer(modifier = Modifier.height(16.dp))

                InputDatePicker(
                    label = "Ngày bắt đầu",
                    value = convertLocalDateToMillis(ngayDienRa),
                    onDateSelected = {
                        if (it != null) {
                            ngayDienRa = convertMillisToLocalDate(it);
                        }
                    },
                    onDismiss = {});
                Spacer(modifier = Modifier.height(16.dp))

                InputDatePicker(
                    label = "Ngày kết thúc",
                    value = convertLocalDateToMillis(ngayKetThuc),
                    onDateSelected =  {
                        if (it != null) {
                            ngayKetThuc = convertMillisToLocalDate(it);
                        }
                    },
                    onDismiss = {});
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
fun MuaGiaiInputScreenPreview() {
    MaterialTheme {
        MuaGiaiInputScreen(rememberNavController())
    }
}