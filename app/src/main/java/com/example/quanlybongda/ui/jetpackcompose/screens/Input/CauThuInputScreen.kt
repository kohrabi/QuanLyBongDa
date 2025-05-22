package com.example.quanlybongda.ui.jetpackcompose.screens.Input

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.Loai.LoaiCT
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import java.lang.StackWalker.Option

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush

@Composable
fun CauThuInputScreen(
    appController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var playerName by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var playerType by remember { mutableStateOf("") }
    var loaiCTOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val loaiCTs = viewModel.cauThuDAO.selectAllLoaiCT();
        loaiCTOptions = loaiCTs.map { OptionValue(value = it.maLCT, label = it.tenLCT) }
    }

    Scaffold(
        backgroundColor = Color(0xFF181928),
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
//                AsyncImage(
//                    model = ImageRequest.Builder(context)
//                        .data(R.drawable.sign_in_banner) // Đã đúng: sử dụng drawable. Comment "URL" ở đây có thể bỏ.
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = "Header Background Image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )
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
                InputTextField(playerName, "Tên cầu thủ", { playerName = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputTextField(birthday, "Ngày sinh", { birthday = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu("Loại cầu thủ", loaiCTOptions);
//                InputTextField(playerType, "Loại cầu thủ", { playerType = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputTextField(notes, "Ghi chú", { notes = it }, modifier = Modifier.height(100.dp))
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val buttonModifier = Modifier.weight(1f).height(48.dp)
                    Button(
                        onClick = { /*TODO*/ },
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
        CauThuInputScreen(rememberNavController())
    }
}