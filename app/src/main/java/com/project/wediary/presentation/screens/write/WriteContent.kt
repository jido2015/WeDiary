package com.project.wediary.presentation.screens.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WriteContent(paddingValues: PaddingValues){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding( top = paddingValues.calculateTopPadding())
            .padding(bottom = paddingValues.calculateBottomPadding())
            .padding(horizontal = 24.dp)
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween) {
    }
}