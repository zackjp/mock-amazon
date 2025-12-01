package com.example.mockamazon.shared

import android.content.Context
import com.example.mockamazon.R
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale


fun LocalDate.toPrimeDeliveryString(
    context: Context,
): String? {
    val deliveryDelta = Period.between(LocalDate.now(), this)
    val daysUntilDelivery = deliveryDelta.days

    return when (daysUntilDelivery) {
        1 -> context.getString(R.string.datetime_tomorrow)
        2 -> context.getString(R.string.datetime_two_day)
        else -> null
    }
}

fun LocalDate.toRelativeDateString(
    context: Context,
    longDate: Boolean = true
): String {
    val deliveryDelta = Period.between(LocalDate.now(), this)
    val daysUntilDelivery = deliveryDelta.days

    return if (daysUntilDelivery == 1) {
        val dateFormat = if (longDate) "MMMM d" else "MMM d"
        val tomorrowString = context.getString(R.string.datetime_tomorrow)
        val dateFormatter = DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault())
        "$tomorrowString, " + dateFormatter.format(this)
    } else {
        val dateFormat = if (longDate) "EEEE, MMMM d" else "EEE, MMM d"
        val dateFormatter = DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault())
        dateFormatter.format(this)
    }
}