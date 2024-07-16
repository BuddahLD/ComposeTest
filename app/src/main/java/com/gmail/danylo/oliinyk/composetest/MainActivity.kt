package com.gmail.danylo.oliinyk.composetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gmail.danylo.oliinyk.composetest.common.Colors.LightBlue
import com.gmail.danylo.oliinyk.composetest.common.Colors.LighterGreen
import com.gmail.danylo.oliinyk.composetest.common.Fonts.PT_SANS
import com.gmail.danylo.oliinyk.composetest.ui.BlueButton
import com.gmail.danylo.oliinyk.composetest.ui.GreenButton

private const val GREEN_BUTTON_ROUTE = "green_button"
private const val BLUE_BUTTON_ROUTE = "blue_button"
private const val MAIN_SCREEN_ROUTE = "main_screen"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen()
        }
    }

    @Composable
    private fun AppScreen() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = MAIN_SCREEN_ROUTE
        ) {
            composable(route = MAIN_SCREEN_ROUTE) {
                MainScreen(
                    onGreenButtonClick = { navController.navigate(GREEN_BUTTON_ROUTE) },
                    onBlueButtonClick = { navController.navigate(BLUE_BUTTON_ROUTE) }
                )
            }

            composable(route = GREEN_BUTTON_ROUTE) {
                GreenButton()
            }

            composable(route = BLUE_BUTTON_ROUTE) {
                BlueButton()
            }
        }
    }

    @Composable
    private fun MainScreen(
        onGreenButtonClick: () -> Unit,
        onBlueButtonClick: () -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(start = 48.dp, end = 48.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = PT_SANS,
                text = "Оберіть, будь ласка, яку анімацію ви хочете подивитися"
            )
            Spacer(modifier = Modifier.height(64.dp))
            Button(
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LighterGreen),
                onClick = { onGreenButtonClick() }
            ) {
                Text(text = "Зелена кнопка")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                onClick = { onBlueButtonClick() }
            ) {
                Text(text = "Синя кнопка")
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun MainScreenPreview() {
        MainScreen(
            onGreenButtonClick = {},
            onBlueButtonClick = {},
        )
    }
}
