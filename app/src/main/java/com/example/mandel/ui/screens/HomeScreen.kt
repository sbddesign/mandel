package com.example.mandel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mandel.ui.theme.*

@Composable
fun HomeScreen(
    balance: String = "0.00",
    onReceive: () -> Unit = {},
    onSend: () -> Unit = {}
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
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "₿ 0",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MandelWhite,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "$ $balance",
                    fontSize = 24.sp,
                    color = MandelWhite.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MandelWhite.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = onReceive,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MandelWhite,
                        containerColor = Color.Transparent
                    ),
                    border = null,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Receive",
                            tint = MandelWhite
                        )
                        Text(
                            text = "Receive",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(56.dp)
                        .background(MandelWhite.copy(alpha = 0.3f))
                )

                OutlinedButton(
                    onClick = onSend,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MandelWhite,
                        containerColor = Color.Transparent
                    ),
                    border = null,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Send",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowUp,
                            contentDescription = "Send",
                            tint = MandelWhite
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MandelTheme {
        HomeScreen()
    }
}