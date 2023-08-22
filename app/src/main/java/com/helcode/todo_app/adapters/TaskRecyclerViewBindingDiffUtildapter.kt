package com.helcode.todo_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.helcode.todo_app.R
import com.helcode.todo_app.databinding.ViewTaskItemBinding
import com.helcode.todo_app.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRecyclerViewBindingDiffUtildapter(
    private val DeleteUpdateCallback: (type:String,position:Int,task:Task)->Unit
): ListAdapter<Task, TaskRecyclerViewBindingDiffUtildapter.ViewHolder>(DiffCallback()) {



    class ViewHolder( val viewTaskItemBinding: ViewTaskItemBinding)
        : RecyclerView.ViewHolder(viewTaskItemBinding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskRecyclerViewBindingDiffUtildapter.ViewHolder {
        return ViewHolder(
           ViewTaskItemBinding.inflate( LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: TaskRecyclerViewBindingDiffUtildapter.ViewHolder, position: Int) {
        val task = getItem(position)
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



    class DiffCallback : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }
}
