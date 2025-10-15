package com.kreggscode.einsteinquotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kreggscode.einsteinquotes.model.Quote

@Database(entities = [Quote::class], version = 1, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
    
    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null
        
        fun getDatabase(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "voltaire_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
