package me.iron_fist222.custombosses.Bosses.Moves.WaterMoves;

import com.google.common.base.Preconditions;
import me.iron_fist222.custombosses.Bosses.Moves.MoveTemplate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
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
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.Set;

public class IcePush extends MoveTemplate {



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
