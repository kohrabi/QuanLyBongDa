package com.example.quanlybongda.ui.jetpackcompose.screens

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.User.CaDo
import com.example.quanlybongda.Services.Data.GoalShort
import com.example.quanlybongda.Services.Data.Match
import com.example.quanlybongda.Services.Data.MatchTeam
import com.example.quanlybongda.Services.Data.Odds
import com.example.quanlybongda.Services.Data.Team
import com.example.quanlybongda.Services.Data.YouTubeVideoItem
import com.example.quanlybongda.Services.FootballAPIViewModel
import com.example.quanlybongda.Services.GenerativeModel
import com.example.quanlybongda.Services.LoadingState
import com.example.quanlybongda.Services.OddResponse
import com.example.quanlybongda.Services.YoutubeAPI
import com.example.quanlybongda.Services.gson
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

val homeColor = Color(0xFF0D904B)
val drawColor = Color(0xFF35495E)
val awayColor = Color(0xFFD13030)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XemTranDauScreen(
    maTD: Int,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
    apiViewModel: FootballAPIViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val lichThiDaus by apiViewModel.matches.collectAsState()
    var banThangs by remember { mutableStateOf(listOf<GoalShort>()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val teams by apiViewModel.teams.collectAsState()
    var isEditable by remember { mutableStateOf(false) }
    var currentMatch by remember { mutableStateOf<Match?>(null)}
    var videoId : LoadingState<String> by remember { mutableStateOf(LoadingState.Loading) }
    var youtubeVideos by remember { mutableStateOf<List<YouTubeVideoItem>>(listOf())}
    var videoCount by remember { mutableStateOf(0) }
    val user by viewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var odd by remember { mutableStateOf<Odds?>(null)}
    var showBettingDialog by remember { mutableStateOf(false) }
    var bettingOptions by remember { mutableStateOf(listOf<BettingOption>(
        BettingOption("Home Win", 0.0, homeColor),
        BettingOption("Draw", 0.0, drawColor),
        BettingOption("Away Win", 0.0, awayColor)
    )) }
    var bettingOption by remember { mutableStateOf<BettingOption>(bettingOptions[0]) }

    var caDos : MutableList<CaDo> by remember { mutableStateOf<MutableList<CaDo>>(mutableListOf()) }

    val setCaDo : () -> Unit = {
        viewModel.viewModelScope.launch {
            caDos = viewModel.caDoDAO.selectCaDoByMaTD(maTD).toMutableList();
            val caDoHomeTeam = caDos.filter { it.doiCuoc == currentMatch?.homeTeam?.id }.sumOf { it.soTien };
            val caDoAwayTeam = caDos.filter { it.doiCuoc == currentMatch?.awayTeam?.id }.sumOf { it.soTien };
            val caDoDraw = caDos.sumOf { it.soTien };

            val tong = caDoHomeTeam + caDoAwayTeam + caDoDraw;
            bettingOptions[0].percentage = caDoHomeTeam.toDouble() / tong.toDouble();
            bettingOptions[0].percentage = (bettingOptions[0].percentage * 1000.0).roundToInt().toDouble() / 1000.0;
            bettingOptions[0].teamId = currentMatch?.homeTeam?.id ?: 0;

            bettingOptions[1].percentage = caDoDraw.toDouble() / tong.toDouble();
            bettingOptions[1].percentage = (bettingOptions[1].percentage * 1000.0).roundToInt().toDouble() / 1000.0;
            bettingOptions[1].teamId = null; // Draw does not have a team ID

            bettingOptions[2].percentage = 1.0 - (bettingOptions[0].percentage + bettingOptions[1].percentage);
            bettingOptions[2].percentage = (bettingOptions[2].percentage * 1000.0).roundToInt().toDouble() / 1000.0;
            bettingOptions[2].teamId = currentMatch?.awayTeam?.id ?: 0;
            bettingOptions = bettingOptions.toMutableList();
        }
    }

    LaunchedEffect(lichThiDaus) {
        if (lichThiDaus is LoadingState.Success) {
            val matches = (lichThiDaus as LoadingState.Success<List<Match>>).data
            currentMatch = matches.firstOrNull { it.id == maTD };
        }
        if (currentMatch == null) {
            snackbarHostState.showSnackbar("Trận đấu không tồn tại")
            return@LaunchedEffect
        }

        val videos = YoutubeAPI.retrofitService.searchVideos(
            query = "${currentMatch?.homeTeam?.name} vs ${currentMatch?.awayTeam?.name} " +
                    "${currentMatch?.competition?.name} ${currentMatch?.utcDate?.year}"
        );
        if (videos.items.isEmpty()) {
            videoId = LoadingState.Error("Không tìm thấy video cho trận đấu này")
            return@LaunchedEffect
        }
        youtubeVideos = videos.items;
        youtubeVideos.get(videoCount).let {
            videoId = LoadingState.Success(it.id.videoId)
        }
        setCaDo();

        val prompt = GenerativeModel.prompt
            .replace("{homeTeam}", currentMatch?.homeTeam?.name ?: "")
            .replace("{awayTeam}", currentMatch?.awayTeam?.name ?: "")
            .replace("{competition}", currentMatch?.competition?.name ?: "")
            .replace("{season}", currentMatch?.utcDate?.year.toString())
        val response = GenerativeModel.model.generateContent(prompt);

        response.text?.let {
            val json = gson.fromJson(it, OddResponse::class.java);
            odd = Odds(
                homeWin = json.homeTeamWinPercentage.roundToInt().toDouble(),
                awayWin = json.awayTeamWinPercentage.roundToInt().toDouble(),
                draw = json.drawPercentage.roundToInt().toDouble()
            );
        }
    }

    LaunchedEffect(teams) {

        if (currentMatch != null && teams is LoadingState.Success) {
            val teamsData = (teams as LoadingState.Success<List<Team>>).data;

            val homeScore = (currentMatch?.score?.fullTime?.home?.toInt() ?: 0);
            val awayScore = (currentMatch?.score?.fullTime?.away?.toInt() ?: 0);

            val homePlayers = teamsData.find { it.id == currentMatch?.homeTeam?.id }?.squad ?: emptyList();
            var canScorePlayersHome = homePlayers.filter { it.position != "Goalkeeper" && it.id % 5 == 0 };
            if (canScorePlayersHome.isEmpty()|| (homeScore != 0 && canScorePlayersHome.size < homeScore / 2))
                canScorePlayersHome = homePlayers.filter { it.position != "Goalkeeper" };

            var awayPlayers = teamsData.find { it.id == currentMatch?.awayTeam?.id }?.squad ?: emptyList();
            var canScorePlayersAway = awayPlayers.filter { it.position != "Goalkeeper" && it.id % 5 == 0 };
            if (canScorePlayersAway.isEmpty() || (awayScore != 0 && canScorePlayersAway.size < awayScore / 2))
                canScorePlayersAway = awayPlayers.filter { it.position != "Goalkeeper" };

            val resultBT = mutableListOf<GoalShort>()
            for (i in 0..<homeScore) {
                val player = canScorePlayersHome.get(i % canScorePlayersHome.size);
                resultBT.add(
                    GoalShort(
                        minute = player.id % 90,
                        playerName = player.name,
                        teamCode = currentMatch?.homeTeam?.tla ?: "",
                        action = "A"
                    )
                )
            }

            for (i in 0..<awayScore) {
                val player = canScorePlayersAway.get(i % canScorePlayersAway.size);
                resultBT.add(
                    GoalShort(
                        minute = player.id % 90,
                        playerName = player.name,
                        teamCode = currentMatch?.awayTeam?.tla ?: "",
                        action = "A"
                    )
                )
            }
            banThangs = resultBT;
        }
    }

    LaunchedEffect(Unit) {
        apiViewModel.loadTeams();
        apiViewModel.loadMatches(viewModel);
    }

    Scaffold(
        containerColor = DarkColorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
        topBar = {
            AppTopBar(
                title = "Lịch thi đấu",
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            if (isEditable)
                AddFloatingButton("Tạo lịch thi đấu", onClick = { navController.navigate("lichThiDauInput") })
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )  { innerPadding ->
        when (lichThiDaus) {
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
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )
            }
            is LoadingState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .padding(horizontal = 16.dp)
                        .background(DarkColorScheme.background),
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(30.dp)) // << SỬA: Tăng khoảng cách

                        currentMatch?.let {
                            MatchResult(
                                match = it,
                                onClick = {},
                                showOdd = true,
                                odd = odd
                            )
                        }
                        Spacer(modifier = Modifier.height(60.dp)) // << SỬA: Tăng khoảng cách

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(DarkColorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            when(videoId) {
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
                                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                                    )
                                }

                                is LoadingState.Success -> {
                                    YoutubeView(
                                        youtubeVideoId = (videoId as LoadingState.Success<String>).data,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        onError = {
                                            videoCount++;
                                            youtubeVideos.get(videoCount).let {
                                                videoId = LoadingState.Success(it.id.videoId)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(30.dp)) // << SỬA: Tăng khoảng cách
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(128.dp)
                                .padding(vertical = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    coroutineScope.launch {
                                        if (currentMatch == null) {
                                            snackbarHostState.showSnackbar("Trận đấu không hợp lệ");
                                            return@launch;
                                        }
                                        if (currentMatch!!.status == "FINISHED" )
                                            snackbarHostState.showSnackbar("Trận đấu đã kết thúc, không thể đặt cược");
                                        else if (currentMatch!!.status == "SUSPENDED" || currentMatch!!.status == "CANCELED")
                                            snackbarHostState.showSnackbar("Trận đấu đã bị hủy, không thể đặt cược");
                                        else if (currentMatch!!.status == "IN_PLAY" || currentMatch!!.status == "PAUSED")
                                            snackbarHostState.showSnackbar("Trận đấu đang diễn ra, không thể đặt cược");
                                        else
                                            showBettingDialog = true;
                                    }
                                },
                        ) {
                            // Home Team
                            val homeWin = bettingOptions[0].percentage.toFloat();
                            val awayWin = bettingOptions[2].percentage.toFloat();
                            val draw = 1.0f - (homeWin + awayWin);
                            if (homeWin > 0.01f) {
                                Box(
                                    modifier = Modifier
                                        .weight(homeWin)
                                        .fillMaxHeight()
                                        .background(homeColor),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "${currentMatch?.homeTeam?.tla}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            // Tie
                            if (draw > 0.01f) {
                                Box(
                                    modifier = Modifier
                                        .weight(draw)
                                        .fillMaxHeight()
                                        .background(drawColor),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "Draw",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            // Away Team
                            if (awayWin > 0.01f) {
                                Box(
                                    modifier = Modifier
                                        .weight(awayWin)
                                        .fillMaxHeight()
                                        .background(awayColor),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "${currentMatch?.awayTeam?.tla}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp)) // << SỬA: Tăng khoảng cách
                        StatisticRowHeader();
                    }
                    items (banThangs) { it ->
                        StatisticRowUpdated(
                            side = it.teamCode,
                            player = it.playerName,
                            action = it.action,
                            time = it.minute.toString(),
                            modifier)
                    }

                }
            }
        }
    }

    BettingDialog(
        showDialog = showBettingDialog,
        onDismiss = { showBettingDialog = false },
        selectedOption = bettingOption,
        allOptions = bettingOptions,
        onOptionSelected = {},
        onPlaceBet = { money, option ->
            viewModel.viewModelScope.launch {
                if (currentMatch == null || user == null) {
                    snackbarHostState.showSnackbar("Trận đấu hoặc người dùng không hợp lệ");
                    return@launch;
                }
                val caDo = CaDo(
                    userId = user!!.id,
                    maTD = currentMatch!!.id,
                    doiCuoc = option.teamId,
                    soTien = money.toInt()
                );
                viewModel.caDoDAO.upsertCaDo(caDo);
                caDos.add(caDo);
                setCaDo();
                snackbarHostState.showSnackbar("Đặt cược thành công: ${option.name} - ${money} VNĐ");
            }
            showBettingDialog = false;
        },
    )
}


@Composable
fun MatchResult(
    match: Match,
    onClick : () -> Unit,
    showOdd: Boolean = false,
    odd: Odds? = null,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A2234)) // Solid color background instead of gradient
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // Main content
        Column(modifier = Modifier.fillMaxWidth()) {
            // League and venue information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Competition name with flag
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    match.area.flag?.let { flagUrl ->
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(flagUrl)
                                .decoderFactory(SvgDecoder.Factory())
                                .build(),
                            contentDescription = "League flag",
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = match.competition.name,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Date/time
                Text(
                    text = match.utcDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status indicator
            StatusIndicator(
                status = match.status,
//                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Teams and score
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home team
                TeamDisplay(
                    team = match.homeTeam,
                    alignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                )

                // Score in the middle
                if (match.status != "SCHEDULED" && match.status != "TIMED") {
                    ScoreDisplay(
                        homeScore = match.score.fullTime.home,
                        awayScore = match.score.fullTime.away,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                } else {
                    Text(
                        text = "VS",
                        color = Color(0xFFE2B257), // Gold color for VS
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // Away team
                TeamDisplay(
                    team = match.awayTeam,
                    alignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                )
            }

            // Venue information if available
            match.venue?.let { venue ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Venue",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = venue,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Odds section
            if (showOdd) {
                OddsSection(
                    match = match,
                    odd = odd,
                )
            }
        }
    }
}

@Composable
private fun TeamDisplay(
    team: MatchTeam,
    alignment: Alignment.Horizontal,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        AsyncImage(
            model = team.crest,
            contentDescription = "${team.name} logo",
            modifier = Modifier.size(56.dp),
            error = painterResource(id = R.drawable.ic_menu_help)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = team.name,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = if (alignment == Alignment.Start) TextAlign.Start else TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = team.shortName ?: "",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            textAlign = if (alignment == Alignment.Start) TextAlign.Start else TextAlign.End
        )
    }
}

@Composable
private fun ScoreDisplay(
    homeScore: Int?,
    awayScore: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = "${homeScore ?: "-"}",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " : ",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${awayScore ?: "-"}",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun OddsItem(
    label: String,
    value: Double?,
    teamName: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .width(85.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2A3548)
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = value?.toString() ?: "-",
                    color = Color(0xFFE2B257), // Gold color for odds
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        teamName?.let {
            Text(
                text = it,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun StatisticRowHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Team",
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.15f), // Cột Side
            textAlign = TextAlign.Center
        )
        Text(
            text = "Player",
            color = textWhiteColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier
                .weight(0.5f)
                .padding(start = 8.dp) // Cột Player, chiếm nhiều không gian nhất
        )
        Text(
            text = "Action",
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.15f), // Cột Action
            textAlign = TextAlign.Center
        )
        Text(
            text = "Time",
            color = textMutedColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.2f), // Cột Time
            textAlign = TextAlign.End
        )
    }

    // Divider to separate header from content
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.2f))
    )
}
