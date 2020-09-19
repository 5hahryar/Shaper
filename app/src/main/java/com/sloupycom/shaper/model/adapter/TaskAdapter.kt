package com.sloupycom.shaper.model.adapter

import android.app.Application
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.R
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.utils.Util
import net.igenius.customcheckbox.CustomCheckBox
import kotlin.collections.ArrayList

class TaskAdapter(
    application: Application,
    private val taskStateListener: TaskStateListener,
    private val activityContext: Context
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val mComponent = DaggerDependencyComponent.create()
    var mList: ArrayList<Task> = arrayListOf()
    private val mUtil: Util = mComponent.getUtil()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.mTitle.text = mList[position].title

        //Change item colors based on state
        if (mList[position].state == "DONE") {
            holder.mTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.mCardView.setCardBackgroundColor(activityContext.getColor(R.color.task_item_background_done))
            holder.mCardView.strokeColor = activityContext.getColor(R.color.task_item_stroke_done)
            holder.mCardView.alpha = 0.5f
            holder.mCheckBox.isChecked = true
        } else if (mUtil.isDateBeforeToday(mList[position].next_due)) {
            holder.mCardView.setCardBackgroundColor(activityContext.getColor(R.color.task_item_background_overdue))
            holder.mCardView.strokeColor =
                activityContext.getColor(R.color.task_item_stroke_overdue)
            holder.mCardView.alpha = 1f
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