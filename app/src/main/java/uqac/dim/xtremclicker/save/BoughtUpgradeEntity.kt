package uqac.dim.xtremclicker.save

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bought_upgrades")
data class BoughtUpgradeEntity(
    @PrimaryKey
    val id: Long,
    var count: Int
)