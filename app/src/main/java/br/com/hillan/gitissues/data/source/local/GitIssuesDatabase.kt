package br.com.hillan.gitissues.data.source.local

import androidx.room.Room
import androidx.room.Database
import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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

        fun getInstance(context: Context): GitIssuesDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    GitIssuesDatabase::class.java,
                    DATABASE_NAME
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }

}

