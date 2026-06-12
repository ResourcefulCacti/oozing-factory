package com.grodomir.oozingfactory.registry;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.common.block.ModBlocks;
import com.grodomir.oozingfactory.common.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class OozingCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OozingFactoryMod.MODID);

    public static final Supplier<CreativeModeTab> OOZING_CREATIVE_TAB = CREATIVE_MODE_TAB.register("oozing_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Blocks.SLIME_BLOCK))
                    .title(Component.translatable("creativetab.oozingfactory.oozing_tab"))
                    .displayItems((display, output) -> {
                        ModItems.ITEMS.getEntries().forEach(item ->
                                output.accept(item.get()));
                        ModBlocks.BLOCKS.getEntries().forEach(block ->
                                output.accept(block.get()));
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
