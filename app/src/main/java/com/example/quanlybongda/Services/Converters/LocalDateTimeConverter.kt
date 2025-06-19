package com.example.quanlybongda.Services.Converters

import com.example.quanlybongda.Database.DateConverter
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LocalDateTimeConverter : JsonSerializer<LocalDateTime?>, JsonDeserializer<LocalDateTime?> {
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return JsonPrimitive(DateConverter.LocalDateTimeToString(src))
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        return DateConverter.StringToLocalDateTime(json.getAsString())
    }


}
