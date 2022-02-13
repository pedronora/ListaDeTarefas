package vc.nora.listadetarefas.repository

import androidx.lifecycle.LiveData
import vc.nora.listadetarefas.data.TaskDao
import vc.nora.listadetarefas.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    val readAllData: LiveData<List<Task>> = taskDao.readAllData()

    suspend fun insetTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
}