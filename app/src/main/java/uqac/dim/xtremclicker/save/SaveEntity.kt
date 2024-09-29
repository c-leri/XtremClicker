package uqac.dim.xtremclicker.save

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "save")
data class SaveEntity(
    @PrimaryKey
    val id: Int,
    var score: Long,
    var musicEnabled: Boolean,
    var soundsEnabled: Boolean
)