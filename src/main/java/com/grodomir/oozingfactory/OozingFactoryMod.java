package com.grodomir.oozingfactory;

import com.grodomir.oozingfactory.common.block.ModBlocks;
import com.grodomir.oozingfactory.common.block.custom.SlimeCauldron;
import com.grodomir.oozingfactory.common.fluid.BaseFluidTypes;
import com.grodomir.oozingfactory.common.fluid.ModFluidTypes;
import com.grodomir.oozingfactory.common.fluid.ModFluids;
import com.grodomir.oozingfactory.common.item.ModItems;
import com.grodomir.oozingfactory.particle.ModParticles;
import com.grodomir.oozingfactory.particle.SlimeFluidParticle;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(OozingFactoryMod.MODID)
public class OozingFactoryMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "oozingfactory";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public OozingFactoryMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModParticles.register(modEventBus);
        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);

        modEventBus.addListener(this::registerCauldronFluidContent);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    private void registerCauldronFluidContent(RegisterCauldronFluidContentEvent event){
        event.register(
                ModBlocks.SLIME_CAULDRON.get(),
                ModFluids.SOURCE_SLIME.get(),
                1000,
                null
        );
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents{

        @SubscribeEvent
        public static void registerParitcleFactories(RegisterParticleProvidersEvent event){
            event.registerSpriteSet(ModParticles.SLIME_FLUID_PARTICLES.get(), SlimeFluidParticle.Provider::new);
        }

        @SubscribeEvent
        public static void registerCauldronColor(RegisterColorHandlersEvent.Block event){
            event.register((state, level, pos, tintIndex) -> {
                if(tintIndex == 0){
                    return ModFluidTypes.SLIME_FLUID_TYPE.get().getTintColor();
                    //return 0xA17BCA52;
                }
                return -1;
            }, ModBlocks.SLIME_CAULDRON.get());
        }

        @SubscribeEvent
        public static void registerFluidExtensions(RegisterClientExtensionsEvent event){

            //BaseFluidTypes slime = (BaseFluidTypes) ModFluidTypes.SLIME_FLUID_TYPE.get();
            BaseFluidTypes slime = ModFluidTypes.SLIME_FLUID_TYPE.get();

            event.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return slime.getStillTexture();
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return slime.getFlowingTexture();
                }

                @Override
                public int getTintColor() {
                    return slime.getTintColor();
                }

                @Override
                public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                    return slime.getFogColor();
                }

                @Override
                public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                    RenderSystem.setShaderFogStart(1f);
                    RenderSystem.setShaderFogEnd(6f);
                }
            }, slime);
        }
    }
}