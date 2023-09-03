package com.example.calendarapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.calendarapp.R
import com.example.calendarapp.adapters.TasksAdapter
import com.example.calendarapp.databinding.DayItemLayoutBinding
import com.example.calendarapp.databinding.FragmentCalendarViewBinding
import com.example.calendarapp.model.UserTaskIdModel
import com.example.calendarapp.utils.UtilityObj.getDaysInMonth
import com.example.calendarapp.utils.UtilityObj.getFirstDayOfWeek
import com.example.calendarapp.utils.UtilityObj.getMonthNameFromMonthNo
import com.example.calendarapp.viewModels.CalendarViewModel
import com.example.calendarapp.viewModels.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class CalendarViewFragment : Fragment() {

    private var binding: FragmentCalendarViewBinding? = null

    private var previousSelectedItem: DayItemLayoutBinding? = null


    private var tasksAdapter: TasksAdapter? = null

    private val calendarViewModel by activityViewModels<CalendarViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentCalendarViewBinding.inflate(LayoutInflater.from(context), container, false)
        return binding?.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLiveDataObserver()
        setCurrentDateCalendarView()
        this@CalendarViewFragment.tasksAdapter = TasksAdapter().apply {
            submitList(
                emptyList()
            )
        }
        binding?.tasksListAdapter = tasksAdapter

        setClickListeners()
    }

    private fun setLiveDataObserver() {
        calendarViewModel.monthYearSharedLiveData.observe(viewLifecycleOwner) {
            binding?.apply {
                date.text = "${it.monthName}, ${it.year}"

                calendarViewModel.selectedYear = it.year
                calendarViewModel.selectedMonthId = it.monthId
                calendarViewModel.selectedMonthName = it.monthName
            }
            setDaysInGridLayout(
                getFirstDayOfWeek(it.year, it.monthId),
                getDaysInMonth(it.monthId, it.year),
                null
            )
        }

        calendarViewModel.tasksListLiveData?.observe(viewLifecycleOwner) {
            tasksAdapter?.apply {
                submitList(null)
                submitList(it.tasks)
                notifyDataSetChanged()
            }
        }
    }


    private fun setDaysInGridLayout(firstDayOfWeek: Int, totalDaysInMonth: Int, selectedDay: Int?) {
        previousSelectedItem = null
        binding?.apply {
            daysView.removeAllViews()
            val dayNameData = arrayOf(
                "Sun",
                "Mon",
                "Tue",
                "Wed",
                "Thr",
                "Fri",
                "Sat"
            )

            for (item in dayNameData) {

                val daysBinding =
                    (DayItemLayoutBinding.inflate(
                        LayoutInflater.from(daysView.context)
                    ))

                daysBinding.apply {
                    dayName.text = item

                    val layoutParams = GridLayout.LayoutParams()
                    layoutParams.setMargins(16, 16, 16, 16)

                    root.layoutParams = layoutParams
                    root.isClickable = false
                    root.isFocusable = false
                }
                daysView.addView((daysBinding.root))
            }


            for (day in 0 until firstDayOfWeek) {
                val daysBinding =
                    (DayItemLayoutBinding.inflate(
                        LayoutInflater.from(daysView.context)
                    ))

                daysBinding.apply {
                    root.visibility = View.GONE

                    val layoutParams = GridLayout.LayoutParams()

                    layoutParams.columnSpec =
                        GridLayout.spec(GridLayout.UNDEFINED, 1f)


                    root.layoutParams = layoutParams
                    root.isClickable = false
                    root.isFocusable = false
                }
                daysView.addView((daysBinding.root))
            }

            for (day in firstDayOfWeek until (totalDaysInMonth + firstDayOfWeek)) {
                val daysBinding =
                    (DayItemLayoutBinding.inflate(
                        LayoutInflater.from(daysView.context)
                    ))

                daysBinding.apply {
                    dayName.text = ((day - firstDayOfWeek) + 1).toString()

                    val layoutParams = GridLayout.LayoutParams()
                    layoutParams.setMargins(16, 16, 16, 16)

                    root.layoutParams = layoutParams


                    root.apply {
                        setOnClickListener {

                            if (previousSelectedItem != null && previousSelectedItem != daysBinding) {
                                previousSelectedItem?.apply {
                                    root.apply {
                                        context?.let {
                                            setBackgroundResource(R.drawable.day_background_unselected)
                                        }
                                    }

                                    dayName.setTextColor(
                                        ContextCompat.getColor(
                                            dayName.context,
                                            R.color.primary_green
                                        )
                                    )
                                }
                            }

                            setBackgroundResource(R.drawable.day_background_selected)

                            dayName.setTextColor(
                                ContextCompat.getColor(
                                    dayName.context,
                                    R.color.white
                                )
                            )

                            previousSelectedItem = daysBinding
                        }
                    }


                    selectedDay?.let {
                        if (it == ((day - firstDayOfWeek) + 1)) {
                            root.setBackgroundResource(R.drawable.day_background_selected)


                            dayName.setTextColor(
                                ContextCompat.getColor(
                                    dayName.context,
                                    R.color.white
                                )
                            )

                            previousSelectedItem = daysBinding
                        }
                    }
                }
                daysView.addView((daysBinding.root))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentDateCalendarView() {
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.monthValue
        val day = currentDate.dayOfMonth

        binding?.date?.text = "${getMonthNameFromMonthNo(monthValue = month)}, ${year}"
        calendarViewModel.selectedYear = year
        calendarViewModel.selectedMonthId = month
        calendarViewModel.selectedMonthName = getMonthNameFromMonthNo(monthValue = month)

        setDaysInGridLayout(
            getFirstDayOfWeek(year, month),
            getDaysInMonth(month, year),
            day
        )
    }


    private fun setClickListeners() {
        binding?.apply {
            dateView.setOnClickListener {
                findNavController().navigate(R.id.monthsPickerDialog)
            }
            tasksAdapter?.listener = { view, item, position ->
                when (view.id) {
                    R.id.deleteTask -> {
                        calendarViewModel.deleteCalendarTask(
                            UserTaskIdModel(
                                userId = USER_ID,
                                taskId = item.taskId
                            )
                        )
                    }

                    R.id.taskItemContainer -> {
                        (view.findViewById<TextView>(R.id.taskDes)).apply {
                            visibility = if (visibility == View.VISIBLE) {
                                View.GONE
                            } else {
                                View.VISIBLE
                            }

                            if (this.isVisible) {
                                this.text = item.taskModel.description
                            }
                        }
                    }
                }
            }

            addTaskBtn.setOnClickListener {
                if (previousSelectedItem != null) {
                    findNavController().navigate(R.id.createNewTaskFragment)
                } else {
                    Toast.makeText(addTaskBtn.context, "Please select any date", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

}