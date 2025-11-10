package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.data.MongoDB
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.ContactCreatedResponse
import com.example.shared.ContactMessage
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Api(routeOverride = "contact")
suspend fun contact(context: ApiContext) {
    try {
        val body = context.req.body?.decodeToString()
        if (body == null) {
            context.res.setBodyText(Json.encodeToString(ApiResponse.Error(message = "Invalid request body")))
            return
        }
        // Deserialize into shared DTO using the generic decodeFromString
        val contact = Json.decodeFromString<ContactMessage>(body)
        if (contact.name.isBlank() || contact.email.isBlank() || contact.message.isBlank()) {
            context.res.setBodyText(Json.encodeToString(ApiResponse.Error(message = "name, email and message are required")))
            return
        }
        val createdId = context.data.getValue<MongoDB>().saveContactMessage(contact)
        if (createdId != null) {
            val resp = ContactCreatedResponse(id = createdId)
            // set HTTP 201 Created and return JSON body
            context.res.status = 201
            context.res.setBodyText(Json.encodeToString(resp))
        } else {
            context.res.setBodyText(Json.encodeToString(ApiResponse.Error(message = "Failed to save contact message")))
        }
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(ApiResponse.Error(message = e.message.toString())))
    }
}
