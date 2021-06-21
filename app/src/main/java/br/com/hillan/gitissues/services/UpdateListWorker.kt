package br.com.hillan.gitissues.services

import android.app.NotificationManager
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.hillan.gitissues.R
import br.com.hillan.gitissues.repository.IssueRepository
import br.com.hillan.gitissues.ui.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class UpdateListWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {

    val repository: IssueRepository by inject()

    var sharedpreferences: SharedPreferences =
        applicationContext.getSharedPreferences("gitissues_preferences", Context.MODE_PRIVATE)

    lateinit var oldLastIssue: String

    lateinit var newLastIssue: String

    override fun doWork(): Result {

        repository.updateListToDb()
        oldLastIssue = sharedpreferences.getString("lastIssueTitle", "no new issues")!!

        GlobalScope.launch {
            delay(5000)

            repository.lastIssue.collect { it ->
                newLastIssue = it.title
                sharedpreferences.edit().putString("lastIssueTitle", newLastIssue).commit()
                if (oldLastIssue != newLastIssue) {
                    sendNotification(it.title)
                }
            }
        }

        Log.i("WORK_NOTIFICATION", "DONE")
        return Result.success()
    }

    private fun sendNotification(contentText: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = getActivity(applicationContext, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "GITISS01")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New Issue")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())

//        with(NotificationManagerCompat.from(applicationContext)) {
//            // notificationId is a unique int for each notification that you must define
//            notify(1, notificationBuilder.build())
//        }
    }

}