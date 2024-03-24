package me.iron_fist222.custombosses.Bosses.Moves;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.C;

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
        CompoundTag blockState = new CompoundTag();
        this.addAdditionalSaveData(blockState);
        blockState.put("BlockState",NbtUtils.writeBlockState(block));
        this.readAdditionalSaveData(blockState);
    }

    public static List SummonMove(Level world, Vec3 centralPos, BlockState block, Vec3 velocity, Vec3 facingDir, int size, Class<MoveTemplate> cls){
        List<MoveTemplate> moveBlocks = new ArrayList<MoveTemplate>();
        Vec3 curentPos = centralPos;
        Vec3 rVector = facingDir.cross(new Vec3(0,1,0));
        Vec3 uVector = facingDir.cross(rVector);
        Vec3 tempVector;
        Vec3 multiplier = new Vec3(1,1,1);
        if(size > 1) {
            int loopSize = (int) (2*Math.pow(size,2)-size*2);
            for (int index = 0; index < loopSize; index++) {
                try {
                    tempVector = new Vec3(0,0,0);
                    curentPos = centralPos;
                    Constructor<MoveTemplate> ctr = cls.getConstructor(Level.class, Vec3.class, BlockState.class);
                    MoveTemplate currentBlock = ctr.newInstance(world, centralPos, block);
                    float rSqrt = Math.round(Math.sqrt(index));
                    int remainder = index%2;
                    float NextLevelRemainder = (float) (Math.round((2+Math.sqrt(4+8*index))/(4)) - (2+Math.sqrt(4+8*index))/(4));
                    if (NextLevelRemainder == 0){
                        multiplier = multiplier.add(new Vec3(1,1,1));
                    }
                    if(remainder == 0){
                        tempVector = rVector;
                        multiplier = multiplier.multiply(new Vec3(-1,-1,-1));
                    }else{
                        tempVector = uVector;
                    }

                    tempVector = tempVector.multiply(multiplier);
                    curentPos = curentPos.add(tempVector);
                    System.out.println(multiplier);
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

    private static MoveTemplate createBlock(Level world,int current){
        if (current == 0){
            return createBlock(world,current-1);
        }

        return new MoveTemplate(world);

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
