package com.sloupycom.shaper.model.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.sloupycom.shaper.R
import com.sloupycom.shaper.dagger.DaggerDependencyComponent
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.utils.Util
import net.igenius.customcheckbox.CustomCheckBox

class TaskAdapter(
    private val activityContext: Context
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val mComponent = DaggerDependencyComponent.create()
    private val mUtil: Util = mComponent.getUtil()
    private var listener: TaskStateListener? = null

    var data: List<Task> = mutableListOf()
        set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return data[position].id
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.mTitle.text = data[position].title

//        Change item colors based on state
        if (data[position].state == "DONE") {
            holder.mTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.mCardView.setCardBackgroundColor(activityContext.getColor(R.color.task_item_background_done))
            holder.mCardView.strokeColor = activityContext.getColor(R.color.task_item_stroke_done)
            holder.mCheckBox.isChecked = true
        } else if (mUtil.isDateBeforeToday(data[position].next_due)) {
            holder.mCardView.setCardBackgroundColor(activityContext.getColor(R.color.task_item_background_overdue))
            holder.mCardView.strokeColor = activityContext.getColor(R.color.task_item_stroke_overdue)
            holder.mCardView.alpha = 1f
            holder.mCheckBox.isChecked = false
        } else {
            holder.mCheckBox.isChecked = false
            holder.mTitle.paintFlags = holder.mTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.mCardView.setCardBackgroundColor(activityContext.getColor(R.color.task_item_background_due))
            holder.mCardView.strokeColor = activityContext.getColor(R.color.task_item_stroke_due)
            holder.mCardView.alpha = 1f
        }

        holder.mCheckBox.setOnClickListener {
            listener?.onTaskStateChanged(data[position])
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

    fun setOnTaskStateListener(listener: TaskStateListener) {
        this.listener = listener
    }

    interface TaskStateListener {
        fun onTaskStateChanged(task: Task)
    }
}