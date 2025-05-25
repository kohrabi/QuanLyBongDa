
package com.example.quanlybongda.ui.jetpackcompose.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme

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
) { // Đổi tên từ FootballSeasonSelectorScreen thành MuaGiai
    // State to hold the currently selected season ID
    var selectedSeason by remember { mutableStateOf<String?>(null) }

    // List of seasons with detailed information
    val seasons = remember {
        listOf(
            Season(id = "2024-2025", name = "Premier League 2024-2025", startDate = "09/08/2024", endDate = "25/05/2025"),
            Season(id = "2023-2024", name = "Premier League 2023-2024", startDate = "11/08/2023", endDate = "19/05/2024"),
            Season(id = "2022-2023", name = "Premier League 2022-2023", startDate = "05/08/2022", endDate = "28/05/2023"),
            Season(id = "2021-2022", name = "Premier League 2021-2022", startDate = "13/08/2021", endDate = "22/05/2022"),
            Season(id = "2020-2021", name = "Premier League 2020-2021", startDate = "12/09/2020", endDate = "23/05/2021"),
            Season(id = "2019-2020", name = "Premier League 2019-2020", startDate = "09/08/2019", endDate = "26/07/2020"),
        )
    }

    // Main screen layout without bottomBar
    Scaffold(
        containerColor = Color(0xFF121212),
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF121212))
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
                items(seasons) { season ->
                    SeasonCard(
                        season = season,
                        isSelected = selectedSeason == season.id,
                        onSeasonSelect = { selectedSeason = season.id }
                    )
                }
            }

            // Display selected season (optional)
            selectedSeason?.let {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Mùa giải được chọn: $it",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E1E1E))
                        .padding(12.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

// Composable for a single Season Card
@Composable
fun SeasonCard(
    season: Season,
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
                text = season.name,
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
                    text = "Bắt đầu: ${season.startDate}",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Kết thúc: ${season.endDate}",
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