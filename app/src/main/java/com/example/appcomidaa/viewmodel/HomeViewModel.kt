package com.example.appcomidaa.viewmodel
import com.example.appcomidaa.repository.TaskRepositoryImpl
import androidx.lifecycle.ViewModel
import com.example.appcomidaa._ui.fragments.FavoriteFragment
import com.example.appcomidaa.data.ApiTask
import com.example.appcomidaa.data.Task
import com.example.appcomidaa.data.TaskCategory
import com.example.appcomidaa.data.TaskCategory.*
import com.example.appcomidaa.data.onItemFavorite
import com.example.appcomidaa.data.onItemSelected
import java.io.IOException

class HomeViewModel() : ViewModel() {
    // Categorías estáticas (sealed class, enum, etc.)
     private lateinit var lastDeleteTask: Task
     private var indexLastDelete: Int = 0

     private val categories = listOf(
        Study,
        Work,
        Healt,
        House,
        Other
    )
    private val repository = TaskRepositoryImpl()
    private var token = ""

    private var tasks = mutableListOf(
        Task(1,
            "pruebaHouse",
            House,
            false,
            false
        )
    )

    private var favTasks: List<Task> = tasks.filter { it.is_favorite }


    fun setToken(myToken: String){
        token = myToken
    }
    fun getToken() = token

    fun getCategoryList() = categories
    fun getTaskList() = tasks
    fun getFavList () = favTasks
    fun getSizeTask() = tasks.size

    fun addTaskLocal(name:String,category:TaskCategory, is_selected:Boolean = false, is_favorite:Boolean = false){
        val maxId: Int? = tasks.maxOfOrNull { it.id }
        tasks.add(Task(
            maxId!!+ 1,
            name,
            category,
            is_selected,
            is_favorite)
        )
    }
    suspend fun addTaskBD(name:String,category:TaskCategory){
        try {
            repository.createTask(token,Task(-1,name,category,false,false))
        }
        catch (e: Exception){
            tasks.removeAt(tasks.size -1)
            throw IOException("Falló la conexión con el servidor", e)
        }
    }

    fun updateFavoriteLocal() {
        this.favTasks= tasks.filter { it.is_favorite }
    }

    suspend fun updateFavorite(position: Int){
            try {
                repository.updateTask(this.getToken(), tasks[position])
            }
            catch (e: Exception){
                onItemFavorite(tasks[position])
                favTasks = tasks.filter { it.is_favorite }
                throw IOException("Falló la conexión con el servidor", e)
            }
    }

    suspend fun updateFavorite(task: Task){
        try {
            repository.updateTask(token, task)
        }
        catch (e: Exception){
            onItemFavorite(task)
            updateFavoriteLocal()
            throw IOException("Falló la conexión con el servidor", e)
        }
    }

    private fun setApiTasksToTask(listTask: List<ApiTask>){
        tasks = listTask.map { apiTaskToTask(it) }.toMutableList()
    }

    private fun mapCategory(category: String): TaskCategory {
        return when (category) {
            "House" -> House
            "Work" -> Work
            "Study" -> Study
            "Healt" -> Healt
            else -> Other
        }
    }

    private fun apiTaskToTask(apiTask: ApiTask): Task {
        return Task(
            id = apiTask.id,
            name = apiTask.name,
            category = mapCategory(apiTask.category),
            is_selected = apiTask.is_selected,
            is_favorite = apiTask.is_favorite
        )
    }

    suspend fun fetchTask(){
                val tasks: List<ApiTask> = repository.getTasks(getToken())
                setApiTasksToTask(tasks)
    }

    suspend fun updateTask(position: Int){
        try {
            repository.updateTask(token, tasks[position])
        }
        catch (e: Exception){
            onItemSelected(tasks[position])
            throw IOException("Falló la conexión con el servidor", e)
        }
    }

    suspend fun updateTask(task: Task){
        try {
            repository.updateTask(token, task)
        }
        catch (e: Exception){
            onItemSelected(task)
            throw IOException("Falló la conexión con el servidor", e)
        }
    }

    fun deleteTaskLocal(position: Int){
        indexLastDelete = position
        lastDeleteTask = tasks[position]
        tasks.remove(tasks[position])
    }

    fun deleteTaskLocal(task: Task){
        val foundIndex: Int = tasks.indexOfFirst { it.id == task.id }
        indexLastDelete = foundIndex
        lastDeleteTask = tasks[foundIndex]
        tasks.remove(tasks[foundIndex])
    }

    suspend fun deleteTaskBD(){
        try {
            repository.deleteTask(token,lastDeleteTask.id)
        }
        catch (e: Exception){
            tasks.add(indexLastDelete,lastDeleteTask)
            throw IOException("Falló la conexión con el servidor", e)
        }
    }
}