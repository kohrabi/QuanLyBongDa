package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.DateConverter
import com.example.quanlybongda.Database.Schema.LichThiDau
import com.example.quanlybongda.Services.Converters.LocalDateConverter
import com.example.quanlybongda.Services.Data.Match
import com.example.quanlybongda.Services.Data.Team
import com.example.quanlybongda.Services.Data.YouTubeVideoItem
import com.example.quanlybongda.Services.FootballAPIViewModel
import com.example.quanlybongda.Services.LoadingState
import com.example.quanlybongda.Services.YoutubeAPI
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80


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
    val snackbarHostState = remember { SnackbarHostState() }
    var isEditable by remember { mutableStateOf(false) }
    var currentMatch by remember { mutableStateOf<Match?>(null)}
    var videoId : LoadingState<String> by remember { mutableStateOf(LoadingState.Loading) }
    var youtubeVideos by remember { mutableStateOf<List<YouTubeVideoItem>>(listOf())}
    var videoCount by remember { mutableStateOf(0) }

    LaunchedEffect(lichThiDaus) {
        if (lichThiDaus is LoadingState.Success) {
            val matches = (lichThiDaus as LoadingState.Success<List<Match>>).data
            currentMatch = matches.firstOrNull{ it.id == maTD };
        }

        val videos = YoutubeAPI.retrofitService.searchVideos(
            query = "${currentMatch?.homeTeam?.name} vs ${currentMatch?.awayTeam?.name} ${currentMatch?.utcDate?.year}"
        );
        if (videos.items.isEmpty()) {
            videoId = LoadingState.Error("Không tìm thấy video cho trận đấu này")
            return@LaunchedEffect
        }
        youtubeVideos = videos.items;
        youtubeVideos.get(videoCount).let {
            videoId = LoadingState.Success(it.id.videoId)
        }
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
                            FeaturedMatch(
                                match = it,
                                onClick = {}
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
                }
            }
        }
    }
}