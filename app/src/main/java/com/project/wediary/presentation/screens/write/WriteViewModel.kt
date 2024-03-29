package com.project.wediary.presentation.screens.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.wediary.data.repository.MongoDB
import com.project.wediary.model.Diary
import com.project.wediary.model.Mood
import com.project.wediary.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.project.wediary.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import org.mongodb.kbson.ObjectId

class WriteViewModel(private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set
    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }
    private fun getDiaryIdArgument(){
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = WRITE_SCREEN_ARGUMENT_KEY)
        )
    }

    private fun fetchSelectedDiary(){
        if (uiState.selectedDiaryId!= null){
            viewModelScope.launch(Dispatchers.Main) {
                MongoDB.getSelectedDiary(diaryId = ObjectId.invoke(uiState.selectedDiaryId!!))
                    .collect{diary ->
                        if (diary is RequestState.Success){
                            setSelectedDiary(diary = diary.data)
                            setTitle(diary.data.title)
                            setDescription(diary.data.description)
                            setMood(mood = Mood.valueOf(diary.data.mood))
                        }
                    }

            }
        }
    }

    private fun setSelectedDiary(diary: Diary){
        uiState = uiState.copy(selectedDiary = diary)
    }

    fun setTitle(title: String){
        uiState = uiState.copy(title = title)
    }
    fun setDescription(description: String){
        uiState = uiState.copy(description = description)
    }

    private fun setMood(mood: Mood){
        uiState = uiState.copy(mood = mood)
    }

    fun upsertDiary(diary: Diary, onSuccess: () -> Unit, onError: (String) -> Unit){

        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedDiary !=null){
                updateDiary(diary = diary, onSuccess = onSuccess, onError = onError)
            }else{
                insertDiary(diary = diary, onSuccess = onSuccess, onError = onError)
            }
        }
    }
    private suspend fun insertDiary(diary: Diary,
                                    onSuccess:()-> Unit,
                                    onError:(String)-> Unit){
           val result = MongoDB.insertDiary(diary = diary)
           if (result is RequestState.Success){
               withContext(Dispatchers.Main){
                   onSuccess()
               }
           }else if (result is RequestState.Error){
               withContext(Dispatchers.Main){
                   onError(result.error.message.toString())
               }
           }

    }
    private suspend fun updateDiary(diary: Diary, onSuccess: () -> Unit, onError: (String) -> Unit){
       val result = MongoDB.updateDiary(diary = diary.apply {
           _id = ObjectId.invoke(uiState.selectedDiaryId!!)
           date = uiState.selectedDiary!!.date})
        if (result is RequestState.Success){
            withContext(Dispatchers.Main){
                onSuccess()
            }
        }else if (result is RequestState.Error){
            withContext(Dispatchers.Main){
                onError(result.error.message.toString())
            }
        }
    }
}

data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral
)