package uz.mobiler.lesson58_1.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Gender(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
