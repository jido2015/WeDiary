package com.project.wediary.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.navigation
import com.project.wediary.data.repository.MongoDB
import com.project.wediary.model.Diary
import com.project.wediary.presentation.components.DisplayAlertDialog
import com.project.wediary.presentation.screens.auth.AuthenticationScreen
import com.project.wediary.presentation.screens.auth.AuthenticationViewModel
import com.project.wediary.presentation.screens.home.HomeScreen
import com.project.wediary.presentation.screens.home.HomeViewModel
import com.project.wediary.presentation.screens.write.WriteScreen
import com.project.wediary.util.Constants.APP_ID
import com.project.wediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.project.wediary.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(startDestination: String,
                  navController: NavHostController,
                  onDataLoaded: () -> Unit){
    NavHost(startDestination = startDestination,
        navController = navController )
    {
        authenticationRoute(
            navigateToHome ={
            navController.popBackStack()
            navController.navigate(Screen.Home.route) },
            onDataLoaded = onDataLoaded)
        homeRoute(
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateToAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route) },
            onDataLoaded = onDataLoaded)
        writeRoute(onBackPressed = {
            navController.popBackStack()
        })
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
){
    composable(route = Screen.Authentication.route) {
        // Handle Authentication screen composable
        val viewModel: AuthenticationViewModel = viewModel()
        val authenticated = viewModel.authenticated
        val loadingState by viewModel.loadingState

        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }
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
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit
){
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val diaries by viewModel.diaries
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        
        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading){
                onDataLoaded()
            }
        }
        // Handle Home screen composable
        HomeScreen(
            diaries = diaries,
            onMenuClicked ={
            scope.launch { drawerState.open() } },
            onNavigateToWrite = navigateToWrite,
            drawerState= drawerState,
            onSignOutClicked = {signOutDialogOpened = true}
        )
        LaunchedEffect(key1 = Unit) {
            MongoDB.configureRealm()
        }
        
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
fun NavGraphBuilder.writeRoute(onBackPressed: ()-> Unit){
    composable(route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY){
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
        ) {
        // Handle Write screen composable
        WriteScreen(
            selectedDiary = null,
            onDeleteConfirmed = {},
            onBackPressed = onBackPressed)
    }
}
