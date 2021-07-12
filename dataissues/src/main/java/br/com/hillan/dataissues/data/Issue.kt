package br.com.hillan.dataissues.data

import java.util.*
import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Issue(
    @PrimaryKey
    val id: Long?,

    @SerializedName("title")
    @ColumnInfo
    val title: String,

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    val createdAt: Date,

    @SerializedName("body")
    @ColumnInfo
    val body: String,

    @SerializedName("state")
    @ColumnInfo
    val state: String,

    @SerializedName("html_url")
    @ColumnInfo(name = "html_url")
    val htmlUrl: String,

    @SerializedName("user")
    @ColumnInfo
    val user: User
)

data class User(
    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String
)