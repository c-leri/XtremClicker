package uqac.dim.xtremclicker.save

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BoughtUpgradeDao {
    @Query("SELECT * FROM bought_upgrades")
    suspend fun getBoughtUpgrades(): List<BoughtUpgradeEntity>

    @Upsert
    suspend fun upsertBoughtUpgrades(boughtUpgrades: List<BoughtUpgradeEntity>)
}