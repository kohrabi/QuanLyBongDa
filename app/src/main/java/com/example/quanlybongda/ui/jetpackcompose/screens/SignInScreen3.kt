package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.BorderStroke
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
// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush
import com.example.quanlybongda.R

@Composable
fun SignIn3() { // Đổi tên hàm cho rõ nghĩa hơn, hoặc bạn giữ SignIn3()
    val context = LocalContext.current

    Scaffold( // Sử dụng Scaffold để có bottomBar cố định
        backgroundColor = Color(0xFF181928), // Màu nền tối cho toàn bộ Scaffold (phần ngoài của Card nội dung)
        bottomBar = {
            // Thanh điều hướng dưới cùng
//            Row(
//                horizontalArrangement = Arrangement.SpaceAround,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(color = Color(0xFF222232)) // Nền cho thanh điều hướng
//                    .padding(vertical = 12.dp)
//            ) {
//                val iconModifier = Modifier.size(24.dp)
//                val navIcons = listOf(
//                    "file:///android_asset/SignIn2Assets/Home.png" to "Home",
//                    "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/6031839a-8a45-45a1-b81a-42f02b16e98b" to "Nav Icon 2",
//                    "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/beae6481-a4d2-45d7-9327-d6f05e63cb0c" to "Nav Icon 3",
//                    "https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/df23c105-438c-4fc0-953e-2a4ea028254a" to "Nav Icon 4"
//                )
//                navIcons.forEach { (url, description) ->
//                    AsyncImage(
//                        model = ImageRequest.Builder(context).data(url).crossfade(true).build(),
//                        contentDescription = description,
//                        contentScale = ContentScale.Fit,
//                        modifier = iconModifier
//                    )
//                }
//            }

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
    ) { innerScaffoldPadding -> // Padding được cung cấp bởi Scaffold để nội dung không bị che bởi top/bottom bar

        // Column chính chứa toàn bộ nội dung có thể cuộn (ảnh nền, các ô input, buttons)
        Column(
            modifier = Modifier
                .padding(innerScaffoldPadding) // Áp dụng padding từ Scaffold
                .fillMaxSize() // Lấp đầy không gian còn lại
                .verticalScroll(rememberScrollState()) // Cho phép cuộn toàn bộ nội dung này
            // Không cần background ở đây nữa vì Scaffold đã có nền tối
        ) {
            // 1. Phần hình ảnh nền lớn ở trên cùng
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp) // Chiều cao cho vùng ảnh nền
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.sign_in_banner) // URL ảnh nền của bạn
                        .crossfade(true)
                        .build(),
                    contentDescription = "Header Background Image",
                    contentScale = ContentScale.Crop, // Crop để lấp đầy
                    modifier = Modifier.fillMaxSize()
                    // .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)) // Không cần clip ở đây nữa nếu toàn bộ Column không bo góc
                )
                // Các logo nhỏ overlay trên ảnh nền (nếu có, giống code cũ của bạn)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween // Để đẩy 2 Row ra xa nhau
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/937dcb20-0c08-4765-8b8b-adfbcf74189f")
                                .crossfade(true).build(),
                            contentDescription = "Overlay Logo 1",
                            modifier = Modifier.width(54.dp).height(21.dp).clip(RoundedCornerShape(32.dp))
                        )
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5207b008-4e8b-40e8-a4c0-932a5065e653")
                                .crossfade(true).build(),
                            contentDescription = "Overlay Logo 2",
                            modifier = Modifier.width(66.dp).height(11.dp)
                        )
                    }
                    // Phần "Team Name" có thể nằm ở đây nếu nó overlay trên ảnh
                    // Hiện tại tôi bỏ qua để tập trung vào các ô input bên dưới
                }
            }

            // Column chứa các ô điền thông tin và nút bấm, có padding chung
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp) // Padding chung cho form
            ) {
                // Ô "Player Name"
                Text("Player Name", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Cho tất cả các ô input có cùng chiều dài
                        .height(49.dp)
                        .background(color = Color(0xFF222232), shape = RoundedCornerShape(4.dp))
                ) { /* TODO: TextField "Player Name" */ }

                Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa các ô

                // Ô "Birthday"
                Text("Birthday", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                        .background(color = Color(0xFF222232), shape = RoundedCornerShape(4.dp))
                ) { /* TODO: TextField "Birthday" */ }

                Spacer(modifier = Modifier.height(16.dp))

                // Ô "Player Type"
                Text("Player Type", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                        .background(color = Color(0xFF222232), shape = RoundedCornerShape(4.dp))
                ) { /* TODO: TextField "Player Type" */ }

                Spacer(modifier = Modifier.height(16.dp))

                // Ô "Notes"
                Text("Notes", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(bottom = 4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp) // Cho ô Notes cao hơn
                        .background(color = Color(0xFF222232), shape = RoundedCornerShape(4.dp))
                ) { /* TODO: TextField "Notes" */ }

                Spacer(modifier = Modifier.height(32.dp)) // Dịch button xuống một chút nữa

                // Hai Button "New Player" và "Finish"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Khoảng cách giữa 2 button
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
            // Spacer ở cuối để đảm bảo nội dung không bị che khuất hoàn toàn khi cuộn xuống hết
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101010) // Màu nền preview đậm hơn chút
@Composable
fun PlayerEditScreenPreview() { // Đổi tên hàm Preview
    MaterialTheme { // Bọc trong MaterialTheme nếu bạn dùng các thành phần Material
        SignIn3()
    }
}