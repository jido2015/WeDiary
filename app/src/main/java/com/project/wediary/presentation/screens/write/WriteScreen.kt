package com.project.wediary.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.project.wediary.model.Diary
import com.project.wediary.model.Mood

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen (
    uiState: UiState,
    pagerState: PagerState,
    selectedDiary: Diary?,
    moodName: () -> String,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    //Update the mood when selecting an existing Diary
    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                onDeleteConfirmed = onDeleteConfirmed,
                selectedDiary =  selectedDiary,
                moodName = moodName,
                onBackPressed = onBackPressed)
        },
        content = {
            WriteContent(
                pagerState = pagerState,
                paddingValues = it,
                title = uiState.title,
                onTitleChanged = onTitleChanged,
                description = uiState.description,
                onDescriptionChanged =onDescriptionChanged
            )
        }
    )
}