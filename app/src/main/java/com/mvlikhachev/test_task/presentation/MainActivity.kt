package com.mvlikhachev.test_task.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mvlikhachev.test_task.R
import dagger.hilt.android.AndroidEntryPoint


/**
 * Можно сделать конечно лучше, но времени честно говоря нет.
 * Можно разбить на модуль, написать тесты, текст вынести в ресурсы и т.п
 * Кастомные вьюхи тоже очень-очень давно не делал и накидал эту "по-быстрому"
 * Ну и UI красивым тоже не делал, а сделал просто чтоб работало. Дальше можно кастомизировать
 * как дизайнеры захотят
 **/

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}