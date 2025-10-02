package com.example.mandel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mandel.ui.theme.*

@Composable
fun WalletStartScreen(
    onCreateWallet: () -> Unit = {},
    onRestoreWallet: () -> Unit = {}
) {
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
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(MandelPurple, MandelPurple.copy(alpha = 0.6f))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Text(
                        text = "🥜",
                        fontSize = 40.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Text(
                    text = "MANDEL",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MandelWhite,
                    letterSpacing = 2.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Your cash.",
                    fontSize = 24.sp,
                    color = MandelWhite,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Super fast.",
                    fontSize = 24.sp,
                    color = MandelWhite,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Super private.",
                    fontSize = 24.sp,
                    color = MandelWhite,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onCreateWallet,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MandelOrange
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Create new wallet →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MandelWhite
                    )
                }

                OutlinedButton(
                    onClick = onRestoreWallet,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MandelWhite
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(MandelWhite, MandelWhite))
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Restore wallet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WalletStartScreenPreview() {
    MandelTheme {
        WalletStartScreen()
    }
}