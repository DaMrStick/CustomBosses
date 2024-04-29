package me.iron_fist222.custombosses.Bosses.Moves.WaterMoves;

import me.iron_fist222.custombosses.Bosses.Moves.BlockMoveTemplate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class IceSpike extends BlockMoveTemplate {

    public IceSpike(Level world, Vec3 pos, Vec3 velocity, Vec3 facingDir, int size,LivingEntity shooter){
        super(world,pos, Blocks.BLUE_ICE.defaultBlockState(),velocity,facingDir,size, IceSpike.class,shooter);
    }

    public IceSpike(Level world, Vec3 pos, BlockState block){
        super(world,pos,block);
    }

    @Override
    public void HitLivingEntity(EntityHitResult HR) {
        LivingEntity hitEntity = (LivingEntity) HR.getEntity();
        CompoundTag effect = new CompoundTag();
        MobEffect slowEffect = MobEffects.MOVEMENT_SLOWDOWN;
        hitEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3));
        hitEntity.hurt(((Entity) this.shooter).damageSources().magic(), 3);
        if(this.groupParent == null){
            for(BlockMoveTemplate block : this.blocks){
                if (block.position().distanceTo(this.position()) <= 5){
                    block.discard();
                }
            }
        }else{
            for(BlockMoveTemplate block : this.groupParent.blocks){
                if (block.position().distanceTo(this.position()) <= 5){
                    block.discard();
                }
            }
        }

        this.discard();
    }
}
