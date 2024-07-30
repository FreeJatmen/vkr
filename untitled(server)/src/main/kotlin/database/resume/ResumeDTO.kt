package org.example.database.cv

import kotlinx.serialization.Serializable

@Serializable
data class ResumeDTO(
    val rowId: String,
    val login: String,
    val name: String,
    val surname: String,
    val contacts: String,
    val skills: String,
    val aboutYourself: String,
    val resumeName: String
)