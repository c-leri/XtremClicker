package uqac.dim.xtremclicker.ui.clicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uqac.dim.xtremclicker.save.Save
import uqac.dim.xtremclicker.sound.SoundsManager
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class ClickerViewModel @Inject constructor(
    private val save: Save,
    private val soundsManager: SoundsManager
) : ViewModel() {
    private var totalFlatBonus = 0L
    private var totalFactorBonus = 1.0

    private val isSaveReadyObserver = Observer<Boolean> { isSaveReady ->
        if (isSaveReady) {
            save.boughtUpgrades.observeForever(boughtUpgradesObserver)

            score = save.score
        }
    }

    private val boughtUpgradesObserver = Observer<Map<Long, Int>> { boughtUpgrades ->
        reloadTotalFlatBonus(boughtUpgrades)
        reloadTotalFactorBonus(boughtUpgrades)
    }

    init {
        save.isReady.observeForever(isSaveReadyObserver)
    }

    private fun reloadTotalFlatBonus(boughtUpgrades: Map<Long, Int>) {
        var result = 0L

        for (boughtUpgrade in boughtUpgrades) {
            val upgrade = save.upgrades.find { it.id == boughtUpgrade.key } ?: continue

            result += upgrade.flatBonus * boughtUpgrade.value
        }

        totalFlatBonus = result
    }

    private fun reloadTotalFactorBonus(boughtUpgrades: Map<Long, Int>) {
        var result = 1.0

        for (boughtUpgrade in boughtUpgrades) {
            val upgrade = save.upgrades.find { it.id == boughtUpgrade.key } ?: continue

            result += upgrade.factorBonus * boughtUpgrade.value
        }

        totalFactorBonus = result
    }

    fun toggleMusicEnabled() = save.toggleMusicEnabled()
    fun toggleSoundsEnabled() = save.toggleSoundsEnabled()

    fun incrementScore() {
        save.incrementScore(ceil((1 + totalFlatBonus) * totalFactorBonus).toLong())
    }

    fun playClickSound() {
        if (save.soundsEnabled.value != true) return

        // Plays a random sound 1 click out of 8
        if ((0..<8).random() == 0) soundsManager.playRandomClickSound()
    }

    val isSaveReady = save.isReady
    val musicEnabled = save.musicEnabled
    val soundsEnabled = save.soundsEnabled

    lateinit var score: LiveData<Long>

    override fun onCleared() {
        super.onCleared()
        save.isReady.removeObserver(isSaveReadyObserver)
        save.boughtUpgrades.removeObserver(boughtUpgradesObserver)
    }
}