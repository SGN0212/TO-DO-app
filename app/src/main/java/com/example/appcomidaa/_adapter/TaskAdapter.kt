package com.example.appcomidaa._adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidaa.R
import com.example.appcomidaa.data.Task
import com.example.appcomidaa.data.onItemFavorite
import com.example.appcomidaa.data.onItemSelected

class TaskAdapter(
        var tasks: MutableList<Task>,
        private val updateTasks: (Int) -> Unit,
        private val updateFav: (Int) -> Unit,
        private val deleteTask: (Int) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.render(tasks[position])

        val cbSelected: CheckBox = holder.itemView.findViewById(R.id.cbTask)
        val cbFavorite: CheckBox = holder.itemView.findViewById(R.id.cbFavoriteTask)
        val cbDelete: CheckBox = holder.itemView.findViewById(R.id.cbDeleteTask)

        cbSelected.setOnClickListener {
            onItemSelected(tasks[position])
            updateTasks(position)
        }
        cbFavorite.setOnClickListener {
            onItemFavorite(tasks[position])
            updateFav(position)
        }
        cbDelete.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                deleteTask(currentPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
