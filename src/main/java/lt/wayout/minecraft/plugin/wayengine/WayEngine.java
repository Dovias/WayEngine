package lt.wayout.minecraft.plugin.wayengine;

import lt.wayout.minecraft.plugin.wayengine.command.EngineCommand;
/*import lt.wayout.minecraft.plugin.wayengine.packet.ConnectionPipelineReader;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntityManager;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntityHandler;*/
import lt.wayout.minecraft.plugin.wayengine.thread.SingleThreadPool;
import lt.wayout.minecraft.plugin.wayengine.ui.UIHandlingRegistry;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class WayEngine extends JavaPlugin {
    //private PacketEntityManager entityManager;
    private UIHandlingRegistry uiManager;

    @Override
    public void onEnable() {
        //this.entityManager = new PacketEntityManager(new PacketEntityHandler(new ConnectionPipelineReader(this), 64));
        this.uiManager = new UIHandlingRegistry(this);
        PluginCommand command = this.getCommand(this.getName());
        if (command != null) {
            command.setExecutor(new EngineCommand(this));
        }
    }

    @Override
    public void onDisable() {
        //this.entityManager.getHandler().removeAllEntities();
        SingleThreadPool.getInstance().shutdown();
    }

    /*@NotNull
    public PacketEntityManager getPacketEntityManager() {
        return this.entityManager;
    }*/

    @NotNull
    public UIHandlingRegistry getUIManager() {
        return this.uiManager;
    }
}
