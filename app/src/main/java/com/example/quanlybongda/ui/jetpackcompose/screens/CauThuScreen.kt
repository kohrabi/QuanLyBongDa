package com.example.quanlybongda.ui.jetpackcompose.screens

// Thêm import cần thiết để lấy chiều cao thanh trạng thái
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.R
import com.example.quanlybongda.Services.Data.Match
import com.example.quanlybongda.Services.Data.Player
import com.example.quanlybongda.Services.Data.Team
import com.example.quanlybongda.Services.FootballAPIViewModel
import com.example.quanlybongda.Services.LoadingState
import com.example.quanlybongda.Services.TheSportsDBAPI
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple40
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.theme.darkCardBackground
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// Main Composable for the Player List Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CauThuScreen(
    maDoi : Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
    apiViewModel: FootballAPIViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val user by viewModel.user.collectAsState()
    val context = LocalContext.current;
    val snackbarHostState = remember { SnackbarHostState() }
    var isEditable by remember { mutableStateOf(false) }

    val teams by apiViewModel.teams.collectAsState()
    var currentTeam by remember { mutableStateOf<Team?>(null) }


    LaunchedEffect(Unit) {
        when (teams) {
            is LoadingState.Success -> {
                val result = (teams as LoadingState.Success<List<Team>>).data;
                currentTeam = result.find { it.id == maDoi }!!;
            }
            else -> {}
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
        topBar = {
            AppTopBar(
                title = "Cầu thủ",
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = Purple80,
                        )
                    }
                }
            )
        },
        containerColor = DarkColorScheme.background,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        if (currentTeam == null) {
            // Hiển thị thông báo nếu không tìm thấy lịch thi đấu
            Text(
                text = "Không tìm thấy đội bóng với mã: $maDoi",
                color = Color.Red,
                modifier = Modifier.padding(innerPadding)
            )
            return@Scaffold
        }
        // Danh sách cầu thủ
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkColorScheme.background)
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(currentTeam!!.squad) { cauThu ->

                val isFavorite = user?.cauThuYeuThich?.any { it == cauThu.id } ?: false;
                SwipeContainer(
                    item = cauThu,
                    content = {
                        PlayerCard(team = currentTeam, player = cauThu)
                    },
                    leftIcon = Icons.Default.Star,
                    leftSwipe = if (!isFavorite) ({ viewModel.addDoiBongYeuThich(cauThu.id); }) else null,
                    rightIcon = Icons.Default.HeartBroken,
                    rightSwipe = if (!isFavorite) ({ viewModel.addDoiBongYeuThich(cauThu.id); }) else null,
                    backgroundModifier = Modifier.clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

// Composable for a single Player Card
@Composable
fun PlayerCard(team: Team?, player: Player, onClick : () -> Unit = {}) {
    LaunchedEffect(player.id) {
        if ((player.imageURL ?: "") != "") return@LaunchedEffect;
        Log.d("PlayerCard", "Fetching image for player: ${player.name}");
        val result = TheSportsDBAPI.retrofitService.searchPlayer(player.name.replace(" ", "_"));
        if (result.isSuccessful) {
            val playerData = result.body()?.player?.find { it.sportName == "Soccer" };
            player.imageURL = playerData?.cutoutUrl ?: playerData?.thumbnailUrl ?: "";
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = darkCardBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // Main content row
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(top = 16.dp), // Extra padding to account for the shirt number
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Player image and name
                // Player image
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .aspectRatio(1.0f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = if ((player.imageURL ?: "") == "") stringResource(R.string.default_player_avatar) else player.imageURL,
                        contentDescription = "Player Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Right side: Player info and team logo
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Player name

                    // Player position
                    InfoRow(
                        label = "Name:",
                        value = player.name
                    )

                    // Player position
                    InfoRow(
                        label = "Position:",
                        value = player.position ?: "Unknown"
                    )

                    // Nationality
                    InfoRow(
                        label = "Nationality:",
                        value = player.nationality
                    )

                    // Date of birth
                    InfoRow(
                        label = "Born:",
                        value = player.dateOfBirth?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) ?: "Unknown"
                    )
                }
            }

            // Player shirt number badge (top left)
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(32.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Purple40, Purple80)
                        ),
                        shape = CircleShape
                    )
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${player.shirtNumber ?: "?"}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
            }

            // Team logo - larger and at the top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                AsyncImage(
                    model = team?.crest ?: "",
                    contentDescription = "Team Logo",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Team logo - larger and at the top
            team?.area?.flag.let {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                     contentAlignment = Alignment.BottomEnd
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it)
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = "Area Flag",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.7f)
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Preview function for Android Studio
@Preview(showBackground = true, widthDp = 360, heightDp = 720, backgroundColor = 0xFF1C1D2B)
@Composable
fun PreviewCauThu() {
    QuanLyBongDaTheme {
        CauThuScreen(1, rememberNavController())
    }
}