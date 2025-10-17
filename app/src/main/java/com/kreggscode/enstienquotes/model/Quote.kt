package com.kreggscode.enstienquotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Serializable(with = NumberSerializer::class)
    val Number: Int? = null,
    val Category: String = "",
    val Quote: String = "",
    val Work: String = "",
    @Serializable(with = YearSerializer::class)
    val Year: String = "Unknown", // Can handle both Int and String from JSON
    val Tags: String = "",
    val Context: String = "",
    val Popularity: String = "",
    val Reference: String = "",
    val French: String? = null,
    val Bio: String? = null,
    val isFavorite: Boolean = false
)

// Custom serializer to handle both Int and String for Number field (skip header rows)
object NumberSerializer : KSerializer<Int?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Number", PrimitiveKind.INT)
    
    override fun serialize(encoder: Encoder, value: Int?) {
        if (value != null) {
            encoder.encodeInt(value)
        } else {
            encoder.encodeNull()
        }
    }
    
    override fun deserialize(decoder: Decoder): Int? {
        return try {
            decoder.decodeInt()
        } catch (e: Exception) {
            // If it's a string (like "Number" header), return null
            try {
                decoder.decodeString()
                null
            } catch (e: Exception) {
                null
            }
        }
    }
}

// Custom serializer to handle both Int and String for Year field
object YearSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Year", PrimitiveKind.STRING)
    
    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }
    
    override fun deserialize(decoder: Decoder): String {
        return try {
            // Try to decode as Int first
            decoder.decodeInt().toString()
        } catch (e: Exception) {
            // If that fails, decode as String
            try {
                decoder.decodeString()
            } catch (e: Exception) {
                "Unknown"
            }
        }
    }
}

// Extension function to merge and clean quote data
fun Quote.toMergedQuote(): Quote {
    return this.copy(
        Quote = this.Quote.trim(),
        Work = this.Work.trim(),
        Category = this.Category.trim(),
        Tags = this.Tags.trim(),
        Context = this.Context.trim(),
        Popularity = this.Popularity.trim(),
        Reference = this.Reference.trim(),
        French = this.French?.trim(),
        Bio = this.Bio?.trim()
    )
}
