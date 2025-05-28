package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybongda.Database.DatabaseViewModel
import com.example.quanlybongda.Database.Exceptions.*
import com.example.quanlybongda.Database.UserDataStore
import com.example.quanlybongda.navigatePopUpTo
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController : NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    val context = LocalContext.current;
    var userError by remember { mutableStateOf(InputError()) }
    var passwordError by remember { mutableStateOf(InputError()) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope();
    val dataStore by remember { mutableStateOf(UserDataStore(context)) };

    val onLogInClick : () -> Unit = {
        var isError = false;
        if (username.isEmpty()) {
            userError = InputError(true, "Username trống");
            isError = true;
        }
        if (password.isEmpty()) {
            passwordError = InputError(true, "Password trống");
            isError = true;
        }
        if (!isError) {
            scope.launch {
                try {
                    val sessionToken = viewModel.loginIn(username, password);
                    dataStore.saveSessionToken(sessionToken, context);

                    navigatePopUpTo(navController, "muaGiai");
                } catch (e: RuntimeException) {
                    if (e is IncorrectUsername)
                        userError = InputError(true, "Không tồn tại user");
                    else if (e is IncorrectPassword)
                        passwordError = InputError(true, "Không tồn tại password");
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        dataStore.loggedInFlow.collect { sessionToken ->
            val result = viewModel.validateSessionToken(sessionToken);
            if (result.session != null)
                navController.navigate("muaGiai");
        }
    }

    LaunchedEffect(username, password) {
        userError = InputError();
        passwordError = InputError();
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background Image with overlay
        Image(
            painter = rememberAsyncImagePainter("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/a7195ab3-99b7-49a3-923e-03620d8eb0f5"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xCC000000)) // dark overlay
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column {
                Text(
                    text = "Welcome to",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "LiveScore.",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Enter your email address and password to use the application",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Username
                InputTextField(
                    label = "Username",
                    value = username,
                    onValueChange = { username = it },
                    isError = userError.isError,
                    errorMessage = userError.errorMessage,
                )

                Spacer(modifier = Modifier.height(16.dp))

                var passwordVisible by remember { mutableStateOf(false) }
                InputTextField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    isError = passwordError.isError,
                    errorMessage = passwordError.errorMessage,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Default.Visibility
                        else Icons.Default.VisibilityOff

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = Color.LightGray)
                        }
                    },
                    keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = true, onCheckedChange = {}, colors = CheckboxDefaults.colors(Color.White))
                        Text("Remember Me", color = Color.White, fontSize = 14.sp)
                    }
                    Text(
                        text = "Forget Password?",
                        color = Color(0xFFB06AB3),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {}
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onLogInClick,
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
                        Text("SIGN IN", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 42.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Register Now",
                    color = Color(0xFFB06AB3),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("signUp");
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun LoginScreenPreview() { // << ĐỔI TÊN HÀM PREVIEW
    LoginScreen(rememberNavController()) // Gọi Composable chính
}
