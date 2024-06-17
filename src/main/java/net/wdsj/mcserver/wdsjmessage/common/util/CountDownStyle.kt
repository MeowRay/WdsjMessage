package net.wdsj.mcserver.wdsjmessage.common.util

import kotlin.math.roundToInt


interface CountDownStyle {

    data object BLOCK : FOO() {
        override val step: Double = 10.0
        override val passedPartsColor: String = "§a"
        override val passedPartsFoo: String = "■"
        override val remainingPartsColor: String = "§f"
        override val remainingPartsFoo: String = "■"
        override val finishColor: String = "§a"
        override val finishFoo: String = "■"
    }

    data object LINE : FOO() {
        override val step: Double = 30.0
        override val passedPartsColor: String = "§a"
        override val passedPartsFoo: String = "|"
        override val remainingPartsColor: String = "§f"
        override val remainingPartsFoo: String = "|"
        override val finishColor: String = "§a"
        override val finishFoo: String = "|"
    }


    class PROGRESS private constructor(
        private val builder: Builder
    ) : CountDownStyle {

        override fun format(start: Long, end: Long, now: Long): String {
            val totalDuration = end - start
            val passedDuration = now - start

            val partLength = totalDuration / builder.step
            val passedParts = (passedDuration / partLength).roundToInt()
            val remainingParts = builder.step - passedParts

            if (remainingParts <= 0.0) {
                return builder.finishReplacer()
                    .replace("{progress-bar}", builder.finishColor + builder.finishFoo.repeat(builder.step.toInt()))
            }
            val remainingDuration = end - now

            val passedSeconds = passedDuration / 1000.0
            val passedMinutes = passedSeconds / 60
            val remainingSeconds = remainingDuration / 1000.0
            val remainingMinutes = remainingSeconds / 60
            /* val passedHours = passedMinutes / 60
             val remainingHours = remainingMinutes / 60*/


            val suffix =
                if (remainingMinutes.toInt() <= 0) {
                    "§f${String.format("%.1f", remainingSeconds)} §e秒"
                } else "§f${remainingMinutes.toInt()} §e分 §f${String.format("%.1f", remainingSeconds % 60)} §e秒"


            return builder.progressingReplacer(
                passedParts,
                remainingParts,
                passedSeconds,
                passedMinutes,
                remainingSeconds,
                remainingMinutes
            ).replace(
                "{progress-bar}",
                builder.passedPartsColor + builder.passedPartsFoo.repeat(passedParts.coerceAtLeast(0)) + builder.remainingPartsColor + builder.remainingPartsFoo.repeat(
                    remainingParts.roundToInt().coerceAtLeast(0)
                )
            ).replace("{time-remaining}", suffix)
        }


        class Builder internal constructor() {

            var step: Double = 30.0
            var passedPartsColor: String = "§a"
            var passedPartsFoo: String = "|"
            var remainingPartsColor: String = "§f"
            var remainingPartsFoo: String = "|"
            var finishColor: String = "§a"
            var finishFoo: String = "|"

            var progressingReplacer: (
                passedParts: Int,
                remainingParts: Double,
                passedSeconds: Double,
                passedMinutes: Double,
                remainingSeconds: Double,
                remainingMinutes: Double
            ) -> String =
                { passedParts: Int, remainingParts: Double, passedSeconds: Double, passedMinutes: Double, remainingSeconds: Double, remainingMinutes: Double -> "{progress-bar} {time-remaining}" }
            var finishReplacer: () -> String = { "" }


            fun build() = PROGRESS(this)


        }

        companion object {
            fun newBuilder(block: Builder.() -> Unit) = Builder().apply(block)
        }

    }


    data object LINE_AND_TIME :
        CountDownStyle by PROGRESS.newBuilder({
            finishReplacer = { "{progress-bar} §a§l就绪" }


        }).build() {

    }


    abstract class FOO : CountDownStyle {
        abstract val step: Double
        abstract val passedPartsColor: String
        abstract val passedPartsFoo: String
        abstract val remainingPartsColor: String
        abstract val remainingPartsFoo: String
        abstract val finishColor: String
        abstract val finishFoo: String

        override fun format(start: Long, end: Long, now: Long): String {
            val totalDuration = end - start
            val passedDuration = now - start

            val partLength = totalDuration / step
            val passedParts = (passedDuration / partLength).roundToInt()
            val remainingParts = step - passedParts

            if (remainingParts <= 0.0) {
                return finishColor + finishFoo.repeat(step.toInt())
            }
            return passedPartsColor + passedPartsFoo.repeat(passedParts.coerceAtLeast(0)) + remainingPartsColor + remainingPartsFoo.repeat(
                remainingParts.roundToInt().coerceAtLeast(0)
            )
        }
    }

    data object RAINBOW : CountDownStyle {
        override fun format(start: Long, end: Long, now: Long): String {
            val totalDuration = end - start
            val passedDuration = now - start
            val partLength = totalDuration / 10
            val passedParts = (passedDuration / partLength).toDouble().roundToInt()
            val remainingParts = 10 - passedParts
            val passedColor = "§${passedParts + 1}"
            val remainingColor = "§${remainingParts + 1}"
            return passedColor + "■".repeat(passedParts.coerceAtLeast(0)) + remainingColor + "■".repeat(
                remainingParts.coerceAtLeast(
                    0
                )
            )
        }
    }

    data object PERCENTAGE : CountDownStyle {

        override fun format(start: Long, end: Long, now: Long): String {
            val totalDuration = end - start
            val passedDuration = now - start
            val percentage = (passedDuration.toDouble() / totalDuration * 100).coerceAtMost(100.0)
            return "§e${percentage.toInt()}%"
        }
    }

    data object TIME : CountDownStyle {
        override fun format(start: Long, end: Long, now: Long): String {
            val totalDuration = end - start
            val passedDuration = now - start
            val remainingDuration = end - now
            val passedSeconds = passedDuration / 1000
            val remainingSeconds = remainingDuration / 1000
            val passedMinutes = passedSeconds / 60
            val remainingMinutes = remainingSeconds / 60
            val passedHours = passedMinutes / 60
            val remainingHours = remainingMinutes / 60
            return "§e${passedHours}:${passedMinutes % 60}:${passedSeconds % 60} / ${remainingHours}:${remainingMinutes % 60}:${remainingSeconds % 60}"
        }
    }

    data object TIME_SHORT : CountDownStyle {
        override fun format(start: Long, end: Long, now: Long): String {
            val totalDuration = end - start
            val passedDuration = now - start
            val remainingDuration = end - now
            val passedSeconds = passedDuration / 1000
            val remainingSeconds = remainingDuration / 1000
            val passedMinutes = passedSeconds / 60
            val remainingMinutes = remainingSeconds / 60
            val passedHours = passedMinutes / 60
            val remainingHours = remainingMinutes / 60
            return "§e${passedHours}:${passedMinutes % 60}:${passedSeconds % 60} / ${remainingHours}:${remainingMinutes % 60}:${remainingSeconds % 60}"
        }
    }

    data object TIME_SECOND : CountDownStyle {
        override fun format(start: Long, end: Long, now: Long): String {
            val totalDuration = end - start
            val passedDuration = now - start
            val remainingDuration = end - now
            val remainingSeconds = remainingDuration / 1000
            return "§f${remainingSeconds} §e秒"
        }
    }

    data object TIME_MINUTE_SECOND : CountDownStyle {
        override fun format(start: Long, end: Long, now: Long): String {
            val totalDuration = end - start
            val passedDuration = now - start
            val remainingDuration = end - now
            val remainingSeconds = remainingDuration / 1000
            val remainingMinutes = remainingSeconds / 60
            if (remainingMinutes <= 0) {
                return "§f${remainingSeconds} §e秒"
            }
            return "§f${remainingMinutes} §e分 §f${remainingSeconds % 60} §e秒"
        }
    }

    data class CUSTOM(val formatter: (start: Long, end: Long, now: Long) -> String) : CountDownStyle {
        override fun format(start: Long, end: Long, now: Long): String {
            return formatter(start, end, now)
        }
    }

    abstract fun format(start: Long, end: Long, now: Long = System.currentTimeMillis()): String
}
