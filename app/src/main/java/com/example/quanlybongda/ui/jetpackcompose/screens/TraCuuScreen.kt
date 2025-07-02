package com.example.quanlybongda.ui.jetpackcompose.screens

// Thêm import cần thiết để lấy chiều cao thanh trạng thái
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.Services.Data.Player
import com.example.quanlybongda.Services.Data.Team
import com.example.quanlybongda.Services.FootballAPIViewModel
import com.example.quanlybongda.Services.LoadingState
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple40
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkCardBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Main Composable for the Player List Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraCuuScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
    apiViewModel: FootballAPIViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var cauThus by remember { mutableStateOf(listOf<Player>()) }
    val teams by apiViewModel.teams.collectAsState()
    var players by remember { mutableStateOf(listOf<Player>()) }
    var isSearching by remember { mutableStateOf(false) }
    val user by viewModel.user.collectAsState()
    var searchArgument by remember { mutableStateOf("") }
    var cauThuYeuThich by remember { mutableStateOf<Set<Int>>(emptySet()) }

    LaunchedEffect(Unit) {
        cauThuYeuThich = user!!.cauThuYeuThich;
        if (teams is LoadingState.Success) {
            players = (teams as LoadingState.Success<List<Team>>).data.flatMap { it.squad };
            cauThus = players;
        }
    }

    LaunchedEffect(searchArgument) {
        if (isSearching)
            return@LaunchedEffect;
        isSearching = true;
        apiViewModel.viewModelScope.launch {
            delay(500);
            cauThus = players.filter {
                it.name.contains(searchArgument, ignoreCase = true)
            }
            isSearching = false;
        }
    }

    Scaffold(
        topBar = {
            Column {
                AppTopBar(
                    title = "Tra cứu",
                    scrollBehavior = scrollBehavior,
                )
                InputTextField(
                    label = "Tìm kiếm",
                    value = searchArgument,
                    onValueChange = { searchArgument = it },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },

                );
            }
        },
        containerColor = DarkColorScheme.background,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        // Danh sách cầu thủ
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .background(DarkColorScheme.background)
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cauThus) { cauThu ->
                val isFavorite = cauThuYeuThich.contains(cauThu.id)
                PlayerCard(
                    team = null,
                    player = cauThu,
                    isFavorite = isFavorite,
                    onFavoriteClick = {
                        if (isFavorite) {
                            viewModel.removeCauThuYeuThich(cauThu.id)
                            cauThuYeuThich = cauThuYeuThich - cauThu.id
                        } else {
                            viewModel.addCauThuYeuThich(cauThu.id)
                            cauThuYeuThich = cauThuYeuThich + cauThu.id
                        }
                    },
                )
            }
        }
    }
}

// Preview function for Android Studio
@Preview(showBackground = true, widthDp = 360, heightDp = 720, backgroundColor = 0xFF1C1D2B)
@Composable
fun PreviewTraCuu() {
    QuanLyBongDaTheme {
        TraCuuScreen(rememberNavController())
    }
}