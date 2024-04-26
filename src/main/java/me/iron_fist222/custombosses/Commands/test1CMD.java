package me.iron_fist222.custombosses.Commands;

import me.iron_fist222.custombosses.Bosses.Moves.MoveTemplate;
import me.iron_fist222.custombosses.Bosses.Moves.WaterMoves.IcePush;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class test1CMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player sender = (Player) commandSender;
        Location loc = sender.getLocation();
        Vector velocity = new Vector(0,0,1);
        Level msWorld = ((CraftWorld)sender.getWorld()).getHandle();
        IcePush icePush = new IcePush(msWorld,new Vec3(loc.getX(),loc.getY()+1,loc.getZ()),new Vec3(velocity.getX(),velocity.getY(),velocity.getZ()),new Vec3(0,0,1),3,(LivingEntity) commandSender);
        return true;
    }
}
