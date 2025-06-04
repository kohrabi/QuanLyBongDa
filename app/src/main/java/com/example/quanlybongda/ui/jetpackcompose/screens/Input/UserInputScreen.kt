package com.example.quanlybongda.ui.jetpackcompose.screens.Input

// import androidx.compose.ui.geometry.Offset // Cần nếu dùng Offset trong Brush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Schema.User.User
import com.example.quanlybongda.ui.jetpackcompose.screens.AppTopBar
import com.example.quanlybongda.ui.jetpackcompose.screens.InputDropDownMenu
import com.example.quanlybongda.ui.jetpackcompose.screens.InputTextField
import com.example.quanlybongda.ui.jetpackcompose.screens.OptionValue
import com.example.quanlybongda.ui.theme.DarkColorScheme
import com.example.quanlybongda.ui.theme.Purple80
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputScreen(
    user: User,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current;
    val coroutineScope = rememberCoroutineScope()

    var submitted by remember { mutableStateOf(false) }
    var clicked by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf(user.email) }
    var group by remember { mutableStateOf(OptionValue(null, "Vui lòng chọn mùa giải")) }
    var groupOptions by remember { mutableStateOf(listOf<OptionValue>()) }
    val onClick : () -> Unit = {
        clicked = true;
        if (!submitted) {
            coroutineScope.launch {
                viewModel.userDAO.updateUserGroup(
                    user.id,
                    group.value!!,
                )
                submitted = true;
                delay(500);
                navController.popBackStack()
            }
        }
    };

    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            groupOptions = viewModel.userGroupDAO.selectAllUserGroup().map { OptionValue(it.groupId, it.groupName) };
            group = groupOptions.find { it.value == user.groupId }!!;
        }
    }

    Scaffold(
        containerColor = DarkColorScheme.background,
        topBar = {
            AppTopBar(
                title = "User Input",
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = Purple80,
                        )
                    }
                }
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
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
                // Username
//
//                InputTextField(
//                    value = email,
//                    label = "Email",
//                    onValueChange = { email = it },
//                    isError = email.isEmpty() && clicked,
//                    errorMessage = "Email trống");
//                Spacer(modifier = Modifier.height(16.dp))

                InputDropDownMenu(
                    label = "User group",
                    options = groupOptions,
                    selectedOption = group,
                    onOptionSelected = {
                        group = it;
                    },
                    showEmptyError = group.value == null
                );
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val buttonModifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                    Button(
                        onClick = { navController.popBackStack() },
                        shape = RoundedCornerShape(7.dp),
                        contentPadding = PaddingValues(),
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(
                                                0xFF4568DC
                                            ), Color(0xFFB06AB3)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Cancel", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Button(
                        onClick = { onClick() },
                        shape = RoundedCornerShape(7.dp),
                        contentPadding = PaddingValues(),
                        modifier = buttonModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(
                                                0xFFDC456F
                                            ), Color(0xFFB06AB3)
                                        )
                                    )
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