package models

import com.sashka.models.Dimensions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class DimensionsTest {

    @Test
    fun shouldCalculateVolume() {
        val dimensions = Dimensions(2.0, 3.0, 4.0)

        val volume = dimensions.calculateValue()

        assertEquals(24.0, volume, 0.001, "Volume should be length * width * height")
    }

    @Test
    fun shouldCalculateVolumeWithZeroLength() {
        val dimensions = Dimensions(0.0, 3.0, 4.0)

        val volume = dimensions.calculateValue()

        assertEquals(0.0, volume, 0.001, "Volume should be zero when length is zero")
    }
}