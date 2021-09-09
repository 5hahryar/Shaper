package com.sloupycom.shaper.model.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sloupycom.shaper.R
import com.sloupycom.shaper.databinding.ItemTaskBinding
import com.sloupycom.shaper.model.Task
import com.sloupycom.shaper.core.util.Util
import net.igenius.customcheckbox.CustomCheckBox

class TaskAdapter(
    private val activityContext: Context
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    //TODO: Is it ok to have an instance of context in here?

    private var listener: TaskStateListener? = null
    private var recentDeletedItemPosition: Int? = null
    private var recentDeletedItem: Task? = null

    var data: MutableList<Task> = mutableListOf()
        set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return data[position].id
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        holder.binding.textViewTitle.text = data[position].title

//        Change item colors based on state
        when {
            data[position].state == "DONE" -> {
                holder.binding.textViewTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.binding.cardView.setCardBackgroundColor(activityContext.getColor(R.color.task_item_background_done))
                holder.binding.cardView.strokeColor = activityContext.getColor(R.color.task_item_stroke_done)
                holder.binding.checkbox.isChecked = true
            }
            Util.isDateBeforeToday(data[position].next_due) -> {
                holder.binding.cardView.setCardBackgroundColor(activityContext.getColor(R.color.task_item_background_overdue))
                holder.binding.cardView.strokeColor = activityContext.getColor(R.color.task_item_stroke_overdue)
                holder.binding.cardView.alpha = 1f
                holder.binding.checkbox.isChecked = false
            }
            else -> {
                holder.binding.checkbox.isChecked = false
                holder.binding.textViewTitle.paintFlags = holder.itemView.findViewById<TextView?>(R.id.textView_title).paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.binding.cardView.setCardBackgroundColor(activityContext.getColor(R.color.task_item_background_due))
                holder.binding.cardView.strokeColor = activityContext.getColor(R.color.task_item_stroke_due)
                holder.binding.cardView.alpha = 1f
            }
        }

        holder.itemView.findViewById<CustomCheckBox?>(R.id.checkbox).setOnClickListener {
            listener?.onTaskStateChanged(data[position])
        }

    }

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            setIsRecyclable(false)
        }

    }

    fun setOnTaskStateListener(listener: TaskStateListener) {
        this.listener = listener
    }

    fun deleteItem(position: Int) {
        recentDeletedItem = data[position]
        recentDeletedItemPosition = position
        listener?.onTaskItemDeleted(recentDeletedItem!!)
        data.drop(recentDeletedItemPosition!!)
        notifyItemRemoved(recentDeletedItemPosition!!)
    }

    fun undoRemove() {
        listener?.onTaskItemDeleteUndo(recentDeletedItem!!)
        data.add(recentDeletedItemPosition!!, recentDeletedItem!!)
        notifyItemInserted(recentDeletedItemPosition!!)
    }

    interface TaskStateListener {
        fun onTaskStateChanged(task: Task)
        fun onTaskItemDeleted(task: Task)
        fun onTaskItemDeleteUndo(task: Task)
    }
}