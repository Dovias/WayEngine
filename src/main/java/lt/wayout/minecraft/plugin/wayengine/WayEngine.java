package lt.wayout.minecraft.plugin.wayengine;

import lt.wayout.minecraft.plugin.wayengine.command.EngineCommand;
import lt.wayout.minecraft.plugin.wayengine.ui.UIRegistry;
/*import lt.wayout.minecraft.plugin.wayengine.packet.ConnectionPipelineReader;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntityManager;
import lt.wayout.minecraft.plugin.wayengine.packet.entity.PacketEntityHandler;*/
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class WayEngine extends JavaPlugin {
    //private PacketEntityManager entityManager;
    private UIRegistry uiManager;

    @Override
    public void onEnable() {
        //this.entityManager = new PacketEntityManager(new PacketEntityHandler(new ConnectionPipelineReader(this), 64));
        this.uiManager = new UIRegistry(this);
        PluginCommand command = this.getCommand(this.getName());
        if (command != null) {
            command.setExecutor(new EngineCommand(this));
        }
    }

    @Override
    public void onDisable() {
        //this.entityManager.getHandler().removeAllEntities();
    }

    /*@NotNull
    public PacketEntityManager getPacketEntityManager() {
        return this.entityManager;
    }*/

    @NotNull
    public UIRegistry getUIManager() {
        return this.uiManager;
    }
}
