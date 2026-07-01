package com.akash.gkgsmaster.data.database

import androidx.room.TypeConverter
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
}
