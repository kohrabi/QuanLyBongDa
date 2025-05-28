package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.PurpleBlue
import kotlinx.coroutines.launch

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush

@Composable
fun SettingsUserInfo(
    username: String,
    onLogOutClick: () -> Unit = {},
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),

    ) {
        Text(
            text = "Signed in as ",
            modifier = Modifier,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            color = Color.White,
//                        fontWeight = FontWeight.Bold
        )
        Text(
            text = username,
            modifier = Modifier,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,
            color = Purple80,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onLogOutClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF4568DC), Color(0xFFB06AB3))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Log out", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current;
    val coroutineScope = rememberCoroutineScope()

    val user by viewModel.user.collectAsState()
    val session by viewModel.session.collectAsState()

    var tuoiMin by remember { mutableStateOf<Int?>(16) }
    var tuoiMax by remember { mutableStateOf<Int?>(40) }
    var soCauThuMin by remember { mutableStateOf<Int?>(15) }
    var soCauThuMax by remember { mutableStateOf<Int?>(25) }

    var doiDaTrenSanNha by remember { mutableStateOf(OptionValue(1, "Nhà")) }

    var thoiDiemGhiBanToiThieu by remember { mutableStateOf<Float?>(0f) }
    var thoiDiemGhiBanToiDa by remember { mutableStateOf<Float?>(90f) }

    val onClick = {

    };

    Scaffold(
        containerColor = DarkColorScheme.background,
        topBar = {
            AppTopBar(
                title = "Cài đặt",
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            item {
                SettingsUserInfo(
                    username = user?.username ?: "",
                    onLogOutClick = {
                        viewModel.viewModelScope.launch {
                            if (session != null) {
                                viewModel.invalidateSession(session!!.sessionId)
                                navController.navigate("login");
                            }
                        }
                    },
                    navController = navController,
                    modifier = modifier,
                    viewModel = viewModel
                );
                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    color = PurpleBlue,
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                InputIntField(
                    label = "Tuổi cầu thủ tối thiểu",
                    value = tuoiMin,
                    onValueChange = { tuoiMin = it },
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputIntField(
                    label = "Tuổi cầu thủ tối đa",
                    value = tuoiMax,
                    onValueChange = { tuoiMax = it },
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputIntField(
                    label = "Số cầu thủ tối thiểu",
                    value = soCauThuMin,
                    onValueChange = { soCauThuMin = it },
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputIntField(
                    label = "Số cầu thủ tối đa",
                    value = soCauThuMax,
                    onValueChange = { soCauThuMax = it },
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    label = "Đội đá trên sân nhà",
                    options = listOf(OptionValue(1, "Nhà"), OptionValue(2, "Khách")),
                    selectedOption = doiDaTrenSanNha,
                    onOptionSelected = { doiDaTrenSanNha = it },
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputFloatField(
                    label = "Thời điểm ghi bàn tổi thiểu",
                    value = thoiDiemGhiBanToiThieu,
                    onValueChange = { thoiDiemGhiBanToiThieu = it },
                )
                Spacer(modifier = Modifier.height(16.dp))

                InputFloatField(
                    label = "Thời điểm ghi bàn tổi đa",
                    value = thoiDiemGhiBanToiDa,
                    onValueChange = { thoiDiemGhiBanToiDa = it },
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF4568DC), Color(0xFFB06AB3))
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Lưu", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(rememberNavController())
    }
}