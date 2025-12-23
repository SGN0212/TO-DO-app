package com.example.appcomidaa._adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidaa.R
import com.example.appcomidaa.data.Task
import com.example.appcomidaa.data.onItemFavorite
import com.example.appcomidaa.data.onItemSelected

class FavTaskAdapter( var favTasks: List<Task>, private val updateFav:(Task) -> Unit, private val updateTask:(Task) -> Unit, private val deleteTask:(Task) -> Unit) :
    RecyclerView.Adapter<FavTaskViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavTaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return FavTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavTaskViewHolder, position: Int) {
        holder.render(favTasks[position])

        val cbSelected: CheckBox = holder.itemView.findViewById(R.id.cbTask)
        val cbFavorite: CheckBox = holder.itemView.findViewById(R.id.cbFavoriteTask)
        val cbDelete: CheckBox = holder.itemView.findViewById(R.id.cbDeleteTask)

        cbSelected.setOnClickListener {
            onItemSelected(favTasks[position])
            updateTask(favTasks[position])
        }
        cbFavorite.setOnClickListener {
            onItemFavorite(favTasks[position])
            updateFav(favTasks[position])
        }
        cbDelete.setOnClickListener {
            deleteTask(favTasks[position])
        }
    }

    override fun getItemCount(): Int {
        return favTasks.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Task>) {
        favTasks = newList
        notifyDataSetChanged()
    }
}