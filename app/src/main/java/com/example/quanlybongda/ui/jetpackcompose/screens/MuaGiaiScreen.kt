
package com.example.quanlybongda.ui.jetpackcompose.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.homeRoute
import com.example.quanlybongda.navigatePopUpTo
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkCardBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current;
    val selectedMuaGiai by DatabaseViewModel.currentMuaGiai.collectAsState()
    var muaGiais by remember { mutableStateOf(listOf<MuaGiai>()) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedValue by remember { mutableStateOf<MuaGiai?>(null) }
    // List of seasons with detailed information

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            muaGiais = viewModel.muaGiaiDAO.selectAllMuaGiai();
        }
    }

    DisposableEffect(snackbarHostState) {
        onDispose {
            if (selectedValue != null) {
                viewModel.viewModelScope.launch {
                    viewModel.muaGiaiDAO.deleteDSMuaGiai(selectedValue!!);
                    selectedValue = null;
                }
            }
        }
    }

    // Main screen layout without bottomBar
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
        floatingActionButton = {
            AddFloatingButton("Tạo mùa giải", onClick = { navController.navigate("muaGiaiInput") })
        },
        topBar = {
            AppTopBar(
                title = "Mùa giải",
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = DarkColorScheme.background,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
            // List of Season Cards
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 16.dp)
                    .background(DarkColorScheme.background),
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(muaGiais) { muaGiai ->
                    SwipeToDeleteContainer(
                        item = muaGiai,
                        onDelete = {
                            if (selectedValue != null) {
                                viewModel.viewModelScope.launch {
                                    viewModel.muaGiaiDAO.deleteDSMuaGiai(selectedValue!!);
                                    selectedValue = null;
                                }
                            }
                            selectedValue = muaGiai;
                            val result = snackbarHostState
                                .showSnackbar(
                                    message = "Deleted ${muaGiai.tenMG}",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    return@SwipeToDeleteContainer false;
                                }
                                SnackbarResult.Dismissed -> {
                                    viewModel.viewModelScope.launch {
                                        viewModel.muaGiaiDAO.deleteDSMuaGiai(selectedValue!!);
                                        selectedValue = null;
                                    }
                                    return@SwipeToDeleteContainer true;
                                }
                            }
                        },
                        onUpdate = {
                            navController.navigate("muaGiaiInput");
                            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle;
                            savedStateHandle?.set("maMG", it.maMG);
                            savedStateHandle?.set("tenMG", it.tenMG);
                            savedStateHandle?.set("ngayDienRa", it.ngayDienRa);
                            savedStateHandle?.set("ngayKetThuc", it.ngayKetThuc);
                            savedStateHandle?.set("imageURL", it.imageURL);
                        },
                        content = {
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
                        },
                        modifier = Modifier
                            .fillMaxSize(),
                        backgroundModifier = Modifier.clip(RoundedCornerShape(16.dp))
                    )
                }
            }
//        }
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
            containerColor = darkCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSeasonSelect() }
            .then(
                if (isSelected) Modifier.border(6.dp, Purple80, RoundedCornerShape(16.dp))
                else Modifier.border(6.dp, Color.Transparent, RoundedCornerShape(16.dp))
            )
//            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        )
        {
            AsyncImage(
                model = season.imageURL,
                contentDescription = season.tenMG,
                modifier = Modifier.size(64.dp).background(Color.White, CircleShape).clip(CircleShape),
            )
            Column(
                modifier = Modifier.padding(start = 16.dp).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,)
            {
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
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Kết thúc: ${DateConverter.LocalDateToString(season.ngayKetThuc)}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
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
