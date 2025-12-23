package com.example.appcomidaa._adapter

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidaa.R
import com.example.appcomidaa.data.Task
import com.example.appcomidaa.data.TaskCategory
import com.example.appcomidaa.databinding.ItemTaskBinding

class FavTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    fun render(task: Task) {
        binding.tvTask.text = task.name
        binding.cbTask.isChecked = task.is_selected
        binding.cbFavoriteTask.isChecked = task.is_favorite
        if (task.is_selected) {
            binding.tvTask.paintFlags = binding.tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        } else {
            binding.tvTask.paintFlags = binding.tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        val color = when (task.category) {
            TaskCategory.Healt -> R.color.colorHealt
            TaskCategory.House -> R.color.colorHouse
            TaskCategory.Other -> R.color.colorOther
            TaskCategory.Study -> R.color.colorStudy
            else -> R.color.colorWork
        }

        binding.cbTask.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(binding.tvTask.context, color)
        )
    }
}