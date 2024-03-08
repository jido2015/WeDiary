package com.project.wediary.data.repository

import android.util.Log
import com.project.wediary.model.Diary
import com.project.wediary.util.Constants.APP_ID
import com.project.wediary.util.RequestState
import com.project.wediary.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.ZoneId

object MongoDB : MongoRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureRealm()
    }

    override fun configureRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Diary>(query = "ownerId == $0", user.id),
                        name = "User's Diaries"
                    )
                }.log(LogLevel.ALL).build()
            realm = Realm.open(config)
        } else {
            throw UserNotAuthenticatedException()
        }
    }

    override fun getAllDiaries(): Flow<Diaries> {
        return if (user != null) {
            try {
                realm.query<Diary>(query = "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->

                        Log.d("LogResultedData", result.list.toString())
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedDiary(diaryId: ObjectId): RequestState<Diary> {
        return if (user != null) {
            try {
                val diary = realm.query<Diary>(query = "_id == $0", diaryId).find().first()
                RequestState.Success(data = diary)
            } catch (x: Exception) {
                RequestState.Error(x)
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun addNewDiary(diary: Diary): RequestState<Diary> {
        return if (user != null) {
            realm.write {
                try {
                    val addedDiary = copyToRealm(diary.apply { ownerId = user.identity })
                    RequestState.Success(data = addedDiary)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }

        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }
}


private class UserNotAuthenticatedException : Exception("User is not Logged in.")