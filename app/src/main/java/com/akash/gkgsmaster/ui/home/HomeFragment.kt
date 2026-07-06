package com.akash.gkgsmaster.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.data.model.QuizHistory
import com.akash.gkgsmaster.databinding.FragmentHomeBinding
import com.akash.gkgsmaster.utils.Resource
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    
    private lateinit var currentAffairsAdapter: CurrentAffairsAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var notesAdapter: com.akash.gkgsmaster.ui.notes.NotesAdapter
    private lateinit var recentlyReadAdapter: TopicAdapter
    private lateinit var bookmarkedTopicsAdapter: TopicAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerViews()
        setupClickListeners()
        setupObservers()
    }

    private fun setupRecyclerViews() {
        currentAffairsAdapter = CurrentAffairsAdapter { affair ->
            Toast.makeText(context, affair.title, Toast.LENGTH_SHORT).show()
        }
        binding.rvCurrentAffairs.apply {
            adapter = currentAffairsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        categoryAdapter = CategoryAdapter { category ->
            when (category.id) {
                "1" -> findNavController().navigate(R.id.quizSetupFragment)
                "2" -> { // GK Learning
                    val bundle = Bundle().apply {
                        putBoolean("showRecommended", true)
                    }
                    findNavController().navigate(R.id.learnFragment, bundle)
                }
                "4" -> findNavController().navigate(R.id.notesFragment)
                "5" -> findNavController().navigate(R.id.bookletListFragment)
                else -> Toast.makeText(context, "Coming Soon: ${category.title}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvCategories.adapter = categoryAdapter

        notesAdapter = com.akash.gkgsmaster.ui.notes.NotesAdapter(
            onNoteClick = { note ->
                val bundle = Bundle().apply { putString("noteId", note.id) }
                findNavController().navigate(R.id.noteDetailFragment, bundle)
            },
            onPinClick = {},
            onFavouriteClick = {},
            onDeleteClick = {}
        )
        binding.rvRecentNotes.adapter = notesAdapter

        recentlyReadAdapter = TopicAdapter { topic ->
            // Navigate to topic detail
        }
        binding.rvRecentlyRead.adapter = recentlyReadAdapter

        bookmarkedTopicsAdapter = TopicAdapter { topic ->
            // Navigate to topic detail
        }
        binding.rvBookmarkedTopics.adapter = bookmarkedTopicsAdapter
    }

    private fun setupObservers() {
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvUserName.text = getString(R.string.hello_greeting_format, name)
        }

        viewModel.dailyQuote.observe(viewLifecycleOwner) { quote ->
            binding.tvDailyQuote.text = quote
        }

        viewModel.currentAffairs.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (resource.data.isNullOrEmpty()) {
                        binding.layoutShimmer.root.visibility = View.VISIBLE
                        binding.layoutShimmer.shimmerView.startShimmer()
                        binding.scrollView.visibility = View.GONE
                    } else {
                        binding.layoutShimmer.root.visibility = View.GONE
                        binding.scrollView.visibility = View.VISIBLE
                        currentAffairsAdapter.submitList(resource.data)
                    }
                }
                is Resource.Success -> {
                    binding.layoutShimmer.root.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                    currentAffairsAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    binding.layoutShimmer.root.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { list ->
            categoryAdapter.submitList(list)
        }

        viewModel.recentNotes.observe(viewLifecycleOwner) { 
            notesAdapter.submitList(it) 
        }

        viewModel.wordOfTheDay.observe(viewLifecycleOwner) { entry ->
            binding.tvWord.text = entry.word
            binding.tvDefinition.text = entry.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition
        }

        viewModel.recentlyRead.observe(viewLifecycleOwner) { 
            recentlyReadAdapter.submitList(it) 
        }

        viewModel.bookmarkedTopics.observe(viewLifecycleOwner) { 
            bookmarkedTopicsAdapter.submitList(it) 
        }

        viewModel.todayBooklet.observe(viewLifecycleOwner) { booklet ->
            if (booklet != null) {
                binding.cvTodayBooklet.visibility = View.VISIBLE
                binding.tvBookletTitle.text = booklet.title
            } else {
                binding.cvTodayBooklet.visibility = View.GONE
            }
        }

        viewModel.continueLearning.observe(viewLifecycleOwner) { topic ->
            if (topic != null) {
                binding.cvContinueLearning.visibility = View.VISIBLE
                binding.tvLastTopic.text = topic.title
            } else {
                binding.cvContinueLearning.visibility = View.GONE
            }
        }

        viewModel.userProgress.observe(viewLifecycleOwner) { progress ->
            binding.tvXP.text = getString(R.string.total_xp_format, progress.xp)
            binding.tvStreakCount.text = getString(R.string.streak_count_format, progress.dailyStreak)
        }

        viewModel.weeklyProgress.observe(viewLifecycleOwner) { history ->
            setupWeeklyChart(history)
        }
    }

    private fun setupWeeklyChart(history: List<QuizHistory>) {
        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        
        val calendar = Calendar.getInstance()
        for (i in 6 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val dayLabel = sdf.format(calendar.time)
            labels.add(dayLabel)
            
            val dayStart = calendar.clone() as Calendar
            dayStart.set(Calendar.HOUR_OF_DAY, 0)
            dayStart.set(Calendar.MINUTE, 0)
            dayStart.set(Calendar.SECOND, 0)
            dayStart.set(Calendar.MILLISECOND, 0)
            
            val dayEnd = dayStart.timeInMillis + 86400000L
            
            val dayScore = history.filter { it.timestamp in dayStart.timeInMillis until dayEnd }.sumOf { it.score }
            entries.add(BarEntry((6 - i).toFloat(), dayScore.toFloat()))
        }

        val dataSet = BarDataSet(entries, "Daily Score").apply {
            color = ContextCompat.getColor(requireContext(), R.color.primary)
            valueTextColor = Color.WHITE
        }
        
        binding.weeklyBarChart.apply {
            data = BarData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = Color.WHITE
            xAxis.setDrawGridLines(false)
            axisLeft.textColor = Color.WHITE
            axisRight.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
            animateY(1000)
            invalidate()
        }
    }

    private fun setupClickListeners() {
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.cvDailyQuiz.setOnClickListener {
            startDailyChallenge()
        }
        binding.btnStartQuiz.setOnClickListener {
            startDailyChallenge()
        }
        binding.btnReadBooklet.setOnClickListener {
            viewModel.todayBooklet.value?.let { booklet ->
                val bundle = Bundle().apply { putParcelable("booklet", booklet) }
                findNavController().navigate(R.id.bookletReaderFragment, bundle)
            }
        }
        binding.btnResume.setOnClickListener {
            viewModel.continueLearning.value?.let { topic ->
                val bundle = Bundle().apply { putString("topicId", topic.id) }
                findNavController().navigate(R.id.learningDetailFragment, bundle)
            }
        }
    }

    private fun startDailyChallenge() {
        val bundle = Bundle().apply {
            putInt("amount", 10)
            putString("difficulty", "medium")
            putInt("timer", 30)
            putBoolean("isDailyChallenge", true)
        }
        findNavController().navigate(R.id.quizFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
