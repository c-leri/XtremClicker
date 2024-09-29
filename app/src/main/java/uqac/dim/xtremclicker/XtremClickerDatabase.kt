package uqac.dim.xtremclicker

import androidx.room.Database
import androidx.room.RoomDatabase
import uqac.dim.xtremclicker.save.BoughtUpgradeDao
import uqac.dim.xtremclicker.save.BoughtUpgradeEntity
import uqac.dim.xtremclicker.save.SaveDao
import uqac.dim.xtremclicker.save.SaveEntity

@Database(
    entities = [SaveEntity::class, BoughtUpgradeEntity::class],
    version = 1
)
abstract class XtremClickerDatabase : RoomDatabase() {
    abstract fun getSaveDao(): SaveDao
    abstract fun getBoughtUpgradeDao(): BoughtUpgradeDao
}