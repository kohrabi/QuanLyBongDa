package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkCardBackground
import kotlinx.coroutines.launch

// Main Composable for the Football Team Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoiBongScreen(
    navController : NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
) { // Đổi tên từ FootballTeamScreen thành DoiBong
    // List of football teams (sample data)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val currentMuaGiai by viewModel.currentMuaGiai.collectAsState()
    var doiBongs by remember { mutableStateOf(listOf<DoiBong>()) }

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            if (currentMuaGiai == null) {
                doiBongs = viewModel.doiBongDAO.selectAllDoiBong();
                val sanNha = viewModel.sanNhaDAO.selectAllSanNha();
                for (doiBong in doiBongs) {
                    doiBong.tenSan = sanNha.find { doiBong.maSan == it.maSan }!!.tenSan;
                }
            }
            else {
                doiBongs = viewModel.doiBongDAO.selectDoiBongMuaGiai(currentMuaGiai!!.maMG);
                val sanNha = viewModel.sanNhaDAO.selectSanNhaMaMG(currentMuaGiai!!.maMG);
                for (doiBong in doiBongs) {
                    doiBong.tenSan = sanNha.find { doiBong.maSan == it.maSan }!!.tenSan;
                }

            }
        }
    }
    // Main screen layout
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Đội bóng",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkColorScheme.background,
                    scrolledContainerColor = DarkColorScheme.background
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            AddFloatingButton("Tạo đội bóng", onClick = { navController.navigate("doiBongInput") })
        },
        containerColor = DarkColorScheme.background,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các card
        ) {
            items(doiBongs) { doiBong ->
                TeamCard(
                    team = doiBong,
                    onClick = {
                        navController.navigate("cauThu/${doiBong.maDoi}");
                    })
            }
        }
    }
}

// Composable for a single Team Card
@Composable
fun TeamCard(team: DoiBong, onClick : () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = darkCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = team.tenDoi,
                color = Color.White, // Thay MaterialTheme.colorScheme.onSurface
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = team.tenSan,
                color = Color.LightGray,
                fontSize = 16.sp
            )
        }
    }
}

// Preview function for Android Studio
@Preview(showBackground = true, widthDp = 360, heightDp = 720, backgroundColor = 0xFF121212)
@Composable
fun PreviewDoiBong() { // Đổi tên từ PreviewFootballTeamScreen thành PreviewDoiBong
    QuanLyBongDaTheme {
        DoiBongScreen(rememberNavController())
    }
}