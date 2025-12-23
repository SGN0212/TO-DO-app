package com.example.appcomidaa._adapter

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidaa.R
import com.example.appcomidaa.data.Task
import com.example.appcomidaa.data.TaskCategory

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvTask: TextView = view.findViewById(R.id.tvTask)
    private val cbTask: CheckBox = view.findViewById(R.id.cbTask)
    private val cbFavoriteTask: CheckBox = view.findViewById(R.id.cbFavoriteTask)

    fun render(task: Task) {
        tvTask.text = task.name
        cbTask.isChecked = task.is_selected
        cbFavoriteTask.isChecked = task.is_favorite
        if (task.is_selected) {
            tvTask.paintFlags = tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        } else {
            tvTask.paintFlags = tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        val color = when (task.category) {
            TaskCategory.Healt -> R.color.colorHealt
            TaskCategory.House -> R.color.colorHouse
            TaskCategory.Other -> R.color.colorOther
            TaskCategory.Study -> R.color.colorStudy
            TaskCategory.Work -> R.color.colorWork
        }

        cbTask.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(tvTask.context, color)
        )
    }
}