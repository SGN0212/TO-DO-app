package com.example.appcomidaa._ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidaa.R
import com.example.appcomidaa.data.Task
import com.example.appcomidaa._adapter.CategoriesAdapter
import com.example.appcomidaa._adapter.TaskAdapter
import com.example.appcomidaa.data.TaskCategory
import com.example.appcomidaa.data.TaskCategory.*
import androidx.lifecycle.lifecycleScope
import com.example.appcomidaa.viewmodel.HomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var rvCategory: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var rvTask: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var loadingOverlay: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        initUI()
        initListeners()
        fetchTask()
    }
    private fun fetchTask(){
        lifecycleScope.launch {
            showOverlay(true)
            try {
                homeViewModel.fetchTask()
                updateTaskListAdapter()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar tareas", Toast.LENGTH_SHORT).show()
            } finally {
                homeViewModel.updateFavoriteLocal()
                showOverlay(false)
            }

        }
    }
    private fun initComponent() {
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        rvCategory = view?.findViewById(R.id.rvCategory)!!
        rvTask = view?.findViewById(R.id.rvTask)!!
        fabAddTask = view?.findViewById(R.id.fabAddTask)!!
        loadingOverlay = view?.findViewById(R.id.loadingOverlay)!!
    }

    private fun initUI() {
        categoriesAdapter = CategoriesAdapter(homeViewModel.getCategoryList()) {updateCategories(it)}

        rvCategory.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvCategory.adapter = categoriesAdapter



        taskAdapter = TaskAdapter(
            homeViewModel.getTaskList(), { updateTasks(it) } , {updateTasksFav(it)}, { deleteTask(it) } )
        updateTaskListAdapter()

        rvTask.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvTask.adapter = taskAdapter
    }


    private fun updateCategories(position:Int){
        categoriesAdapter.notifyItemChanged(position)
        updateTaskListAdapter()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun updateTaskListAdapter(){
        val selectedCategories = homeViewModel.getCategoryList().filter { it.isSelected }
        val newTask = homeViewModel.getTaskList().filter { selectedCategories.contains(it.category) }
        taskAdapter.tasks = newTask.toMutableList()
        taskAdapter.notifyDataSetChanged()
    }

    private fun initListeners() {
        fabAddTask.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        val dialog = this.context?.let {
            Dialog(it).apply {
                setContentView(R.layout.dialog_new_task)
                show()
            }
        }

        val btnAddTask: Button? = dialog?.findViewById(R.id.btnAddTask)
        val rgCategories: RadioGroup? = dialog?.findViewById(R.id.rgCategories)
        val etDialog: EditText? = dialog?.findViewById(R.id.etDialog)

        btnAddTask?.setOnClickListener {
            val selectedId = rgCategories?.checkedRadioButtonId
            val selectedRadioButton: RadioButton = rgCategories!!.findViewById(selectedId!!)
            val currentCategory: TaskCategory = when (selectedRadioButton.text) {
                getString(R.string.Study) -> Study
                getString(R.string.Work) -> Work
                getString(R.string.Healt) -> Healt
                getString(R.string.House) -> House
                else -> Other
            }
            etDialog?.text?.toString()?.trim()?.let { input ->
                if (input.isNotEmpty()) {

                    homeViewModel.addTaskLocal(input, currentCategory)
                    updateTaskListAdapter()

                    lifecycleScope.launch {
                        try {
                            homeViewModel.addTaskBD(input, currentCategory)
                        } catch (e: Exception) {
                            updateTaskListAdapter()
                            Toast.makeText(requireContext(), "Error al insertar tarea", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
            dialog.hide()
        }
    }


    private fun updateTasks(position:Int) {
        taskAdapter.notifyItemChanged(position)
        lifecycleScope.launch {
            try {
                homeViewModel.updateTask(position)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al modificar tarea", Toast.LENGTH_SHORT).show()
                taskAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun deleteTask(position:Int) {
        val task = taskAdapter.tasks[position]
        taskAdapter.tasks.removeAt(position)
        taskAdapter.notifyItemRemoved(position)

        homeViewModel.deleteTaskLocal(task)
        lifecycleScope.launch {
            try {
                homeViewModel.deleteTaskBD()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al eliminar tarea", Toast.LENGTH_SHORT).show()
                taskAdapter.tasks.add(position, task)
                taskAdapter.notifyItemInserted(position)
            }
        }
    }

    private fun updateTasksFav(position:Int) {
        homeViewModel.updateFavoriteLocal()
        lifecycleScope.launch {
            try {
                homeViewModel.updateFavorite(position)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al modificar tarea", Toast.LENGTH_SHORT).show()
                taskAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun showOverlay(show: Boolean) {
        loadingOverlay.visibility = if (show) View.VISIBLE else View.GONE
    }
}