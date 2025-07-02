package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Services.Data.Match
import com.example.quanlybongda.Services.FootballAPIViewModel
import com.example.quanlybongda.Services.LoadingState
import com.example.quanlybongda.ui.theme.Purple80
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BettingHistoryComponent(
    navController: NavController,
    viewModel: DatabaseViewModel = hiltViewModel(),
    apiViewModel: FootballAPIViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val matches by apiViewModel.matches.collectAsState()
    val competition by apiViewModel.currentCompetition.collectAsState()
    val season by apiViewModel.currentSeason.collectAsState()
    var userBets by remember { mutableStateOf<List<UserBetWithMatchDetails>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(user, matches) {
        if (user != null && matches is LoadingState.Success) {
            val matchesData = (matches as LoadingState.Success<List<Match>>).data

            // Fetch user's betting history
            val userCaDos = viewModel.caDoDAO.selectCaDoByUserID(user!!.id)

            // Map CaDo objects to UserBetWithMatchDetails
            userBets = userCaDos.mapNotNull { caDo ->
                val match = matchesData.find { it.id == caDo.maTD }
                match?.let {
                    val homeTeam = it.homeTeam
                    val awayTeam = it.awayTeam

                    // Determine which team the user bet on
                    val betTeam = when (caDo.doiCuoc) {
                        homeTeam.id -> BettingTeam.HOME
                        awayTeam.id -> BettingTeam.AWAY
                        else -> BettingTeam.DRAW
                    }

                    // Determine outcome based on match results
                    val outcome = if (it.status == "FINISHED") {
                        val homeScore = it.score.fullTime.home ?: 0
                        val awayScore = it.score.fullTime.away ?: 0

                        when {
                            homeScore > awayScore && betTeam == BettingTeam.HOME -> BetOutcome.WIN
                            homeScore < awayScore && betTeam == BettingTeam.AWAY -> BetOutcome.WIN
                            homeScore == awayScore && betTeam == BettingTeam.DRAW -> BetOutcome.WIN
                            else -> BetOutcome.LOSE
                        }
                    } else {
                        BetOutcome.PENDING
                    }

                    UserBetWithMatchDetails(
//                        betId = caDo,
                        match = it,
                        betAmount = caDo.soTien,
                        betTeam = betTeam,
//                        outcome = outcome,
//                        betDate = caDo.timestamp ?: System.currentTimeMillis()
                    )
                }
            }

            isLoading = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A2234)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Betting History for ${competition?.name ?: "Competition not selected"} - ${season}",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Content
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Purple80,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                userBets.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No betting history found for this season",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 16.sp
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.height(350.dp)
                    ) {
                        items(userBets) { bet ->
                            BetHistoryItem(bet = bet) {
                                // Navigate to match details
                                navController.navigate("lapLich/${bet.match.id}") {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BetHistoryItem(
    bet: UserBetWithMatchDetails,
    onClick: () -> Unit
) {

    var outcomeColor = Color.Transparent;
    var outcomeText = "";
    if (bet.match.status != "FINISHED" || bet.match.score.winner == null) {
        outcomeColor = Color(0xFFE2B257);
        outcomeText = "Pending";
    }
    else if (bet.match.status == "FINISHED") {
        when (bet.betTeam) {
            BettingTeam.HOME -> {
                if (bet.match.score.winner == "HOME_TEAM") {
                    outcomeColor = Color(0xFF0D904B)
                    outcomeText = "Won"
                } else {
                    outcomeColor = Color(0xFFD13030)
                    outcomeText = "Lost"
                }
            }
            BettingTeam.AWAY -> {
                if (bet.match.score.winner == "AWAY_TEAM") {
                    outcomeColor = Color(0xFF0D904B)
                    outcomeText = "Won"
                } else {
                    outcomeColor = Color(0xFFD13030)
                    outcomeText = "Lost"
                }
            }
            BettingTeam.DRAW -> {
                if (bet.match.score.winner == "DRAW") {
                    outcomeColor = Color(0xFF0D904B)
                    outcomeText = "Won"
                } else {
                    outcomeColor = Color(0xFFD13030)
                    outcomeText = "Lost"
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = outcomeColor,
                spotColor = outcomeColor
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A3548)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Match info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${bet.match.homeTeam.name} vs ${bet.match.awayTeam.name}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Date
//                Text(
//                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                        .format(Date(bet.betDate)),
//                    color = Color.White.copy(alpha = 0.7f),
//                    fontSize = 12.sp
//                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Competition name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bet.match.competition.emblem)
                        .crossfade(true)
                        .build(),
                    contentDescription = bet.match.competition.name,
                    modifier = Modifier.size(16.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = bet.match.competition.name,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Betting details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team bet on
                val betTeamText = when (bet.betTeam) {
                    BettingTeam.HOME -> bet.match.homeTeam.name
                    BettingTeam.AWAY -> bet.match.awayTeam.name
                    BettingTeam.DRAW -> "Draw"
                }

                val betColor = when (bet.betTeam) {
                    BettingTeam.HOME -> homeColor
                    BettingTeam.AWAY -> awayColor
                    BettingTeam.DRAW -> drawColor
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(betColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(betColor, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = betTeamText,
                        color = betColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Bet amount
                Text(
                    text = "₫${NumberFormat.getNumberInstance(Locale.US).format(bet.betAmount)}",
                    color = Color(0xFFE2B257),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Outcome indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = outcomeText,
                    color = outcomeColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Data class to hold bet information with match details
data class UserBetWithMatchDetails(
    val match: Match,
    val betAmount: Int,
    val betTeam: BettingTeam,
)

enum class BettingTeam {
    HOME, AWAY, DRAW
}

enum class BetOutcome {
    WIN, LOSE, PENDING
}