package com.example.quanlybongda.JetpackCompose.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material. // Import Icon từ material (M2)
import androidx.compose.material.IconButton // Import IconButton từ material (M2)
import androidx.compose.material.Text // Import Text từ material (M2)
// Nếu bạn dùng Material3, các import trên sẽ là từ androidx.compose.material3.*
// và các Icon sẽ từ androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Icon Back
import androidx.compose.material.icons.filled.Search   // Icon Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // Không cần nếu chỉ dùng drawable resource với Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Bỏ import Coil nếu không dùng AsyncImage cho màn hình này nữa
// import coil.compose.AsyncImage
// import coil.request.ImageRequest
import com.example.quanlybongda.R

// Màu sắc (giữ nguyên hoặc điều chỉnh nếu cần)
val screenBackgroundColor = Color(0xFFFFFFFF)
val mainContentBackgroundColor = Color(0xFF181928)
val playerCardBackgroundColor = Color(0xFF414158) // Màu nền thẻ cầu thủ bạn đã đặt
val textColorWhite = Color.White
val textColorSecondary = Color(0xFF797979)
val playerNumberColor = Color(0xFFD1B4FF)
val iconTintColor = Color.White


@Composable
fun PlayerStatsScreen() {
    // val context = LocalContext.current // Chỉ cần nếu dùng AsyncImage với URL

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = screenBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(28.dp))
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = mainContentBackgroundColor,
                    shape = RoundedCornerShape(28.dp)
                )
                .shadow( // Giảm elevation nếu không muốn bóng đổ quá đậm
                    elevation = 8.dp, // Giảm từ 0.dp để có chút bóng đổ nhẹ nếu muốn
                    spotColor = Color(0x40000000), // Bóng đổ nhẹ hơn
                    shape = RoundedCornerShape(28.dp) // Thêm shape cho shadow khớp với background
                )
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 8.dp) // Điều chỉnh padding trên/dưới của vùng cuộn
        ) {
            // Top Row with logos (Hiện tại đang dùng URL, bạn có thể đổi sang drawable nếu muốn)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 18.dp) // Chỉ cần padding bottom
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp) // Tăng padding ngang cho cân đối
            ) {
                // Ví dụ nếu bạn muốn dùng AsyncImage từ URL cho logo này
                // AsyncImage(
                // model = "URL_LOGO_TRAI",
                // contentDescription = "Left Logo",
                // modifier = Modifier.height(24.dp) // Điều chỉnh kích thước
                // )
                Spacer(modifier = Modifier.weight(1f)) // Để đẩy logo phải về cuối nếu chỉ có 1 logo phải
                // AsyncImage(
                // model = "URL_LOGO_PHAI",
                // contentDescription = "Right Logo",
                // modifier = Modifier.height(24.dp) // Điều chỉnh kích thước
                // )
                // Nếu không có logo ở đây nữa thì có thể xóa Row này hoặc thay bằng Spacer
                // Hiện tại, tôi sẽ để trống Row này theo như code gốc của bạn nhưng không có AsyncImage
                // Nếu bạn muốn loại bỏ hoàn toàn, chỉ cần xóa Row này.
                // Giả sử bạn vẫn muốn giữ không gian này:
                Box(modifier = Modifier.height(21.dp)) // Giữ chiều cao tương tự
            }

            // Title Row - ĐÃ SỬA VỚI ICON MATERIAL
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 25.dp, start = 16.dp, end = 16.dp) // Điều chỉnh padding
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
                    fontSize = 18.sp, // Tăng nhẹ font
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
            // BẠN CẦN CÓ CÁC TỆP ẢNH SAU TRONG res/drawable:
            // logo_barca.png (hoặc .xml)
            // brandon_baptista_player.png
            // craig_vetrovs_player.png
            // memphis_depay_player.png
            // frenkie_dejong_player.png

            PlayerCardItem(
                clubLogoResId = R.drawable.logo_barca,
                playerName = "Brandon Baptista",
                playerPosition = "Goalkeeper",
                playerNumber = "19",
                playerImageResId = R.drawable.dejong_barca // << THAY BẰNG ẢNH ĐÚNG
            )
            PlayerCardItem(
                clubLogoResId = R.drawable.logo_barca,
                playerName = "Craig Vetrovs",
                playerPosition = "Right Back",
                playerNumber = "24",
                playerImageResId = R.drawable.depay_barca // << THAY BẰNG ẢNH ĐÚNG
            )
            PlayerCardItem(
                clubLogoResId = R.drawable.logo_barca,
                playerName = "Memphis Depay",
                playerPosition = "Striker", // << SỬA VỊ TRÍ
                playerNumber = "9",       // << SỬA SỐ ÁO (ví dụ)
                playerImageResId = R.drawable.dejong_barca // << THAY BẰNG ẢNH ĐÚNG
            )
            PlayerCardItem(
                clubLogoResId = R.drawable.logo_barca,
                playerName = "Frenkie De Jong",
                playerPosition = "Midfielder", // << SỬA VỊ TRÍ
                playerNumber = "21",      // << SỬA SỐ ÁO (ví dụ)
                playerImageResId = R.drawable.depay_barca // << THAY BẰNG ẢNH ĐÚNG
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
    cardBackgroundColor: Color = playerCardBackgroundColor, // Cho phép tùy chỉnh màu nền thẻ nếu muốn
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
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp) // Padding bên trong thẻ cho nội dung
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Padding cho nội dung trong Row
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Phần thông tin cầu thủ (logo, tên, vị trí, số áo)
            Column(
                modifier = Modifier.weight(0.6f) // Chiếm 60% chiều rộng
            ) {
                Image( // Logo CLB
                    painter = painterResource(id = clubLogoResId),
                    contentDescription = "$playerName Club Logo",
                    contentScale = ContentScale.Fit, // Fit để logo không bị cắt
                    modifier = Modifier
                        .size(30.dp) // Kích thước logo CLB
                        .padding(bottom = 8.dp)
                    // .clip(CircleShape) // Bỏ clip tròn nếu logo không phải hình tròn
                )
                Text(
                    playerName,
                    color = textColor,
                    fontSize = 16.sp, // Tăng font cho tên
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    playerPosition,
                    color = secondaryTextColor,
                    fontSize = 12.sp, // Tăng font cho vị trí
                    fontWeight = FontWeight.Normal, // Giảm weight
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    playerNumber,
                    color = numberColor,
                    fontSize = 22.sp, // Tăng font cho số áo
                    fontWeight = FontWeight.Bold
                )
            }

            // Phần ảnh cầu thủ
            Box(
                modifier = Modifier
                    .weight(0.4f) // Chiếm 40% chiều rộng
                    .fillMaxHeight(), // Để căn chỉnh ảnh theo chiều cao của Row nếu cần
                contentAlignment = Alignment.CenterEnd // Căn ảnh về phía cuối (phải)
            ) {
                Image(
                    painter = painterResource(id = playerImageResId),
                    contentDescription = playerName,
                    contentScale = ContentScale.Crop, // Crop để ảnh lấp đầy
                    modifier = Modifier
                        .height(110.dp) // Kích thước ảnh cầu thủ
                        .width(90.dp)   // Kích thước ảnh cầu thủ
                        .clip(RoundedCornerShape(12.dp)) // Bo góc nhẹ cho ảnh cầu thủ
                )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PlayerStatsScreenPreview() {
    // Để Preview dùng MaterialTheme, bạn cần import nó
    // import androidx.compose.material.MaterialTheme (nếu dùng M2)
    // Hoặc import theme tùy chỉnh của bạn
    // com.example.mobileui.ui.theme.MobileuiTheme { // Sử dụng theme của bạn nếu có
    PlayerStatsScreen()
    // }
}