package com.helcode.todo_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.helcode.todo_app.R
import com.helcode.todo_app.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRecyclerViewAdapter(
    private val DeleteUpdateCallback: (type:String,position:Int,task:Task)->Unit
):RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    private val tasklist = arrayListOf<Task>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleText: TextView = itemView.findViewById(R.id.text_title)
        val descrText: TextView = itemView.findViewById(R.id.text_description)
        val dateText: TextView = itemView.findViewById(R.id.text_date)
        val deleteImg: ImageView = itemView.findViewById(R.id.imgDelete)
        val editImg: ImageView = itemView.findViewById(R.id.imgEdit)
    }

    fun addAllTask(newTaskList: List<Task>) {
        tasklist.clear()
        tasklist.addAll(newTaskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_task_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskRecyclerViewAdapter.ViewHolder, position: Int) {
        val task = tasklist[position]
        holder.titleText.text = task.title
        holder.descrText.text = task.description

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy:HH:mm:ss a", Locale.getDefault())
        holder.dateText.text = dateFormat.format(task.date)

        holder.deleteImg.setOnClickListener {
            if (holder.adapterPosition != -1) {
                DeleteUpdateCallback("delete", holder.adapterPosition, task)
            }
        }

        holder.editImg.setOnClickListener {
            if (holder.adapterPosition != -1) {
                DeleteUpdateCallback("update", holder.adapterPosition, task)
            }
        }
    }


    override fun getItemCount(): Int {
        return tasklist.size
    }
}
