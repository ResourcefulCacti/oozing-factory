package com.grodomir.oozingfactory.compat;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.common.block.entity.OozingBaseSlimeBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class OozingJadeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static final OozingJadeProvider INSTANCE = new OozingJadeProvider();
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "slime_level");

    int maxXP = OozingBaseSlimeBlockEntity.MAX_XP;
    int maxLevel = OozingBaseSlimeBlockEntity.MAX_LEVEL;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag data = blockAccessor.getServerData();
        if(data.contains("xp")){
            int xp = data.getInt("xp");
            int currentLevel = data.getInt("current_level");
            if(currentLevel < maxLevel){
                iTooltip.add(Component.translatable("slimehover.oozingfactory.xp")
                        .append(Component.literal(": " + xp + "/" + maxXP)));
            }
            iTooltip.add(Component.translatable("slimehover.oozingfactory.lvl")
                    .append(Component.literal(": " + currentLevel + "/" + maxLevel)));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if(blockAccessor.getBlockEntity() instanceof OozingBaseSlimeBlockEntity blockEntity){
            compoundTag.putInt("xp", blockEntity.getXp());
            compoundTag.putInt("current_level", blockEntity.getCurrentLevel());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
