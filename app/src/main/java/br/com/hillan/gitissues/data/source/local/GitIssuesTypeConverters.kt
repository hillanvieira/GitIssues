package br.com.hillan.gitissues.data.source.local

import java.util.*
import com.google.gson.Gson
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import br.com.hillan.gitissues.data.models.User
import br.com.hillan.gitissues.data.State

class GitIssuesTypeConverters {

    @TypeConverter
    fun fromUserToJson(user: User): String {
        return Gson().toJson(user)
    }

    @TypeConverter
    fun fromJsonToUser(user: String): User {
        val type = object : TypeToken<User>() {}.type
        return Gson().fromJson<User>(user, type)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun toState(value: String) = enumValueOf<State>(value)

    @TypeConverter
    fun fromState(value: State) = value.name

}