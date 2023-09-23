package wrapper

import models.CobaltResponse
import models.CobaltResponseStatus
import models.PickerType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CobaltResponseConverter {
    @Test
    fun testWrapError() {
        // Create a CobaltResponse representing an error
        val errorResponse = CobaltResponse(CobaltResponseStatus.ERROR, text = "An error occurred")

        // Test that wrap converts it to an ErrorResponse
        val wrappedResponse = errorResponse.wrap()
        assertEquals(ErrorResponse("An error occurred"), wrappedResponse)
    }

    @Test
    fun testWrapRedirect() {
        // Create a CobaltResponse representing a redirect
        val redirectResponse = CobaltResponse(CobaltResponseStatus.REDIRECT, "http://example.com")

        // Test that wrap converts it to a RedirectResponse
        val wrappedResponse = redirectResponse.wrap()
        assertEquals(RedirectResponse("http://example.com"), wrappedResponse)
    }

    @Test
    fun testWrapStream() {
        // Create a CobaltResponse representing a stream
        val streamResponse = CobaltResponse(CobaltResponseStatus.STREAM, "http://example.com/stream")

        // Test that wrap converts it to a StreamResponse
        val wrappedResponse = streamResponse.wrap()
        assertEquals(StreamResponse("http://example.com/stream"), wrappedResponse)
    }

    @Test
    fun testWrapSuccess() {
        // Create a CobaltResponse representing success
        val successResponse = CobaltResponse(CobaltResponseStatus.SUCCESS, text = "Success")

        // Test that wrap converts it to a SuccessResponse
        val wrappedResponse = successResponse.wrap()
        assertEquals(SuccessResponse("Success"), wrappedResponse)
    }

    @Test
    fun testWrapRateLimit() {
        // Create a CobaltResponse representing rate limit
        val rateLimitResponse = CobaltResponse(CobaltResponseStatus.RATELIMIT, text = "Rate limited")

        // Test that wrap converts it to a RateLimitResponse
        val wrappedResponse = rateLimitResponse.wrap()
        assertEquals(RateLimitResponse("Rate limited"), wrappedResponse)
    }

    @Test
    fun testWrapPicker() {
        // Create a CobaltResponse representing picker
        val pickerResponse = CobaltResponse(
            CobaltResponseStatus.PICKER,
            pickerType = PickerType.VARIOUS,
            picker = emptyList(),
            audio = "Audio URL"
        )

        // Test that wrap converts it to a PickerResponse
        val wrappedResponse = pickerResponse.wrap()
        assertEquals(PickerResponse(PickerType.VARIOUS, emptyList(), "Audio URL"), wrappedResponse)
    }
}