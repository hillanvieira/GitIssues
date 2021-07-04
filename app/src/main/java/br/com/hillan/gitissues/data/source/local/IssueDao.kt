package br.com.hillan.gitissues.data.source.local

import androidx.room.*
import androidx.lifecycle.LiveData
import br.com.hillan.gitissues.data.models.Issue
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface IssueDao {

    @Query("SELECT * FROM issue")
    fun observeIssues(): LiveData<List<Issue>>

    @Query("SELECT * FROM issue WHERE id LIKE :id  LIMIT 1")
    fun observeIssueById(id: Long): LiveData<Issue>

    @Query("SELECT * FROM issue WHERE   id = (SELECT MAX(id)  FROM issue)  LIMIT 1")
    fun observeLastIssue(): LiveData<Issue>

    @Query("SELECT * FROM issue")
    suspend fun getIssues(): List<Issue>

    @Query("SELECT * FROM issue WHERE id LIKE :id  LIMIT 1")
    suspend fun getIssueById(id: Long): Issue

    @Query("SELECT * FROM issue WHERE   id = (SELECT MAX(id)  FROM issue)  LIMIT 1")
    suspend fun getLastIssue(): Issue

    @Insert(onConflict = REPLACE)
    suspend fun insertIssue(issues: Issue)

    @Insert(onConflict = REPLACE)
    suspend fun insertIssues(vararg issues: Issue)

    @Insert(onConflict = REPLACE)
    suspend fun insertListIssues(issues: List<Issue>)

    @Update
    suspend fun updateIssue(issue: Issue)

    @Query("DELETE FROM issue WHERE id LIKE :id")
    suspend fun deleteIssueById(id: Long)

    @Query("DELETE FROM issue")
    suspend fun deleteIssues()

    @Delete
    suspend fun delete(issue: Issue)


    //Find by array of id
//    @Query("SELECT * FROM issue WHERE id IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<Issue>
}
