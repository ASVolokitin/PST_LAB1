package org.example.session

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.Base64
import java.util.Properties

/**
 * Небольшой файловый стор сессии: сохраняет cookies и время сохранения.
 * Формат простой, чтобы его было легко читать и отлаживать.
 */
class SessionStore(
    private val filePath: Path,
    private val clock: Clock = Clock.systemUTC(),
) {
    fun save(snapshot: SessionSnapshot) {
        val props = Properties()
        props[KEY_SAVED_AT] = snapshot.savedAt.toEpochMilli().toString()

        snapshot.cookies.forEach { (name, value) ->
            props[COOKIE_PREFIX + encode(name)] = encode(value)
        }

        filePath.parent?.let { Files.createDirectories(it) }
        Files.newOutputStream(filePath).use { output ->
            props.store(output, "session snapshot")
        }
    }

    fun load(): SessionSnapshot? {
        if (!Files.exists(filePath)) return null

        val props = Properties()
        Files.newInputStream(filePath).use { input ->
            props.load(input)
        }

        val savedAtEpoch = props.getProperty(KEY_SAVED_AT)?.toLongOrNull() ?: return null
        val savedAt = Instant.ofEpochMilli(savedAtEpoch)

        val cookies = linkedMapOf<String, String>()
        props.stringPropertyNames()
            .filter { it.startsWith(COOKIE_PREFIX) }
            .forEach { key ->
                val encodedName = key.removePrefix(COOKIE_PREFIX)
                val encodedValue = props.getProperty(key)

                if (!encodedValue.isNullOrBlank()) {
                    cookies[decode(encodedName)] = decode(encodedValue)
                }
            }

        return SessionSnapshot(cookies = cookies, savedAt = savedAt)
    }

    fun clear() {
        Files.deleteIfExists(filePath)
    }

    fun isValid(maxAge: Duration): Boolean {
        val snapshot = load() ?: return false
        val age = Duration.between(snapshot.savedAt, Instant.now(clock))
        return !age.isNegative && age <= maxAge
    }

    private fun encode(value: String): String {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.toByteArray(StandardCharsets.UTF_8))
    }

    private fun decode(value: String): String {
        return String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8)
    }

    private companion object {
        const val KEY_SAVED_AT = "meta.savedAtEpochMs"
        const val COOKIE_PREFIX = "cookie."
    }
}

data class SessionSnapshot(
    val cookies: Map<String, String>,
    val savedAt: Instant,
)
