package com.example.appcomidaa._adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidaa.R
import com.example.appcomidaa.data.TaskCategory
import com.example.appcomidaa.data.onItemSelected

class CategoriesAdapter(private val categories: List<TaskCategory>, val updateCategory: (Int) -> Unit) :
    RecyclerView.Adapter<CategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_category, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.render(categories[position])
        val cvCategory: CardView = holder.itemView.findViewById(R.id.cvCategory)
        cvCategory.setOnClickListener {
            onItemSelected(categories[position])
            updateCategory(position)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}