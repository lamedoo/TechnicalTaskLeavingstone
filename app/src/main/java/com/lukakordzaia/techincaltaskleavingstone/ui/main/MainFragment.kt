package com.lukakordzaia.techincaltaskleavingstone.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.lukakordzaia.techincaltaskleavingstone.R
import com.lukakordzaia.techincaltaskleavingstone.utils.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.club_info.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlinx.android.synthetic.main.members_recyclerview.*
import kotlin.math.abs

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var adapter: MembersAdapter
    private var currentPage = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)

        viewModel.toastMessage.observe(viewLifecycleOwner, EventObserver {
            requireContext().createToast(it, Toast.LENGTH_LONG)
        })

        viewModel.getFitnessInfo()

        viewModel.showProgress.observe(viewLifecycleOwner, EventObserver {
            if (!it) {
                main_progressBar.setGone()
                main_toolbar_container.setVisible()
                main_content.setVisible()
            }
        })

        //FIRST PART OF THE PAGE
        viewModel.fitnessInfo.observe(viewLifecycleOwner, {
            Picasso.get().load(it.imageUrl).into(fitness_club_image)
        })

        viewModel.clubInfo.observe(viewLifecycleOwner, { info ->
            total_members_count.text = info[0]
            avg_time_count.text = info[1]
            total_time_count.text = info[2]
        })

        group_button.setOnClickListener {
            viewModel.onGroupOptionsPressed()
        }

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>("snackBarMessage")?.observe(viewLifecycleOwner) { result ->
            createSnackBar(main_fragment_root, result)
        }

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (abs(verticalOffset) == appbar.totalScrollRange) {
                members_top_container.setBackgroundColor(Color.WHITE)
                main_fragment_root.setBackgroundColor(Color.WHITE)
                appbar_plus_button.setVisible()
            } else {
                members_top_container.background = ResourcesCompat.getDrawable(requireContext().resources, R.drawable.members_top_background, null)
                main_fragment_root.setBackgroundColor(Color.parseColor("#e4f7fb"))
                appbar_plus_button.setGone()
            }
        })

        viewModel.navigateScreen.observe(viewLifecycleOwner, EventObserver {
            navController(it)
        })


        //SECOND PART OF THE PAGE
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        viewModel.getMembersInfo(currentPage)
        adapter = MembersAdapter(
                requireContext(),
                {
                    viewModel.setChosenMember(it)
                },
                {
                    createSnackBar(members_nested_scroll, it)
                }
        )
        rv_members.adapter = adapter
        rv_members.layoutManager = layoutManager

        viewModel.membersInfo.observe(viewLifecycleOwner, {
            adapter.getMembersList(it)
        })

        members_nested_scroll.setOnScrollChangeListener {
            v: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->

            if (scrollY == v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {

                viewModel.hasMore.observe(viewLifecycleOwner, {
                    if (!it) {
                        rv_progressBar.setGone()
                    } else {
                        rv_progressBar.setVisible()
                        currentPage++
                        Log.d("currentpage", currentPage.toString())
                        viewModel.getMembersInfo(currentPage)
                    }
                })
            }
        }

        viewModel.chosenMember.observe(viewLifecycleOwner, {
            if (it.id != null) {
                adapter.getChosenMember(it.id)
            }
        })

        viewModel.userInfo.observe(viewLifecycleOwner, {
            if (it.id != null) {
                adapter.getUserInfo(it.id)
            }
        })
    }
}