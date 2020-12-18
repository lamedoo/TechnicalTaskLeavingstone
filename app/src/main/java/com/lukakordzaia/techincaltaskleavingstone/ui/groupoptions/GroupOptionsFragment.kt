package com.lukakordzaia.techincaltaskleavingstone.ui.groupoptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lukakordzaia.techincaltaskleavingstone.R
import com.lukakordzaia.techincaltaskleavingstone.utils.createSnackBar
import kotlinx.android.synthetic.main.fragment_group_options.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_members.*

class GroupOptionsFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_group_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        group_options_close.setOnClickListener {
            dismiss()
        }

        group_options_permissions_container.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set("snackBarMessage", "უფლებების მინიჭება")
            dismiss()
        }

        group_options_remove_container.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set("snackBarMessage", "წევრების წაშლა")
            dismiss()
        }

        group_options_leave_container.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set("snackBarMessage", "ჯგუფის დატოვება")
            dismiss()
        }
    }
}