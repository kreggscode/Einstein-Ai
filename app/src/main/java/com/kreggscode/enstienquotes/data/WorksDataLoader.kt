package com.kreggscode.enstienquotes.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Data models matching JSON structure
data class MajorWork(
    val id: String,
    val title: String,
    val subtitle: String,
    val year: String,
    val type: String,
    val icon: String,
    val summary: String,
    val keyEquation: String? = null,
    val keyEquationExplanation: String? = null,
    val sections: List<Section> = emptyList(),
    val funFacts: List<String> = emptyList(),
    val equations: List<EquationDetail> = emptyList()
)

data class Section(
    val title: String,
    val content: String
)

data class EquationDetail(
    val formula: String,
    val name: String,
    val explanation: String
)

data class Essay(
    val id: String,
    val title: String,
    val year: String,
    val publication: String,
    val icon: String,
    val summary: String,
    val openingQuote: String,
    val mainThemes: List<String>,
    val keyPoints: List<KeyPoint>,
    val relevanceToday: List<String> = emptyList(),
    val closingThought: String? = null
)

data class KeyPoint(
    val title: String,
    val content: String
)

data class Letter(
    val id: String,
    val title: String,
    val date: String,
    val recipient: String,
    val location: String,
    val icon: String,
    val summary: String,
    val historicalContext: String,
    val letterText: String,
    val keyPoints: List<KeyPoint>,
    val legacy: String? = null
)

data class Paper(
    val id: String,
    val title: String,
    val year: String,
    val journal: String,
    val date: String? = null,
    val icon: String,
    val summary: String,
    val context: String,
    val abstract: String? = null,
    val keyEquations: List<String>? = null,
    val predictions: List<Prediction>? = null,
    val papers: List<SubPaper>? = null,
    val legacy: String? = null
)

data class Prediction(
    val prediction: String,
    val description: String,
    val confirmed: String,
    val impact: String
)

data class SubPaper(
    val number: Int,
    val title: String,
    val date: String,
    val topic: String,
    val pages: String,
    val abstract: String,
    val keyEquation: String? = null,
    val keyEquations: List<String>? = null,
    val impact: String,
    val nobelPrize: String? = null
)

class WorksDataLoader(private val context: Context) {
    private val gson = Gson()
    
    suspend fun loadMajorWorks(): List<MajorWork> = withContext(Dispatchers.IO) {
        try {
            val json = context.assets.open("einstein_major_works.json")
                .bufferedReader()
                .use { it.readText() }
            
            val type = object : TypeToken<List<MajorWork>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun loadEssays(): List<Essay> = withContext(Dispatchers.IO) {
        try {
            val json = context.assets.open("einstein_essays.json")
                .bufferedReader()
                .use { it.readText() }
            
            val type = object : TypeToken<List<Essay>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun loadLetters(): List<Letter> = withContext(Dispatchers.IO) {
        try {
            val json = context.assets.open("einstein_letters.json")
                .bufferedReader()
                .use { it.readText() }
            
            val type = object : TypeToken<List<Letter>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun loadPapers(): List<Paper> = withContext(Dispatchers.IO) {
        try {
            val json = context.assets.open("einstein_papers.json")
                .bufferedReader()
                .use { it.readText() }
            
            val type = object : TypeToken<List<Paper>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun getMajorWorkById(id: String): MajorWork? {
        return loadMajorWorks().find { it.id == id }
    }
    
    suspend fun getEssayById(id: String): Essay? {
        return loadEssays().find { it.id == id }
    }
    
    suspend fun getLetterById(id: String): Letter? {
        return loadLetters().find { it.id == id }
    }
    
    suspend fun getPaperById(id: String): Paper? {
        return loadPapers().find { it.id == id }
    }
}
