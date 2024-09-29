package uqac.dim.xtremclicker.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uqac.dim.xtremclicker.R
import uqac.dim.xtremclicker.databinding.FragmentStoreBinding
import uqac.dim.xtremclicker.databinding.StoreItemBinding
import uqac.dim.xtremclicker.upgrade.Upgrade
import java.text.NumberFormat

@AndroidEntryPoint
class StoreFragment : Fragment() {
    private var _binding: FragmentStoreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val numberFormat = NumberFormat.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val storeViewModel: StoreViewModel by viewModels()

        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        storeViewModel.score.observe(viewLifecycleOwner) { score ->
            binding.score.text = getString(R.string.store_score).format(numberFormat.format(score))
        }

        binding.upgrades.adapter = object : BaseAdapter() {
            override fun getCount(): Int = storeViewModel.getUpgradesCount()

            override fun getItem(position: Int): Upgrade = storeViewModel.getUpgrade(position)

            override fun getItemId(position: Int): Long = storeViewModel.getUpgrade(position).id

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val binding: StoreItemBinding =
                    if (convertView != null) StoreItemBinding.bind(convertView)
                    else StoreItemBinding.inflate(inflater, parent, false)

                val upgrade = getItem(position)

                binding.name.text = upgrade.name
                binding.description.text = upgrade.description
                binding.icon.setImageDrawable(upgrade.icon)

                storeViewModel.boughtUpgrades.observe(viewLifecycleOwner) {
                    binding.owned.text = getString(R.string.store_owned).format(
                        numberFormat.format(
                            it[upgrade.id] ?: 0
                        )
                    )
                }

                storeViewModel.upgradePrices.observe(viewLifecycleOwner) {
                    binding.price.text =
                        getString(R.string.store_price).format(numberFormat.format(it[upgrade.id]))
                }

                return binding.root
            }
        }

        binding.upgrades.setOnItemClickListener { _: AdapterView<*>, _: View, _: Int, id: Long ->
            storeViewModel.buyUpgrade(id)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}