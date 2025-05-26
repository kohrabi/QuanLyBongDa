package com.example.quanlybongda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quanlybongda.ui.jetpackcompose.screens.*
import com.example.quanlybongda.ui.jetpackcompose.screens.Input.*
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.darkContentBackground
import com.example.quanlybongda.ui.theme.darkTextMuted

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
            saveState = true
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
    var selectedItem by remember { mutableStateOf("") }

    val routes = listOf(
//        BottomNavigationType("login", "Home", Icons.Default.Home),
//        BottomNavigationType("signUp", ,),
//        BottomNavigationRoute("banThang/1", "Home", Icons.Default.Home),
        BottomNavigationRoute("baoCao", "Báo cáo", Icons.Default.Home),
        BottomNavigationRoute("lapLich", "Lập lịch", Icons.Default.Schedule),
        BottomNavigationRoute("muaGiai", "Mùa giải", Icons.Default.Star),
        BottomNavigationRoute("doiBong", "Đội bóng", Icons.Default.Groups),
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            if (currentDestination?.route != "login" && currentDestination?.route != "signUp") {
                BottomNavigation(
                    backgroundColor = darkContentBackground.copy(alpha = 0.9f),
                    contentColor =  darkTextMuted,
                ) {
                    routes.forEach { route ->
                        BottomNavigationItem(
                            icon = { Icon(route.icon, contentDescription = route.description) },
                            selected = selectedItem == route.name,
                            onClick = {
                                selectedItem = route.name
                                navController.navigate(route.name) {
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
                            },
                            selectedContentColor = Color.White,
                            unselectedContentColor = darkTextMuted,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.fillMaxSize().background(DarkColorScheme.background)
        ) {
            val modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding());
            composable("login") { LoginScreen(navController, modifier) }
            composable("signUp") { SignUpScreen(navController, modifier) }
            composable("baoCao") { BaoCaoScreen(navController, modifier) }
            composable("banThang/{maTD}", arguments = listOf(navArgument("maTD") { type = NavType.IntType})) { backStackEntry ->
                BanThangScreen(backStackEntry.arguments?.getInt("maTD") ?: 1, navController, modifier)
            }
            composable("hoSo") { KetQuaTranDauScreen(navController, modifier) }
            composable("lapLich") { LapLichScreen(navController, modifier) }
            composable("muaGiai") { MuaGiaiScreen(navController, modifier) }
            composable("doiBong") { DoiBongScreen(navController, modifier) }
            composable("cauThu/{maDoi}", arguments = listOf(navArgument("maDoi") { type = NavType.IntType})) { backStackEntry ->
                CauThuScreen(backStackEntry.arguments?.getInt("maDoi") ?: 0, navController, modifier)
            }

            composable("cauThuInput/{maDoi}", arguments = listOf(navArgument("maDoi") { type = NavType.IntType})) { backStackEntry ->
                CauThuInputScreen(backStackEntry.arguments?.getInt("maDoi") ?: 0, navController, modifier)
            }
            composable("doiBongInput") { DoiBongInputScreen(navController, modifier) }
            composable("banThangInput/{maTD}", arguments = listOf(navArgument("maTD") { type = NavType.IntType})) { backStackEntry ->
                if (backStackEntry.arguments == null) {
                    navController.popBackStack();
                    return@composable;
                }
                BanThangInputScreen(backStackEntry.arguments!!.getInt("maTD"), navController, modifier)
            }
            composable("lichThiDauInput") { LichThiDauInputScreen(navController, modifier) }
            composable("muaGiaiInput") { MuaGiaiInputScreen(navController, modifier) }
        }
    }
}
