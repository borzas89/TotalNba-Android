package example.com.totalnba.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import example.com.totalnba.databinding.FragmentPlayerSearchBinding

@AndroidEntryPoint
class PlayerSearchFragment: Fragment() {

    private var _binding: FragmentPlayerSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerSearchViewModel by viewModels()

    lateinit var adapter: PlayerStatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayerSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.run {
            viewModel = this@PlayerSearchFragment.viewModel
        }

        val dividerItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.predictionRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.adapter = PlayerStatAdapter(listOf())

        return binding.root
    }

}