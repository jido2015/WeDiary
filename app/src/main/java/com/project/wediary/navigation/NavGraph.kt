package com.project.wediary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.wediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

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