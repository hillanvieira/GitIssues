package br.com.hillan.dataissues.data.source.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.hillan.dataissues.data.Issue

const val DATABASE_NAME = "issues.db"

@Database(entities = [Issue::class], version = 1)
@TypeConverters(GitIssuesTypeConverters::class)
abstract class GitIssuesDatabase : RoomDatabase() {

    abstract fun issueDao(): IssueDao

}

