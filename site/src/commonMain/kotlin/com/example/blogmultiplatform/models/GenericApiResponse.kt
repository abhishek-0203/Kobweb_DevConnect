package com.example.blogmultiplatform.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
sealed class GenericResponse {
    @Serializable
    @SerialName("success")
    object Success : GenericResponse()

    @Serializable
    @SerialName("error")
    data class Error(val message: String) : GenericResponse()
}

