package com.example.calendarapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.databinding.TaskItemBinding
import com.example.calendarapp.model.TaskIdDetailModel


class TasksAdapter() :
    androidx.recyclerview.widget.ListAdapter<TaskIdDetailModel, TasksAdapter.TasksViewHolder>(
        object : DiffUtil.ItemCallback<TaskIdDetailModel>() {
            override fun areItemsTheSame(
                oldItem: TaskIdDetailModel,
                newItem: TaskIdDetailModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: TaskIdDetailModel,
                newItem: TaskIdDetailModel
            ): Boolean {
                return oldItem.taskId == newItem.taskId && oldItem.taskModel.title == newItem.taskModel.title && oldItem.taskModel.description == newItem.taskModel.description
            }
        }
    ) {

    lateinit var listener: ((view: View, item: TaskIdDetailModel, position: Int) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = TaskItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    inner class TasksViewHolder(private val viewBinding: TaskItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: TaskIdDetailModel, position: Int) {
            bind(viewBinding, item, adapterPosition)
        }
    }

    fun bind(viewBinding: TaskItemBinding, item: TaskIdDetailModel, position: Int) {
        viewBinding.apply {
            taskText.text = item.taskModel.title

            deleteTask.setOnClickListener {
                listener.invoke(it, item, position)
            }

            taskItemContainer.setOnClickListener {
                listener.invoke(it, item, position)
            }

        }
    }
}