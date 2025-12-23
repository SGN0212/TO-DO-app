package com.example.appcomidaa._ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcomidaa.R
import com.example.appcomidaa._adapter.FavTaskAdapter
import com.example.appcomidaa.data.Task
import com.example.appcomidaa.databinding.FragmentFavoriteBinding
import com.example.appcomidaa.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    //private lateinit var rvFavTask: RecyclerView
    private lateinit var favTaskAdapter: FavTaskAdapter
    private lateinit var homeViewModel: HomeViewModel

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        initUI()
    }

    private fun initComponent() {
        //rvFavTask = view?.findViewById(R.id.rvFavTask)!!
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    private fun initUI() {
        favTaskAdapter = FavTaskAdapter( homeViewModel.getFavList(),{updateFavorite(it)},{updateTask(it)},{deleteTask(it)})
        binding.rvFavTask.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvFavTask.adapter = favTaskAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateFavorite(task: Task){
        homeViewModel.updateFavoriteLocal()
        favTaskAdapter.favTasks= homeViewModel.getFavList()
        favTaskAdapter.notifyDataSetChanged()
        lifecycleScope.launch {
            try {
                homeViewModel.updateFavorite(task)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al modificar tarea", Toast.LENGTH_SHORT).show()
                favTaskAdapter.favTasks= homeViewModel.getFavList()
                favTaskAdapter.notifyDataSetChanged()
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteTask(task: Task) {
        homeViewModel.deleteTaskLocal(task)
        homeViewModel.updateFavoriteLocal()
        favTaskAdapter.favTasks= homeViewModel.getFavList()
        favTaskAdapter.notifyDataSetChanged()
        lifecycleScope.launch {
            try {
                homeViewModel.deleteTaskBD()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al eliminar tarea", Toast.LENGTH_SHORT).show()
                homeViewModel.updateFavoriteLocal()
                favTaskAdapter.favTasks= homeViewModel.getFavList()
                favTaskAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateTask(task: Task) {

        favTaskAdapter.notifyDataSetChanged()
        lifecycleScope.launch {
            try {
                homeViewModel.updateTask(task)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al modificar tarea", Toast.LENGTH_SHORT).show()
                favTaskAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}