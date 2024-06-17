package net.wdsj.mcserver.wdsjmessage.common

import net.wdsj.mcserver.wdsjmessage.common.cell.MessageCell
import java.util.*

class QueuedCell(val cell: MessageCell<*>) {

    val uuid = UUID.randomUUID()

    var audiences: MutableList<Audience<*>> = mutableListOf()


    var priority: Byte = Priority.NORMAL
    var type: Type = Type.ACTION_BAR
    var removeIfNotActiveAudience: Boolean = true


    object Priority {

        const val LOWEST: Byte = -64
        const val LOW: Byte = -32 // 当有比LOW更高的优先级时，LOW会被忽略，当同时拥有LOW和LOWEST时，LOWEST 将会merge到 LOW
        const val NORMAL: Byte = 0 // 当只有相同优先级时，会merge
        const val HIGH: Byte = 32  // 当有HIGH和NORMAL时，NORMAL会被merge到HIGH
        const val HIGHEST: Byte = 64 // 当有HIGHEST时，其他的优先级会被忽略，如果有多个HIGHEST，会被merge

    }


    enum class Type {
        ACTION_BAR,
        TITLE,
        SUBTITLE,
        MESSAGE
    }

}


fun QueuedCell.cancel() {
    MessageQueueManager.dequeueMessage(this)
}