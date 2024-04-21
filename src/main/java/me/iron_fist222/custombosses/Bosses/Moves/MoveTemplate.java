package me.iron_fist222.custombosses.Bosses.Moves;

import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R2.event.CraftEventFactory;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoveTemplate extends FallingBlockEntity {

    public List<MoveTemplate> blocks;
    private BlockState blockState;
    public int time;
    public int lifetime = 60;

    public MoveTemplate(Level world){
        super(EntityType.FALLING_BLOCK, world);
        this.setNoGravity(true);
    }

    public MoveTemplate(Level world, Vec3 position, BlockState block){
        super(EntityType.FALLING_BLOCK,world);
        this.teleportTo(position.x,position.y,position.z);
        CompoundTag blockStateTag = new CompoundTag();
        this.addAdditionalSaveData(blockStateTag);
        blockStateTag.put("BlockState", NbtUtils.writeBlockState(block));
        this.readAdditionalSaveData(blockStateTag);
        this.blockState = this.getBlockState();
        this.setNoGravity(true);
    }


    public MoveTemplate(Level world, Vec3 defLoc, BlockState block, Vec3 velocity, Vec3 facingDir, int size, Class cls){
        super(EntityType.FALLING_BLOCK,world);
        this.teleportTo(defLoc.x,defLoc.y,defLoc.z);
//        this.setDeltaMovement(velocity);
        this.setNoGravity(true);
        this.blocks = SummonMove(world,defLoc,block,velocity,facingDir,size,cls);
        CompoundTag blockStateTag = new CompoundTag();
        this.addAdditionalSaveData(blockStateTag);
        blockStateTag.put("BlockState",NbtUtils.writeBlockState(block));
        this.readAdditionalSaveData(blockStateTag);
        this.blockState = this.getBlockState();
    }

    public static List SummonMove(Level world, Vec3 centralPos, BlockState block, Vec3 velocity, Vec3 facingDir, int size, Class<MoveTemplate> cls){
        List<MoveTemplate> moveBlocks = new ArrayList<MoveTemplate>();
        Vec3 curentPos = centralPos;
        Vec3 rVector = facingDir.cross(new Vec3(0,1,0));
        Vec3 uVector = facingDir.cross(rVector);
        uVector = uVector.normalize();
        rVector = rVector.normalize();
        System.out.println(rVector);
        System.out.println(uVector);
        Vec3 tempVector;
        Vec3 multiplier = new Vec3(1,1,1);
        boolean InParts = false;
        float NextLevelRemainder = -1;
        float InsidePartsRemainder = -1;
        if(size > 1) {
            int loopSize = (int) (2*Math.pow(size,2)-size*2);
            for (int index = 0; index < loopSize; index++) {
                try {
                    tempVector = new Vec3(0,0,0);
                    curentPos = centralPos;
                    Constructor<MoveTemplate> ctr = cls.getConstructor(Level.class, Vec3.class, BlockState.class);
                    MoveTemplate currentBlock = ctr.newInstance(world, centralPos, block);

                    int remainder = index%2;
                    if (NextLevelRemainder == 0 && index != 0){
                        multiplier = new Vec3 ((Math.round((2+Math.sqrt(4+8*(index+1)))/(4))),(Math.round((2+Math.sqrt(4+8*(index+1)))/(4))),(Math.round((2+Math.sqrt(4+8*(index+1)))/(4))));
                        InParts = false;

                    }
                    if (InsidePartsRemainder==0 && index != 4){
                        multiplier = multiplier.subtract(1,1,1);
                        InParts = true;

                    }
                    if(remainder == 0){
                        tempVector = rVector;
                        if (InParts){
                            tempVector = tempVector.add(uVector.multiply(Math.abs(multiplier.x),Math.abs(multiplier.y),Math.abs(multiplier.z)));
                        }
                        multiplier = multiplier.multiply(-1,-1,-1);
                    }else{
                        tempVector = uVector;
                        if (InParts){
                            tempVector = tempVector.add(rVector.multiply(Math.abs(multiplier.x),Math.abs(multiplier.y),Math.abs(multiplier.z)).multiply(-1,-1,-1));
                        }
                    }

                    tempVector = tempVector.multiply(multiplier);
                    curentPos = curentPos.add(tempVector);

                    currentBlock.teleportTo(curentPos.x,curentPos.y,curentPos.z);
//                    currentBlock.setDeltaMovement(velocity);
                    currentBlock.setNoGravity(true);
                    System.out.println((byte) currentBlock.getYRot());
                    moveBlocks.add(currentBlock);
                    NextLevelRemainder = (float) (Math.round((2+Math.sqrt(4+8*(index+1)))/(4)) - (2+Math.sqrt(4+8*(index+1)))/(4));
                    InsidePartsRemainder = (float) (Math.round((2+Math.sqrt(4+8*(index-3)))/(4)) - (2+Math.sqrt(4+8*(index-3)))/(4));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return moveBlocks;
    }

    private static MoveTemplate createBlock(Level world,int current){
        if (current == 0){
            return createBlock(world,current-1);
        }

        return new MoveTemplate(world);

    }

    @Override
    public void tick() {

//        System.out.println(this.getId());

//        this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));

        ((ServerLevel)this.level()).getChunkSource().chunkMap.broadcast(this, new ClientboundSetEntityMotionPacket(this.getId(),this.getDeltaMovement()));
//        ((ServerLevel)this.level()).getChunkSource().chunkMap.broadcast(this, new ClientboundMoveEntityPacket.Rot(this.getId(),(byte) this.getYRot(),(byte)this.getXRot(),false));

        for(ServerPlayer player :  MinecraftServer.getServer().getPlayerList().getPlayers()){
            ServerPlayerConnection SPC = player.connection;           //THESE CODES WORKS TO CREATE LIKE SENDING THE VELOCITY PACKETS N STUF im wayne btw
//            SPC.send(new ClientboundMoveEntityPacket.Rot(this.getId(),(byte) this.getYRot(),(byte)this.getXRot(),false));
//            SPC.send(new ClientboundSetEntityMotionPacket(this.getId(),this.getDeltaMovement()));
//            SPC.send(new ClientboundMoveEntityPacket.Pos(this.getId(),(short) this.getX(),(short) this.getY(),(short) this.getZ(),false));
//            SPC.send(new ClientboundExplodePacket(player.getX(),player.getY(),player.getZ(),20.0f,new ArrayList<BlockPos>(),null));
        }


        this.move(MoverType.SELF, this.getDeltaMovement());
        ++this.time;
//        System.out.println(time);
        if (this.time >= this.lifetime){
            for(Entity passenger : this.passengers){
                passenger.setDeltaMovement(this.getDeltaMovement().multiply(1.4,1.4,1.4));
                passenger.hurtMarked = true;
            }
            this.discard();
        }
        BlockPos blockposition = this.blockPosition();
//        ((ServerLevel)this.level()).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockposition, this.level().getBlockState(blockposition)));
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(this.level(),this,this.position(),this.getDeltaMovement().add(this.position()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1), this::canHitEntity);
        if (hit!=null) {
            if (hit.getType() == HitResult.Type.ENTITY) {
                EntityHitResult eHit = hit;
                Entity hitEntity = eHit.getEntity();
                LivingEntity liveEntity = null;
                try {
                    liveEntity = (LivingEntity) hitEntity;
                } catch (Exception _) {
                }
                if (liveEntity != null) {
                    HitLivingEntity(hit);
                }
            }
        }


    }

    boolean canHitEntity(Entity entity){
        if(entity.canBeHitByProjectile()){
            return true;
        }else{
            return false;
        }
    }
    public void HitLivingEntity(EntityHitResult HR){
        //empty on purpose bc its template
    }
}
