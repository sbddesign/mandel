package com.example.mandel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mandel.ui.theme.*

@Composable
fun WelcomeScreen(
    bitcoinAddress: String = "₿dizzy.giraffe@twelve.cash",
    onContinue: () -> Unit = {}
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Welcome!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MandelWhite,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Your bitcoin address is",
                    fontSize = 18.sp,
                    color = MandelWhite,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MandelOrange)) {
                        append(bitcoinAddress)
                    }
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "Share it with the\nworld to get paid.",
                fontSize = 18.sp,
                color = MandelWhite,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MandelOrange
                ),
                shape = RoundedCornerShape(8.dp)
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

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    MandelTheme {
        WelcomeScreen()
    }
}