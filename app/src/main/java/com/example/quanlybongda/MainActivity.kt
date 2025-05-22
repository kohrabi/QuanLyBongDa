package com.example.quanlybongda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.ui.jetpackcompose.screens.BaoCaoScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.GhiNhanScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.KetQuaTranDauScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.Input.BanThangInputScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.LapLichScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.Input.CauThuInputScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.Input.DoiBongInputScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.Input.LichThiDauInputScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.LoginScreen
import com.example.quanlybongda.ui.jetpackcompose.screens.SignUpScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val databaseViewModel: DatabaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuanLyBongDaTheme {
                AppNavigation();
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController();
    var selectedItem by remember { mutableStateOf("") }

    val topLevelRoutes = listOf(
        "login",
        "signUp",
        "baoCao",
        "ghiNhan",
        "hoSo",
        "lapLich",
        "cauThu",
        "banThang",
        "doiBong",
        "lichThiDau",
    )

    Scaffold(
        bottomBar = {
//            BottomNavigation(
//                backgroundColor = Color(0xFF1C1C2A),
//                contentColor = Color.White
//            ) {
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
//                    selected = true,
//                    onClick = { /* TODO */ }
//                )
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Default.List, contentDescription = "Ranking") },
//                    selected = false,
//                    onClick = { /* TODO */ }
//                )
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Default.Schedule, contentDescription = "Schedule") },
//                    selected = false,
//                    onClick = { /* TODO */ }
//                )
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
//                    selected = false,
//                    onClick = { /* TODO */ }
//                )
//            }
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                topLevelRoutes.forEach { topLevelRoute ->
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(topLevelRoute) },
                        selected = selectedItem == topLevelRoute,
                        onClick = {
                            selectedItem = topLevelRoute
                            navController.navigate(topLevelRoute) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { it
        NavHost(navController = navController,startDestination = "login") {
            composable("login") { LoginScreen(navController, Modifier) }
            composable("signUp") { SignUpScreen(navController, Modifier) }
            composable("baoCao") { BaoCaoScreen(navController, Modifier) }
            composable("ghiNhan") { GhiNhanScreen(navController) }
            composable("hoSo") { KetQuaTranDauScreen(Modifier) }
            composable("lapLich") { LapLichScreen(navController, Modifier) }
            composable("cauThu") { CauThuInputScreen() }
            composable("doiBong") { DoiBongInputScreen() }
            composable("banThang") { BanThangInputScreen() }
            composable("lichThiDau") { LichThiDauInputScreen() }
        }
    }
}