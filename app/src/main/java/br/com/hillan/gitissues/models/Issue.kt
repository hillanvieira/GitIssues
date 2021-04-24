package br.com.hillan.gitissues.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.hillan.gitissues.IssueState
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Issue(
    @PrimaryKey
    val id: Long?,

    @ColumnInfo
    val title: String,

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    val createdAt: Date,

    @ColumnInfo
    val body: String,

    @ColumnInfo
    val state: String,

    @SerializedName("html_url")
    @ColumnInfo(name = "html_url")
    val htmlUrl: String,

    @ColumnInfo
    val user: User
)

data class User(
    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String
    )