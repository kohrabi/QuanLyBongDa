package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun HoSoScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F24))
    ) {


        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopBar(modifier)

            PlayerCard(
                team = "Barcelona",
                name = "Frenkie De Jong",
                score = "9",
                modifier
            )

            TeamRow(
                teamName = "Manchester City",
                score = "2",
                logoUrl = "https://upload.wikimedia.org/wikipedia/en/e/eb/Manchester_City_FC_badge.svg",
                modifier
            )

            // List of Players
            PlayerRow("Cooper Calzoni", "4", modifier)
            PlayerRow("Alfredo Saris", "4", modifier)
            PlayerRow("Jakob Levin", "4", modifier)
            PlayerRow("Alfonso Kenter", "3", modifier)
            PlayerRow("Emerson Septimus", "3", modifier)
            PlayerRow("Brandon Vaccaro", "2", modifier)

            Spacer(modifier = modifier.height(24.dp))
        }
    }
}

@Composable
fun TopBar(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
        )
        Text(
            text = "Goals Scored",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Menu",
            tint = Color.White
        )
    }
}

@Composable
fun PlayerCard(team: String, name: String, score: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1B2038))
            .fillMaxWidth()
            .padding(vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = team,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = name,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Goals Score",
                color = Color(0xFFDADADA),
                fontSize = 12.sp,
                modifier = modifier.padding(top = 6.dp)
            )
            Text(
                text = score,
                color = Color(0xFFFF9E0D),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun TeamRow(teamName: String, score: String, logoUrl: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = logoUrl,
            contentDescription = null,
            modifier = modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Text(
            text = teamName,
            color = Color.White,
            fontSize = 14.sp,
            modifier = modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Text(
            text = score,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun PlayerRow(name: String, goals: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF414158))
        )
        Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            modifier = modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        Text(
            text = goals,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0B0F)
@Composable
fun HoSoScreenPreview() {
    MaterialTheme {
        HoSoScreen(Modifier)
    }
}