package com.project.wediary.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.project.wediary.model.Diary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen (
    selectedDiary: Diary?,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            WriteTopBar(
                onDeleteConfirmed = onDeleteConfirmed,
                selectedDiary =  selectedDiary,
                onBackPressed = onBackPressed)
        },
        content = {}
    )
}