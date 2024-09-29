package uqac.dim.xtremclicker.ui.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uqac.dim.xtremclicker.save.Save
import uqac.dim.xtremclicker.upgrade.Upgrade
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val save: Save
) : ViewModel() {
    private val _upgradePrices: MutableLiveData<Map<Long, Long>> = MutableLiveData(mapOf())

    private val isSaveReadyObserver = Observer<Boolean> { isSaveReady ->
        if (isSaveReady) {
            save.boughtUpgrades.observeForever(boughtUpgradesObserver)

            score = save.score
        }
    }

    private val boughtUpgradesObserver = Observer<Map<Long, Int>> {
        updateUpgradePrices(it)
    }


    init {
        save.isReady.observeForever(isSaveReadyObserver)
    }

    private fun updateUpgradePrices(boughtUpgrades: Map<Long, Int>) {
        _upgradePrices.value = save.upgrades.associate {
            val boughtCount = boughtUpgrades[it.id] ?: 0
            it.id to floor(it.basePrice * (1 + it.priceIncrementFactor * boughtCount)).toLong()
        }
    }

    fun getUpgradesCount(): Int = save.upgrades.count()

    fun getUpgrade(position: Int): Upgrade = save.upgrades[position]

    fun buyUpgrade(id: Long) {
        if (_upgradePrices.value?.get(id) == null) return

        save.buyUpgrade(id, _upgradePrices.value!![id]!!)
    }

    val boughtUpgrades: LiveData<Map<Long, Int>> = save.boughtUpgrades
    val upgradePrices: LiveData<Map<Long, Long>> = _upgradePrices

    lateinit var score: LiveData<Long>

    override fun onCleared() {
        super.onCleared()
        save.isReady.removeObserver(isSaveReadyObserver)
        save.boughtUpgrades.removeObserver(boughtUpgradesObserver)
    }
}