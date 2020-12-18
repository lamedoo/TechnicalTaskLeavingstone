package com.lukakordzaia.techincaltaskleavingstone.ui.members

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lukakordzaia.techincaltaskleavingstone.R
import com.lukakordzaia.techincaltaskleavingstone.utils.createSnackBar
import kotlinx.android.synthetic.main.fragment_members.*

class MembersFragment : Fragment(R.layout.fragment_members) {
    private lateinit var viewModel: MembersViewModel
    private lateinit var adapter: MembersAdapter
    private var previousTotal = 0
    private var currentPage = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MembersViewModel::class.java)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        viewModel.getUserInfo()
        viewModel.getMembersInfo(currentPage)
        adapter = MembersAdapter(
                requireContext(),
                {
                    viewModel.setChosenMember(it)
                },
                {
                    when (it) {
                        "key" -> {
                            createSnackBar(members_root1, "უფლებების მინიჭება")
                        }
                        "remove" -> {
                            createSnackBar(members_root1, "ჯგუფიდან წაშლა")
                        }
                    }
                }
        )
        rv_members1.adapter = adapter
        rv_members1.layoutManager = layoutManager

        viewModel.membersInfo.observe(viewLifecycleOwner, Observer {
            adapter.getMembersList(it)
            previousTotal = it.size
        })

        viewModel.hasMore.observe(viewLifecycleOwner, Observer {
        })

        members_root1.setOnScrollChangeListener {
                v: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->

            if (scrollY == v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                currentPage++
                Log.d("currentpage", currentPage.toString())
                viewModel.getMembersInfo(currentPage)
            }
        }

        viewModel.chosenMember.observe(viewLifecycleOwner, Observer {
            it.id?.let { it1 -> adapter.getChosenMember(it1) }
        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            it.id?.let { it1 -> adapter.getUserInfo(it1) }
        })
    }
}