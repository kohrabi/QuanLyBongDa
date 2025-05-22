package com.example.quanlybongda.ui.jetpackcompose.screens // Giữ nguyên package của bạn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.BanThang
import com.example.quanlybongda.Database.Schema.LichThiDau
// import androidx.compose.foundation.Image // Đã có ở trên, không cần import lại
// import androidx.compose.foundation.layout.size // Đã có trong layout.*

import com.example.quanlybongda.R // << QUAN TRỌNG: Import lớp R của dự án bạn

// Màu sắc dựa trên thiết kế image_5022a5.png
val darkScreenBackground = Color(0xFF1E1E2C)
val scoreColor = Color(0xFFE0FF00)
val fullTimeColor = Color(0xFF4CFF89)
val textWhiteColor = Color.White
val textMutedColor = Color(0xFFA0A3BD)

// BỎ URL PLACEHOLDER KHÔNG CẦN THIẾT
// const val BACKGROUND_IMAGE_URL = "https://i.imgur.com/your_background_image.png"
// const val FCB_LOGO_URL = "https://i.imgur.com/nWgD6YI.png" // Không dùng trong GhiNhan()
// const val MANCITY_LOGO_URL1 = "https://i.imgur.com/yiLJgk9.png" // Không dùng trong GhiNhan()


@Composable
fun GhiNhanScreen(
    maTD: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    var lichThiDau by remember { mutableStateOf<LichThiDau?>(null) }
    var tiSoDoiMot by remember { mutableStateOf(0) }
    var tiSoDoiHai by remember { mutableStateOf(0) }
    var banThangs by remember { mutableStateOf(listOf<BanThang>()) }

    LaunchedEffect(Unit) {
        lichThiDau = viewModel.lichThiDauDAO.selectLichThiDauMaTD(maTD);
        tiSoDoiMot =
            viewModel.banThangDAO.selectSoBanThangTranDauDoi(maTD, lichThiDau!!.doiMot) +
            viewModel.banThangDAO.selectSoBanThangPhanLuoiTranDauDoi(maTD, lichThiDau!!.doiHai);
        tiSoDoiHai = viewModel.banThangDAO.selectSoBanThangTranDauDoi(maTD, lichThiDau!!.doiHai) +
                viewModel.banThangDAO.selectSoBanThangPhanLuoiTranDauDoi(maTD, lichThiDau!!.doiMot);
        banThangs = viewModel.banThangDAO.selectBanThang(maTD);
        val loaiBTs = viewModel.banThangDAO.selectAllLoaiBT();
        val cauThus = viewModel.cauThuDAO.selectCauThuTGTD(maTD);
        for (banThang in banThangs) {
            val cauThu = cauThus.find { banThang.maCT == it.maCT }!!;
            if (cauThu.maDoi == lichThiDau!!.doiMot)
                banThang.side = "L";
            else if (cauThu.maDoi == lichThiDau!!.doiHai)
                banThang.side = "R";
            banThang.tenCT = cauThu.tenCT;
            banThang.tenLBT = loaiBTs.find { banThang.maLBT == it.maLBT }!!.tenLBT;
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkScreenBackground)
    ) {
        Image(
            // painter = rememberAsyncImagePainter(BACKGROUND_IMAGE_URL), // THAY THẾ DÒNG NÀY
            painter = rememberAsyncImagePainter(R.drawable.football_stadium), // << THAY BẰNG TÊN FILE DRAWABLE CỦA BẠN
            contentDescription = "Background Stadium",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textWhiteColor
                    )
                }
                Text(
                    text = "Final Score",
                    color = textWhiteColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = { /* TODO: Handle info action */ }) {
                    Box(Modifier
                    )
                }
            }

            // Khoảng cách giữa TopBar và Full Time
            Spacer(modifier = Modifier.height(30.dp)) // << SỬA: Tăng khoảng cách

            Text(
                text = "Full Time",
                color = fullTimeColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Khoảng cách giữa Full Time và Khu vực tỷ số
            Spacer(modifier = Modifier.height(20.dp)) // << SỬA: Tăng khoảng cách

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.arsenal_logo_4x), // Giả sử đây là logo đội 1
                    contentDescription = "Team 1 Logo", // Sửa contentDescription cho phù hợp
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = "$tiSoDoiMot - $tiSoDoiHai",
                    color = scoreColor,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.mancity_logo_4x), // << THAY BẰNG LOGO ĐỘI 2, ví dụ: R.drawable.logo_mancity
                    contentDescription = "Team 2 Logo", // Sửa contentDescription cho phù hợp
                    modifier = Modifier.size(70.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Statistic Match",
                color = textWhiteColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                banThangs.forEach { it ->
                    // Sử dụng một action placeholder, bạn có thể thay đổi nếu cần
                    StatisticRowUpdated(side = it.side, player = it.tenCT, action = it.tenLBT, time = it.thoiDiem.toString(), modifier)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatisticRowUpdated(side: String, player: String, action: String, time: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = side,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.15f), // Cột Side
            textAlign = TextAlign.Center
        )
        Text(
            text = player,
            color = textWhiteColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.weight(0.5f).padding(start = 8.dp) // Cột Player, chiếm nhiều không gian nhất
        )
        Text(
            text = action,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.15f), // Cột Action
            textAlign = TextAlign.Center
        )
        Text(
            text = time,
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.2f), // Cột Time
            textAlign = TextAlign.End
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF1E1E2C)
@Composable
fun FinalScoreScreenPreview() {
    MaterialTheme {
        GhiNhanScreen(1, rememberNavController())
    }
}