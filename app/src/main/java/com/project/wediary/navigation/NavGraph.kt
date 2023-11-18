package com.project.wediary.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.wediary.presentation.screens.auth.AuthenticationScreen
import com.project.wediary.presentation.screens.auth.AuthenticationViewModel
import com.project.wediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController){
    NavHost(startDestination = startDestination,
        navController = navController )
    {
        authenticationRoute()
        homeRoute()
        write()
    }
}

fun NavGraphBuilder.authenticationRoute(){
    composable(route = Screen.Authentication.route) {
        // Handle Authentication screen composable
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState

        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        AuthenticationScreen(
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
                        if (it){

                            Log.d("LogTokenId", tokenId)
                            viewModel.setLoading(false)
                            messageBarState.addSuccess("Successfully Authenticated")
                        }
                    },
                    onError = {
                        viewModel.setLoading(false)
                        messageBarState.addError(it)
                    }
                )
            },
            onDialogDismissed = {message ->
                messageBarState.addError(Exception(message))
            }
        )
    }
}
fun NavGraphBuilder.homeRoute(){
    composable(route = Screen.Home.route) {
        // Handle Home screen composable
    }
}
fun NavGraphBuilder.write(){
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