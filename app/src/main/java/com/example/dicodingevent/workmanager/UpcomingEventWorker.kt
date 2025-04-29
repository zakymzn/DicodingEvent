package com.example.dicodingevent.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.loopj.android.http.SyncHttpClient
import com.example.dicodingevent.R
import com.example.dicodingevent.data.remote.response.EventListResponse
import com.loopj.android.http.AsyncHttpResponseHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header

class UpcomingEventWorker (
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private var resultStatus: Result? = null
    private var eventId: Int? = null

    override fun doWork(): Result {
        return getUpcomingEventNotification()
    }

    private fun getUpcomingEventNotification(): Result {
        Log.d(TAG, "getUpcomingEventNotification: Start...")
        Looper.prepare()
        val client = SyncHttpClient()
        val url = "https://event-api.dicoding.dev/events?active=$UPCOMING_EVENT&limit=1"
        Log.d(TAG, "getUpcomingEventNotification: $url")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header?>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val moshi = Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()

                    val jsonAdapter = moshi.adapter(EventListResponse::class.java)
                    val response = jsonAdapter.fromJson(result)

                    response?.let {
                        val newestEvent = it.listEvents.firstOrNull()
                        if (newestEvent != null) {
                            if (eventId == null) {
                                eventId = it.listEvents[0].id
                            }
                            val newestEventName = it.listEvents[0].name
                            val newestEventDate = it.listEvents[0].beginTime

                            val title = "Jangan lewatkan event terbaru"
                            val message = "$newestEventName pada $newestEventDate"
                            showNotification(title, message)
                        }
                    }

                    Log.d(TAG, "onSuccess: Selesai...")
                    resultStatus = Result.success()
                } catch (e: Exception) {
                    showNotification("Get Upcoming Event Failed", e.message)
                    Log.d(TAG, "onSuccess: Gagal...")
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header?>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d(TAG, "onFailure: Gagal...")
                showNotification("Get Upcoming Event Failed", error.message)
                resultStatus = Result.failure()
            }
        })
        return resultStatus as Result
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private val TAG = UpcomingEventWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val UPCOMING_EVENT = -1
        const val CHANNEL_ID = "upcoming_event_channel"
        const val CHANNEL_NAME = "Upcoming Event Channel"
    }
}