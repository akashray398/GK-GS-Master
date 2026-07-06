package com.akash.gkgsmaster.data.database

import androidx.room.TypeConverter
import com.akash.gkgsmaster.data.model.BookletType
import com.akash.gkgsmaster.data.model.Chapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromChapterList(value: List<Chapter>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toChapterList(value: String): List<Chapter> {
        val listType = object : TypeToken<List<Chapter>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromBookletType(value: BookletType): String {
        return value.name
    }

    @TypeConverter
    fun toBookletType(value: String): BookletType {
        return try {
            BookletType.valueOf(value)
        } catch (e: Exception) {
            BookletType.ORIGINAL_BOOKLET
        }
    }
}
