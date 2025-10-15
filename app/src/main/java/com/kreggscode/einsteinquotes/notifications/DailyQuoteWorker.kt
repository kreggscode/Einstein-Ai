package com.kreggscode.einsteinquotes.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kreggscode.einsteinquotes.MainActivity
import com.kreggscode.einsteinquotes.R
import com.kreggscode.einsteinquotes.data.QuoteDatabase
import kotlinx.coroutines.flow.first

class DailyQuoteWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    companion object {
        const val CHANNEL_ID = "daily_quote_channel"
        const val NOTIFICATION_ID = 1
    }
    
    override suspend fun doWork(): Result {
        return try {
            val database = QuoteDatabase.getDatabase(applicationContext)
            val quotes = database.quoteDao().getAllQuotes().first()
            
            if (quotes.isNotEmpty()) {
                val randomQuote = quotes.random()
                showNotification(randomQuote.Quote, randomQuote.Work)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    private fun showNotification(quote: String, work: String) {
        createNotificationChannel()
        
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Daily Quote from Voltaire")
            .setContentText(quote)
            .setStyle(NotificationCompat.BigTextStyle().bigText("$quote\n\n— $work"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Quotes"
            val descriptionText = "Receive daily quotes from Voltaire"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
