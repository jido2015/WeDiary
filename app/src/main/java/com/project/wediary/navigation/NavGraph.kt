package com.project.wediary.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.wediary.presentation.components.DisplayAlertDialog
import com.project.wediary.presentation.screens.auth.AuthenticationScreen
import com.project.wediary.presentation.screens.auth.AuthenticationViewModel
import com.project.wediary.presentation.screens.home.HomeScreen
import com.project.wediary.util.Constants.APP_ID
import com.project.wediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController){
    NavHost(startDestination = startDestination,
        navController = navController )
    {
        authenticationRoute(navigateToHome ={
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        })
        homeRoute(
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)

            })
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit
){
    composable(route = Screen.Authentication.route) {
        // Handle Authentication screen composable
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated = viewModel.authenticated
        val loadingState by viewModel.loadingState

        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(
            authenticated = authenticated.value,
            loadingState = loadingState,
            onTapState = oneTapState,
            messageBarState = messageBarState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = {tokenId ->
                viewModel.signInWithMongoAtlas(tokenId = tokenId,
                    onSuccess = {
                        messageBarState.addSuccess("Successfully Authenticated")
                        viewModel.setLoading(false)
                    },
                    onError = {
                        viewModel.setLoading(false)
                        messageBarState.addError(it)
                    }
                )
            },
            onDialogDismissed = {message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}
fun NavGraphBuilder.homeRoute(
    navigateToWrite: () -> Unit,
    navigateToAuth: () -> Unit
){
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()
        // Handle Home screen composable
        HomeScreen(onMenuClicked ={ 
            scope.launch { drawerState.open() } },
            onNavigateToWrite = navigateToWrite,
            drawerState= drawerState,
            onSignOutClicked = {signOutDialogOpened = true}
        )
        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out from your google account?",
            dialogOpened = signOutDialogOpened,
            onDialogClosed = {signOutDialogOpened = false },
            onYesClicked = { scope.launch ( Dispatchers.IO ){
                val user = App.create(APP_ID).currentUser
                if (user !=null){
                    user.logOut()
                    withContext(Dispatchers.Main){
                        navigateToAuth()
                    }
                }
            } })
    }

}
fun NavGraphBuilder.writeRoute(){
    composable(route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY){
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
        ) {
        // Handle Write screen composable
    }
}
