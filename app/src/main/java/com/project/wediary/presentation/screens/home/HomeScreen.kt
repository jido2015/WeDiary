package com.project.wediary.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.wediary.R
import com.project.wediary.data.repository.Diaries
import com.project.wediary.util.RequestState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    onNavigateToWrite: () -> Unit
) {
    NavigationDrawer(drawerState = drawerState, onSignOutClicked = onSignOutClicked) {
        Scaffold(
            topBar = {
                HomeTopBar(onMenuClicked = onMenuClicked)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { onNavigateToWrite() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "New Diary Icon"
                    )
                }
            },
            content = {
                when (diaries){
                    is RequestState.Success ->{
                        HomeContent(paddingValues = it, diaryNotes = diaries.data, onclick = {})
                    }
                    is RequestState.Error ->{
                        EmptyPage(
                            title = "Error",
                            subtitle = "${diaries.error.message}"
                        )
                    }
                    is RequestState.Loading ->{
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center){
                            CircularProgressIndicator()
                        }
                    }
                    else -> {}
                }
            }
        )
    }

}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                content = {
                    Image(
                        modifier = Modifier.size(250.dp),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Image"
                    )

                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.google_logo),
                                    contentDescription = "Google Logo"
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Sign Out")
                            }
                        },
                        selected = false,
                        onClick = { onSignOutClicked() })
                }
            )
        },
        content = content
    )
}