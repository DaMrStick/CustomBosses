package me.iron_fist222.custombosses.Bosses.Moves.WaterMoves;

import me.iron_fist222.custombosses.Bosses.Moves.BlockMoveTemplate;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class IcePush extends BlockMoveTemplate {



    public IcePush(Level world, Vec3 pos, Vec3 velocity, Vec3 facingDir, int size,LivingEntity shooter){
        super(world,pos,Blocks.ICE.defaultBlockState(),velocity,facingDir,size,IcePush.class,shooter);
    }


    public IcePush(Level world,Vec3 pos,BlockState block){
        super(world,pos,block);

    }


    @Override
    public void HitLivingEntity(EntityHitResult HR) {
        LivingEntity hitEntity = (LivingEntity) HR.getEntity();

        double speed = 10;
        double HeightMultiplier = 2;
        double SpeedMultiplier = 4;
        Vec3 velocity = this.getDeltaMovement();
        velocity = new Vec3(velocity.x*SpeedMultiplier,velocity.y*HeightMultiplier,velocity.z*SpeedMultiplier); //height multiplier
        HR.getEntity().setDeltaMovement(velocity);
        HR.getEntity().hurtMarked = true;  //idk why this line works but i think it updates player stuff and it works better than packets, Its what bukkit uses when you do setVelocity
        ((ServerLevel)this.level()).getChunkSource().chunkMap.broadcast(this, new ClientboundMoveEntityPacket.Pos(HR.getEntity().getId(),(short) HR.getEntity().getX(),(short) HR.getEntity().getY(),(short) HR.getEntity().getZ(),HR.getEntity().onGround));
    }
}
