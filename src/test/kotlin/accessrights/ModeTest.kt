package accessrights

import org.junit.Assert
import org.junit.Test

class ModeTest {
    private val boolUniverse = listOf(false, true)
    private val rgUniverse =
            boolUniverse.flatMap { r ->
                boolUniverse.flatMap { w ->
                    boolUniverse.map { x -> RightsGroup(r, w, x) }
                }
            }

    @Test
    fun rgAsBits() {
        rgUniverse.forEachIndexed { expected, rg ->
            Assert.assertEquals("$rg", expected, rg.bits)
        }
    }

    @Test
    fun rgStr() {
        val rgStrs = listOf(
                "---",
                "--x",
                "-w-",
                "-wx",
                "r--",
                "r-x",
                "rw-",
                "rwx"
        )

        (rgUniverse zip rgStrs).forEach { (rg, expected) ->
            Assert.assertEquals("$rg", expected, rg.str)
        }
    }

    private class ModeTestCase(
            bitStr: String,
            val str: String,
            val mode: Mode
    ) {
        val bits = Integer.parseUnsignedInt(bitStr, 8)
    }

    private val modeTestCases = listOf(
            ModeTestCase("777", "rwxrwxrwx", Mode { rwx + rwx + rwx }),
            ModeTestCase("000", "---------", Mode { void + void + void }),
            ModeTestCase("755", "rwxr-xr-x", Mode { rwx + rx + rx }),
            ModeTestCase("644", "rw-r--r--", Mode { rw + r + r }),
            ModeTestCase("421", "r---w---x", Mode { r + w + x }),
            ModeTestCase("653", "rw-r-x-wx", Mode { rw + rx + wx })
    )

    @Test
    fun modeBits() {
        modeTestCases.forEach {
            Assert.assertEquals("$it", it.bits, it.mode.bits)
        }
    }

    @Test
    fun modeStr() {
        modeTestCases.forEach {
            Assert.assertEquals("$it", it.str, it.mode.str)
        }
    }
}
