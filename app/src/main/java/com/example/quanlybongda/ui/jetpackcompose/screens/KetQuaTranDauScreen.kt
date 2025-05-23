package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.* // Material 2 components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Nên dùng filled hoặc outlined nhất quán
import androidx.compose.material.icons.filled.MoreVert  // Nên dùng filled hoặc outlined nhất quán
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.ReturnTypes.CauThuBanThang
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.R // << QUAN TRỌNG: Import lớp R của dự án bạn
import kotlinx.coroutines.CoroutineScope

@Composable
fun KetQuaTranDauScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {

    var cauThus by remember { mutableStateOf(listOf<CauThuBanThang>()) }
    var doiBong by remember { mutableStateOf<DoiBong?>(null) }

    LaunchedEffect(Unit) {
        val maDoi = 16;
        cauThus = viewModel.cauThuDAO.selectCauThuDoiBongWithBanThang(maDoi);
        doiBong = viewModel.doiBongDAO.selectDoiBongMaDoi(maDoi);
        cauThus = cauThus.sortedByDescending { it.banThang }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F24)) // Nền chính của màn hình là màu đặc
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HoSoTopBar(Modifier) // Đổi tên TopBar để tránh trùng lặp nếu có TopBar khác trong cùng package

            PlayerCardHoSo( // Đổi tên PlayerCard để tránh trùng lặp
                team = doiBong?.tenDoi ?: "",
                name = cauThus.getOrNull(0)?.cauThu?.tenCT ?: "",
                score = (cauThus.getOrNull(0)?.banThang ?: 0).toString(),
                Modifier
            )

            TeamRowHoSo( // Đổi tên TeamRow để tránh trùng lặp
                teamName = doiBong?.tenDoi ?: "",
                score = "",
                // logoUrl = "https://upload.wikimedia.org/wikipedia/en/e/eb/Manchester_City_FC_badge.svg" // THAY THẾ DÒNG NÀY
                logoResId = R.drawable.mancity_logo, // << THAY BẰNG TÊN FILE DRAWABLE CỦA BẠN
                Modifier
            )

            // List of Players
            cauThus.forEach {
                PlayerRowHoSo(it.cauThu.tenCT, it.banThang.toString(), Modifier)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HoSoTopBar(modifier: Modifier = Modifier) { // Đổi tên từ TopBar
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack, // Sử dụng Icons.Filled cho nhất quán
            contentDescription = "Back",
            tint = Color.White
        )
        Text(
            text = "Goals Scored", // Tiêu đề này có thể cần thay đổi cho màn hình "Hồ Sơ"
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f) // Để Text chiếm không gian và tự căn giữa
        )
        Icon(
            imageVector = Icons.Filled.MoreVert, // Sử dụng Icons.Filled cho nhất quán
            contentDescription = "Menu",
            tint = Color.White
        )
    }
}

@Composable
fun PlayerCardHoSo(team: String, name: String, score: String, modifier: Modifier = Modifier) { // Đổi tên từ PlayerCard
    // Nội dung PlayerCard này không có hình ảnh, giữ nguyên nếu đây là thiết kế
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1B2038))
            .fillMaxWidth()
            .padding(vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = team,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = name,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Goals Score", // Label này có thể cần thay đổi
                color = Color(0xFFDADADA),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = score,
                color = Color(0xFFFF9E0D),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun TeamRowHoSo(teamName: String, score: String, logoResId: Int, modifier: Modifier = Modifier) { // Đổi tên và thay logoUrl thành logoResId
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        AsyncImage(
//            model = logoResId, // << SỬ DỤNG RESOURCE ID
//            contentDescription = "$teamName Logo", // Cung cấp contentDescription tốt hơn
//            modifier = modifier
//                .size(40.dp)
//                .clip(CircleShape),
//            contentScale = ContentScale.Fit
//        )
        Text(
            text = teamName,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Text(
            text = score,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PlayerRowHoSo(name: String, goals: String, modifier: Modifier = Modifier) { // Đổi tên từ PlayerRow
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Box(
//            modifier = modifier
//                .size(36.dp)
//                .clip(CircleShape)
//                .background(Color(0xFF414158))
//        )
        Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Text(
            text = goals,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun HoSoScreenPreview() {
    MaterialTheme {
        KetQuaTranDauScreen(rememberNavController())
    }
}