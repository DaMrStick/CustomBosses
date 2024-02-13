package me.iron_fist222.custombosses.Commands;

import me.iron_fist222.custombosses.Bosses.Entitys.WaterBoss;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.entity.Player;

public class SummonWaterBoss implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        System.out.println("boss command used");
        for(Player player : Bukkit.getOnlinePlayers()){
            WaterBoss boss = new WaterBoss(player.getLocation());
            ServerLevel world = ((CraftWorld) player.getWorld()).getHandle();
            world.addFreshEntity(boss);
        }
        return true;
    }
}
