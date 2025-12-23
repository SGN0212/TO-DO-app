package com.example.appcomidaa.data

data class Task (
    val id: Int,
    val name: String,
    val category: TaskCategory,
    var is_selected: Boolean,
    var is_favorite: Boolean,
)

data class ApiTask(
    val id: Int,
    val name: String,
    val category: String,
    val is_selected: Boolean,
    val is_favorite: Boolean
)

data class TaskUpdateRequest(
    val name: String?,
    val category: String?,
    val is_selected: Boolean,
    val is_favorite: Boolean
)

fun onItemSelected(task:Task){
    task.is_selected = !task.is_selected
}

fun onItemFavorite(task:Task){
    task.is_favorite = !task.is_favorite
}