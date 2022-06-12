package uz.mobiler.lesson58_1.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Region::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("region_id")
    ),
        ForeignKey(
            entity = Gender::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("gender_id")
        )]
)
data class Passport(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "middle_name")
    val middleName: String,
    @ColumnInfo(name = "region_id")
    val regionId: Int,
    val city: String,
    val address: String,
    val date: String,
    @ColumnInfo(name = "life_time")
    val lifeTime: String,
    @ColumnInfo(name = "gender_id")
    val genderId: Int,
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    @ColumnInfo(name = "series_letter")
    val seriesLetter: String,
    @ColumnInfo(name = "series_number")
    val seriesNumber: Int
)
