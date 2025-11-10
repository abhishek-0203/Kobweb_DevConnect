package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class ContactMessage(
    val id: String? = null,
    val name: String,
    val email: String,
    val phone: String? = null,
    val subject: String? = null,
    val message: String,
    val createdAt: Long? = null
)
