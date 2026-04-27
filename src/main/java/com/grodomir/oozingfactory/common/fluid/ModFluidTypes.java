package com.grodomir.oozingfactory.common.fluid;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.particle.ModParticles;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

public class ModFluidTypes {
    public static final ResourceLocation SLIME_STILL_RL = ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "block/slime_still");
    public static final ResourceLocation SLIME_FLOWING_RL = ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "block/slime_flowing");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, OozingFactoryMod.MODID);

    public static final DeferredHolder<FluidType, BaseFluidTypes> SLIME_FLUID_TYPE =
            FLUID_TYPES.register("slime", () ->
                    new BaseFluidTypes(
                            SLIME_STILL_RL,
                            SLIME_FLOWING_RL,
                            0xA17BCA52 ,
                            new Vector3f(102f / 255f, 1f, 102f / 255f),
                            FluidType.Properties.create().density(3000).viscosity(6000).canSwim(false).canDrown(true).canExtinguish(false).canConvertToSource(false).supportsBoating(true).addDripstoneDripping(0.175F, ModParticles.SLIME_FLUID_PARTICLES.get(), Blocks.LAVA_CAULDRON, null)
                    ))
            ;

    public static void register(IEventBus eventBus){
        FLUID_TYPES.register(eventBus);
    }
}
