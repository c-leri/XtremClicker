package uqac.dim.xtremclicker.save

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SaveDao {
    @Insert
    suspend fun insertSave(saveEntity: SaveEntity)

    @Update
    suspend fun updateSave(saveEntity: SaveEntity)

    @Delete
    suspend fun deleteSave(saveEntity: SaveEntity)

    @Query("SELECT * FROM save WHERE id = :id")
    suspend fun getSave(id: Int): SaveEntity?
}