package com.example.calendarapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.calendarapp.R
import com.example.calendarapp.adapters.MonthsAdapter
import com.example.calendarapp.databinding.FragmentCreateTaskBottomSheetDialogFragmentBinding
import com.example.calendarapp.databinding.FragmentMonthsPickerDialogBinding
import com.example.calendarapp.model.MonthYearDataModel
import com.example.calendarapp.model.MonthsDataModelClass
import com.example.calendarapp.viewModels.CalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import java.util.Calendar

@AndroidEntryPoint
class MonthsPickerDialogFragment : BottomSheetDialogFragment() {

    private var binding: FragmentMonthsPickerDialogBinding? = null

    private var monthsPickerAdapter: MonthsAdapter? = null

    private val monthYearDataSharedViewModel by activityViewModels<CalendarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            updateBottomSheetBackground(it as BottomSheetDialog)
        }
        return FragmentMonthsPickerDialogBinding.inflate(inflater).apply {
            binding = this
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monthsPickerAdapter = MonthsAdapter().apply {
            submitList(
                monthYearDataSharedViewModel.getMonthsList()
            )
        }


        binding?.apply {
            monthsAdapter = monthsPickerAdapter

            yearText.text = monthYearDataSharedViewModel.selectedYear.toString()
            setClickListener()

            monthYearDataSharedViewModel.selectedYear = yearText.text.toString().toInt()
        }

        monthYearDataSharedViewModel.previousSelectedMonthId =
            monthYearDataSharedViewModel.selectedMonthId
        monthsPickerAdapter?.listener = { view, item, position ->
            if (monthYearDataSharedViewModel.previousSelectedPosition != -1 && monthYearDataSharedViewModel.previousSelectedPosition != position) {
                monthsPickerAdapter?.currentList?.get(monthYearDataSharedViewModel.previousSelectedPosition)
                    ?.apply {
                        isSelected = false
                    }
                monthsPickerAdapter?.notifyItemChanged(monthYearDataSharedViewModel.previousSelectedPosition)
            }
            monthYearDataSharedViewModel.previousSelectedPosition = position
            monthYearDataSharedViewModel.selectedMonthId = item.monthId
            monthYearDataSharedViewModel.selectedMonthName = item.monthName
        }
    }

    private fun updateBottomSheetBackground(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet: FrameLayout? =
            bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.drawable.rounded_square_white)
    }


    private fun setClickListener() {
        binding?.apply {
            leftArrow.setOnClickListener {
                var currentYear = yearText.text.toString().toInt()
                if (currentYear >= 1865) {
                    currentYear--
                    yearText.text = "$currentYear"
                    if (currentYear < 1865) {
                        it.apply {
                            isEnabled = false
                            isClickable = false
                            alpha = 0.5f
                        }
                    } else {
                        it.apply {
                            isEnabled = true
                            isClickable = true
                            alpha = 1f
                        }
                    }
                }

                monthYearDataSharedViewModel.selectedYear = currentYear
            }

            rightArrow.setOnClickListener {
                var currentYear = yearText.text.toString().toInt()
                currentYear++
                yearText.text = "$currentYear"

                monthYearDataSharedViewModel.selectedYear = currentYear
            }

            cancelText.setOnClickListener {
                dismiss()
            }

            selectButton.setOnClickListener {
                monthYearDataSharedViewModel.updateMonthYearLiveData(
                    MonthYearDataModel(
                        monthId = monthYearDataSharedViewModel.selectedMonthId,
                        year = monthYearDataSharedViewModel.selectedYear,
                        monthName = monthYearDataSharedViewModel.selectedMonthName
                    )
                )
                dismiss()
            }
        }
    }
}
