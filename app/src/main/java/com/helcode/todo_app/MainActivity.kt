package com.helcode.todo_app

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.helcode.todo_app.adapters.TaskRecyclerViewAdapter
import com.helcode.todo_app.adapters.TaskRecyclerViewBindingAdapter
import com.helcode.todo_app.databinding.ActivityMainBinding
import com.helcode.todo_app.models.Task
import com.helcode.todo_app.utils.SetupDialog
import com.helcode.todo_app.utils.Status
import com.helcode.todo_app.utils.clearEditText
import com.helcode.todo_app.utils.longToastShow
import com.helcode.todo_app.utils.validateEditText
import com.helcode.todo_app.viewmodel.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val addTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            SetupDialog(R.layout.add_dialog_task)
        }
    }

    private val updateTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            SetupDialog(R.layout.update_task_dialog)
        }
    }
    private val loadingTaskDialog: Dialog by lazy {
        Dialog(this, R.style.DialogCustomTheme).apply {
            SetupDialog(R.layout.loading_dialog)
        }
    }
    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)


        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.imgClose)
        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.imgClose)

        //addDialog
        val edTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val edTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        //addDialog
        edTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateEditText(edTitle, edTitleL)
            }

        })

        //addDialog
        val edDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val edDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        //addDialog
        edDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateEditText(edDesc, edDescL)
            }

        })





        addCloseImg.setOnClickListener { addTaskDialog.dismiss() }
        updateCloseImg.setOnClickListener { updateTaskDialog.dismiss() }

        mainBinding.addTaskFABtn.setOnClickListener {
            clearEditText(edTitle, edTitleL)
            clearEditText(edDesc, edDescL)
            addTaskDialog.show()
        }

        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.btn_save_add)

        saveTaskBtn.setOnClickListener {
            if (validateEditText(edTitle, edTitleL) && validateEditText(edDesc, edDescL)) {
                addTaskDialog.dismiss()
                val newTask = Task(
                    id = UUID.randomUUID().toString(),
                    title = edTitle.text.toString().trim(),
                    description = edDesc.text.toString().trim(),
                    date = Date()
                )
                taskViewModel.insertTask(newTask).observe(this) {
                    when (it.status) {
                        Status.LOADING -> {
                            loadingTaskDialog.show()
                        }

                        Status.SUCCESS -> {
                            loadingTaskDialog.dismiss()
                            if (it.data?.toInt() != -1) {
                                longToastShow("Tarea Guardada!!")
                            }
                        }

                        Status.ERROR -> {
                            loadingTaskDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }
                    }
                }
            }
        }

        //uppdateDialog
        val upTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.upTaskTitle)
        val upTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.upTaskTitleL)

        //updateDialog
        upTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateEditText(upTitle, upTitleL)
            }

        })

        //uppdateDialog
        val upDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.upTaskDescription)
        val upDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.upTaskDescriptionL)

        //updateDialog
        upTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateEditText(upDesc, upDescL)
            }

        })

        val updateTaskBtn = updateTaskDialog.findViewById<Button>(R.id.btn_update)


        ///delete or update
        val taskRecyclerViewAdapter = TaskRecyclerViewBindingAdapter { type,
                                                                position,
                                                                task ->
            if (type == "delete") {
                taskViewModel
                    //  .deleteTask(task)
                    .deleteTaskUsingId(taskId = task.id)
                    .observe(this) {
                        when (it.status) {
                            Status.LOADING -> {
                                loadingTaskDialog.show()
                            }

                            Status.SUCCESS -> {
                                loadingTaskDialog.dismiss()
                                if (it.data != -1) {
                                    longToastShow("Tarea Borrada!!")
                                }
                            }

                            Status.ERROR -> {
                                loadingTaskDialog.dismiss()
                                it.message?.let { it1 -> longToastShow(it1) }
                            }
                        }
                    }
            } else if (type == "update") {
                upTitle.setText(task.title)
                upDesc.setText(task.description)
                updateTaskBtn.setOnClickListener {
                    if (validateEditText(upTitle, upTitleL) && validateEditText(upDesc, upDescL)) {
                        val updateTask = Task(
                            task.id,
                            upTitle.text.toString().trim(),
                            upDesc.text.toString().trim(),
                            Date()
                        )
                        updateTaskDialog.dismiss()
                        loadingTaskDialog.show()

                        taskViewModel
                            .updateTask(updateTask)
                            .observe(this) {
                                when (it.status) {
                                    Status.LOADING -> {
                                        loadingTaskDialog.show()
                                    }

                                    Status.SUCCESS -> {
                                        loadingTaskDialog.dismiss()
                                        if (it.data != -1) {
                                            longToastShow("Tarea Actualizada!!")
                                        }
                                    }

                                    Status.ERROR -> {
                                        loadingTaskDialog.dismiss()
                                        it.message?.let { it1 -> longToastShow(it1) }
                                    }
                                }
                            }
                    }
                }
                updateTaskDialog.show()
            }
        }

        mainBinding.recycler.adapter = taskRecyclerViewAdapter
        callGetTaskList(taskRecyclerViewAdapter)
    }


    private fun callGetTaskList(taskRecyclerViewAdapter: TaskRecyclerViewBindingAdapter) {
        loadingTaskDialog.show()
        CoroutineScope(Dispatchers.Main).launch {

            taskViewModel.getTaskList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingTaskDialog.show()
                    }

                    Status.SUCCESS -> {
                        it.data?.collect { taskList ->
                            loadingTaskDialog.dismiss()
                            taskRecyclerViewAdapter.addAllTask(taskList)
                        }
                    }

                    Status.ERROR -> {
                        loadingTaskDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }
                }
            }

        }
    }
}