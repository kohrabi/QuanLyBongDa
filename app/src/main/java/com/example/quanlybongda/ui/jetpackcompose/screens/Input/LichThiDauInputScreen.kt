package com.example.quanlybongda.ui.jetpackcompose.screens.Input

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDatePicker
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import com.example.quanlybongda.ui.jetpackcompose.screens.convertLocalDateTimeToMillis
import com.example.quanlybongda.ui.jetpackcompose.screens.convertMillisToLocalDateTime
import java.time.LocalDateTime


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LichThiDauInputScreen(
    appController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var doiBongOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var vongTDOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var doiThangOptions by remember { mutableStateOf(listOf<OptionValue>()) }

    var doiMot by remember { mutableStateOf(OptionValue.DEFAULT) }
    var doiHai by remember { mutableStateOf(OptionValue.DEFAULT) }
    var vongTD by remember { mutableStateOf(OptionValue.DEFAULT) }
    var doiThang by remember { mutableStateOf(OptionValue.DEFAULT) }
    var ngayGioDuKien by remember { mutableStateOf(LocalDateTime.now()) }
    var ngayGioThucTe by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        doiBongOptions = viewModel.doiBongDAO.selectAllDoiBong().map { OptionValue(value = it.maDoi, label = it.tenDoi) };
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
        backgroundColor = Color(0xFF181928),
        modifier = modifier
    ) { innerScaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(innerScaffoldPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(260.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        AsyncImage(
//                            model = ImageRequest.Builder(context)
//                                //.data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/937dcb20-0c08-4765-8b8b-adfbcf74189f") // THAY THẾ
//                                .data(R.drawable.football_stadium) // << THAY BẰNG TÊN FILE DRAWABLE CỦA BẠN
//                                .crossfade(true).build(),
//                            contentDescription = "Overlay Logo 1",
//                            modifier = Modifier.width(54.dp).height(21.dp).clip(RoundedCornerShape(32.dp))
//                        )
//                        AsyncImage(
//                            model = ImageRequest.Builder(context)
//                                //.data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5207b008-4e8b-40e8-a4c0-932a5065e653") // THAY THẾ
//                                .data(R.drawable.football_stadium) // << THAY BẰNG TÊN FILE DRAWABLE CỦA BẠN
//                                .crossfade(true).build(),
//                            contentDescription = "Overlay Logo 2",
//                            modifier = Modifier.width(66.dp).height(11.dp)
//                        )
//                    }
//                    // Phần "Team Name" (nếu có)
//                }
//            }

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val buttonModifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                    Button(
                        onClick = { /*TODO*/ },
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
                            Text("New Player", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Button(
                        onClick = { /*TODO*/ },
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