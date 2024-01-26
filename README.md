# Cobaltik

Kotlin Multiplaform wrapper library for media downloader Cobalt API ([cobalt.tools](http://cobalt.tools "cobalt.tools"))

## How to add cobaltik as a dependency
Gradle (kotlin)
```kotlin
implementation("me.darefox:cobaltik:2.0.0")
``` 

Gradle
```groovy
implementation 'me.darefox:cobaltik:2.0.0'
```

Apache Maven
```xml
<dependency>
    <groupId>me.darefox</groupId>
    <artifactId>cobaltik</artifactId>
    <version>2.0.0</version>
</dependency>
```

## What class should I use? 
Difference between Wrapper (Cobalt) and Raw API implementation (CobaltRaw) is:
|[Cobalt](https://github.com/DareFox/cobaltik#using-wrapper-class-cobalt)|[CobaltRaw](https://github.com/DareFox/cobaltik#using-raw-api-class-cobaltraw)|
|--|--|
|Returns response in sealed class. To get data you need to use `when(response)`|Returns response unwrapped so you don't need to identify class, but it contains a lot of possible nulls|
|You can request with just url, general builder, specific builder, your own custom builder or `CobaltRequest`|You can only request with `CobaltRequest`|

## Using wrapper class (Cobalt)
### Create simple request
```kotlin
import me.darefox.cobaltik.wrapper.Cobalt

suspend fun simpleRequest(): WrappedCobaltResponse {
    val cobaltApiUrl = " https://co.wuk.sh/" // or other url of api instance
    val cobaltClient = Cobalt(cobaltApiUrl)

    val youtubeVideo = "https://www.youtube.com/watch?v=qWNQUvIk954"
    // request download url with default settings
    return cobaltClient.request(youtubeVideo)
}
```

### Get data from wrapped response
```kotlin
// convert generic response to specific
suspend fun getSpecificResponse() {
    val response: WrappedCobaltResponse = simpleRequest()
    when (response) {
        is ErrorResponse -> println("error: ${response.text}")
        is RedirectResponse -> println("redirect: ${response.redirectUrl}")
        is StreamResponse -> println("cobalt stream: ${response.streamUrl}")
        else /* PickerResponse, RateLimitResponse, SuccessResponse */ -> println(response)
    }
}
```

### Override default request settings
``` kotlin
suspend fun overrideDefaultRequest(cobaltClient: Cobalt): WrappedCobaltResponse {
    // request download url with overriding default settings
    val youtubeVideo = "https://www.youtube.com/watch?v=qWNQUvIk954"
    return cobaltClient.request(youtubeVideo) {
        videoQuality = VideoQuality.MAX
        videoCodec = VideoCodec.AV1
        muteAudio = true
    }
}
```

### Use specific builders
```kotlin
suspend fun useSpecificBuidler(cobaltClient: Cobalt): WrappedCobaltResponse {
    // request download url with specific builders
    val youtubeVideo = "https://www.youtube.com/watch?v=qWNQUvIk954"
    val audioBuilder = AudioRequestBuilder(youtubeVideo)
    cobaltClient.request(audioBuilder) {
        // AudioRequestBuilder create only-audio requests
        // so you can edit only audio related settings
        audioFormat = AudioFormat.MP3
        useDubLang = false
        // videoCodec = VideoCodec.AV1 - ERROR
    }

    val tiktokVideo = "https://www.tiktok.com/@rooroo01636/video/7284682918864145696"
    val tiktokBuilder = TikTokRequestBuilder(tiktokVideo)
    return cobaltClient.request(tiktokBuilder) {
        // TikTokRequestBuilder configures TikTok-specific settings,
        // but Cobalt may handle other supported media (e.g., YouTube) if the URL provided is different
        downloadFullTikTokAudio = true
        removeTikTokWatermark = true
        muteAudio = false
    }
}
```

### Use request object instead of builders
```kotlin
suspend fun useRequestObject(cobaltClient: Cobalt): WrappedCobaltResponse {
    // request download url using request object
    val youtubeVideo = "https://www.youtube.com/watch?v=qWNQUvIk954"
    val request = CobaltRequest(
        url = youtubeVideo,
        videoCodec = VideoCodec.AV1,
        videoQuality = VideoQuality.MAX,
        useDubLang = true,
        isAudioMuted = false
    )
    return cobaltClient.request(request)
}
```

### Get server info
```kotlin
fun getServerInfo(cobaltClient: Cobalt): CobaltServerInfo {
    return cobaltClient.getServerInfo()
}
```

## Using raw API class (CobaltRaw)
### Create raw client
```kotlin
fun createRawClient(): CobaltRaw {
    val cobaltApiUrl = "https://co.wuk.sh/" // or other url of api instance
    return CobaltRaw(cobaltApiUrl)
}
```
### Create raw request
```kotlin
suspend fun rawRequest(rawClient: CobaltRaw): CobaltResponse {
    val request = CobaltRequest(
        url = "https://www.youtube.com/watch?v=qWNQUvIk954",
        videoCodec = VideoCodec.AV1,
        videoQuality = VideoQuality.MAX,
        useDubLang = true,
        isAudioMuted = false
    )
    
    return rawClient.request(request)
}
```

### Get server info
```kotlin
suspend fun getServerInfoRaw(rawClient: CobaltRaw): CobaltServerInfo {
    return rawClient.getServerInfo()
}
```

