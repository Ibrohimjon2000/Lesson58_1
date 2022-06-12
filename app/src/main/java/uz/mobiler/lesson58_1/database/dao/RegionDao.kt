package uz.mobiler.lesson58_1.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.mobiler.lesson58_1.database.entity.Region

@Dao
interface RegionDao {

    @Insert
    fun addRegion(region: Region)

    @Query("select*from region")
    fun getRegions(): List<Region>

    @Query("select*from region where id=:id")
    fun getRegionById(id: Int): Region

}