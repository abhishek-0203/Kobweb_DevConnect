package com.example.blogmultiplatform.pages

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*
import kotlinx.browser.window
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.core.Page
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.shared.ContactMessage
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.ContactCreatedResponse
import com.example.blogmultiplatform.util.parseData

@Page("/contact")
@Composable
fun ContactPage() {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var sending by remember { mutableStateOf(false) }

    // small helper for basic client-side email check
    fun isEmailValid(e: String) = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex().matches(e)
    // basic phone pattern: allow digits, spaces, +, -, parentheses
    fun isPhoneValid(p: String) = Regex("""^[0-9+\s()\-]{7,20}$""").matches(p)

    // Container center
    Div(attrs = {
        style {
            property("display", "flex")
            property("justify-content", "center")
            property("padding", "40px 16px")
        }
    }) {
        // Card
        Div(attrs = {
            style {
                property("max-width", "760px")
                property("width", "100%")
                property("background", "linear-gradient(180deg,#ffffff 0%, #fbfbff 100%)")
                property("border-radius", "14px")
                property("box-shadow", "0 8px 30px rgba(46,54,115,0.08)")
                property("padding", "28px")
                property("border", "1px solid rgba(99,102,241,0.06)")
            }
        }) {
            // Header
            H1(attrs = {
                style {
                    property("margin", "0 0 6px 0")
                    property("font-size", "22px")
                    property("color", "#2d2f42")
                }
            }) { Text("Contact & Support") }
            P(attrs = {
                style {
                    property("margin", "0 0 18px 0")
                    property("color", "#6b6f86")
                }
            }) { Text("Have a question, bug, or complaint? Send us a message and we’ll get back as soon as possible.") }

            // Notification
            status?.let { msg ->
                Div(attrs = {
                    style {
                        property("margin", "8px 0 16px 0")
                        property("padding", "10px 12px")
                        property("border-radius", "8px")
                        if (isError) {
                            property("background", "#fff2f0")
                            property("color", "#9b2c2c")
                            property("border", "1px solid #ffd6d6")
                        } else {
                            property("background", "#f3fbf7")
                            property("color", "#1f7a4a")
                            property("border", "1px solid #c7f0db")
                        }
                    }
                }) { Text(msg) }
            }

            // Form grid
            Div(attrs = {
                style {
                    property("display", "grid")
                    property("grid-template-columns", "1fr 1fr")
                    property("gap", "12px")
                    property("margin-bottom", "12px")
                }
            }) {
                // Name
                Div {
                    Label(attrs = { style { property("font-size", "13px"); property("color", "#4b4f63"); } }) { Text("Name") }
                    Input(type = InputType.Text, attrs = {
                        placeholder("Your full name")
                        attr("value", name)
                        onInput { val el = it.target as org.w3c.dom.HTMLInputElement; name = el.value }
                        style {
                            property("width", "100%")
                            property("padding", "12px")
                            property("margin-top", "6px")
                            property("border-radius", "8px")
                            property("border", "1px solid #e8eaf6")
                            property("box-shadow", "inset 0 1px 0 rgba(255,255,255,0.6)")
                        }
                    })
                }

                // Email
                Div {
                    Label(attrs = { style { property("font-size", "13px"); property("color", "#4b4f63"); } }) { Text("Email") }
                    Input(type = InputType.Email, attrs = {
                        placeholder("you@example.com")
                        attr("value", email)
                        onInput { val el = it.target as org.w3c.dom.HTMLInputElement; email = el.value }
                        style {
                            property("width", "100%")
                            property("padding", "12px")
                            property("margin-top", "6px")
                            property("border-radius", "8px")
                            property("border", "1px solid #e8eaf6")
                        }
                    })
                }

                // Phone
                Div {
                    Label(attrs = { style { property("font-size", "13px"); property("color", "#4b4f63"); } }) { Text("Phone (optional)") }
                    Input(type = InputType.Tel, attrs = {
                        placeholder("+1 555 555 5555")
                        attr("value", phone)
                        onInput { val el = it.target as org.w3c.dom.HTMLInputElement; phone = el.value }
                        style {
                            property("width", "100%")
                            property("padding", "12px")
                            property("margin-top", "6px")
                            property("border-radius", "8px")
                            property("border", "1px solid #e8eaf6")
                        }
                    })
                }
            }

            // Subject
            Div(attrs = { style { property("margin-bottom", "12px") } }) {
                Label(attrs = { style { property("font-size", "13px"); property("color", "#4b4f63"); } }) { Text("Subject (optional)") }
                Input(type = InputType.Text, attrs = {
                    placeholder("Short summary")
                    attr("value", subject)
                    onInput { val el = it.target as org.w3c.dom.HTMLInputElement; subject = el.value }
                    style {
                        property("width", "100%")
                        property("padding", "12px")
                        property("margin-top", "6px")
                        property("border-radius", "8px")
                        property("border", "1px solid #e8eaf6")
                    }
                })
            }

            // Message
            Div(attrs = { style { property("margin-bottom", "14px") } }) {
                Label(attrs = { style { property("font-size", "13px"); property("color", "#4b4f63"); } }) { Text("Message") }
                TextArea(value = message, attrs = {
                    placeholder("Describe your issue or feedback in detail...")
                    onInput { val el = it.target as org.w3c.dom.HTMLTextAreaElement; message = el.value }
                    style {
                        property("width", "100%")
                        property("padding", "12px")
                        property("margin-top", "6px")
                        property("border-radius", "8px")
                        property("border", "1px solid #e8eaf6")
                        property("min-height", "160px")
                        property("resize", "vertical")
                    }
                })
            }

            // Actions
            Div(attrs = { style { property("display", "flex"); property("align-items", "center"); property("gap", "12px") } }) {
                Button(attrs = {
                    if (sending) disabled()
                     onClick {
                        // Basic validation
                        isError = false
                        status = null

                        if (name.isBlank() || email.isBlank() || message.isBlank()) {
                            isError = true
                            status = "Please fill name, email and message."
                            return@onClick
                        }
                        if (!isEmailValid(email)) {
                            isError = true
                            status = "Please enter a valid email address."
                            return@onClick
                        }
                        if (phone.isNotBlank() && !isPhoneValid(phone)) {
                            isError = true
                            status = "Please enter a valid phone number or leave blank."
                            return@onClick
                        }

                        sending = true
                        scope.launch {
                            try {
                                val payload = ContactMessage(id = null, name = name.trim(), email = email.trim(), phone = phone.ifBlank { null }, subject = subject.ifBlank { null }, message = message.trim())
                                val resp = window.api.tryPost(apiPath = "contact", body = Json.encodeToString(payload).encodeToByteArray())?.decodeToString()
                                if (resp != null) {
                                    try {
                                        val created = resp.parseData<ContactCreatedResponse>()
                                        isError = false
                                        status = "Message sent — thank you! (id=${'$'}{created.id})"
                                        // clear fields
                                        name = ""; email = ""; phone = ""; subject = ""; message = ""
                                    } catch (_: Throwable) {
                                        try {
                                            val parsed = resp.parseData<ApiResponse>()
                                            if (parsed is ApiResponse.Error) {
                                                isError = true
                                                status = "Failed: ${'$'}{parsed.message}"
                                            } else {
                                                isError = true
                                                status = "Unexpected server response"
                                            }
                                        } catch (e: Throwable) {
                                            isError = true
                                            status = "Failed to parse server response"
                                        }
                                    }
                                } else {
                                    isError = true
                                    status = "Failed to send message."
                                }
                            } catch (ex: Throwable) {
                                isError = true
                                status = "Error: " + (ex.message ?: "unknown")
                                console.error("Contact send failed", ex)
                            } finally {
                                sending = false
                            }
                        }
                    }
                    style {
                        property("background", "#6C5CE7")
                        property("color", "white")
                        property("border", "none")
                        property("padding", "10px 16px")
                        property("border-radius", "10px")
                        property("cursor", "pointer")
                        property("font-weight", "600")
                    }
                }) { Text(if (sending) "Sending..." else "Send Message") }

                // secondary action
                Button(attrs = {
                    onClick { name = ""; email = ""; phone = ""; subject = ""; message = ""; status = null; isError = false }
                    style {
                        property("background", "transparent")
                        property("color", "#6b6f86")
                        property("border", "1px solid #ebedf6")
                        property("padding", "8px 12px")
                        property("border-radius", "10px")
                        property("cursor", "pointer")
                    }
                }) { Text("Clear") }

                // small note
                Div(attrs = { style { property("margin-left", "auto"); property("color", "#9aa0b8"); property("font-size", "13px") } }) {
                    Text("We usually respond within 48 hours")
                }
            }
        }
    }
}
