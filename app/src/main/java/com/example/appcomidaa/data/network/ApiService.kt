package com.example.appcomidaa.data.network
import com.example.appcomidaa.data.ApiTask
import com.example.appcomidaa.data.TaskUpdateRequest
import com.example.appcomidaa.data.model.ApiResponse
import com.example.appcomidaa.data.model.LoginResponse
import retrofit2.http.*
import retrofit2.Response

interface ApiService {
    @POST("login")
    suspend fun login(@Body credentials: Map<String, String>): Response<LoginResponse>

    @GET("tasks")
    suspend fun getTasks(@Header("Authorization") token: String): Response<List<ApiTask>>

    @POST("tasks")
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Body task: TaskUpdateRequest
    ): Response<ApiResponse>

    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body task: TaskUpdateRequest
    ): Response<ApiResponse>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ApiResponse>
}