package com.example.calendarapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarapp.R
import com.example.calendarapp.databinding.MonthViewBinding
import com.example.calendarapp.model.MonthsDataModelClass

class MonthsAdapter() :
    androidx.recyclerview.widget.ListAdapter<MonthsDataModelClass, MonthsAdapter.MonthsViewHolder>(
        object : DiffUtil.ItemCallback<MonthsDataModelClass>() {
            override fun areItemsTheSame(
                oldItem: MonthsDataModelClass,
                newItem: MonthsDataModelClass
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MonthsDataModelClass,
                newItem: MonthsDataModelClass
            ): Boolean {
                return oldItem.monthName == newItem.monthName && oldItem.monthId == newItem.monthId
            }
        }
    ) {

    lateinit var listener: ((view: View, item: MonthsDataModelClass, position: Int) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthsViewHolder {
        val binding = MonthViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MonthsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthsViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    inner class MonthsViewHolder(private val viewBinding: MonthViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: MonthsDataModelClass, position: Int) {
            bind(viewBinding, item, position)
        }
    }

    fun bind(viewBinding: MonthViewBinding, item: MonthsDataModelClass, position: Int) {
        viewBinding.apply {
            monthName.text = item.monthName

            setCardBackgroundColor(item.isSelected, viewBinding)

            root.setOnClickListener {
                if (item.isSelected.not()) {
                    item.apply {
                        isSelected = true
                    }
                }

                setCardBackgroundColor(item.isSelected, viewBinding)

                listener.invoke(it, item, position)
            }
        }
    }

    private fun setCardBackgroundColor(isSelected: Boolean, viewBinding: MonthViewBinding) {
        viewBinding.apply {
            if (isSelected) {
                monthCardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context, R.color.primary_green
                    )
                )

                monthName.setTextColor(
                    ContextCompat.getColor(
                        root.context, R.color.white
                    )
                )
            } else {
                monthCardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context, R.color.white
                    )
                )

                monthName.setTextColor(
                    ContextCompat.getColor(
                        root.context, R.color.primary_green
                    )
                )
            }
        }
    }
}