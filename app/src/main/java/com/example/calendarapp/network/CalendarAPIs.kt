package com.example.calendarapp.network

import com.example.calendarapp.model.TaskResponseModel
import com.example.calendarapp.model.UserId
import com.example.calendarapp.model.UserTaskIdModel
import com.example.calendarapp.model.UserTaskModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CalendarAPIs {
    @POST("/api/storeCalendarTask")
    suspend fun addNewCalendarTask(@Body userTaskModel: UserTaskModel): Response<Any>

    @POST("/api/deleteCalendarTask")
    suspend fun deleteCalendarTask(@Body userTaskIdModel: UserTaskIdModel): Response<Any>

    @POST("/api/getCalendarTaskList")
    suspend fun getCalendarTasksList(@Body userId: UserId): Response<TaskResponseModel>

}