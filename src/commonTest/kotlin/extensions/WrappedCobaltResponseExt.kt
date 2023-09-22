package extensions

import models.CobaltError
import models.PickerType
import wrapper.ErrorResponse
import wrapper.PickerResponse
import wrapper.SuccessResponse
import wrapper.WrappedCobaltResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class WrappedCobaltResponseExt {
    @Test
    fun testThrowIfErrorWithErrorResponse() {
        // Create an ErrorResponse
        val errorResponse = ErrorResponse("An error occurred")

        // Test that throwIfError throws CobaltError
        val exception = assertFailsWith<CobaltError> {
            errorResponse.throwIfError()
        }

        // Check the exception message
        assertEquals("An error occurred", exception.message)
    }

    @Test
    fun testThrowIfErrorWithOtherResponse() {
        listOf(
            SuccessResponse("Success"),
            PickerResponse(PickerType.VARIOUS, emptyList(), "Audio")
        ).forEach { wrappedResponse ->
            // Test that throwIfError returns the response itself
            val result = wrappedResponse.throwIfError()

            // Check that the result is the same as the original response
            assertEquals(wrappedResponse, result)
        }
    }
}