package me.iron_fist222.custombosses.Bosses.Moves;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class MoveTemplate extends FallingBlockEntity {

    public List<MoveTemplate> blocks;


    public MoveTemplate(Level world){
        super(EntityType.FALLING_BLOCK, world);
        this.setNoGravity(true);
    }

    public MoveTemplate(Level world, Vec3 position, BlockState block){
        super(EntityType.FALLING_BLOCK,world);
        this.teleportTo(position.x,position.y,position.z);
        CompoundTag blockState = new CompoundTag();
        this.addAdditionalSaveData(blockState);
        blockState.put("BlockState", NbtUtils.writeBlockState(block));
        this.readAdditionalSaveData(blockState);
        this.setNoGravity(true);
    }


    public MoveTemplate(Level world, Vec3 defLoc, BlockState block, Vec3 velocity, Vec3 facingDir, int size, Class cls){
        super(EntityType.FALLING_BLOCK,world);
        this.teleportTo(defLoc.x,defLoc.y,defLoc.z);
        this.setDeltaMovement(velocity);
        this.setNoGravity(true);
        this.blocks = SummonMove(world,defLoc,block,velocity,facingDir,size,cls);
    }

    public static List SummonMove(Level world, Vec3 centralPos, BlockState block, Vec3 velocity, Vec3 facingDir, int size, Class<MoveTemplate> cls){
        List<MoveTemplate> moveBlocks = new ArrayList<MoveTemplate>();
        Vec3 curentPos = centralPos;
        Vec3 rVector = facingDir.cross(new Vec3(0,1,0));
        Vec3 uVector = facingDir.cross(rVector);
        System.out.println(rVector);
        System.out.println(uVector);

        if(size > 1) {
            for (int index = 0; index < (size ^ 2); index++) {
                try {
                    Constructor<MoveTemplate> ctr = cls.getConstructor(Level.class, Vec3.class, BlockState.class);
                    MoveTemplate currentBlock = ctr.newInstance(world, centralPos, block);
                    curentPos = curentPos.add(rVector);
                    currentBlock.teleportTo(curentPos.x,curentPos.y,curentPos.z);
                    currentBlock.setDeltaMovement(velocity);
                    currentBlock.setNoGravity(true);
                    moveBlocks.add(currentBlock);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return moveBlocks;
    }


    @Override
    public void tick() {

        /*HitResult hit = ProjectileUtil.getEntityHitResult(this.level(),this,this.position(),this.getDeltaMovement().add(this.position()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1), this::canHitEntity);
        if (hit!=null) {
            if (hit.getType() == HitResult.Type.ENTITY) {
                EntityHitResult eHit = (EntityHitResult) hit;
                Entity hitEntity = eHit.getEntity();
                LivingEntity liveEntity = null;
                try {
                    liveEntity = (LivingEntity) hitEntity;
                } catch (Exception _) {
                }
                if (liveEntity != null) {
                    liveEntity.setLastHurtByMob(this.shooter);
                    CompoundTag effect = new CompoundTag();
                    MobEffect slowEffect = MobEffects.MOVEMENT_SLOWDOWN;
                    liveEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3));
                    liveEntity.hurt(((Entity) this.shooter).damageSources().magic(), 3);
                    this.discard();
                }
            }
        }*/


        if(this.onGround){
            this.discard();
        }
    }
}
