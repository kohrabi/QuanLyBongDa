package com.example.quanlybongda

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp // Thêm import này để sử dụng dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.quanlybongda.ui.jetpackcompose.screens.BaoCao
import com.example.quanlybongda.ui.jetpackcompose.screens.GhiNhan
import com.example.quanlybongda.ui.jetpackcompose.screens.HoSo
import com.example.quanlybongda.ui.jetpackcompose.screens.LapLich
import com.example.quanlybongda.ui.jetpackcompose.screens.Login
import com.example.quanlybongda.ui.jetpackcompose.screens.SignIn3
import com.example.quanlybongda.ui.jetpackcompose.screens.SignIn2
import com.example.quanlybongda.ui.jetpackcompose.screens.TraCuu
import com.example.quanlybongda.ui.jetpackcompose.screens.MuaGiai
import com.example.quanlybongda.ui.jetpackcompose.screens.DoiBong
import com.example.quanlybongda.ui.jetpackcompose.screens.TaoDoiBong

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val databaseViewModel: DatabaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Đảm bảo thanh trạng thái được hiển thị
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            QuanLyBongDaTheme {
                // Lấy chiều cao của thanh trạng thái
                val density = LocalDensity.current
                val statusBarHeight = with(density) {
                    WindowInsets.statusBars.getTop(density).toDp()
                }

                // Khoảng cách bổ sung giữa thanh trạng thái và giao diện
                val additionalSpacing = 8.dp // Đã sửa lỗi bằng cách thêm import dp

                // Bao bọc giao diện chính để thêm padding phía trên
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = statusBarHeight + additionalSpacing)
                ) {
//                    BaoCao()
//                    GhiNhan()
//                    HoSo()
//                    LapLich()
//                    Login()
//                    SignIn3()
//                    SignIn2()
//                    TraCuu()
//                    MuaGiai()
//                    DoiBong()
                    TaoDoiBong()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, viewModel: DatabaseViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        val test = viewModel.selectAllUserGroupWithRole()
        Log.d("AY", test.toString())
    }

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuanLyBongDaTheme {
        Greeting("Android")
    }
}