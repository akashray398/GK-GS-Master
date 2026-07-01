package com.akash.gkgsmaster.ui.analytics

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.AnalyticsData
import com.akash.gkgsmaster.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalyticsFragment : Fragment(R.layout.fragment_analytics) {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnalyticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAnalyticsBinding.bind(view)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.analyticsData.observe(viewLifecycleOwner) { data ->
            setupWeeklyChart(data)
            setupAccuracyChart(data)
            setupTopicPerformanceChart(data)
            setupMonthlyChart(data)
            setupStudyTimeChart(data)
        }
    }

    private fun setupWeeklyChart(data: AnalyticsData) {
        val entries = data.weeklyProgress.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value)
        }
        val dataSet = BarDataSet(entries, "Lessons Completed").apply {
            color = Color.parseColor("#4CAF50")
            valueTextColor = Color.WHITE
        }
        binding.weeklyBarChart.apply {
            this.data = BarData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(data.weeklyProgress.map { it.label })
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            axisLeft.textColor = Color.WHITE
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.textColor = Color.WHITE
            animateY(1000)
            invalidate()
        }
    }

    private fun setupAccuracyChart(data: AnalyticsData) {
        val entries = listOf(
            PieEntry(data.quizAccuracy, "Correct"),
            PieEntry(100f - data.quizAccuracy, "Incorrect")
        )
        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(Color.parseColor("#4CAF50"), Color.parseColor("#F44336"))
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }
        binding.accuracyPieChart.apply {
            this.data = PieData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false
            centerText = "${data.quizAccuracy}%"
            setCenterTextColor(Color.WHITE)
            setHoleColor(Color.TRANSPARENT)
            animateXY(1000, 1000)
            invalidate()
        }
    }

    private fun setupTopicPerformanceChart(data: AnalyticsData) {
        val entries = data.topicPerformance.mapIndexed { index, topic ->
            BarEntry(index.toFloat(), topic.accuracy)
        }
        val dataSet = BarDataSet(entries, "Mastery %").apply {
            color = Color.parseColor("#2196F3")
            valueTextColor = Color.WHITE
        }
        binding.topicHorizontalBarChart.apply {
            this.data = BarData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(data.topicPerformance.map { it.topicName })
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            axisLeft.textColor = Color.WHITE
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
            animateY(1000)
            invalidate()
        }
    }

    private fun setupMonthlyChart(data: AnalyticsData) {
        val entries = data.monthlyProgress.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.value)
        }
        val dataSet = LineDataSet(entries, "Monthly Trend").apply {
            color = Color.parseColor("#FF9800")
            setCircleColor(Color.WHITE)
            lineWidth = 2f
            circleRadius = 4f
            setDrawCircleHole(false)
            valueTextColor = Color.WHITE
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        binding.monthlyLineChart.apply {
            this.data = LineData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(data.monthlyProgress.map { it.label })
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            axisLeft.textColor = Color.WHITE
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.textColor = Color.WHITE
            animateX(1000)
            invalidate()
        }
    }

    private fun setupStudyTimeChart(data: AnalyticsData) {
        val entries = data.studyTime.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.minutes.toFloat())
        }
        val dataSet = BarDataSet(entries, "Study Minutes").apply {
            color = Color.parseColor("#9C27B0")
            valueTextColor = Color.WHITE
        }
        binding.studyTimeBarChart.apply {
            this.data = BarData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(data.studyTime.map { it.day })
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            axisLeft.textColor = Color.WHITE
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.textColor = Color.WHITE
            animateY(1000)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
