package com.akash.gkgsmaster.ui.science

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.ScienceTable
import com.akash.gkgsmaster.data.model.ScienceTopic
import com.akash.gkgsmaster.databinding.FragmentScienceDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScienceDetailFragment : Fragment(R.layout.fragment_science_detail) {

    private var _binding: FragmentScienceDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScienceDetailBinding.bind(view)

        val topic = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("topic", ScienceTopic::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("topic")
        }

        topic?.let { setupUI(it) }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupUI(topic: ScienceTopic) {
        binding.tvTitle.text = topic.title
        binding.tvCategory.text = topic.category
        binding.tvContent.text = topic.content

        // Setup Important Points
        if (topic.importantPoints.isNotEmpty()) {
            binding.llImportantPoints.visibility = View.VISIBLE
            topic.importantPoints.forEach { point ->
                val tv = TextView(requireContext()).apply {
                    text = "• $point"
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    setPadding(0, 4, 0, 4)
                }
                binding.llPointsContainer.addView(tv)
            }
        } else {
            binding.llImportantPoints.visibility = View.GONE
        }

        // Setup Examples
        if (topic.examples.isNotEmpty()) {
            binding.llExamples.visibility = View.VISIBLE
            topic.examples.forEach { example ->
                val tv = TextView(requireContext()).apply {
                    text = "→ $example"
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    setPadding(0, 4, 0, 4)
                }
                binding.llExamplesContainer.addView(tv)
            }
        } else {
            binding.llExamples.visibility = View.GONE
        }

        // Setup Tables
        topic.tables.forEach { table ->
            addTableToUI(table)
        }
    }

    private fun addTableToUI(scienceTable: ScienceTable) {
        val context = requireContext()
        val llTable = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 24, 0, 0)
            layoutParams = params
        }

        scienceTable.title?.let {
            val tvTitle = TextView(context).apply {
                text = it
                setTextColor(ContextCompat.getColor(context, R.color.primary))
                textSize = 16f
                setTypeface(null, Typeface.BOLD)
                setPadding(0, 0, 0, 8)
            }
            llTable.addView(tvTitle)
        }

        val tableLayout = TableLayout(context).apply {
            layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundResource(R.drawable.bg_table_border) // Need to create this
        }

        // Add Headers
        val headerRow = TableRow(context).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.primary))
            setPadding(0, 8, 0, 8)
        }
        scienceTable.headers.forEach { header ->
            val tv = TextView(context).apply {
                text = header
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setTypeface(null, Typeface.BOLD)
                gravity = Gravity.CENTER
                setPadding(8, 0, 8, 0)
            }
            headerRow.addView(tv)
        }
        tableLayout.addView(headerRow)

        // Add Rows
        scienceTable.rows.forEach { rowData ->
            val row = TableRow(context).apply {
                setPadding(0, 8, 0, 8)
            }
            rowData.forEach { cellData ->
                val tv = TextView(context).apply {
                    text = cellData
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    gravity = Gravity.CENTER
                    setPadding(8, 0, 8, 0)
                }
                row.addView(tv)
            }
            tableLayout.addView(row)
        }

        llTable.addView(tableLayout)
        binding.llTablesContainer.addView(llTable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
