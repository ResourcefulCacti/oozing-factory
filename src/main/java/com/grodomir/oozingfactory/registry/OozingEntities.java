package com.grodomir.oozingfactory.registry;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.common.block.ModBlocks;
import com.grodomir.oozingfactory.common.block.custom.OozingBaseSlimeBlock;
import com.grodomir.oozingfactory.common.block.entity.BasicSieveEntity;
import com.grodomir.oozingfactory.common.block.entity.OozingBaseSlimeBlockEntity;
import com.grodomir.oozingfactory.common.block.entity.upgrade_station.UpgradeStationBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class OozingEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, OozingFactoryMod.MODID);

    public static final Supplier<BlockEntityType<OozingBaseSlimeBlockEntity>> BASE_SLIME_BE =
            BLOCK_ENTITIES.register("base_slime_be", () -> new BlockEntityType<>(
                    OozingBaseSlimeBlockEntity::new, Set.of(), null) {
                @Override
                public boolean isValid(BlockState state) {
                    return state.getBlock() instanceof OozingBaseSlimeBlock;
                }
            });

    public static final Supplier<BlockEntityType<BasicSieveEntity>> BASIC_SIEVE_BE =
            BLOCK_ENTITIES.register("basic_sieve_be", () -> BlockEntityType.Builder.of(
                    BasicSieveEntity::new, ModBlocks.BASIC_SIEVE.get()).build(null));

    public static final Supplier<BlockEntityType<UpgradeStationBlockEntity>> UPGRADE_STATION_BE =
            BLOCK_ENTITIES.register("upgrade_station_be", () -> BlockEntityType.Builder.of(
                    UpgradeStationBlockEntity::new, ModBlocks.UPGRADE_STATION.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,
                UPGRADE_STATION_BE.get(), UpgradeStationBlockEntity::getEnergyHandler);
    }
}
