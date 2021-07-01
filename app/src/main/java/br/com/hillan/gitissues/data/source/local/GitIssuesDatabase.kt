package br.com.hillan.gitissues.data.source.local

import androidx.room.Room
import androidx.room.Database
import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.util.concurrent.Executors
import br.com.hillan.gitissues.data.models.Issue
import br.com.hillan.gitissues.data.converters.Converters

private const val DATABASE_NAME = "issues.db"

@Database(entities = [Issue::class], version = 1)
@TypeConverters(Converters::class)
abstract class GitIssuesDatabase : RoomDatabase() {
    abstract fun issueDao(): IssueDao

    companion object {
        @Volatile
        private var INSTANCE: GitIssuesDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)


        fun getInstance(context: Context): GitIssuesDatabase? {
            if (INSTANCE == null) {
                synchronized(GitIssuesDatabase::class.java) {
                    if (INSTANCE == null) {

                        INSTANCE = Room.databaseBuilder(
                            context,
                            GitIssuesDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE
        }

    }
}

