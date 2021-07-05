package br.com.hillan.gitissues.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.hillan.gitissues.data.models.Issue

private const val DATABASE_NAME = "issues.db"

@Database(entities = [Issue::class], version = 1)
@TypeConverters(GitIssuesTypeConverters::class)
abstract class GitIssuesDatabase : RoomDatabase() {

    abstract fun issueDao(): IssueDao

}

