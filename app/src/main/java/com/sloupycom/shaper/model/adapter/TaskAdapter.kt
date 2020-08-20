package com.sloupycom.shaper.model.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.sloupycom.shaper.model.Repo
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.R
import net.igenius.customcheckbox.CustomCheckBox
import kotlin.collections.ArrayList

class TaskAdapter(private val taskStateListener: TaskStateListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var mList: ArrayList<Task> = arrayListOf(Task(), Task())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.today_due_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.mTitle.text = mList[position].name
        when (mList[position].state){
            "DONE" -> {
                holder.mTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.mCardView.setCardBackgroundColor(Color.parseColor("#8DF48D"))
                holder.mCardView.alpha = 0.5f
                holder.mCheckBox.isChecked = true
            }
            "OVERDUE" -> {
                holder.mCardView.setCardBackgroundColor(Color.parseColor("#FF905F"))
                holder.mCardView.alpha = 0.1f
            }

        }

        holder.mRelativeLayout.setOnClickListener { v ->
            //Open Task Overview
        }

        holder.mCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            taskStateListener.onTaskStateChanged(mList[position])
        }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardView: CardView
        var mRelativeLayout: RelativeLayout
        var mTitle: TextView
        var mCheckBox: CustomCheckBox

        init {
            setIsRecyclable(false)
            mCardView = itemView.findViewById(R.id.cardView)
            mRelativeLayout = itemView.findViewById(R.id.relativeLayout)
            mTitle = itemView.findViewById(R.id.textView_title)
            mCheckBox = itemView.findViewById(R.id.checkbox)
        }
    }

    interface TaskStateListener {
        fun onTaskStateChanged(task: Task)
    }
}