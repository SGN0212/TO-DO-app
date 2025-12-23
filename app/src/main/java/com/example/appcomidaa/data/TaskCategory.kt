package com.example.appcomidaa.data


sealed class TaskCategory(val categoryName: String, var isSelected: Boolean = true) {
    object Study : TaskCategory("Study")
    object Work : TaskCategory("Work")
    object Healt : TaskCategory("Healt")
    object House : TaskCategory("House")
    object Other : TaskCategory("Other")
}

fun onItemSelected(category: TaskCategory){
    category.isSelected= !category.isSelected
}