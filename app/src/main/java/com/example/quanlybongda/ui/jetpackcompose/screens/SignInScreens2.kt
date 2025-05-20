package com.example.quanlybongda.ui.jetpackcompose.screens

// import androidx.compose.foundation.BorderStroke // Không thấy sử dụng trong code này
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quanlybongda.R // << QUAN TRỌNG: Import R class của project

// Phần comment CoilImage cũ đã đúng khi bạn bỏ đi
// @Composable
// fun CoilImage(imageModel: () -> Any, imageOptions: ImageOptions, modifier: Modifier) { ... }
// data class ImageOptions(val contentScale: ContentScale)

@Composable
fun SignIn2() {
    val context = LocalContext.current

    Scaffold(
        backgroundColor = Color(0xFFFFFFFF),
        bottomBar = {
            // Phần bottomBar đã comment: Nếu bạn muốn dùng lại, hãy sửa như ví dụ dưới
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

                // Giả sử bạn đã copy các icon từ assets vào drawable:
                // home_icon.png, list_icon.png, clock_icon.png, me_icon.png

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.home_icon) // << THAY THẾ
                        .crossfade(true)
                        .build(),
                    contentDescription = "Home Icon",
                    contentScale = ContentScale.Fit,
                    modifier = iconModifier
                )
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.list_icon) // << THAY THẾ
                        .crossfade(true)
                        .build(),
                    contentDescription = "List Icon",
                    contentScale = ContentScale.Fit,
                    modifier = iconModifier
                )
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.clock_icon) // << THAY THẾ
                        .crossfade(true)
                        .build(),
                    contentDescription = "Clock Icon",
                    contentScale = ContentScale.Fit,
                    modifier = iconModifier
                )
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.me_icon) // << THAY THẾ
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Icon",
                    contentScale = ContentScale.Fit,
                    modifier = iconModifier
                )
            }
            */

            // BottomNavigation hiện tại đang sử dụng Material Icons, không cần thay đổi
            BottomNavigation(
                backgroundColor = Color(0xFF1C1C2A),
                contentColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    selected = true,
                    onClick = { /* TODO */ },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Ranking") },
                    selected = false,
                    onClick = { /* TODO */ },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Schedule, contentDescription = "Schedule") },
                    selected = false,
                    onClick = { /* TODO */ },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = { /* TODO */ },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray
                )
            }
        }
    ) { innerPadding ->
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        // .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/e79db67f-c718-419e-95d5-5e909f0fc46b") // THAY THẾ
                        .data(R.drawable.football_stadium) // << THAY BẰNG TÊN FILE DRAWABLE
                        .crossfade(true)
                        .build(),
                    contentDescription = "Header Image",
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
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                // .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/a4b1923a-eaac-41ed-ae29-3988b8f62e18") // THAY THẾ
                                .data(R.drawable.football_stadium) // << THAY BẰNG TÊN FILE DRAWABLE
                                .crossfade(true)
                                .build(),
                            contentDescription = "Overlay Logo 1",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(54.dp)
                                .height(21.dp)
                                .clip(RoundedCornerShape(32.dp))
                        )
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                // .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/9f4dccce-3e2e-4fc2-8444-7f5658f46138") // THAY THẾ
                                .data(R.drawable.football_stadium) // << THAY BẰNG TÊN FILE DRAWABLE
                                .crossfade(true)
                                .build(),
                            contentDescription = "Overlay Logo 2",
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedButton(
                    onClick = { println("Pressed!") },
                    border = BorderStroke(0.dp, Color.Transparent), // Không viền
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent // Nền trong suốt để thấy Brush
                    ),
                    contentPadding = PaddingValues(), // Bỏ padding mặc định
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(shape = RoundedCornerShape(7.dp)) // Bo góc cho button
                        .background( // Áp dụng Brush cho nền
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF4568DC), Color(0xFFB06AB3))
                            )
                        )
                ) {
                    // Nội dung của Button (Text "ADD")
                    Text(
                        "ADD",
                        color = Color(0xFFFFFFFF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SignInScreen2Preview() {
    // Bạn có thể bọc trong MaterialTheme nếu muốn xem trước với theme của ứng dụng
    // MaterialTheme {
    SignIn2()
    // }
}