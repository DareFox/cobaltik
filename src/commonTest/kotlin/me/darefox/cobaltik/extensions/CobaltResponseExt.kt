package me.darefox.cobaltik.extensions

import me.darefox.cobaltik.extensions.isError
import me.darefox.cobaltik.extensions.throwIfError
import me.darefox.cobaltik.models.CobaltError
import me.darefox.cobaltik.models.CobaltResponse
import me.darefox.cobaltik.models.CobaltResponseStatus
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CobaltResponseExt {
    @Test
    fun testIsError() {
        // Create a CobaltResponse representing an error
        val errorResponse = CobaltResponse(CobaltResponseStatus.ERROR, text = "An error occurred")

        // Create a CobaltResponse representing success
        val successResponse = CobaltResponse(CobaltResponseStatus.SUCCESS)

        // Check if isError correctly identifies error responses
        assertTrue(errorResponse.isError())
        assertFalse(successResponse.isError())
    }

    @Test
    fun testThrowIfError() {
        // Create a CobaltResponse representing an error
        val errorResponse = CobaltResponse(CobaltResponseStatus.ERROR, text = "An error occurred")

        // Create a CobaltResponse representing success
        val successResponse = CobaltResponse(CobaltResponseStatus.SUCCESS)

        // Test that throwIfError throws an exception for error responses
        assertFailsWith<CobaltError> {
            errorResponse.throwIfError()
        }

        // Test that throwIfError returns the response for success responses
        val returnedResponse = successResponse.throwIfError()
        assertEquals(successResponse, returnedResponse)
    }
}
