package com.example.quanlybongda.ui.jetpackcompose.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController : NavController,
    modifier: Modifier = Modifier,
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope();

    val onSignInClick : () -> Unit = {
        scope.launch {
            // TODO: Sign in
            navController.navigate("hoSo");
        }
    }

    Box(
        modifier = modifier
            .padding()
            .fillMaxSize()
    ) {
        // Background Image with overlay
        Image(
            painter = rememberAsyncImagePainter("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/a7195ab3-99b7-49a3-923e-03620d8eb0f5"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize()
        )
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xCC000000)) // dark overlay
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = modifier.height(40.dp))

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
                Spacer(modifier = modifier.height(12.dp))
                Text(
                    text = "Enter your email address and password to use the application",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Spacer(modifier = modifier.height(24.dp))

                // Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color(0xFF222232), shape = RoundedCornerShape(6.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        textColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray
                    )
                )

                Spacer(modifier = modifier.height(16.dp))

                var passwordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Default.Visibility
                        else Icons.Default.VisibilityOff

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = Color.LightGray)
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color(0xFF222232), shape = RoundedCornerShape(6.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        textColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray
                    )
                )

                Spacer(modifier = modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = true, onCheckedChange = {}, colors = CheckboxDefaults.colors(Color.White))
                        Text("Remember Me", color = Color.White, fontSize = 14.sp)
                    }
                    Text(
                        text = "Forget Password?",
                        color = Color(0xFFB06AB3),
                        fontSize = 14.sp,
                        modifier = modifier.clickable {}
                    )
                }

                Spacer(modifier = modifier.height(24.dp))

                Button(
                    onClick = onSignInClick,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = modifier
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
                modifier = modifier.fillMaxWidth().padding(vertical = 42.dp),
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
                    modifier = modifier.clickable {
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
    LoginScreen(rememberNavController(), Modifier) // Gọi Composable chính
}
