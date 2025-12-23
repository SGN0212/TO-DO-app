package com.example.appcomidaa._adapter
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidaa.R
import com.example.appcomidaa.data.TaskCategory

class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val cvCategory : CardView = view.findViewById(R.id.cvCategory)
    private val tvCategoryName:TextView = view.findViewById<TextView>(R.id.tvCategoryName)
    private val divider:View = view.findViewById<View>(R.id.divider)

    fun render(taskCategory:TaskCategory){

        val color = when (taskCategory.isSelected){
            true -> R.color.item_task_category
            false -> R.color.brownC
        }

        cvCategory.setCardBackgroundColor(ContextCompat.getColor(cvCategory.context,color))

        when(taskCategory){
            TaskCategory.Healt -> {
                tvCategoryName.text= itemView.context.getString(R.string.Healt)
                divider.setBackgroundColor(ContextCompat.getColor(tvCategoryName.context, R.color.colorHealt))
            }
            TaskCategory.House -> {
                tvCategoryName.text= itemView.context.getString(R.string.House)
                divider.setBackgroundColor(ContextCompat.getColor(tvCategoryName.context, R.color.colorHouse))
            }
            TaskCategory.Other -> {
                tvCategoryName.text= itemView.context.getString(R.string.Other)
                divider.setBackgroundColor(ContextCompat.getColor(tvCategoryName.context, R.color.colorOther))
            }
            TaskCategory.Study -> {
                tvCategoryName.text= itemView.context.getString(R.string.Study)
                divider.setBackgroundColor(ContextCompat.getColor(tvCategoryName.context, R.color.colorStudy))
            }
            TaskCategory.Work -> {
                tvCategoryName.text= itemView.context.getString(R.string.Work)
                divider.setBackgroundColor(ContextCompat.getColor(tvCategoryName.context, R.color.colorWork))
            }
        }
    }
}