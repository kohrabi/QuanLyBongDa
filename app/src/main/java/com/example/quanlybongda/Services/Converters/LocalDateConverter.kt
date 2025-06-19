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
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LocalDateConverter : JsonSerializer<LocalDate?>, JsonDeserializer<LocalDate?> {
    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return JsonPrimitive(DateConverter.LocalDateToString(src))
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate? {
        return DateConverter.StringToLocalDate(json.getAsString())
    }


}
