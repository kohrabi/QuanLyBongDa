
package com.example.quanlybongda.ui.jetpackcompose.screens

import android.widget.Toast
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.homeRoute
import com.example.quanlybongda.navigatePopUpTo
import com.example.quanlybongda.Services.Data.Competition
import com.example.quanlybongda.Services.FootballAPIViewModel
import com.example.quanlybongda.Services.LoadingState
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkCardBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

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
    apiViewModel: FootballAPIViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current;

    var isEditable by remember { mutableStateOf(false) }
    val user by viewModel.user.collectAsState()

    val competitions by apiViewModel.currentSeasonCompetitions.collectAsState()
    val currentCompetition by apiViewModel.currentCompetition.collectAsState()
    var selectedValue by remember { mutableStateOf<Competition?>(null) }
    val loadingSeasonDetail by apiViewModel.loadingSeasonsDetail.collectAsState()
    val currentYear = LocalDate.now().year
    val seasonOptions = (currentYear downTo (currentYear - 2)).map { OptionValue(it, it.toString()) }

    val currentSeason by apiViewModel.currentSeason.collectAsState()
    var selectedSeason by remember { mutableStateOf<OptionValue>(seasonOptions.find { it.value == currentSeason }!!) }
    // List of seasons with detailed information

    LaunchedEffect(Unit) {
        apiViewModel.loadCompetitions();
    }


    // Main screen layout without bottomBar
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
        floatingActionButton = {
            if (isEditable)
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

        when (competitions) {
            is LoadingState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Purple80,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            is LoadingState.Error -> {

                ErrorComponent(
                    message = "Dữ liệu không tồn tại",
                    onRetry = {},
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                )
            }

            is LoadingState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .padding(horizontal = 16.dp)
                        .background(DarkColorScheme.background),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    val competitions = (competitions as LoadingState.Success<List<Competition>>).data
                    
                    item {
                        InputDropDownMenu(
                            label = "Chọn năm mùa giải",
                            options = seasonOptions,
                            selectedOption = selectedSeason,
                            onOptionSelected = {
                                selectedSeason = it
                                apiViewModel.setCurrentSeason(selectedSeason.value!!)
                                apiViewModel.loadCompetitionCurrentSeasonDetail();
                            },
                            showEmptyError = false,
                        )
                    }
                    items (competitions) { competition ->
                        SeasonCard(
                            season = competition,
                            currentSeason = currentSeason,
                            isSelected = currentCompetition?.id == competition.id,
                            onSeasonSelect = {
                                selectedValue = competition;
                                apiViewModel.setCurrentCompetition(selectedValue);
                                coroutineScope.launch {
                                    Toast.makeText(
                                        context,
                                        "Chọn mùa giải ${competition.name} thành công",
                                        Toast.LENGTH_SHORT
                                    ).show();
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
}

// Composable for a single Season Card
@Composable
fun SeasonCard(
    season: Competition,
    currentSeason: Int,
    isSelected: Boolean,
    onSeasonSelect: () -> Unit
) {
    val animateBorder by animateDpAsState(
        targetValue = if (isSelected) 6.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Border Animation"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = darkCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSeasonSelect() }
            .border(
                width = animateBorder,
                color = if (isSelected) Purple80 else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        )
        {
            AsyncImage(
                model = season.emblem,
                contentDescription = season.name,
                modifier = Modifier.size(64.dp).background(Color.White, CircleShape).clip(CircleShape),
            )
            Column(
                modifier = Modifier.padding(start = 16.dp).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,)
            {
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
                        text = "Bắt đầu: ${DateConverter.LocalDateToString(season.currentSeason.startDate)}",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Kết thúc: ${DateConverter.LocalDateToString(season.currentSeason.endDate)}",
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
