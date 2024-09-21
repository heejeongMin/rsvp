package com.min.rsvp.util

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class RSVPOptionsConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>): String {
        return attribute.map { it }.joinToString(",")
    }

    override fun convertToEntityAttribute(value: String): List<String> {
        return value.split(",").dropLastWhile { it.isEmpty() }.map { it }
    }
}