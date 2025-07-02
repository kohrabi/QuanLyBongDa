package com.example.quanlybongda.ui.jetpackcompose.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlybongda.Services.Data.Match
import com.example.quanlybongda.Services.Data.Odds
import kotlin.math.min


@Composable
fun BettingDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    balance: Int,
    selectedOption: BettingOption,
    allOptions: List<BettingOption>,
    onOptionSelected: (BettingOption) -> Unit,
    onPlaceBet: (Int, BettingOption) -> Unit
) {
    if (showDialog) {
        val betAmount = remember { mutableStateOf("") }
        val currentOption = remember { mutableStateOf(selectedOption) }

        LaunchedEffect(selectedOption) {
            currentOption.value = selectedOption
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = Color(0xFF1A2234),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Color indicator bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(currentOption.value.color)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Place Your Bet",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    // Betting options selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        allOptions.forEach { option ->
                            BettingOptionChip(
                                option = option,
                                isSelected = currentOption.value == option,
                                onClick = {
                                    currentOption.value = option
                                    onOptionSelected(option)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Community betting stats
                    CommunityBettingStats(allOptions)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Enter betting amount:",
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = betAmount.value,
                        onValueChange = {
                            // Only accept digits and ensure min value of 100,000 VND
                            if (it.isEmpty() || it.matches(Regex("^\\d+$"))) {
                                betAmount.value = min((it.toIntOrNull() ?: 0), balance).toString()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        leadingIcon = {
                            Text(
                                text = "₫",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        placeholder = {
                            Text(
                                text = "100,000",
                                color = Color.Gray
                            )
                        },
                        supportingText = {
                            Text(
                                text = "Minimum bet: 100,000 VND",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        },
                        isError = betAmount.value.isNotEmpty() &&
                                (betAmount.value.toIntOrNull() ?: 0) < 100_000
                    )

                    Text(
                        text = "Your balance: ₫${balance}",
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp),
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (betAmount.value.isEmpty() || (betAmount.value.toIntOrNull() ?: 0) < 100_000) {
                            Log.e("BettingDialog", "Invalid bet amount: ${betAmount.value}")
                            return@Button
                        }
                        betAmount.value.toIntOrNull()?.let { amount ->
                            onPlaceBet(amount, currentOption.value)
                        }
                        onDismiss()
                    },
                    enabled = betAmount.value.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentOption.value.color
                    )
                ) {
                    Text("Place Bet")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White.copy(alpha = 0.7f)
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun BettingOptionChip(
    option: BettingOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) option.color.copy(alpha = 0.2f) else Color(0xFF2A3548)
        ),
        border = if (isSelected) BorderStroke(2.dp, option.color) else null
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Color indicator dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(option.color, CircleShape)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = option.name.split(" ").firstOrNull() ?: "",
                color = if (isSelected) option.color else Color.White.copy(alpha = 0.8f),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${option.percentage}%",
                color = if (isSelected) option.color else Color(0xFFE2B257),
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CommunityBettingStats(options: List<BettingOption>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2A3548))
            .padding(12.dp)
    ) {
        Text(
            text = "Community Betting Stats",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal bar chart showing bet distribution
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
        ) {
            options.forEach { option ->

//                if (option.percentage.toFloat() < 0.01f) return@forEach // Skip options with negligible percentage
                Box(
                    modifier = Modifier
                        .weight(if (option.percentage <= 0.01) 0.01f else option.percentage.toFloat())
                        .fillMaxHeight()
                        .background(option.color)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(option.color, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${option.percentage}%",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// Data class to represent betting options
data class BettingOption(
    val name: String,
    var percentage: Double,
    val color: Color,
    var teamId: Int? = null,
)

@Composable
fun OddsSection(
    match: Match,
    odd: Odds?
) {
    var showDialog by remember { mutableStateOf(false) }

    val options = listOf(
        BettingOption("Home Win", odd?.homeWin ?: 0.0, homeColor),
        BettingOption("Draw", odd?.draw ?: 0.0, drawColor),
        BettingOption("Away Win", odd?.awayWin ?: 0.0, awayColor)
    )

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Match Odds",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            options.forEach { it ->
                OddsItem(
                    label = it.name,
                    value = it.percentage,
                    teamName = match.homeTeam.name,
                    color = it.color,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun OddsItem(
    label: String,
    value: Double?,
    teamName: String?,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .width(85.dp)
                .clickable { onClick() },
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

                // Small color indicator
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(width = 24.dp, height = 3.dp)
                        .background(color = color, shape = RoundedCornerShape(1.dp))
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