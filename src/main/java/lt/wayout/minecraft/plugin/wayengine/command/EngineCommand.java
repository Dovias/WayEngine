package lt.wayout.minecraft.plugin.wayengine.command;

import com.google.common.base.Preconditions;
import lt.wayout.minecraft.plugin.wayengine.WayEngine;

import lt.wayout.minecraft.plugin.wayengine.protocol.ContainerType;
import lt.wayout.minecraft.plugin.wayengine.ui.UI;
import lt.wayout.minecraft.plugin.wayengine.ui.container.*;
import lt.wayout.minecraft.plugin.wayengine.ui.container.handler.GUIRecalculatedActionHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EngineCommand implements CommandExecutor {
    private final WayEngine plugin;
    //private HandledPacketEntity entity;
    private final UI<GUIContainerView> view;

    public EngineCommand(WayEngine plugin) {
        this.plugin = Preconditions.checkNotNull(plugin, "WayEngine object cannot be null!");

        GUIContainerItem containerItem = new GUIContainerItem(new ItemStack(Material.GOLD_INGOT)) {
            @Override
            public void invoke(@NotNull GUIContainerView uiView, @NotNull ClickType type, @NotNull Player player) {
                player.sendMessage("View element!");
            }
        };

        this.view = new ServerGUIContainer(this.plugin, ContainerType.GENERIC_9X4, "Powered by WayEngine", new GUIRecalculatedActionHandler(), List.of(
                new GUIContainerItem(new ItemStack(Material.DIAMOND)) {
                    @Override
                    public void invoke(@NotNull GUIContainerView uiView, @NotNull ClickType type, @NotNull Player player) {
                        player.sendMessage("You got mail!");
                        if (type == ClickType.RIGHT) {
                            uiView.setElement(null, 54);
                        } else if (type == ClickType.LEFT) {
                            uiView.setElement(containerItem, 54);

                        } else if (type == ClickType.MIDDLE) {
                            uiView.setElement(containerItem, 55);
                        }
                    }
                }
        ));
        this.plugin.getUIManager().register(this.view);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmdObject, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + " WAYOUT" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Šis serveris naudoja " + ChatColor.GREEN + plugin.getName() + " " + plugin.getDescription().getVersion() + ChatColor.GRAY + " įskiepių varikliuką" + ChatColor.DARK_GRAY + '.');
            return false;
        } else if (args[0].equalsIgnoreCase("debug") && sender instanceof Player player) {
            if (args.length == 2 && args[1].equalsIgnoreCase("stop")) {
                sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + " WAYOUT" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Varikliuko " + ChatColor.RED + "„debug“" + ChatColor.GRAY + " funkcija buvo sėkmingai įjungta!" + ChatColor.GREEN + "įjungta" + ChatColor.GRAY + '!');
                //this.tracker.debugStop = true;
                //this.tracker.stop(player);
                return false;
            }
            sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + " WAYOUT" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + "Varikliuko " + ChatColor.GREEN + "„debug“" + ChatColor.GRAY + " funkcija buvo sėkmingai " + ChatColor.GREEN + "įjungta" + ChatColor.GRAY + '!');
            this.view.open(player);
            //this.plugin.getConfig().set("item-debug", Serialization.serializeItems(player.getInventory().getContents()));
            //this.plugin.saveConfig();
            //Map<String, Object> this.plugin.getConfig().get("item-debug");

            /*CompoundTag anotherCompoundTag =  new CompoundTag();
            CompoundTag thirdCompoundTag = new CompoundTag();
            anotherCompoundTag.put("secondCompoundTag", thirdCompoundTag);
            thirdCompoundTag.putInt("type", 1);

            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("amount", 69420);
            compoundTag.put("firstCompoundTag", anotherCompoundTag);
            compoundTag.put("anotherCompoundTag", new CompoundTag());*/
            //this.view.open(player);
            /*PacketPlayerMeta meta = new PacketPlayerMeta().setSkin(new PlayerSkin(
                    "eyJ0aW1lc3RhbXAiOjE1ODEyNzU1ODE4NTUsInByb2ZpbGVJZCI6IjU2Njc1YjIyMzJmMDRlZTA4OTE3OWU5YzkyMDZjZmU4IiwicHJvZmlsZU5hbWUiOiJUaGVJbmRyYSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjlkZGEyNWEwMGU1MzM0NzYyMmJmYWM4MjQzMTdjMDI1YmU1MTU1NDFjMTRkYmRhNGY3ZWNlMGJkNzZjMDY0ZiJ9fX0=",
                    "HLE4VLlJD9wlnCHUaqTL9e/s4WZ88saxqloeffCtYEhTvpwMh1NOu32sOL0Z9oCKTUdFSbGXGyziZiMx9QEqfm4EC4KFXT2UlOoqmxrFr2NHgqeiU8beHk8i43gJH8Hme5yFfSA28/+m0L8G1kV+pq85pJdQ/5+UtVt3eN1TzjoWN5KISQc6YdkvYQLb/B0ryMq/I/ZUq442IhRQJJ3PzHKcNSskq6qzNnYMpw+dUnbZlJEXncvzkzkNUzU/ceoQvyITa27RziabGN6CRXXCwngcl3jS/my1XJ3+U1koLElhrwmIzc+VRPmjmkUGH6/rxILQfCQBNyigwuZMiHL1QNlKioxmdI546h2a7iLGC7tp3/9jKOHgEPRnkAklu0sdsbbPh+xDZGDVYK/tEKe/8+jauOtjq8rvvlTGA6ma0Lhpb2gjQ7cBFoBHmVMmprKwvS5AmHIuGvf2lVB2jEoEF8LB2d8kDANdKpoNN/WlQ3vbOA4/awLaoGsUTSJH8M2JV1SW8mmPMH9MMSqlKm3FCjRerrMQ/u1lgztd7Fkrga/uKc1LwGZu8j+dFFGrmvQ07x8xP4S7gElmDtMGrrGIeGot7UsONMe2XYShmEeLzcq5HRYAm1v34zPtW7YcUGIjQf/X7clIX56chvlXjWu2qR4/aCk/YZ+0Q0JZU21+LoQ="
            )).setGlow(ChatColor.AQUA).setDisplayNameVisibilityState(NametagVisiblity.HOVER);*/
            /*PacketSnowGolemMeta meta = new PacketSnowGolemMeta().setDisplayName("Besmegenis dovias").setDisplayNameVisibilityState(true).setPumpkinHatState(false);
            this.entity = this.plugin.getPacketEntityManager().spawnPersistentPacketEntity(EntityType.SNOWMAN, meta, player.getLocation());
            PacketTracker tracker = new PacketEntityTargetTracker(this.entity.getEntity());
            tracker.addEntity(player);
            tracker.addEntity(Bukkit.getPlayer("Dovias"));
            this.entity.addTracker(tracker);
            this.plugin.getPacketEntityManager().getHandler()*/



            /*PacketLivingEntityMeta meta = new PacketLivingEntityMeta().setLeashHolder(Bukkit.getPlayer("Dovias").getEntityId()).setCollisionState(false);
            this.entity = this.plugin.getPacketEntityManager().spawnPacketEntity(EntityType.COD, meta, player.getLocation());
            PacketTracker tracker = new PacketEntityFollowTracker(this.entity.getEntity(), player, Bukkit.getPlayer("Dovias"), new Location(null, 0.0d, 0.6d, 0.0d, -90.0f, 0.0f));
            this.entity.addTracker(tracker);*/
        }
        return false;
    }
}
