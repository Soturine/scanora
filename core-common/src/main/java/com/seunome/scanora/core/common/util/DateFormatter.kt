package com.seunome.scanora.core.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateFormatter(
    private val locale: Locale = Locale("pt", "BR"),
    private val timeZone: TimeZone = TimeZone.getTimeZone("America/Sao_Paulo"),
) {
    fun format(timestamp: Long): String {
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", locale)
        formatter.timeZone = timeZone
        return formatter.format(Date(timestamp))
    }
}

