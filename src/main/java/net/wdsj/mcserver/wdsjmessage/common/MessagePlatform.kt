package net.wdsj.mcserver.wdsjmessage.common

import net.wdsj.mcserver.wdsjmessage.common.MessageQueueManager.cellProcessors

abstract class MessagePlatform(val manager: MessageQueueManager) {


    abstract fun initializePlatform()

    abstract fun destroyPlatform()

    abstract val ticksPeriod: Long

    open fun tick() {
        cellProcessors.forEach { processor ->
            val actions =
                processor.process(ticksPeriod, manager.listQueuedCell().filter { it.type == QueuedCell.Type.ACTION_BAR })

            for ((user, m) in actions) {
                val message = m.build() ?: continue
                user.send(QueuedCell.Type.ACTION_BAR, message)
            }
        }
        manager.listQueuedCell().filter { !it.cell.isActive() }.forEach { manager.dequeueMessage(it) }

    }


}