package com.example.quanlybongda

import android.provider.ContactsContract.Data
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.Database.Schema.LichThiDau
import com.example.quanlybongda.Database.Schema.MuaGiai
import com.example.quanlybongda.Database.Schema.User.User
import com.example.quanlybongda.Services.FootballAPIViewModel
import com.example.quanlybongda.ui.jetpackcompose.screens.*
import com.example.quanlybongda.ui.jetpackcompose.screens.Input.*
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import com.example.quanlybongda.ui.theme.PurpleGrey80
import com.example.quanlybongda.ui.theme.darkContentBackground
import com.example.quanlybongda.ui.theme.darkTextMuted
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

data class BottomNavigationRoute(
    val name : String,
    val description : String,
    val icon : ImageVector
)


fun navigatePopUpTo(navController: NavController, routeName : String) {
    navController.navigate(routeName) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = false
            inclusive = false
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

val homeRoute = "baoCao";
@Composable
fun AppNavigation() {
    val navController = rememberNavController();
    val rootRoute = "login"
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember { derivedStateOf { currentBackStackEntry?.destination?.route ?: homeRoute } }
    val viewModel : DatabaseViewModel = hiltViewModel();
    val apiViewModel : FootballAPIViewModel = hiltViewModel();
    val routes = listOf(
        BottomNavigationRoute("baoCao", "Báo cáo", Icons.Default.Home),
        BottomNavigationRoute("lapLich", "Lập lịch", Icons.Default.Schedule),
        BottomNavigationRoute("muaGiai", "Mùa giải", Icons.Default.Star),
        BottomNavigationRoute("doiBong", "Đội bóng", Icons.Default.Groups),
        BottomNavigationRoute("traCuu", "Tra cứu", Icons.Default.Search),
        BottomNavigationRoute("settings", "Cài đặt", Icons.Default.Settings),
    )

    Scaffold(
        bottomBar = {
            if (currentRoute != "login" && currentRoute != "signUp") {
                NavigationBar(
                    containerColor = darkContentBackground//.copy(alpha = 0.9f),
//                    contentColor =  darkTextMuted,
                ) {
                    routes.forEach { route ->
                        NavigationBarItem(
                            icon = {
                                Icon(imageVector = route.icon, contentDescription = route.description)
                            },
                            label = {
                                Text(route.description, color = Color.White)
                            },
                            selected = currentRoute == route.name,
                            onClick = { navigatePopUpTo(navController, route.name) },
                            colors = NavigationBarItemColors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = Purple80,
                                selectedIndicatorColor = Purple80,
                                unselectedIconColor = Color.White,
                                unselectedTextColor = Color.Transparent,
                                disabledIconColor = PurpleGrey80,
                                disabledTextColor = Color.Transparent,
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = rootRoute,
            modifier = Modifier.fillMaxSize().background(DarkColorScheme.background)
        ) {
            val modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding());
            composable("login") { LoginScreen(navController, modifier, viewModel) }
            composable("signUp") { SignUpScreen(navController, modifier, viewModel) }
            composable("settings") { SettingsScreen(navController, modifier, viewModel) }

            composable("baoCao") { BaoCaoScreen(navController, modifier, viewModel, apiViewModel) }
            composable("hoSo") { KetQuaTranDauScreen(navController, modifier, viewModel) }
            composable("traCuu") { TraCuuScreen(navController, modifier, viewModel, apiViewModel) }
            composable("lapLich") { LapLichScreen(navController, modifier, viewModel, apiViewModel) }
            composable("muaGiai") { MuaGiaiScreen(navController, modifier, viewModel, apiViewModel) }
            composable("doiBong") { DoiBongScreen(navController, modifier, viewModel, apiViewModel) }
            composable("cauThu/{maDoi}", arguments = listOf(navArgument("maDoi") { type = NavType.IntType})) { backStackEntry ->
                CauThuScreen(backStackEntry.arguments?.getInt("maDoi") ?: 0, navController, modifier, viewModel, apiViewModel)
            }
            composable("lapLich/{maTD}", arguments = listOf(navArgument("maTD") { type = NavType.IntType })) { backStackEntry ->
                val maTD = backStackEntry.arguments?.getInt("maTD") ?: 0
                XemTranDauScreen(
                    maTD = maTD,
                    navController = navController,
                    modifier = modifier,
                    viewModel = viewModel,
                    apiViewModel = apiViewModel
                )
            }

            composable("userInput/{userID}", arguments = listOf(navArgument("userID") { type = NavType.IntType})) {backStackEntry ->

                val savedStateHandle = backStackEntry.savedStateHandle;
                val user = User(
                    id = savedStateHandle.get("id")!!,
                    email = savedStateHandle.get("email") ?: "",
                    passwordHash = "",
                    username = savedStateHandle.get("username") ?: "",
                    groupId = savedStateHandle.get("groupId") ?: 0
                )
                UserInputScreen(
                    user = user,
                    navController = navController,
                    modifier = modifier,
                    viewModel = viewModel
                );

            }
        }
    }
}
