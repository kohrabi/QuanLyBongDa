package com.example.quanlybongda.ui.jetpackcompose.screens.Input

import android.widget.Toast
import com.example.quanlybongda.ui.jetpackcompose.screens.InputTextField
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.DoiBong
import com.example.quanlybongda.navigatePopUpTo
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush

@Composable
fun DoiBongInputScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val context = LocalContext.current;
    val coroutineScope = rememberCoroutineScope()
    val currentMuaGiai by DatabaseViewModel.currentMuaGiai.collectAsState()
    var tenDoi by remember { mutableStateOf("") }
    var sanNhaOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    var sanNha by remember { mutableStateOf(OptionValue.DEFAULT) }
    val onClick = {
        coroutineScope.launch {
            if (currentMuaGiai == null) {
                Toast.makeText(context, "WARNING: muaGiai không hợp lệ", Toast.LENGTH_SHORT).show();
                navigatePopUpTo(navController, "muaGiai");
                return@launch;
            }
            viewModel.doiBongDAO.upsertDoiBong(
                DoiBong(
                    tenDoi = tenDoi,
                    maSan = sanNha.value,
                    maMG = currentMuaGiai!!.maMG,
                )
            )
            delay(500);
            navController.popBackStack()
        }
    };

    LaunchedEffect(Unit) {
        sanNhaOptions = viewModel.sanNhaDAO.selectAllSanNha().map { OptionValue(it.maSan, it.tenSan) };
    }

    Scaffold(
        backgroundColor = Color(0xFF181928),
        modifier = modifier.padding(top = 24.dp)
    ) { innerScaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(innerScaffoldPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                InputTextField(value = tenDoi, label = "Tên đội", onValueChange = { tenDoi = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    name = "Sân nhà",
                    options = sanNhaOptions,
                    selectedOption = sanNha,
                    onOptionSelected = { sanNha = it })
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val buttonModifier = Modifier.weight(1f).height(48.dp)
                    Button(
                        onClick = { navController.popBackStack() },
                        shape = RoundedCornerShape(7.dp),
                        contentPadding = PaddingValues(),
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                brush = Brush.horizontalGradient(colors = listOf(Color(0xFF4568DC), Color(0xFFB06AB3)))
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("New Player", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Button(
                        onClick = { onClick() },
                        shape = RoundedCornerShape(7.dp),
                        contentPadding = PaddingValues(),
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(
                                brush = Brush.horizontalGradient(colors = listOf(Color(0xFFDC456F), Color(0xFFB06AB3)))
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Finish", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
fun DoiBongInputScreenPreview() {
    MaterialTheme {
        DoiBongInputScreen(rememberNavController())
    }
}