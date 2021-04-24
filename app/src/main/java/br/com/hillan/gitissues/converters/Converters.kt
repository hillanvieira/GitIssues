package br.com.hillan.gitissues.converters

import androidx.room.TypeConverter
import br.com.hillan.gitissues.IssueState

import br.com.hillan.gitissues.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {

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
    fun toIssueState(value: String) = enumValueOf<IssueState>(value)

    @TypeConverter
    fun fromIssueState(value: IssueState) = value.name

}