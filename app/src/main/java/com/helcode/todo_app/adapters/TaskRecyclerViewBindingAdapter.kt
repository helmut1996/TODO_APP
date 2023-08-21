package com.helcode.todo_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.helcode.todo_app.R
import com.helcode.todo_app.databinding.ViewTaskItemBinding
import com.helcode.todo_app.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRecyclerViewBindingAdapter(
    private val DeleteUpdateCallback: (type:String,position:Int,task:Task)->Unit
):RecyclerView.Adapter<TaskRecyclerViewBindingAdapter.ViewHolder>() {

    private val tasklist = arrayListOf<Task>()

    class ViewHolder( val viewTaskItemBinding: ViewTaskItemBinding)
        : RecyclerView.ViewHolder(viewTaskItemBinding.root)

    fun addAllTask(newTaskList: List<Task>) {
        tasklist.clear()
        tasklist.addAll(newTaskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskRecyclerViewBindingAdapter.ViewHolder {
        return ViewHolder(
           ViewTaskItemBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: TaskRecyclerViewBindingAdapter.ViewHolder, position: Int) {
        val task = tasklist[position]
        holder.viewTaskItemBinding.textTitle.text = task.title
        holder.viewTaskItemBinding.textDescription.text = task.description

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy:HH:mm:ss a", Locale.getDefault())
        holder.viewTaskItemBinding.textDate.text = dateFormat.format(task.date)

        holder.viewTaskItemBinding.imgDelete.setOnClickListener {
            if (holder.adapterPosition != -1) {
                DeleteUpdateCallback("delete", holder.adapterPosition, task)
            }
        }

        holder.viewTaskItemBinding.imgEdit.setOnClickListener {
            if (holder.adapterPosition != -1) {
                DeleteUpdateCallback("update", holder.adapterPosition, task)
            }
        }
    }


    override fun getItemCount(): Int {
        return tasklist.size
    }
}
