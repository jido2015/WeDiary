package com.project.wediary.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.project.wediary.model.Diary
import com.project.wediary.presentation.components.DisplayAlertDialog
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary: Diary?,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack ,
                    contentDescription = "Back Arrow Icon")
            }
        },

        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Happy",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    ),
                    textAlign = TextAlign.Center
                )
            }
        },

        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.DateRange ,
                    contentDescription = "Date Icon")
            }
            if (selectedDiary !=null){
                DeleteDiaryAction(
                    selectedDiary = selectedDiary,
                    onDeleteConfirmed = onDeleteConfirmed
                )
            }
        }
    )
}

@Composable
fun DeleteDiaryAction(
    selectedDiary: Diary?,
    onDeleteConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Delete")
            }, onClick = {
                openDialog = true
                expanded = false
            }
        )
    }
    DisplayAlertDialog(
        title = "Delete",
        message = "Are you sure you want to permanently delete this diary note '${selectedDiary?.title}'?",
        dialogOpened = openDialog,
        onDialogClosed = { openDialog = false },
        onYesClicked = onDeleteConfirmed
    )
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Overflow Menu Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}