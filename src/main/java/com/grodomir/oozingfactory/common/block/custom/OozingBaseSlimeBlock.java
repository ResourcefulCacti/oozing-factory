package com.grodomir.oozingfactory.common.block.custom;

import com.grodomir.oozingfactory.common.block.entity.OozingBaseSlimeBlockEntity;
import com.grodomir.oozingfactory.registry.OozingEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OozingBaseSlimeBlock extends SlimeBlock implements EntityBlock {
    public OozingBaseSlimeBlock(Properties properties) {
        super(properties);
    }

    int xp = 0;
    int maxXP = 10;
    int level = 1;
    int maxLevel = 5;

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        super.updateEntityAfterFallOn(level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("slimehover.oozingfactory.xp")
                .append(Component.literal(textFormat(xp, maxXP))));

        tooltipComponents.add(Component.translatable("slimehover.oozingfactory.lvl")
                .append(Component.literal(textFormat(level, maxLevel))));
    }

    private String textFormat(int current, int max){
        return ": " + current + "/" + max;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OozingBaseSlimeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == OozingEntities.BASE_SLIME_BE.get()){
            return (level1, pos, state1, blockEntity) -> OozingBaseSlimeBlockEntity.tick(level1, pos, state1, (OozingBaseSlimeBlockEntity) blockEntity);
        }
        return null;
    }
}
