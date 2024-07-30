package org.example.addVacancies

import kotlinx.serialization.Serializable

@Serializable
data class VacanciesRemote(
    val rowId: String,
    val login: String,
    val name: String,
    val payment: String,
    val expertise: String,
    val employment: String,
    val description: String
)