package com.kartikcd.mediumx.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kartikcd.api.models.entities.Author

class Converters {
    @TypeConverter
    fun fromAuthor(author: Author): String {
        val gson = Gson()
        val type = object : TypeToken<Author>() {}.type
        return gson.toJson(author, type)
    }

    @TypeConverter
    fun toAuthor(value: String): Author {
        val gson = Gson()
        val type = object : TypeToken<Author>() {}.type
        return gson.fromJson(value, type)
    }
}