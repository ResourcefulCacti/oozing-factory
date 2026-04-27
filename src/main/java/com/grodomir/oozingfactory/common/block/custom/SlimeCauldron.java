package com.grodomir.oozingfactory.common.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SlimeCauldron extends AbstractCauldronBlock {
    public static final MapCodec<SlimeCauldron> CODEC = simpleCodec(SlimeCauldron::new);

    public SlimeCauldron(Properties properties) {
        super(properties, CauldronInteraction.EMPTY);
    }

    @Override
    protected MapCodec<SlimeCauldron> codec() {
        return CODEC;
    }

    @Override
    public boolean isFull(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return 3;
    }
}
