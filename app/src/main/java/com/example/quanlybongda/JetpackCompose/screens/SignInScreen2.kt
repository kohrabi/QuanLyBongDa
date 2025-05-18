package com.example.quanlybongda.JetpackCompose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.* // Scaffold, ButtonDefaults, OutlinedButton, Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState // Đã có
import androidx.compose.foundation.verticalScroll // Đã có
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
// import androidx.compose.material.MaterialTheme // Bạn có thể không cần import này trực tiếp ở đây nếu dùng theme mặc định
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext // Import LocalContext
import coil.compose.AsyncImage // << IMPORT ASYNCIMAGE TỪ COIL
import coil.request.ImageRequest // << IMPORT IMAGEREQUEST TỪ COIL


@Composable
fun YourScreenContentWithFixedBottomNav() {
    val context = LocalContext.current // Lấy context ở đây để dùng cho các ImageRequest

    Scaffold(
        backgroundColor = Color(0xFFFFFFFF), // Nền trắng cho toàn bộ màn hình
        bottomBar = {
            // 4. Thanh điều hướng dưới cùng - CỐ ĐỊNH


            BottomNavigation(
                backgroundColor = Color(0xFF1C1C2A),
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    selected = true,
                    onClick = { /* TODO */ }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Ranking") },
                    selected = false,
                    onClick = { /* TODO */ }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Schedule, contentDescription = "Schedule") },
                    selected = false,
                    onClick = { /* TODO */ }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = { /* TODO */ }
                )
            }
        }
    ) { innerPadding -> // innerPadding được cung cấp bởi Scaffold
        // Nội dung chính của màn hình (phần card màu tối, có thể cuộn)
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(color = Color(0xFF181928))
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Phần hình ảnh và nội dung overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage( // THAY THẾ CoilImage bằng AsyncImage
                    model = ImageRequest.Builder(context)
                        .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/e79db67f-c718-419e-95d5-5e909f0fc46b")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Header Image", // Thêm content description
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage( // THAY THẾ CoilImage bằng AsyncImage
                            model = ImageRequest.Builder(context)
                                .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/a4b1923a-eaac-41ed-ae29-3988b8f62e18")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Overlay Logo 1", // Thêm content description
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(54.dp)
                                .height(21.dp)
                                .clip(RoundedCornerShape(32.dp))
                        )
                        AsyncImage( // THAY THẾ CoilImage bằng AsyncImage
                            model = ImageRequest.Builder(context)
                                .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/9f4dccce-3e2e-4fc2-8444-7f5658f46138")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Overlay Logo 2", // Thêm content description
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(66.dp)
                                .height(11.dp)
                        )
                    }
                    Column {
                        Text(
                            "Team Name",
                            color = Color(0xFFFFFFFF),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(49.dp)
                                .background(
                                    color = Color(0xFF222232),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) { /* TODO: TextField */ }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Phần "Home Stadium"
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    "Home Stadium",
                    color = Color(0xFFFFFFFF),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                        .background(
                            color = Color(0xFF222232),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) { /* TODO: TextField */ }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Phần Button "ADD"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedButton(
                    onClick = { println("Pressed!") },
                    border = BorderStroke(0.dp, Color.Transparent),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(shape = RoundedCornerShape(7.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF4568DC), Color(0xFFB06AB3))
                            )
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            "ADD",
                            color = Color(0xFFFFFFFF),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Spacer ở cuối nội dung cuộn để tạo khoảng trống nếu nội dung ngắn
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SignInScreen() {
    YourScreenContentWithFixedBottomNav()
}