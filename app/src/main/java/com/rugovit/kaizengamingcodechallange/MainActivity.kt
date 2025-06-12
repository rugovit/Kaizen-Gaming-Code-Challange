package com.rugovit.kaizengamingcodechallange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rugovit.kaizengamingcodechallange.ui.navigation.AppNavigation
import com.rugovit.kaizengamingcodechallange.ui.theme.KaizenGamingCodeChallangeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KaizenGamingCodeChallangeTheme {
                AppNavigation()
            }
        }
    }
}
