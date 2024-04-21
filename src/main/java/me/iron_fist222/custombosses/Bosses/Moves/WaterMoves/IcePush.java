package me.iron_fist222.custombosses.Bosses.Moves.WaterMoves;

import com.google.common.base.Preconditions;
import me.iron_fist222.custombosses.Bosses.Moves.MoveTemplate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class IcePush extends MoveTemplate {



    public IcePush(Level world, Vec3 pos, Vec3 velocity, Vec3 facingDir, int size){
        super(world,pos,Blocks.ICE.defaultBlockState(),velocity,facingDir,size,IcePush.class);
    }

    public IcePush(Level world){
        super(world);
    }

    public IcePush(Level world,Vec3 pos,BlockState block){
        super(world,pos,block);

    }


    @Override
    public void HitLivingEntity(EntityHitResult HR) {
        LivingEntity hitEntity = (LivingEntity) HR.getEntity();
        CraftEntity craftEntity = hitEntity.getBukkitEntity();
        double speed = 10;
        Vector direction = this.getBukkitEntity().getFacing().getDirection();
        direction.setY(direction.getY()*3);
      /*  Player player = null;
        try {
            player = (Player) hitEntity.getBukkitEntity();
        }catch (Exception _){}
        if (player != null){
            System.out.println("player");
            player.setVelocity();
        }else {*/
//        Vector velocity = this.getDeltaMovement();
//        HR.getEntity().setDeltaMovement(this.getDeltaMovement());
//        HR.getEntity().hurtMarked = true;
//        System.out.println(hitEntity.startRiding(this));
        boolean canceled = false;
        for(String tag : hitEntity.getTags()){
            if(tag == "testabc"){
                canceled = true;
            }
        }
        if(!canceled){
//            hitEntity.startRiding(this);
//            hitEntity.addTag("testabc");
        }

        System.out.println(hitEntity.getTags());


    }
}
