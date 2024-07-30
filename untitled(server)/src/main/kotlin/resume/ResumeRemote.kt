package org.example.cv

import kotlinx.serialization.Serializable

@Serializable
data class ResumeRemote(
    val rowId: String,
    val login: String,
    val name: String,
    val surname: String,
    val contacts: String,
    val skills: String,
    val aboutYourself: String,
    val resumeName: String
)