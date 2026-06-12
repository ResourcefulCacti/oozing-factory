package com.grodomir.oozingfactory.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TickDelete extends Block {
    int delayedTick = 0;

    public TickDelete(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.scheduleTick(pos, this, 0);
        System.out.println(delayedTick);
        delayedTick++;
        if(delayedTick >=20){
            System.out.println("2");
            level.removeBlock(pos, false);
            delayedTick = 0;
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        System.out.println("Placed");
        level.scheduleTick(pos, this, 1);
    }
}
