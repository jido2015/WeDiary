package com.project.wediary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.project.wediary.data.repository.MongoDB
import com.project.wediary.navigation.Screen
import com.project.wediary.navigation.SetupNavGraph
import com.project.wediary.ui.theme.WeDiaryTheme
import com.project.wediary.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    var keepSplashOpen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpen
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            WeDiaryTheme {
                val navController = rememberNavController()
                SetupNavGraph(startDestination = getStartDestination(),
                    navController = navController,
                    onDataLoaded ={ keepSplashOpen = false })
            }
        }
    }

    private fun getStartDestination(): String{
        val user = App.Companion.create(APP_ID).currentUser
        return if (user!=null && user.loggedIn) Screen.Home.route
        else Screen.Authentication.route
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeDiaryTheme {
        Greeting("Android")
    }
}