package br.com.hillan.gitissues.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import br.com.hillan.gitissues.models.Issue
import kotlinx.coroutines.flow.Flow

@Dao
interface IssueDao {
    @Query("SELECT * FROM issue")
    fun getAll(): Flow<List<Issue>>

//    @Query("SELECT * FROM issue WHERE id IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<Issue>

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