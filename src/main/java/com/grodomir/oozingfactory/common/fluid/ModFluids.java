package com.grodomir.oozingfactory.common.fluid;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.common.block.ModBlocks;
import com.grodomir.oozingfactory.common.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, OozingFactoryMod.MODID);


    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOURCE_SLIME = FLUIDS.register("source_slime",
            () -> new BaseFlowingFluid.Source(ModFluids.SLIME_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_SLIME = FLUIDS.register("flowing_slime",
            () -> new BaseFlowingFluid.Flowing(ModFluids.SLIME_PROPERTIES));


    public static final BaseFlowingFluid.Properties SLIME_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.SLIME_FLUID_TYPE, SOURCE_SLIME, FLOWING_SLIME).slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SLIME_FLUID_BLOCK).bucket(ModItems.SLIME_BUCKET);

    public static void register(IEventBus eventBus){
        FLUIDS.register(eventBus);
    }

}
