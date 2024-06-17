package net.wdsj.mcserver.wdsjmessage.bukkit

import net.wdsj.mcserver.wdsjmessage.common.MessagePlatform
import net.wdsj.mcserver.wdsjmessage.common.MessageQueueManager
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class BukkitPlatform(val plugin: WdsjMessageBukkit) : MessagePlatform(MessageQueueManager) {

    val ticks = 2L

    private var task: BukkitTask? = null


    override val ticksPeriod: Long = ticks * 50L

    override fun initializePlatform() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable {
            tick()
        }, 1, ticks)
    }

    override fun destroyPlatform() {
        task?.cancel()
    }


}