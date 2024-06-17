package net.wdsj.mcserver.wdsjmessage.common.cell

import net.md_5.bungee.api.chat.TextComponent
import net.wdsj.mcserver.wdsjmessage.common.util.CountDownStyle
import net.wdsj.mcserver.wdsjmessage.common.msg.Message
import net.wdsj.mcserver.wdsjmessage.common.msg.TextComponentMessage

class CountDownTextMessageCell private constructor(
    var startTextProvider: () -> TextComponent,
    var endTextProvider: () -> TextComponent?,
    var countdownStartTimeProvider: () -> Long,
    var countdownEndTimeProvider: () -> Long,
    var countdownStyleProvider: () -> CountDownStyle,
    var countdownFinish: () -> Unit,
) : MessageCell<TextComponent> {

    override fun isActive(): Boolean {
        return isCountdown()
    }

    override fun take(intervalMillis: Long): Message<TextComponent> {
        val startText = startTextProvider()
        val endText = endTextProvider() ?: TextComponent("")
        val countdownStartTime = countdownStartTimeProvider()
        val countdownEndTime = countdownEndTimeProvider()
        val countDownStyle = countdownStyleProvider()
        if (System.currentTimeMillis() >= countdownEndTime) {
            countdownFinish()
        }
        return TextComponentMessage(
            TextComponent(
                startText,
                TextComponent(countDownStyle.format(countdownStartTime, countdownEndTime, System.currentTimeMillis())),
                endText
            )
        )
    }

    fun setFinish() {
        setCountdownEnd(System.currentTimeMillis())
    }

    fun setCountdownEnd(time: Long) {
        countdownEndTimeProvider = { time }
    }

    fun createRemainingTimeMessage(): TextComponent {
        return TextComponent(startTextProvider(), TextComponent("剩余时间: ${calculateRemainingTime() / 1000} 秒"))
    }

    fun isCountdown(): Boolean {
        return System.currentTimeMillis() < countdownEndTimeProvider()
    }

    fun calculateRemainingTime(): Long {
        return countdownEndTimeProvider() - System.currentTimeMillis()
    }

    fun getTotalCountdownTime(): Long {
        return countdownEndTimeProvider() - countdownStartTimeProvider()
    }


    data class Builder private constructor(
        var startText: TextComponent = TextComponent("§f冷却时间: "),
        var endText: TextComponent? = TextComponent(""),
        var countdownStartTime: Long = System.currentTimeMillis(),
        var countdownEndTime: Long = 0,
        var countdownStyle: CountDownStyle = CountDownStyle.LINE_AND_TIME,
    ) {

        var countdownFinish: () -> Unit = {}


        fun setCountdownDuration(millis: Long) = apply {
            this.countdownEndTime = System.currentTimeMillis() + millis
            if (millis <= 0) throw IllegalArgumentException("冷却时间必须大于 0 毫秒")
        }


        fun build(): CountDownTextMessageCell {
            if (countdownEndTime == 0L) throw IllegalArgumentException("结束时间未设置")
            return CountDownTextMessageCell(
                startTextProvider = { startText },
                endTextProvider = { endText },
                countdownStartTimeProvider = { countdownStartTime },
                countdownEndTimeProvider = { countdownEndTime },
                countdownStyleProvider = { countdownStyle },
                countdownFinish = countdownFinish
            )
        }

        companion object {
            @JvmStatic
            fun newBuilder(block: Builder.() -> Unit): Builder {
                return Builder().apply(block)
            }
        }
    }
}

/*fun net.wdsj.mcserver.wdsjmessage.common.cell.CountDownTextMessageCell.Builder.withFinishSound() = apply {
    cooldownFinish = {

    }
}*/
