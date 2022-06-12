package uz.mobiler.lesson58_1.database.dao

import androidx.room.*
import uz.mobiler.lesson58_1.database.entity.Passport

@Dao
interface PassportDao {

    @Insert
    fun addPassport(passport: Passport)

    @Update
    fun editPassport(passport: Passport)

    @Delete
    fun deletePassport(passport: Passport)

    @Query("select *from passport")
    fun getPassports(): List<Passport>

    @Query("select*from passport where id=:id")
    fun getPassportById(id: Int): Passport

}