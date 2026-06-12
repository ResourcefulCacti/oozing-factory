package com.grodomir.oozingfactory.screen;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.screen.upgrade_station.UpgradeStationMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OozingMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, OozingFactoryMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<BasicSieveMenu>> BASIC_SIEVE_MENU =
            registerMenuType("basic_sieve_menu", BasicSieveMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<UpgradeStationMenu>> UPGRADE_STATION_MENU =
            registerMenuType("upgrade_station_menu", UpgradeStationMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
