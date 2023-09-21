package wrapper.builders

import models.CobaltRequest

/**
 * An interface for building Cobalt requests. It allows the construction of CobaltRequest objects
 * with various configuration options.
 *
 * @param T The specific builder implementation type.
 *
 * @see CobaltRequestBuilder
 * @see AudioRequestBuilder
 * @see VideoRequestBuilder
 * @see YouTubeRequestBuilder
 * @see TikTokRequestBuilder
 */
interface RequestBuilder<T> {
    /**
     * Builds and returns a CobaltRequest with the current builder's configuration.
     *
     * @return A CobaltRequest with the configured parameters.
     */
    fun build(): CobaltRequest
    /**
     * Builds a CobaltRequest by applying the given lambda function to the current builder.
     *
     * @param func A lambda function that configures the builder.
     * @return A CobaltRequest with the configured parameters.
     */
    fun build(func: T.() -> Unit): CobaltRequest
}