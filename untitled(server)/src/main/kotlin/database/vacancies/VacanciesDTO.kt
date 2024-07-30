package org.example.database.vacancies

import kotlinx.serialization.Serializable

@Serializable
class VacanciesDTO (
    val rowId: String,
    val login: String,
    val name: String,
    val payment: String,
    val expertise: String,
    val employment: String,
    val description: String
)