package com.grodomir.oozingfactory.common.block.custom;

import com.grodomir.oozingfactory.common.item.ModItems;
import com.grodomir.oozingfactory.registry.OozingCauldronInteractions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class SlimeCauldron extends AbstractCauldronBlock {
    public static final MapCodec<SlimeCauldron> CODEC = simpleCodec(SlimeCauldron::new);

    public SlimeCauldron(Properties properties) {
        super(properties, OozingCauldronInteractions.SLIME);
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
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(Items.CAULDRON);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return 3;
    }
}
