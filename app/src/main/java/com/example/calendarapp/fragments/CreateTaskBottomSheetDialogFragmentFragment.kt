package com.example.calendarapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.calendarapp.R
import com.example.calendarapp.databinding.FragmentCreateTaskBottomSheetDialogFragmentBinding
import com.example.calendarapp.model.TaskModel
import com.example.calendarapp.model.UserTaskModel
import com.example.calendarapp.viewModels.CalendarViewModel
import com.example.calendarapp.viewModels.USER_ID
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateTaskBottomSheetDialogFragmentFragment : BottomSheetDialogFragment() {

    private var binding: FragmentCreateTaskBottomSheetDialogFragmentBinding? = null

    private val calendarViewModel by activityViewModels<CalendarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            updateBottomSheetBackground(it as BottomSheetDialog)
        }
        return FragmentCreateTaskBottomSheetDialogFragmentBinding.inflate(inflater).apply {
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            cancel.setOnClickListener {
                dismiss()
            }
            createNewTaskBtn.setOnClickListener {
                if (title.text?.isEmpty() == true) {
                    Toast.makeText(
                        createNewTaskBtn.context,
                        "Please add title to create task",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (des.text?.isEmpty() == true) {
                    Toast.makeText(
                        createNewTaskBtn.context,
                        "Please add description to create task",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        createNewTaskBtn.context,
                        "${title.text}, ${des.text}",
                        Toast.LENGTH_LONG
                    ).show()

                    calendarViewModel.addNewCalendarTask(
                        UserTaskModel(
                            userId = USER_ID,
                            taskModel = TaskModel(
                                title = title.text.toString(),
                                description = des.text.toString()
                            )
                        )
                    )
                    dismiss()
                }
            }
        }
    }


    private fun updateBottomSheetBackground(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet: FrameLayout? =
            bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.drawable.rounded_square_white)
    }

}