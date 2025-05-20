package com.example.quanlybongda

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quanlybongda.Database.Schema.CauThu
import com.example.quanlybongda.ui.theme.QuanLyBongDaTheme
import com.example.quanlybongda.ui.jetpackcompose.screens.BaoCao
import com.example.quanlybongda.ui.jetpackcompose.screens.GhiNhan
import com.example.quanlybongda.ui.jetpackcompose.screens.HoSo
import com.example.quanlybongda.ui.jetpackcompose.screens.LapLich
import com.example.quanlybongda.ui.jetpackcompose.screens.Login
import com.example.quanlybongda.ui.jetpackcompose.screens.SignIn3
import com.example.quanlybongda.ui.jetpackcompose.screens.SignIn2



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val databaseViewModel: DatabaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            QuanLyBongDaTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                    HoSoScreen()
//                }
//            }
            QuanLyBongDaTheme {
                // Gọi trực tiếp Composable màn hình của bạn ở đây
//                BaoCao()
//                GhiNhan()
//                HoSo()
//                LapLich()
//                Login()
//                SignIn3()
//                SignIn2()


            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, viewModel: DatabaseViewModel = hiltViewModel()) {
//    var cauThu by remember { mutableStateOf<List<CauThu>>(listOf()) }

    LaunchedEffect(Unit) {
        val test = viewModel.selectAllUserGroupWithRole();
        Log.d("AY", test.toString());
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