@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class)

package com.walletconnect.lab.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.walletconnect.lab.compose.ui.theme.WalletConnectTheme
import com.walletconnect.web3.modal.domain.configuration.Config
import com.walletconnect.web3.modal.ui.navigateToWeb3Modal
import com.walletconnect.web3.modal.ui.web3ModalGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val modalSheetState = rememberModalBottomSheetState(
                        initialValue = ModalBottomSheetValue.Hidden,
                        skipHalfExpanded = false
                    )
                    val bottomSheetNavigator = BottomSheetNavigator(modalSheetState)
                    val navController = rememberNavController(bottomSheetNavigator)

                    ModalBottomSheetLayout(
                        bottomSheetNavigator = bottomSheetNavigator,
                        sheetBackgroundColor = Color.Transparent,
                        sheetElevation = 0.dp,
                        scrimColor = Color.Unspecified
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "first_page"
                        ) {
                            composable("first_page") {
                                FirstPage(navController)
                            }
                            web3ModalGraph(modalSheetState)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FirstPage(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { navController.navigateToWeb3Modal(Config.Connect(uri = "asdjkljklasdjklsdajlasdjl123jjkl jklasjlajsdfjklasdjklfjkla21jkl asjkljklasjkldajksljkldasjkldjklasjkl dasjkl12jkl321jkl jklasdjkl jklsdajklasdjkldjklajkasjkl jklasjklasdjlasdjlkasdjklasdjkl wc:::/// asdjklasjkldasjkldjklasjkldasldjklasjkldjklasjkldaskljklasl")) }
        ) {
            Text(text = "Connect")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WalletConnectTheme {
    }
}