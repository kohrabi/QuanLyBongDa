
package com.example.quanlybongda.ui.jetpackcompose.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.homeRoute
import com.example.quanlybongda.navigatePopUpTo
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Data class for Season information
data class Season(
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String
)

// Main Composable for the Football Season Selector Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuaGiaiScreen(
    navController : NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
) {
    val context = LocalContext.current;
    val selectedMuaGiai by viewModel.currentMuaGiai.collectAsState()
    var muaGiais by remember { mutableStateOf(listOf<MuaGiai>()) }
    val coroutineScope = rememberCoroutineScope()
    // List of seasons with detailed information

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            muaGiais = viewModel.muaGiaiDAO.selectAllMuaGiai();
        }
    }

    // Main screen layout without bottomBar
    Scaffold(
        floatingActionButton = {
            AddFloatingButton("Tạo mùa giải", onClick = { navController.navigate("muaGiaiInput") })
        },
        containerColor = DarkColorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkColorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chọn Mùa Giải",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Tìm kiếm",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp).clickable { /* Handle search click */ }
                )
            }

            // List of Season Cards
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(muaGiais) { muaGiai ->
                    SeasonCard(
                        season = muaGiai,
                        isSelected = selectedMuaGiai?.maMG == muaGiai.maMG,
                        onSeasonSelect = {
                            viewModel.selectMuaGiai(muaGiai)
                            coroutineScope.launch {
                                Toast.makeText(context, "Chọn mùa giải ${muaGiai.tenMG} thành công", Toast.LENGTH_SHORT).show();
                                delay(500)
                                navigatePopUpTo(navController, homeRoute);
                            }
                        }
                    )
                }
            }
        }
    }
}

// Composable for a single Season Card
@Composable
fun SeasonCard(
    season: MuaGiai,
    isSelected: Boolean,
    onSeasonSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSeasonSelect() }
            .then(
                if (isSelected) Modifier.border(2.dp, Color(0xFF9C27B0), RoundedCornerShape(16.dp))
                else Modifier.border(2.dp, Color.Transparent, RoundedCornerShape(16.dp))
            )
            .padding(if (isSelected) 0.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = season.tenMG,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bắt đầu: ${DateConverter.LocalDateToString(season.ngayDienRa)}",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Kết thúc: ${DateConverter.LocalDateToString(season.ngayKetThuc)}",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Preview function for Android Studio
@Preview(showBackground = true, widthDp = 360, heightDp = 720, backgroundColor = 0xFF121212)
@Composable
fun PreviewMuaGiai() { // Đổi tên từ PreviewFootballSeasonSelectorScreen thành PreviewMuaGiai
    QuanLyBongDaTheme {
        MuaGiaiScreen(rememberNavController())
    }
}