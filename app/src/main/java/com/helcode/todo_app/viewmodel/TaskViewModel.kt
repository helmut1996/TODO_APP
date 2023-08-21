package com.helcode.todo_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.helcode.todo_app.models.Task
import com.helcode.todo_app.repository.TaskRepository
import com.helcode.todo_app.utils.Resource

class TaskViewModel(application: Application): AndroidViewModel(application) {
private val taskRepository= TaskRepository(application)


    fun getTaskList()= taskRepository.getTaskList()

    fun insertTask(task:Task):MutableLiveData<Resource<Long>>{
        return taskRepository.insertTask(task)
    }

    fun deleteTask(task:Task):MutableLiveData<Resource<Int>>{
        return taskRepository.deleteTask(task)
    }

    fun deleteTaskUsingId(taskId:String):MutableLiveData<Resource<Int>>{
        return taskRepository.deleteTaskUsinId(taskId)
    }

    fun updateTask(task:Task):MutableLiveData<Resource<Int>>{
        return taskRepository.updateTask(task)
    }

    fun updateTaskParticularField(taskId:String,title:String,description:String):MutableLiveData<Resource<Int>>{
        return taskRepository.updateTaskParticularField(taskId, title, description)
    }
}