package com.example.calendarapp.model

import com.google.gson.annotations.SerializedName

data class MonthsDataModelClass(
    var isSelected: Boolean = false,
    var monthName: String? = null,
    var monthId: Int = 0
)

data class MonthYearDataModel(
    var monthId: Int = 0,
    var year: Int = 0,
    var monthName: String? = null
)

data class UserTaskModel(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("task")
    val taskModel: TaskModel
)

data class TaskModel(
    val title: String,
    val description: String
)

data class UserTaskIdModel(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("task_id")
    val taskId: Int
)

data class UserId(
    @SerializedName("user_id")
    val userId: Int
)

data class TaskResponseModel(
    @SerializedName("tasks")
    val tasks: List<TaskIdDetailModel>
)

data class TaskIdDetailModel(
    @SerializedName("task_id")
    val taskId: Int,
    @SerializedName("task_detail")
    val taskModel: TaskModel
)

data class NetworkErrorModel(
    var errorMessage: String? = null,
    var errorId: Int? = null
)