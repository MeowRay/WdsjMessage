package net.wdsj.mcserver.wdsjmessage.bukkit

import net.wdsj.mcserver.wdsjmessage.common.MessageQueueManager
import net.wdsj.servercore.WdsjServerAPI
import net.wdsj.servercore.common.command.CommandProxyBuilder
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class WdsjMessageBukkit : JavaPlugin() {


    companion object{
        lateinit var INSTANCE : WdsjMessageBukkit
    }

    init {
        INSTANCE = this
    }

    val platform = BukkitPlatform(this)

    override fun onEnable() {
        // Plugin startup logic

        MessageQueueManager.initializePlatform(platform)

        WdsjServerAPI.getPluginManager().registerCommand(
            CommandProxyBuilder<CommandSender>(
                this, MessageCommand()
            ).setName("WdsjMessage").setLabel("wdsjmessage")
        )
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


}
