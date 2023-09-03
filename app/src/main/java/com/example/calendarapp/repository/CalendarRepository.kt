package com.example.calendarapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.calendarapp.model.NetworkErrorModel
import com.example.calendarapp.model.TaskResponseModel
import com.example.calendarapp.model.UserId
import com.example.calendarapp.model.UserTaskIdModel
import com.example.calendarapp.model.UserTaskModel
import com.example.calendarapp.network.CalendarAPIs
import javax.inject.Inject

class CalendarRepository @Inject constructor(private val calendarApi: CalendarAPIs) {

    private val _tasksListLiveData = MutableLiveData<TaskResponseModel>()
    val tasksListLiveData: LiveData<TaskResponseModel>
        get() = _tasksListLiveData

    private val _errorLiveData = MutableLiveData<NetworkErrorModel>()
    val errorLiveData: LiveData<NetworkErrorModel>
        get() = _errorLiveData

    suspend fun addNewCalendarTask(userTaskModel: UserTaskModel) {
        calendarApi.addNewCalendarTask(userTaskModel)
    }

    suspend fun getCalendarTasksList(userId: UserId) {
        try {
            val responseApi = calendarApi.getCalendarTasksList(userId)
            if (responseApi.isSuccessful && responseApi.body() != null) {
                _tasksListLiveData.postValue(responseApi.body())
            } else {
                _errorLiveData.postValue(
                    NetworkErrorModel(
                        errorMessage = responseApi.message(),
                        errorId = 2
                    )
                )
            }
        } catch (e: Exception) {
            _errorLiveData.postValue(NetworkErrorModel(errorMessage = e.message, errorId = 2))
        }
    }

    suspend fun deleteCalendarTask(userTaskIdModel: UserTaskIdModel) {
        calendarApi.deleteCalendarTask(userTaskIdModel)
    }
}