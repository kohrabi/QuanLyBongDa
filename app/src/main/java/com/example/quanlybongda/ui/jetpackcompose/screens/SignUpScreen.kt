package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
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
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController : NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel(),
) {
    val context = LocalContext.current;
    var usernameError by remember { mutableStateOf(InputError()) }
    var emailError by remember { mutableStateOf(InputError()) }
    var passwordError by remember { mutableStateOf(InputError()) }
    val dataStore by remember { mutableStateOf(UserDataStore(context)) };

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val onSignUpClick : () -> Unit = {
        var isError = false;
        if (username.isEmpty()) {
            usernameError = InputError(true, "Username không thể trống");
            isError = true;
        }
        if (email.isEmpty()) {
            emailError = InputError(true, "Email không thể trống");
            isError = true;
        }
        if (password.isEmpty()) {
            passwordError = InputError(true, "Password không thể trống");
            isError = true;
        }

        if (!isError) {
            scope.launch {
                try {
                    val sessionToken = viewModel.createUser(email, username, password);
                    dataStore.saveSessionToken(sessionToken, context);
                    navController.navigate("muaGiai");
                }
                catch (e : RuntimeException) {
                    if (e is UsernameFormat)
                        usernameError = InputError(true, "Username phải có lớn hơn 8 kí tự và bé hơn 32 kí tự");
                    else if (e is EmailFormat)
                        emailError = InputError(true, "Email có format không đúng");
                    else if (e is EmailAvailability)
                        emailError = InputError(true, "Email đã tồn tại");
                }
            }
        }
    };

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
                    text = "Enter your email address, username and password to sign up",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))


                // Email
                InputTextField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it; emailError.isError = false; },
                    isError = emailError.isError,
                    errorMessage = emailError.errorMessage,
                    keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Username
                InputTextField(
                    label = "Username",
                    value = username,
                    onValueChange = { username = it; usernameError.isError = false; },
                    isError = usernameError.isError,
                    errorMessage = usernameError.errorMessage,
                )
                Spacer(modifier = Modifier.height(16.dp))

                var passwordVisible by remember { mutableStateOf(false) }
                InputTextField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it; passwordError.isError = false; },
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
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Forget Password?",
                        color = Color(0xFFB06AB3),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {}
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onSignUpClick,
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
                        Text("SIGN UP", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 46.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Have an account? ",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Login Now",
                    color = Color(0xFFB06AB3),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SignUpPreview() { // << ĐỔI TÊN HÀM PREVIEW
    SignUpScreen(rememberNavController(), Modifier) // Gọi Composable chính
}
