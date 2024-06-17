package net.wdsj.mcserver.wdsjmessage.common.processor

import net.wdsj.mcserver.wdsjmessage.common.Audience
import net.wdsj.mcserver.wdsjmessage.common.QueuedCell
import net.wdsj.mcserver.wdsjmessage.common.msg.Message

class DefaultProcessor : CellProcessor {

    override fun process(ticksPeriod: Long, cells: List<QueuedCell>): Map<Audience<*>, AudienceMessage> {
        val audiencePackets = cells
            .flatMap { it.audiences }
            .associateWith { AudienceMessage() }

        for (cell: QueuedCell in cells) {
            val take = cell.cell.take(ticksPeriod)
            if (cell.removeIfNotActiveAudience) {
                cell.audiences.removeIf {
                    !it.isActive()
                }
            }
            for (audience in cell.audiences) {
                audiencePackets[audience]!!.addPriorityMessage(cell.priority, take)
            }
        }

        return audiencePackets

    }

    class AudienceMessage {

        private val priorityMessages: MutableMap<Byte, MutableList<Message<*>>> = mutableMapOf()

        fun addPriorityMessage(priority: Byte, message: Message<*>) {
            priorityMessages.getOrPut(priority) { mutableListOf() }.add(message)
        }

        fun build(): Message<*>? {
            val sortedPriorities = priorityMessages.keys.sortedDescending()

            var resultMessage: Message<Any>? = null
            var higherPriority: Byte? = null
            for (priority in sortedPriorities) {
                if (higherPriority != null && higherPriority - priority > 32) break

                val messages = priorityMessages[priority] ?: continue
                for (message in messages) {
                    resultMessage = if (resultMessage == null) {
                        higherPriority = priority
                        message as Message<Any>
                    } else {
                        resultMessage.merge(message as Message<Any>)
                    }
                }

                if (priority == QueuedCell.Priority.HIGHEST) {
                    return resultMessage
                }
            }
            return resultMessage
        }
    }

}

