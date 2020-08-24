package com.sloupycom.shaper.model.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.R
import com.sloupycom.shaper.utils.General
import net.igenius.customcheckbox.CustomCheckBox
import kotlin.collections.ArrayList

class TaskAdapter(val context: Context, private val taskStateListener: TaskStateListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var mList: ArrayList<Task> = arrayListOf(Task(), Task())
    private val mGeneral: General = General()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.today_due_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.mTitle.text = mList[position].name

        if (mList[position].state == "DONE") {
            holder.mTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.mCardView.setCardBackgroundColor(context.getColor(R.color.task_item_background_done))
            holder.mCardView.strokeColor = context.getColor(R.color.task_item_stroke_done)
            holder.mCardView.alpha = 0.5f
            holder.mCheckBox.isChecked = true
        } else if (mGeneral.isDateBeforeToday(mList[position].next_due)) {
            holder.mCardView.setCardBackgroundColor(context.getColor(R.color.task_item_background_overdue))
            holder.mCardView.strokeColor = context.getColor(R.color.task_item_stroke_overdue)
            holder.mCardView.alpha = 1f
        }

        holder.mRelativeLayout.setOnClickListener { v ->
            //Open Task Overview
        }

        holder.mCheckBox.setOnCheckedChangeListener { _, _ ->
            taskStateListener.onTaskStateChanged(mList[position])
        }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardView: MaterialCardView
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