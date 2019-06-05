package accessrights

data class Mode(
    val u: RightsGroup,
    val g: RightsGroup,
    val o: RightsGroup
) {
    val bits = listOf(
        u.bits,
        g.bits,
        o.bits
    ).concatBitBlocks(3)

    val str = "${u.str}${g.str}${o.str}"

    companion object {
        operator fun invoke(block: ModeBuildingScope.() -> Mode) = ModeBuildingScope.block()
    }
}

data class RightsGroup(
    val r: Boolean,
    val w: Boolean,
    val x: Boolean
) {
    val bits = listOf(
        r.asBit,
        w.asBit,
        x.asBit
    ).concatBitBlocks(1)

    val str = "${r.str(rMark)}${w.str(wMark)}${x.str(xMark)}"
    private fun Boolean.str(mark: Char) = if (this) mark else sMark

    companion object {
        const val rMark = 'r'
        const val wMark = 'w'
        const val xMark = 'x'

        const val sMark = '-'

        private fun Char.parse(expectedMark: Char) = when (this) {
            expectedMark -> true
            sMark        -> false
            else         -> error("Unexpected mark $this")
        }

        fun parse(str: String): RightsGroup {
            require(str.length == 3) { "Input length must be 3, but was ${str.length} for $str" }
            return RightsGroup(
                str[0].parse(rMark),
                str[1].parse(wMark),
                str[2].parse(xMark)
            )
        }
    }
}

private val Boolean.asBit get() = if (this) 1 else 0
private fun Iterable<Int>.concatBitBlocks(blockLen: Int) = fold(0) { acc, block -> acc shl blockLen or block }

object ModeBuildingScope {
    data class IncompleteMode(
        val u: RightsGroup,
        val g: RightsGroup
    )

    operator fun RightsGroup.plus(g: RightsGroup) = IncompleteMode(this, g)
    operator fun IncompleteMode.plus(o: RightsGroup) = Mode(u, g, o)

    val void = RightsGroup.parse("---")
    val r = RightsGroup.parse("r--")
    val w = RightsGroup.parse("-w-")
    val x = RightsGroup.parse("--x")
    val rw = RightsGroup.parse("rw-")
    val rx = RightsGroup.parse("r-x")
    val wx = RightsGroup.parse("-wx")
    val rwx = RightsGroup.parse("rwx")
}
