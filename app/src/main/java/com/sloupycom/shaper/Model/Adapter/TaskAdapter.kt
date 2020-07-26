package com.sloupycom.shaper.Model.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sloupycom.shaper.Model.Repo
import com.sloupycom.shaper.Model.Task
import com.sloupycom.shaper.R
import net.igenius.customcheckbox.CustomCheckBox
import kotlin.collections.ArrayList

class TaskAdapter(mRepo: Repo, val context: Context) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>()
    , Repo.OnDataChanged {

    private var mList: ArrayList<Task> = arrayListOf()

    init {
        mRepo.getDueTasks(this)

    }

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
            "DONE" -> {holder.mRelativeLayout.setBackgroundColor(context.getColor(R.color.green))
                holder.mCardView.alpha = 0.5f
                holder.mCheckBox.isChecked = true
            }
            "OVERDUE" -> holder.mRelativeLayout.setBackgroundColor(context.getColor(R.color.orange))
            "DUE" -> holder.mRelativeLayout.setBackgroundColor(context.getColor(R.color.colorPrimary))
            "ONGOING" -> holder.mRelativeLayout.setBackgroundColor(context.getColor(R.color.colorPrimary))
        }
        setListeners(holder, position)
    }

    private fun setListeners(holder: TaskViewHolder, position: Int) {
        holder.mCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked){
                val task = mList[position]
                task.state = "DONE"
                Repo().updateTask(mList[position], task.id)
            }
        }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardView: CardView
        var mRelativeLayout: RelativeLayout
        var mTitle: TextView
        var mCheckBox: CustomCheckBox

        init {
            mCardView = itemView.findViewById(R.id.cardView)
            mRelativeLayout = itemView.findViewById(R.id.relativeLayout)
            mTitle = itemView.findViewById(R.id.textView_title)
            mCheckBox = itemView.findViewById(R.id.checkBox)
        }
    }

    override fun onDataChanged(data: ArrayList<Task>) {
        Log.w("REPO", "onEvent")
        mList.clear()
        mList.addAll(data)
        notifyDataSetChanged()
    }
}