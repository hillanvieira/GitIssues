package br.com.hillan.gitissues.workers

import androidx.work.Worker
import android.content.Intent
import android.content.Context
import br.com.hillan.gitissues.R
import androidx.work.WorkerParameters
import android.app.NotificationManager
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import br.com.hillan.gitissues.ui.MainActivity
import android.content.Context.NOTIFICATION_SERVICE
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import br.com.hillan.dataissues.data.source.DefaultIssueRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import timber.log.Timber

@HiltWorker
class UpdateListWorker @AssistedInject constructor(
    private val defaultIssueRepository: DefaultIssueRepository,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    var sharedpreferences: SharedPreferences =
        applicationContext.getSharedPreferences("gitissues_preferences", Context.MODE_PRIVATE)

    var oldLastIssue: String = ""
    var newLastIssue: String = ""

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            oldLastIssue = sharedpreferences.getString("lastIssueTitle", "no new issues")!!

            defaultIssueRepository.refreshIssues()

            delay(5000)

            //repository.lastIssue.take(1).collect { it -> newLastIssue = it.title }

            val lastIssue = defaultIssueRepository.getLastIssue()
            if (lastIssue.isSuccess) {
                newLastIssue = lastIssue.getOrNull()!!.title
            }

            Timber.i("NEW LAST ISSUE $newLastIssue")
            Timber.i("PREPARING")

            if (oldLastIssue != newLastIssue) {

                sendNotification(newLastIssue)
                Timber.i("NOTIFYING")

            } else {

                Timber.i("NOT_NOTIFY")

            }

            sharedpreferences.edit().putString("lastIssueTitle", newLastIssue).apply()
            Timber.i("LAUNCHED")

        }
        return Result.success()
    }

    private fun sendNotification(contentText: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = (Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //To start an activity
        // val pendingIntent = getActivity(applicationContext, 0, intent, 0)

        //to start an fragment
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.issueListFragment)
            .createPendingIntent()

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