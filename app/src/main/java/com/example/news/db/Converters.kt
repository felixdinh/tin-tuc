package com.example.news.db

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun  listToJson(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String> {
        return Gson().fromJson(value, Array<String>::class.java).toList()
    }

}