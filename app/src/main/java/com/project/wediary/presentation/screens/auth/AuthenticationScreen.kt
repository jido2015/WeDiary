package com.project.wediary.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.project.wediary.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import java.lang.Exception

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    onTapState: OneTapSignInState,
    messageBarState: MessageBarState,
    onButtonClicked: () -> Unit
){
    Scaffold(
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    loadingState = loadingState, onButtonClicked = onButtonClicked)
            }
        }
    )
    
    OneTapSignInWithGoogle(state = onTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived =  {
            tokenId -> messageBarState.addSuccess("Successfully Authenticated") },
        onDialogDismissed = {
            message -> messageBarState.addError(Exception(message))
        }
    )
}