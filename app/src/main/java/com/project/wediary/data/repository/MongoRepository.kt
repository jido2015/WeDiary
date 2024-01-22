package com.project.wediary.data.repository

import com.project.wediary.model.Diary
import com.project.wediary.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
interface MongoRepository {
    fun configureRealm()
    fun getAllDiaries(): Flow<Diaries>
}