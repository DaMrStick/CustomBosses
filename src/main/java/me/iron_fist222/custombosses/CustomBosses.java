package me.iron_fist222.custombosses;

import me.iron_fist222.custombosses.Commands.SummonWaterBoss;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomBosses extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        System.out.println("customBosses plugin v1 is now enabled");
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("SummonWaterBoss").setExecutor(new SummonWaterBoss());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

/*    @EventHandler
    public void BossMoveHitGround(EntityChangeBlockEvent e){
        System.out.println("hi");
        if (((CraftEntity)e.getEntity()).getHandle().getTags().contains("DeleteOnLanding")){
            System.out.println("it worked!");
            e.getEntity().remove();
            e.setCancelled(true);
        }

    }*/


}
