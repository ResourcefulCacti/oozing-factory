package com.grodomir.oozingfactory.common.block.entity;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.registry.OozingEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class OozingBaseSlimeBlockEntity extends BlockEntity {
    private int xp = 0;
    private int current_level = 1;
    public static final int MAX_XP = 5;
    public static final int MAX_LEVEL = 5;

    private int tickCounter = 0;

    public OozingBaseSlimeBlockEntity(BlockPos pos, BlockState blockState) {
        super(OozingEntities.BASE_SLIME_BE.get(), pos, blockState);
    }

    public void addXP(int amount){
        if (current_level >= MAX_LEVEL) return;
        xp += amount;
        if (xp >= MAX_XP){
            xp = 0;
            current_level++;
        }
        setChanged();
    }

    public int getXp(){
        return xp;
    }

    public int getCurrentLevel(){
        return current_level;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, OozingBaseSlimeBlockEntity oozingEntity){
        /*if(!level.isClientSide()){
            //System.out.println("Block name: " + BuiltInRegistries.BLOCK.getKey(blockState.getBlock()));
            OozingFactoryMod.LOGGER.info("Block name: {}", BuiltInRegistries.BLOCK.getKey(blockState.getBlock()));
        }*/
        if(!level.isClientSide()){
            oozingEntity.tickCounter++;
            if(oozingEntity.tickCounter >= 40){
                oozingEntity.tickCounter = 0;
                oozingEntity.addXP(1);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("xp", xp);
        tag.putInt("current_level", current_level);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        xp = tag.getInt("xp");
        current_level = tag.getInt("current_level");
        super.loadAdditional(tag, registries);
    }
}
