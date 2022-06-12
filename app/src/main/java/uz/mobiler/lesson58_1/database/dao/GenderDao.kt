package uz.mobiler.lesson58_1.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.mobiler.lesson58_1.database.entity.Gender

@Dao
interface GenderDao {

    @Insert
    fun addGender(gender: Gender)

    @Query("select*from gender")
    fun getGenders(): List<Gender>

    @Query("select*from gender where id=:id")
    fun getGenderById(id: Int): Gender
}