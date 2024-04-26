package me.iron_fist222.custombosses.Bosses.Entitys;


import com.mojang.serialization.Dynamic;
import me.iron_fist222.custombosses.Bosses.Moves.MoveTemplate;
import me.iron_fist222.custombosses.Bosses.Moves.WaterMoves.IcePush;
import me.iron_fist222.custombosses.Bosses.Moves.WaterMoves.IceShoot;
import me.iron_fist222.custombosses.Bosses.Moves.WaterMoves.IceSpike;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.util.Vector;
import org.joml.Vector3d;

import javax.annotation.Nullable;


public class WaterBoss extends Villager implements RangedAttackMob {

    float attackCooldown;
    float attackTime;
    public WaterBoss(Location loc){
        super(EntityType.VILLAGER,((CraftWorld) loc.getWorld()).getHandle());
        ServerLevel server = ((CraftWorld) loc.getWorld()).getHandle();
        this.teleportTo(loc.getX(),loc.getY(),loc.getZ());
        this.goalSelector.addGoal(1,new RangedAttackGoal(this,1.0,1,1));
        this.attackCooldown = 3;
    }


    @Override
    public void refreshBrain(ServerLevel worldserver) {}

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        VillagerProfession villagerprofession = this.getVillagerData().getProfession();
        Brain<Villager> behaviorcontroller = this.brainProvider().makeBrain(dynamic);
        return behaviorcontroller;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0,new FloatGoal(this));
        this.goalSelector.addGoal(1,new RangedAttackGoal(this,0.6,5,20)); //CHANGE THIS TO 12 WHEN DONE DEBUGGINg
        this.goalSelector.addGoal(2,new WaterAvoidingRandomStrollGoal(this,0.5));
        this.goalSelector.addGoal(3,new LookAtPlayerGoal(this, Player.class,3.0f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float distance)/*note if distance is 1 it is in range*/ {
        ++this.attackTime;
        if (this.getTarget() != null & distance < 1 & this.attackTime > this.attackCooldown)
        {

            this.attackTime = 0;
            World world = this.getBukkitEntity().getWorld();
            Level msWorld = ((CraftWorld)world).getHandle();
            //get enemy that it wants to attack
            LivingEntity target = this.getTarget();
            float speed = 2f;
            //get the direction to target
            Vector3d DirectionToTarget = new Vector3d(target.getX()-this.getX(),target.getY()-this.getY(),target.getZ()-this.getZ());
            //normalize the direction so that a farther player wont make the projectile start 10 blocks away form boss
            DirectionToTarget = DirectionToTarget.normalize();
            //multiply the direction by 10 so that it will be 1 block in front of boss NOT USED RN
            Vector velocity = new Vector(DirectionToTarget.x*speed,DirectionToTarget.y*speed,DirectionToTarget.z*speed);
            Location loc = this.getBukkitEntity().getLocation();
//            IcePush icePush = new IcePush(msWorld,new Vec3(loc.getX(),loc.getY()+1,loc.getZ()),new Vec3(velocity.getX(),velocity.getY(),velocity.getZ()),new Vec3(DirectionToTarget.x,DirectionToTarget.y,DirectionToTarget.z),3,(LivingEntity) this);
            IceSpike iceSpike = new IceSpike(msWorld,new Vec3(loc.getX(),loc.getY()+1,loc.getZ()),new Vec3(velocity.getX(),velocity.getY(),velocity.getZ()),new Vec3(DirectionToTarget.x,DirectionToTarget.y,DirectionToTarget.z),2,(LivingEntity) this);


        }

    }

    @Override
    public void handleEntityEvent(byte b0) {}

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity entityliving) {}
}
