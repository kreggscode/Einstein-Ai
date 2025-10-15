package com.kreggscode.einsteinquotes.data

import android.content.Context
import com.kreggscode.einsteinquotes.model.Quote
import com.kreggscode.einsteinquotes.model.toMergedQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import java.io.IOException

class QuoteRepository(private val quoteDao: QuoteDao, private val context: Context) {
    
    val allQuotes: Flow<List<Quote>> = quoteDao.getAllQuotes()
    val favoriteQuotes: Flow<List<Quote>> = quoteDao.getFavoriteQuotes()
    
    private val json = Json { 
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    suspend fun loadQuotesFromAssets() {
        try {
            // Check if database already has quotes
            val existingQuotes = quoteDao.getQuoteCount()
            if (existingQuotes > 0) {
                android.util.Log.d("QuoteRepository", "Database already has $existingQuotes quotes, skipping load")
                return
            }
            
            android.util.Log.d("QuoteRepository", "Loading quotes from assets...")
            
            // Load dataset1.json
            val dataset1Json = context.assets.open("dataset1.json").bufferedReader().use { it.readText() }
            android.util.Log.d("QuoteRepository", "Dataset1 loaded, size: ${dataset1Json.length}")
            val quotes1 = json.decodeFromString<List<Quote>>(dataset1Json)
            android.util.Log.d("QuoteRepository", "Dataset1 parsed: ${quotes1.size} quotes")
            
            // Load dataset2.json
            val dataset2Json = context.assets.open("dataset2.json").bufferedReader().use { it.readText() }
            android.util.Log.d("QuoteRepository", "Dataset2 loaded, size: ${dataset2Json.length}")
            val quotes2 = json.decodeFromString<List<Quote>>(dataset2Json)
            android.util.Log.d("QuoteRepository", "Dataset2 parsed: ${quotes2.size} quotes")
            
            // Merge and clean data
            val allQuotes = (quotes1 + quotes2).map { it.toMergedQuote() }
            android.util.Log.d("QuoteRepository", "Total quotes to insert: ${allQuotes.size}")
            
            // Insert into database
            quoteDao.insertAll(allQuotes)
            android.util.Log.d("QuoteRepository", "Quotes inserted successfully!")
        } catch (e: IOException) {
            android.util.Log.e("QuoteRepository", "IOException loading quotes", e)
            e.printStackTrace()
        } catch (e: Exception) {
            android.util.Log.e("QuoteRepository", "Exception loading quotes", e)
            e.printStackTrace()
        }
    }
    
    fun getQuotesByCategory(category: String): Flow<List<Quote>> {
        return quoteDao.getQuotesByCategory(category)
    }
    
    fun getQuotesByWork(work: String): Flow<List<Quote>> {
        return quoteDao.getQuotesByWork(work)
    }
    
    suspend fun getQuoteById(id: Int): Quote? {
        return quoteDao.getQuoteById(id)
    }
    
    suspend fun toggleFavorite(quote: Quote) {
        quoteDao.updateFavoriteStatus(quote.id, !quote.isFavorite)
    }
    
    suspend fun updateQuote(quote: Quote) {
        quoteDao.updateQuote(quote)
    }
}
