package br.com.hillan.gitissues.services

import android.app.NotificationManager
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.hillan.gitissues.R
import br.com.hillan.gitissues.models.Issue
import br.com.hillan.gitissues.repository.IssueRepository
import br.com.hillan.gitissues.ui.MainActivity
import kotlinx.coroutines.GlobalScope
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class UpdateListWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent{

    val repository: IssueRepository by inject()

    override fun doWork(): Result {

        repository.updateListToDb()

        GlobalScope.launch {
            repository.lastIssue.collect{it ->
                sendNotification(it.title)}
        }

        Log.i("WORK TEST", "DONE")

        return Result.success()
    }

    private fun sendNotification(contentText: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = getActivity(applicationContext, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "GITISS01")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New Issue")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent).setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, notificationBuilder.build())
        }
    }


}