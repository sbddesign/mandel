package com.example.mandel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mandel.ui.theme.*

@Composable
fun BlockedScreen(
    onBack: () -> Unit = {}
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "We're sorry",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MandelWhite,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "It looks like we're not able to serve you at this time. Try again when a new regime takes over.",
                    fontSize = 18.sp,
                    color = MandelWhite,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Mistyped your email? Try again.",
                    fontSize = 16.sp,
                    color = MandelWhite.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MandelOrange
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "← Back",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MandelWhite
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlockedScreenPreview() {
    MandelTheme {
        BlockedScreen()
    }
}