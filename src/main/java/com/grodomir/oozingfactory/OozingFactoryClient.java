package com.grodomir.oozingfactory;

import com.grodomir.oozingfactory.common.fluid.ModFluidTypes;
import com.grodomir.oozingfactory.common.fluid.ModFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = OozingFactoryMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = OozingFactoryMod.MODID, value = Dist.CLIENT)
public class OozingFactoryClient {
    public OozingFactoryClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        OozingFactoryMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        OozingFactoryMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        //ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SLIME.get(), RenderType.translucent());
        //ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SLIME.get(), RenderType.translucent());
    }
}
