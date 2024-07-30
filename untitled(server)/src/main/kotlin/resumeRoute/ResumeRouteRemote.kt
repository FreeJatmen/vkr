package org.example.resumeRoute

import kotlinx.serialization.Serializable

@Serializable
data class ResumeRouteRemote(
    val rowId: String,
    val loginResume: String,
    val loginVacancy: String,
    val idResume: String,
    val idVacancy: String
)