package uqac.dim.xtremclicker.ui.clicker

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uqac.dim.xtremclicker.R
import uqac.dim.xtremclicker.databinding.FragmentClickerBinding
import java.text.NumberFormat

@AndroidEntryPoint
class ClickerFragment : Fragment() {
    private var _binding: FragmentClickerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val numberFormat = NumberFormat.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val clickerViewModel: ClickerViewModel by viewModels()

        _binding = FragmentClickerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        clickerViewModel.isSaveReady.observe(viewLifecycleOwner) { isSaveReady: Boolean ->
            toggleProgressBar(!isSaveReady)

            if (isSaveReady) {
                clickerViewModel.score.observe(viewLifecycleOwner) { score ->
                    binding.score.text =
                        getString(R.string.clicker_score).format(numberFormat.format(score))
                }
            }
        }

        clickerViewModel.musicEnabled.observe(viewLifecycleOwner) { musicEnabled ->
            if (musicEnabled) {
                binding.musicEnabledButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.music,
                        null
                    )
                )
            } else {
                binding.musicEnabledButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.no_music,
                        null
                    )
                )
            }
        }

        clickerViewModel.soundsEnabled.observe(viewLifecycleOwner) { soundsEnabled ->
            if (soundsEnabled) {
                binding.soundsEnabledButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.sounds,
                        null
                    )
                )
            } else {
                binding.soundsEnabledButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.no_sounds,
                        null
                    )
                )
            }
        }

        binding.musicEnabledButton.setOnClickListener {
            clickerViewModel.toggleMusicEnabled()
        }

        binding.soundsEnabledButton.setOnClickListener {
            clickerViewModel.toggleSoundsEnabled()
        }

        binding.button.setOnClickListener {
            clickerViewModel.incrementScore()

            clickerViewModel.playClickSound()

            AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(binding.score, "scaleX", 1.2f, 1f).apply {
                    duration = 200
                }).with(
                    ObjectAnimator.ofFloat(binding.score, "scaleY", 1.2f, 1f).apply {
                        duration = 200
                    }).with(
                    ObjectAnimator.ofFloat(binding.score, "rotation", 5f, -2f, 0f)
                        .apply {
                            duration = 200
                        })
                start()
            }
        }

        return root
    }

    private fun toggleProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingIndicator.visibility = View.VISIBLE

            binding.score.visibility = View.GONE
            binding.button.visibility = View.GONE
        } else {
            binding.loadingIndicator.visibility = View.GONE

            binding.score.visibility = View.VISIBLE
            binding.button.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}