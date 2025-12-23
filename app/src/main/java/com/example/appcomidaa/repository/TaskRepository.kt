package com.example.appcomidaa.repository

import android.util.Log
import com.example.appcomidaa.data.ApiTask
import com.example.appcomidaa.data.Task
import com.example.appcomidaa.data.TaskUpdateRequest
import com.example.appcomidaa.data.model.*
import com.example.appcomidaa.data.network.RetrofitClient

interface TaskRepository {
    suspend fun login(username: String, password: String): LoginResponse?
    suspend fun getTasks(token: String): List<ApiTask>
    suspend fun createTask(token: String, task: Task): ApiResponse?
    suspend fun updateTask(token: String, task: Task): ApiResponse?
    suspend fun deleteTask(token: String, taskId: Int): ApiResponse?
}

class TaskRepositoryImpl : TaskRepository {
    private val api = RetrofitClient.apiService

    override suspend fun login(username: String, password: String): LoginResponse? {
        val response = api.login(mapOf("username" to username, "password" to password))
        return response.body()
    }

    override suspend fun getTasks(token: String): List<ApiTask> {
        val response = api.getTasks("Bearer $token")
        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
        }
        return response.body() ?: emptyList()
    }

    override suspend fun createTask(token: String, task: Task): ApiResponse? {
        val taskRequest = TaskUpdateRequest(task.name,task.category.categoryName,task.is_selected,task.is_favorite)

        try {
            val response = api.createTask("Bearer $token", taskRequest)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API", "Error durante createTask", e)
            throw e
        }

        /*val taskMap = mapOf(
            "name" to task.name,
            "category" to task.category.categoryName,
            "is_selected" to task.is_selected,
            "is_favorite" to task.is_favorite
        )
        */
    }

    override suspend fun updateTask(token: String, task: Task): ApiResponse? {
        val taskRequest = TaskUpdateRequest(task.name,task.category.categoryName,task.is_selected,task.is_favorite)
        try {
            val response = api.updateTask("Bearer $token", task.id, taskRequest)
            return response.body()
        } catch (e: Exception) {
            Log.e("API", "Error durante updateTask", e)
            throw e
        }
    }

    override suspend fun deleteTask(token: String, taskId: Int): ApiResponse? {
        try {
            val response = api.deleteTask("Bearer $token", taskId)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API", "Error durante updateTask", e)
            throw e
        }
    }
}