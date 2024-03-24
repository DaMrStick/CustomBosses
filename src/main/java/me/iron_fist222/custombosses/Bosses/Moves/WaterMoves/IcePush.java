package me.iron_fist222.custombosses.Bosses.Moves.WaterMoves;

import me.iron_fist222.custombosses.Bosses.Moves.MoveTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

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




}
