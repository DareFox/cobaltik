import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalSerializationApi::class)
internal val ktor = LifecycleKtor((30).toDuration(DurationUnit.SECONDS)) {
    HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
            })
        }
    }
}