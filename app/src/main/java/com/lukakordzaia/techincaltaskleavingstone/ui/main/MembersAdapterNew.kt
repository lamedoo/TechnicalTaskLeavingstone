package com.lukakordzaia.techincaltaskleavingstone.ui.main

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lukakordzaia.techincaltaskleavingstone.R
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.MembersInfo
import com.lukakordzaia.techincaltaskleavingstone.utils.setGone
import com.lukakordzaia.techincaltaskleavingstone.utils.setVisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_members_item.view.*


class MembersAdapterNew(
        private val context: Context,
        private val onMemberClick: (member: MembersInfo.Member) -> Unit,
        private val onButtonClick: (button: String) -> Unit
) : RecyclerView.Adapter<MembersAdapterNew.ViewHolder>() {
    private var list: List<MembersInfo.Member> = ArrayList()
    private var chosenMember: Int = 0
    private var longPress = false
    private var userId: Int = 0

    fun getMembersList(list: List<MembersInfo.Member>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun getChosenMember(chosenMember: Int) {
        this.chosenMember = chosenMember
        notifyDataSetChanged()
    }

    fun getUserInfo(userId: Int) {
        this.userId = userId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_members_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = list[position]

        val isChosen = member.id == chosenMember

        if (userId == member.id) {
            holder.memberItemRoot.background = ResourcesCompat.getDrawable(context.resources, R.drawable.default_user_background, null)
            holder.memberItemRoot.isClickable = false
        } else {
            holder.memberItemRoot.setOnClickListener {
                onMemberClick(member)
                longPress = false
            }

            holder.memberItemRoot.setOnLongClickListener {
                onMemberClick(member)
                longPress = true
                true
            }
        }

        if (isChosen) {
            if (!longPress) {
                holder.memberItemRoot.background = ResourcesCompat.getDrawable(context.resources, R.drawable.member_short_press_background, null)
                holder.memberTimeTextView.setGone()
                holder.memberKeyImageButton.setVisible()
            } else {
                holder.memberItemRoot.background = ResourcesCompat.getDrawable(context.resources, R.drawable.member_long_press_background, null)
                holder.memberTimeTextView.setGone()
                holder.memberRemoveImageButton.setVisible()
            }
        }

        if (holder.memberKeyImageButton.isVisible) {
            holder.memberKeyImageButton.setOnClickListener {
                onButtonClick("key")
            }
        }

        if (holder.memberRemoveImageButton.isVisible) {
            holder.memberRemoveImageButton.setOnClickListener {
                onButtonClick("remove")
            }
        }

        val hours = SpannableStringBuilder(member.hours)

        val words: HashSet<String?> = object : HashSet<String?>() {
            init {
                add("სთ")
                add("წთ")
            }
        }
        val s: String = hours.toString()
        for (word in words) {
            val len = word!!.length
            var i = 0
            while (s.indexOf(word, i).also { i = it } >= 0) {
                hours.setSpan(AbsoluteSizeSpan(11, true), i, i + len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                i += len
            }
        }

        holder.memberIdTextView.text = member.id.toString()
        Picasso.get().load(member.imageUrl).into(holder.memberImageImageView)
        holder.memberNameTextView.text = member.name
        holder.memberTimeTextView.text = hours
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val memberItemRoot: ConstraintLayout = view.members_item_root
        val memberIdTextView: TextView = view.members_item_id
        val memberImageImageView: ImageView = view.member_item_image
        val memberNameTextView: TextView = view.members_item_name
        val memberTimeTextView: TextView = view.members_item_time
        val memberRemoveImageButton: ImageButton = view.members_item_remove
        val memberKeyImageButton: ImageButton = view.members_item_key
    }
}