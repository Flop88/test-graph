package com.mvlikhachev.test_task.presentation.screen.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mvlikhachev.test_task.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val startButton = view.findViewById<Button>(R.id.start_button)
        val pointCountEditText = view.findViewById<EditText>(R.id.point_count)

        startButton.setOnClickListener {
            val count = pointCountEditText.text.toString().toIntOrNull() ?: 0
            if (count > 0) {
                viewModel.fetchPoints(count)
            } else {
                Toast.makeText(requireContext(), "Введите корректное число", Toast.LENGTH_SHORT).show()
            }
        }

        // Подписываемся на изменения в uiState
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        // Показываем прогресс бар
                    }
                    is UiState.Success -> {
                        val bundle = bundleOf("points_list" to uiState.points.sortedBy { it.x })
                        findNavController().navigate(R.id.action_mainFragment_to_graphFragment, bundle)
                        viewModel.clearState()
                    }
                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "Нет данных", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Idle -> {
                        // Ничего не делаем
                    }
                }
            }
        }

        return view
    }

}