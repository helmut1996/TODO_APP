package com.helcode.todo_app.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.helcode.todo_app.database.TaskDatabase
import com.helcode.todo_app.models.Task
import com.helcode.todo_app.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.Error
import java.lang.Exception

class TaskRepository(application: Application) {

    private val taskDao = TaskDatabase.getInstance(application).taskDao


    fun getTaskList() = flow {
        emit(Resource.Loading())
        try {
            val result = taskDao.getTaskList()
            emit(Resource.Success(result))
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun insertTask(task:Task)= MutableLiveData<Resource<Long>>().apply {
        postValue(Resource.Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val  result = taskDao.insertTask(task)
                postValue(Resource.Success(result))
            }
        }catch (e:Exception){
            postValue(Resource.Error(e.message.toString()))
        }
    }


    fun deleteTask(task:Task)= MutableLiveData<Resource<Int>>().apply {
        postValue(Resource.Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val  result = taskDao.deleteTask(task)
                postValue(Resource.Success(result))
            }
        }catch (e:Exception){
            postValue(Resource.Error(e.message.toString()))
        }
    }

    fun deleteTaskUsinId(taskId:String)= MutableLiveData<Resource<Int>>().apply {
        postValue(Resource.Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val  result = taskDao.deleteTaskUsingId(taskId)
                postValue(Resource.Success(result))
            }
        }catch (e:Exception){
            postValue(Resource.Error(e.message.toString()))
        }
    }


    fun updateTask(task:Task)= MutableLiveData<Resource<Int>>().apply {
        postValue(Resource.Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val  result = taskDao.updateTask(task)
                postValue(Resource.Success(result))
            }
        }catch (e:Exception){
            postValue(Resource.Error(e.message.toString()))
        }
    }


    fun updateTaskParticularField(taskId:String,title:String,description:String)= MutableLiveData<Resource<Int>>().apply {
        postValue(Resource.Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val  result = taskDao.updateTaskParticularField(taskId, title, description)
                postValue(Resource.Success(result))
            }
        }catch (e:Exception){
            postValue(Resource.Error(e.message.toString()))
        }
    }
}