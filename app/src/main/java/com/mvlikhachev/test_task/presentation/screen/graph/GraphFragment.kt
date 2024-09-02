package com.mvlikhachev.test_task.presentation.screen.graph

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.mvlikhachev.test_task.R
import com.mvlikhachev.test_task.domain.model.Point
import com.mvlikhachev.test_task.presentation.view.GraphView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraphFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graph, container, false)
        val points =  arguments?.getSerializable("points_list") as List<Point>
        val graphView = view.findViewById<GraphView>(R.id.graph_view)

        graphView.setPoints(points)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val points =  arguments?.getSerializable("points_list") as List<Point>
        val tableView = view.findViewById<TableLayout>(R.id.table_view)

        // Создание заголовка
        val headerRow = TableRow(requireContext())
        val headerX = TextView(requireContext())
        val headerY = TextView(requireContext())

        headerX.text = "X"
        headerX.setTextColor(Color.BLACK)

        headerY.text = "Y"
        headerY.setTextColor(Color.BLACK)

        headerRow.addView(headerX)
        headerRow.addView(headerY)
        tableView.addView(headerRow)

        // Добавление данных
        for (point in points) {
            val row = TableRow(requireContext())
            val textViewX = TextView(requireContext())
            val textViewY = TextView(requireContext())
            val textViewDivider = TextView(requireContext())

            textViewX.text = point.x.toString()
            textViewX.setTextColor(Color.BLACK)

            textViewY.text = point.y.toString()
            textViewY.setTextColor(Color.BLACK)

            textViewDivider.text = " - "
            textViewDivider.setTextColor(Color.BLACK)

            row.addView(textViewX)
            row.addView(textViewY)
            row.addView(textViewDivider)

            tableView.addView(row)
        }
    }

}