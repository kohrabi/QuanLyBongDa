package com.example.quanlybongda.ui.jetpackcompose.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.homeRoute
import com.example.quanlybongda.navigatePopUpTo
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkCardBackground
import kotlinx.coroutines.delay
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
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedValue by remember { mutableStateOf<DoiBong?>(null) }
    val user by viewModel.user.collectAsState()
    var isEditable by remember { mutableStateOf(false) }

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

    LaunchedEffect(user) {
        if (user == null)
            return@LaunchedEffect;
        viewModel.viewModelScope.launch {
            isEditable = viewModel.checkPageEditable(user!!.groupId, "doi");
        }
    }

    DisposableEffect(snackbarHostState) {
        onDispose {
            if (selectedValue != null) {
                viewModel.viewModelScope.launch {
                    viewModel.doiBongDAO.deleteDoiBong(selectedValue!!);
                    selectedValue = null;
                }
            }
        }
    }

    // Main screen layout
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
        topBar = {
            AppTopBar(
                title = "Đội bóng",
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (isEditable)
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
                SwipeToDeleteContainer(
                    item = doiBong,
                    isEditable = isEditable,
                    onDelete = {
                        if (selectedValue != null) {
                            viewModel.viewModelScope.launch {
                                viewModel.doiBongDAO.deleteDoiBong(selectedValue!!);
                                selectedValue = null;
                            }
                        }
                        selectedValue = doiBong;
                        val result = snackbarHostState
                            .showSnackbar(
                                message = "Deleted ${doiBong.tenDoi}",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                return@SwipeToDeleteContainer false;
                            }
                            SnackbarResult.Dismissed -> {
                                viewModel.viewModelScope.launch {
                                    viewModel.doiBongDAO.deleteDoiBong(selectedValue!!);
                                    selectedValue = null;
                                }
                                return@SwipeToDeleteContainer true;
                            }
                        }
                    },
                    onUpdate = {
                        navController.navigate("doiBongInput");
                        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle;
                        savedStateHandle?.set("maDoi", it.maDoi);
                        savedStateHandle?.set("tenDoi", it.tenDoi);
                        savedStateHandle?.set("maSan", it.maSan);
                        savedStateHandle?.set("maMG", it.maMG);
                        savedStateHandle?.set("imageURL", it.imageURL);
                    },
                    content = {
                        TeamCard(
                            team = doiBong,
                            onClick = {
                                navController.navigate("cauThu/${doiBong.maDoi}");
                            })
                    },
                    modifier = Modifier.fillMaxSize(),
                    backgroundModifier = Modifier.clip(RoundedCornerShape(16.dp))
                )
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
                modifier = Modifier.size(64.dp),
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