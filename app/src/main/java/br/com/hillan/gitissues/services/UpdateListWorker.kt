package br.com.hillan.gitissues.services

import android.app.NotificationManager
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.hillan.gitissues.R
import br.com.hillan.gitissues.repository.IssueRepository
import br.com.hillan.gitissues.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class UpdateListWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams), KoinComponent {

    val repository: IssueRepository by inject()

    var sharedpreferences: SharedPreferences =
        applicationContext.getSharedPreferences("gitissues_preferences", Context.MODE_PRIVATE)

    var oldLastIssue: String = ""
    var newLastIssue: String = ""

    override fun doWork(): Result {

        oldLastIssue = sharedpreferences.getString("lastIssueTitle", "no new issues")!!

        GlobalScope.launch(Dispatchers.IO) {
            repository.updateListToDb()

            delay(5000)
            //repository.lastIssue.take(1).collect { it -> newLastIssue = it.title }
            Log.i("WORK_NOTIFICATION", repository.getLast().title)
            Log.i("WORK_NOTIFICATION", "PREPARING")
            if (oldLastIssue != newLastIssue) {
                sendNotification(newLastIssue)
                Log.i("WORK_NOTIFICATION", "NOTIFYING")
            }else{
                Log.i("WORK_NOTIFICATION", "NOT_NOTIFY")
            }
            sharedpreferences.edit().putString("lastIssueTitle", newLastIssue).apply()
        }
        Log.i("WORK_", "LAUNCHED")

        return Result.success()
    }

    private fun sendNotification(contentText: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_SINGLE_TOP)

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