package com.example.quanlybongda.ui.jetpackcompose.screens.Input

import com.example.quanlybongda.ui.jetpackcompose.screens.TextField
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quanlybongda.Database.DatabaseViewModel
// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush
import com.example.quanlybongda.R // << QUAN TRỌNG: Đảm bảo bạn đã import R

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
    var notes by remember { mutableStateOf("") }

    Scaffold(
        backgroundColor = Color(0xFF181928),
        bottomBar = {
            // Thanh điều hướng dưới cùng (phần comment giữ nguyên, nếu bạn dùng lại thì cần sửa tương tự)
            /*
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF222232))
                    .padding(vertical = 12.dp)
            ) {
                val iconModifier = Modifier.size(24.dp)
                // Nếu dùng lại phần này, các URL ở đây cũng cần đổi sang R.drawable
                // Ví dụ "file:///android_asset/SignIn2Assets/Home.png" -> R.drawable.home_icon (sau khi copy file vào drawable)
                val navIcons = listOf(
                    R.drawable.home_icon_drawable to "Home", // Ví dụ thay thế
                    R.drawable.nav_icon_2_drawable to "Nav Icon 2", // Ví dụ thay thế
                    R.drawable.nav_icon_3_drawable to "Nav Icon 3", // Ví dụ thay thế
                    R.drawable.nav_icon_4_drawable to "Nav Icon 4"  // Ví dụ thay thế
                )
                navIcons.forEach { (drawableId, description) ->
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(drawableId).crossfade(true).build(),
                        contentDescription = description,
                        contentScale = ContentScale.Fit,
                        modifier = iconModifier
                    )
                }
            }
            */

//            BottomNavigation(
//                backgroundColor = Color(0xFF1C1C2A), // Nền cho BottomNavigation
//                contentColor = Color.White // Màu mặc định cho icon và text
//            ) {
//                // Sử dụng Icons.Filled hoặc Icons.Outlined cho nhất quán
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
//                    selected = true, // Mục này đang được chọn
//                    onClick = { /* TODO: Handle navigation */ },
//                    selectedContentColor = Color.White, // Màu khi được chọn
//                    unselectedContentColor = Color.Gray // Màu khi không được chọn
//                )
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Filled.List, contentDescription = "Ranking") },
//                    selected = false,
//                    onClick = { /* TODO: Handle navigation */ },
//                    selectedContentColor = Color.White,
//                    unselectedContentColor = Color.Gray
//                )
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Filled.Schedule, contentDescription = "Schedule") },
//                    selected = false,
//                    onClick = { /* TODO: Handle navigation */ },
//                    selectedContentColor = Color.White,
//                    unselectedContentColor = Color.Gray
//                )
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
//                    selected = false,
//                    onClick = { /* TODO: Handle navigation */ },
//                    selectedContentColor = Color.White,
//                    unselectedContentColor = Color.Gray
//                )
//            }
        }
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
                TextField(playerName, "Tên cầu thủ", { playerName = it })
                Spacer(modifier = Modifier.height(16.dp))

                TextField(birthday, "Ngày sinh", { birthday = it })
                Spacer(modifier = Modifier.height(16.dp))

                TextField(playerType, "Loại cầu thủ", { playerType = it })
                Spacer(modifier = Modifier.height(16.dp))

                TextField(notes, "Ghi chú", { notes = it }, modifier = Modifier.height(100.dp))
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