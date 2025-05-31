package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
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
    val currentMuaGiai by DatabaseViewModel.currentMuaGiai.collectAsState()
    var doiBongs by remember { mutableStateOf(listOf<DoiBong>()) }

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            if (currentMuaGiai != null) {
                doiBongs = viewModel.doiBongDAO.selectDoiBongMuaGiai(currentMuaGiai!!.maMG!!);
                val sanNha = viewModel.sanNhaDAO.selectAllSanNha();
                for (doiBong in doiBongs) {
                    doiBong.tenSan = sanNha.find { doiBong.maSan == it.maSan }!!.tenSan;
                }

            }
        }
    }
    // Main screen layout
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Đội bóng",
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            AddFloatingButton("Tạo đội bóng", onClick = { navController.navigate("doiBongInput") })
        },
        containerColor = DarkColorScheme.background,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
                .background(DarkColorScheme.background),
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
            .padding(16.dp)
        ) {
            AsyncImage(
                model = team.imageURL,
                contentDescription = team.tenDoi,
                modifier = Modifier.size(64.dp).background(Color.White, CircleShape).clip(CircleShape),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = team.tenDoi,
                    color = Color.White, // Thay MaterialTheme.colorScheme.onSurface
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = team.tenSan,
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
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