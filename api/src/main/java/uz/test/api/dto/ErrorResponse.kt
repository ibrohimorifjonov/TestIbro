package uz.test.api.dto

import org.json.JSONObject


data class ErrorResponse(
    val jsonResponse: JSONObject = JSONObject()
)