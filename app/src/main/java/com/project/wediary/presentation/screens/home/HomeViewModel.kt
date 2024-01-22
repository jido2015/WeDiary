package com.project.wediary.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.wediary.data.repository.Diaries
import com.project.wediary.data.repository.MongoDB
import com.project.wediary.util.RequestState
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }

    private fun observeAllDiaries(){

        viewModelScope.launch {
            MongoDB.getAllDiaries().collect { result ->

                Log.d("MongoResult", result.toString())
                diaries.value = result
            }
        }

    }
}