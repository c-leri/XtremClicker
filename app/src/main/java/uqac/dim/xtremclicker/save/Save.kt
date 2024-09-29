package uqac.dim.xtremclicker.save

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uqac.dim.xtremclicker.upgrade.UpgradeParser
import java.util.Timer
import java.util.TimerTask

class Save(
    application: Application,
    private val saveDao: SaveDao,
    private val boughtUpgradeDao: BoughtUpgradeDao
) {
    private companion object {
        const val SAVE_ID = 1
        const val BASE_SCORE = 0L
        const val BASE_MUSIC_ENABLED = true
        const val BASE_SOUNDS_ENABLED = true
    }

    private lateinit var entity: SaveEntity

    private val _isReady = MutableLiveData(false)
    private val _musicEnabled = MutableLiveData(true)
    private val _soundsEnabled = MutableLiveData(true)

    private lateinit var _score: MutableLiveData<Long>
    private lateinit var _boughtUpgrades: MutableLiveData<Map<Long, Int>>

    init {
        CoroutineScope(Dispatchers.Main).launch {
            loadSave()
        }
        // Save every minute
        Timer().scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    writeSave()
                }
            }, 60000, 60000
        )
    }

    private suspend fun loadSave() {
        var save = saveDao.getSave(SAVE_ID)

        if (save == null) {
            save = SaveEntity(SAVE_ID, BASE_SCORE, BASE_MUSIC_ENABLED, BASE_SOUNDS_ENABLED)
            saveDao.insertSave(save)
        }

        entity = save

        _musicEnabled.value = entity.musicEnabled
        _soundsEnabled.value = entity.soundsEnabled

        _score = MutableLiveData(entity.score)
        score = _score

        _boughtUpgrades = MutableLiveData(
            boughtUpgradeDao.getBoughtUpgrades()
                .associateBy({ it.id }, { it.count })
        )
        boughtUpgrades = _boughtUpgrades

        _isReady.value = true
    }

    fun writeSave() {
        if (_isReady.value == true) {
            CoroutineScope(Dispatchers.IO).launch {
                entity.score = _score.value!!
                entity.musicEnabled = _musicEnabled.value!!
                entity.soundsEnabled = _soundsEnabled.value!!
                saveDao.updateSave(entity)
                boughtUpgradeDao.upsertBoughtUpgrades(_boughtUpgrades.value!!.map {
                    BoughtUpgradeEntity(
                        it.key,
                        it.value
                    )
                })
            }
        }
    }

    fun incrementScore(increment: Long) {
        if (_score.value != null) _score.value = _score.value!! + increment
    }

    fun toggleMusicEnabled() {
        _musicEnabled.value = _musicEnabled.value != true
    }

    fun toggleSoundsEnabled() {
        _soundsEnabled.value = _soundsEnabled.value != true
    }

    fun buyUpgrade(id: Long, price: Long) {
        if (_boughtUpgrades.value == null || _score.value == null || _score.value!! < price) return

        _score.value = _score.value!! - price

        val mutableBoughtUpgrades = _boughtUpgrades.value!!.toMutableMap()

        var count = 1

        if (mutableBoughtUpgrades.containsKey(id)) {
            count += mutableBoughtUpgrades[id]!!
        }

        mutableBoughtUpgrades[id] = count

        _boughtUpgrades.value = mutableBoughtUpgrades
    }

    val isReady: LiveData<Boolean> = _isReady
    val musicEnabled: LiveData<Boolean> = _musicEnabled
    val soundsEnabled: LiveData<Boolean> = _soundsEnabled
    val upgrades = UpgradeParser.getUpgrades(application.resources)

    lateinit var score: LiveData<Long>
    lateinit var boughtUpgrades: LiveData<Map<Long, Int>>
}