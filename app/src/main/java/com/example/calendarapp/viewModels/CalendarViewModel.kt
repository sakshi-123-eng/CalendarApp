package com.example.calendarapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.model.MonthYearDataModel
import com.example.calendarapp.model.MonthsDataModelClass
import com.example.calendarapp.model.NetworkErrorModel
import com.example.calendarapp.model.TaskResponseModel
import com.example.calendarapp.model.UserId
import com.example.calendarapp.model.UserTaskIdModel
import com.example.calendarapp.model.UserTaskModel
import com.example.calendarapp.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

const val USER_ID = 650248

@HiltViewModel
class CalendarViewModel @Inject constructor(private val repository: CalendarRepository) :
    ViewModel() {

    // shared live data
    var monthYearSharedLiveData = MutableLiveData<MonthYearDataModel>()

    var tasksListLiveData: LiveData<TaskResponseModel>? = null

    var errorLiveData: LiveData<NetworkErrorModel>? = null

    var selectedMonthId: Int = 0
    var previousSelectedMonthId: Int = 0
    var selectedYear: Int = 0
    var selectedMonthName: String? = null
    var previousSelectedPosition: Int = -1

    private var monthsList = arrayListOf(
        MonthsDataModelClass(
            monthName = "Jan", monthId = 1
        ),
        MonthsDataModelClass(monthName = "Feb", monthId = 2),
        MonthsDataModelClass(monthName = "Mar", monthId = 3),
        MonthsDataModelClass(monthName = "Apr", monthId = 4),
        MonthsDataModelClass(monthName = "May", monthId = 5),
        MonthsDataModelClass(monthName = "Jun", monthId = 6),
        MonthsDataModelClass(monthName = "Jul", monthId = 7),
        MonthsDataModelClass(monthName = "Aug", monthId = 8),
        MonthsDataModelClass(monthName = "Sep", monthId = 9),
        MonthsDataModelClass(monthName = "Oct", monthId = 10),
        MonthsDataModelClass(monthName = "Nov", monthId = 11),
        MonthsDataModelClass(monthName = "Dec", monthId = 12)
    )

    fun getMonthsList(): List<MonthsDataModelClass> {
        return monthsList.mapIndexed { index, it ->
            if (it.monthId == selectedMonthId) previousSelectedPosition = index
            MonthsDataModelClass(
                monthId = it.monthId,
                monthName = it.monthName,
                isSelected = it.monthId == selectedMonthId
            )
        }
    }


    init {
        getCalendarTasksList(UserId(USER_ID))
        tasksListLiveData = repository.tasksListLiveData
        errorLiveData = repository.errorLiveData
    }

    fun addNewCalendarTask(userTaskModel: UserTaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addNewCalendarTask(userTaskModel.copy(userId = USER_ID))
                getCalendarTasksList(UserId(USER_ID))
            } catch (e: Exception) {
                throw IOException(e)
            }
        }
    }

    fun deleteCalendarTask(userTaskIdModel: UserTaskIdModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteCalendarTask(userTaskIdModel)
                getCalendarTasksList(UserId(USER_ID))
            } catch (e: Exception) {
                throw IOException(e)
            }
        }
    }

    fun getCalendarTasksList(userId: UserId) {
        viewModelScope.launch {
            repository.getCalendarTasksList(userId)
        }
    }


    fun updateMonthYearLiveData(value: MonthYearDataModel) {
        monthYearSharedLiveData.postValue(value)
    }
}