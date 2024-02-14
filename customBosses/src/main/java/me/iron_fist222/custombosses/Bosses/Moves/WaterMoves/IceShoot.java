package me.iron_fist222.custombosses.Bosses.Moves.WaterMoves;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

public class IceShoot extends FallingBlockEntity {

    final private LivingEntity shooter; //the boss or entity that shot the falling ice
    final private LivingEntity target;
    private int lifeTime;
    public IceShoot(Location loc, LivingEntity owner,LivingEntity enemy){
        super(EntityType.FALLING_BLOCK,((CraftWorld) loc.getWorld()).getHandle());
        this.teleportTo(((CraftWorld)loc.getWorld()).getHandle(),new Vec3(loc.getX(),loc.getY(),loc.getZ()));
        CompoundTag thisData = new CompoundTag();
        addAdditionalSaveData(thisData);
        thisData.put("BlockState", NbtUtils.writeBlockState(Blocks.BLUE_ICE.defaultBlockState()));
        readAdditionalSaveData(thisData);
        this.shooter = owner;
        this.target = enemy;
        this.addTag("DeleteOnLanding");
        LivingEntity nearestEntity = this.getCommandSenderWorld().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT, null, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(5));
        if (nearestEntity!=null & nearestEntity!=this.shooter) {
            if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                System.out.println("it does intersect");
            }
        }
    }


    @Override
    public void tick() {

        this.move(MoverType.SELF, this.getDeltaMovement());
        ++this.lifeTime;
        if (this.lifeTime > 50) {
            this.discard();
        }


        LivingEntity nearestEntity;
      /*  List<LivingEntity> test = getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().inflate(10));
        for(LivingEntity entity : test){
            System.out.println(entity);
        }*/
        nearestEntity = this.getCommandSenderWorld().getNearestEntity(LivingEntity.class, TargetingConditions.DEFAULT, null, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(5));
        //System.out.println(this.getBoundingBox().intersects(nearestEntity.getBoundingBox()));
        /*if (nearestEntity != null) {
            if (this.getBoundingBox().intersects(nearestEntity.getBoundingBox())) {
                nearestEntity.setLastHurtByMob(this.shooter);
                CompoundTag effect = new CompoundTag();
                MobEffect slowEffect = MobEffects.MOVEMENT_SLOWDOWN;
                nearestEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3));
                nearestEntity.hurt(((Entity) this.shooter).damageSources().magic(), 3);
                this.discard();
            }else{
                System.out.println(this.getBoundingBox());
                System.out.println(nearestEntity.getBoundingBox());
                System.out.println(this.getBoundingBox().intersects(nearestEntity.getBoundingBox()));
            }
        }*/
        HitResult hit = ProjectileUtil.getEntityHitResult(this.level(),this,this.position(),this.getDeltaMovement().add(this.position()),this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1), this::canHitEntity);
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
        }
        /*
        if (this.getBoundingBox().intersects(target.getBoundingBox())){
            target.setLastHurtByMob(this.shooter);
            CompoundTag effect = new CompoundTag();
            MobEffect slowEffect = MobEffects.MOVEMENT_SLOWDOWN;
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 3));
            target.hurt(((Entity) this.shooter).damageSources().magic(), 3);
            this.discard();
        }*/
        if (this.onGround){
            this.discard();
        }


    }

    protected boolean canHitEntity(Entity entity){
        LivingEntity temp = null;
        if (!entity.canBeHitByProjectile()) {
            return false;
        }
        else{
            return true;
        }
    }



}