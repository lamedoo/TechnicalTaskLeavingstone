package com.lukakordzaia.techincaltaskleavingstone.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.lukakordzaia.techincaltaskleavingstone.R
import com.lukakordzaia.techincaltaskleavingstone.ui.members.MembersAdapter
import com.lukakordzaia.techincaltaskleavingstone.utils.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.club_info.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_members.*
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlin.math.abs

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var adapterNew: MembersAdapterNew
    private val args: MainFragmentArgs by navArgs()
    private var previousTotal = 0
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        viewModel.getFitnessInfo(args.FitnesInfo)

        //FIRST PARGE OF THE PAGE
        viewModel.fitnessInfo.observe(viewLifecycleOwner, Observer {
            Picasso.get().load(it.imageUrl).into(fitness_club_image)
        })

        viewModel.clubInfo.observe(viewLifecycleOwner, Observer { info ->
            total_members_count.text = info[0]
            avg_time_count.text = info[1].filter { it.isDigit() }
            total_time_count.text = info[2].filter { it.isDigit() }
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
            } else {
                members_top_container.background = ResourcesCompat.getDrawable(requireContext().resources, R.drawable.members_top_background, null)
                main_fragment_root.setBackgroundColor(Color.parseColor("#e4f7fb"))
            }
        })

        viewModel.navigateScreen.observe(viewLifecycleOwner, EventObserver {
            navController(it)
        })


        //SECOND PART OF THE PAGE
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        args.FitnesInfo.me?.let { viewModel.getUserInfo(it) }
        viewModel.getMembersInfo(currentPage)
        adapterNew = MembersAdapterNew(
                requireContext(),
                {
                    viewModel.setChosenMember(it)
                },
                {
                    when (it) {
                        "key" -> {
                            createSnackBar(members_root, "უფლებების მინიჭება")
                        }
                        "remove" -> {
                            createSnackBar(members_root, "ჯგუფიდან წაშლა")
                        }
                    }
                }
        )
        rv_members.adapter = adapterNew
        rv_members.layoutManager = layoutManager

        viewModel.membersInfo.observe(viewLifecycleOwner, Observer {
            adapterNew.getMembersList(it)
            previousTotal = it.size
        })

        viewModel.hasMore.observe(viewLifecycleOwner, Observer {
        })

        members_root.setOnScrollChangeListener {
            v: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->

            if (scrollY == v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                currentPage++
                Log.d("currentpage", currentPage.toString())
                viewModel.getMembersInfo(currentPage)
            }
        }

        viewModel.chosenMember.observe(viewLifecycleOwner, Observer {
            it.id?.let { it1 -> adapterNew.getChosenMember(it1) }
        })

        viewModel.userInfo.observe(viewLifecycleOwner, Observer {
            it.id?.let { it1 -> adapterNew.getUserInfo(it1) }
        })
    }

}