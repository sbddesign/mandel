package com.example.mandel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mandel.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailSignUpScreen(
    onContinue: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MandelBlack)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Time to sign up",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MandelWhite
                )

                Text(
                    text = "First thing's first: we need\nyour email address.",
                    fontSize = 18.sp,
                    color = MandelWhite,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Your Email Address",
                        fontSize = 16.sp,
                        color = MandelWhite,
                        fontWeight = FontWeight.Medium
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                text = "name@email.com",
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MandelWhite,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = MandelWhite,
                            unfocusedTextColor = MandelWhite,
                            cursorColor = MandelWhite,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        singleLine = true
                    )
                }

                Button(
                    onClick = { onContinue(email) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MandelOrange,
                        disabledContainerColor = MandelOrange.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                ) {
                    Text(
                        text = "Continue →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MandelWhite
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailSignUpScreenPreview() {
    MandelTheme {
        EmailSignUpScreen()
    }
}