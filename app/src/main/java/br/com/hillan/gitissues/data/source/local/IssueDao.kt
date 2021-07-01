package br.com.hillan.gitissues.data.source.local

import androidx.room.*
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import br.com.hillan.gitissues.data.models.Issue
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface IssueDao {
    @Query("SELECT * FROM issue")
    fun getAll(): Flow<List<Issue>>

//    @Query("SELECT * FROM issue WHERE id IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<Issue>

    @Query("SELECT * FROM issue WHERE   id = (SELECT MAX(id)  FROM issue)  LIMIT 1")
    fun getLastIssue(): Flow<Issue>

    @Query("SELECT * FROM issue WHERE   id = (SELECT MAX(id)  FROM issue)  LIMIT 1")
    suspend fun getLastIssue2(): Issue

    @Query("SELECT * FROM issue WHERE id LIKE :id  LIMIT 1")
    fun findById(id: Long): LiveData<Issue>

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg issues: Issue)

    @Insert(onConflict = REPLACE)
    fun insertList(issues: List<Issue>)

    @Delete
    fun delete(issue: Issue)

    @Update
    fun update(issue: Issue)
}