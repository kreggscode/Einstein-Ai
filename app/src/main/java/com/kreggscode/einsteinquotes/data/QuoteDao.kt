package com.kreggscode.einsteinquotes.data

import androidx.room.*
import com.kreggscode.einsteinquotes.model.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<Quote>>
    
    @Query("SELECT * FROM quotes WHERE isFavorite = 1")
    fun getFavoriteQuotes(): Flow<List<Quote>>
    
    @Query("SELECT * FROM quotes WHERE Category = :category")
    fun getQuotesByCategory(category: String): Flow<List<Quote>>
    
    @Query("SELECT * FROM quotes WHERE Work = :work")
    fun getQuotesByWork(work: String): Flow<List<Quote>>
    
    @Query("SELECT * FROM quotes WHERE id = :id")
    suspend fun getQuoteById(id: Int): Quote?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<Quote>)
    
    @Update
    suspend fun updateQuote(quote: Quote)
    
    @Query("UPDATE quotes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
    
    @Delete
    suspend fun deleteQuote(quote: Quote)
    
    @Query("DELETE FROM quotes")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM quotes")
    suspend fun getQuoteCount(): Int
}
