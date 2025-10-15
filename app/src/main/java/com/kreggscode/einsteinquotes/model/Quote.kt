package com.kreggscode.einsteinquotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val Number: Int? = null,
    val Category: String,
    val Quote: String,
    val Work: String,
    val Year: String, // Changed to String to handle "Unknown" values
    val Tags: String,
    val Context: String,
    val Popularity: String,
    val Reference: String,
    val isFavorite: Boolean = false
)
