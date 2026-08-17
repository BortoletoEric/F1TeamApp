package io.github.bortoletoeric.f1teamapp.domain.model

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

data class Driver(
    val id: String,
    val teamId: String,
    val name: String,
    val surname: String,
    val number: Int?,
    val nationality: String,
    val birthday: String,
    val position: Int,
    val photoUrl: String?,
    val points: Float
) {
    val fullName: String get() = "$name $surname"

    val age: Int get() {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val birthDate = LocalDate.parse(birthday, formatter)
            Period.between(birthDate, LocalDate.now()).years
        } catch (e: Exception) {
            0
        }
    }
}