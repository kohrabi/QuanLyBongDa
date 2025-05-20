package com.example.quanlybongda.ui.jetpackcompose.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
// import androidx.compose.foundation.shape.CircleShape // Không thấy dùng CircleShape trực tiếp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
// import androidx.compose.ui.platform.LocalContext // Không cần nếu chỉ dùng drawable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Import R class của project bạn
import com.example.quanlybongda.R // << THAY com.example.mobileui BẰNG PACKAGE THỰC TẾ CỦA BẠN NẾU KHÁC

// Màu sắc (giữ nguyên)
val screenBackgroundColor = Color(0xFFFFFFFF)
val mainContentBackgroundColor = Color(0xFF181928)
val playerCardBackgroundColor = Color(0xFF414158)
val textColorWhite = Color.White
val textColorSecondary = Color(0xFF797979)
val playerNumberColor = Color(0xFFD1B4FF)
val iconTintColor = Color.White


@Composable
fun TraCuu() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = screenBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(28.dp)) // Clip ở đây có thể không cần nếu background bên trong đã có shape
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = mainContentBackgroundColor,
                    shape = RoundedCornerShape(28.dp) // Áp dụng shape cho background
                )
                .shadow(
                    elevation = 8.dp,
                    spotColor = Color(0x40000000),
                    shape = RoundedCornerShape(28.dp)
                )
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            // Top Row with logos (Sử dụng drawable nếu muốn)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, // Hoặc Arrangement.End nếu chỉ có logo phải
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 18.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Ví dụ thêm logo bên trái từ drawable
                // Image(
                //     painter = painterResource(id = R.drawable.your_left_logo_name),
                //     contentDescription = "Left Logo",
                //     modifier = Modifier.height(24.dp) // Điều chỉnh kích thước
                // )

                Spacer(modifier = Modifier.weight(1f)) // Để đẩy logo phải về cuối

                // Ví dụ thêm logo bên phải từ drawable
                // Image(
                //     painter = painterResource(id = R.drawable.your_right_logo_name),
                //     contentDescription = "Right Logo",
                //     modifier = Modifier.height(24.dp) // Điều chỉnh kích thước
                // )
                // Nếu không có logo, bạn có thể giữ Box trống hoặc xóa Row này
                Box(modifier = Modifier.height(21.dp)) // Giữ không gian nếu không có logo
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 25.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { /* TODO: Handle back action */ }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = iconTintColor
                    )
                }
                Text(
                    "Player Statistic",
                    color = textColorWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* TODO: Handle search action */ }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = iconTintColor
                    )
                }
            }

            // --- Player Cards ---
            // Đảm bảo các file ảnh sau có trong res/drawable:
            // logo_barca.png
            // brandon_baptista_player.png
            // craig_vetrovs_player.png
            // memphis_depay_player.png
            // frenkie_dejong_player.png

            PlayerCardItem(
                clubLogoResId = R.drawable.barca_logo_4x, // Giả sử tất cả từ Barca
                playerName = "Brandon Baptista",
                playerPosition = "Goalkeeper",
                playerNumber = "19",
                playerImageResId = R.drawable.depay_barca // << SỬA CHO ĐÚNG
            )
            PlayerCardItem(
                clubLogoResId = R.drawable.barca_logo_4x,
                playerName = "Craig Vetrovs",
                playerPosition = "Right Back",
                playerNumber = "24",
                playerImageResId = R.drawable.dejong_barca // << SỬA CHO ĐÚNG
            )
            PlayerCardItem(
                clubLogoResId = R.drawable.tottenham_logo,
                playerName = "Memphis Depay",
                playerPosition = "Striker",
                playerNumber = "9",
                playerImageResId = R.drawable.depay_barca // << SỬA CHO ĐÚNG
            )
            PlayerCardItem(
                clubLogoResId = R.drawable.mancity_logo,
                playerName = "Frenkie De Jong",
                playerPosition = "Midfielder",
                playerNumber = "21",
                playerImageResId = R.drawable.dejong_barca // << SỬA CHO ĐÚNG
            )
        }
    }
}

@Composable
fun PlayerCardItem(
    clubLogoResId: Int,
    playerName: String,
    playerPosition: String,
    playerNumber: String,
    playerImageResId: Int,
    cardBackgroundColor: Color = playerCardBackgroundColor,
    textColor: Color = textColorWhite,
    secondaryTextColor: Color = textColorSecondary,
    numberColor: Color = playerNumberColor
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .background(
                color = cardBackgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            // .clip(RoundedCornerShape(16.dp)) // Không cần clip ở đây nếu background đã có shape
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.6f)
            ) {
                Image(
                    painter = painterResource(id = clubLogoResId),
                    contentDescription = "Club Logo", // Có thể cải thiện: "${playerName}'s Club Logo"
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(bottom = 8.dp)
                )
                Text(
                    playerName,
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    playerPosition,
                    color = secondaryTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    playerNumber,
                    color = numberColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight(), // Giúp ảnh căn giữa theo chiều dọc nếu Row cao hơn ảnh
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(id = playerImageResId),
                    contentDescription = playerName, // Ảnh của cầu thủ
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(110.dp)
                        .width(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TraCuuScreenPreview() {
    // MaterialTheme { // Bỏ comment nếu bạn có MaterialTheme định nghĩa
    TraCuu()
    // }
}