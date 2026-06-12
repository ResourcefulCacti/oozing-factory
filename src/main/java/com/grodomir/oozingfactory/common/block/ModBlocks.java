package com.grodomir.oozingfactory.common.block;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.common.block.custom.BasicSieve;
import com.grodomir.oozingfactory.common.block.custom.SlimeCauldron;
import com.grodomir.oozingfactory.common.block.custom.OozingBaseSlimeBlock;
import com.grodomir.oozingfactory.common.block.custom.upgrade_station.UpgradeStationBlock;
import com.grodomir.oozingfactory.common.fluid.ModFluids;
import com.grodomir.oozingfactory.common.item.ModItems;
import com.grodomir.oozingfactory.setup.OozingSetup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(OozingFactoryMod.MODID);
    public static final DeferredRegister.Blocks NON_ITEM_BLOCKS =
            DeferredRegister.createBlocks(OozingFactoryMod.MODID);

    public static final DeferredBlock<Block> BASIC_SIEVE = registerBlock("basic_sieve",
            () -> new BasicSieve(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F).noOcclusion().isValidSpawn(Blocks::never).randomTicks()));

    //To do: Advanced Sieve

    public static final DeferredBlock<Block> UPGRADE_STATION = registerBlock("upgrade_station",
            () -> new UpgradeStationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<LiquidBlock> SLIME_FLUID_BLOCK = NON_ITEM_BLOCKS.register("slime_fluid",
            () -> new LiquidBlock(ModFluids.SOURCE_SLIME.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static final DeferredBlock<Block> SLIME_CAULDRON = NON_ITEM_BLOCKS.register("slime_cauldron",
            () -> new SlimeCauldron(BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)));

    public static final DeferredBlock<Block> TEMP_IRON_SLIME = registerBlock("temp_iron_slime",
            () -> new OozingBaseSlimeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK)));

    public static final DeferredBlock<Block> TICK_SMTH = registerBlock("tick_smth",
            () -> new TickDelete(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_WOOL)));



    private static <T extends  Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static final Map<String, DeferredBlock<Block>> SLIME_MAP = new HashMap<>();

    private static void readThis(){
        OozingSetup.getLoaded().forEach((name, data) -> {
            data.ingredient();
            data.result();
            data.tier();
            customSlime(name);
            System.out.println("Test in loop for " + name + "\n" + data.ingredient().toString() +" && " + data.result().toString() + " of tier" + data.tier());
        });
        System.out.println("Test out of loop");
    }

    private static void customSlime(String id){
        String name = id + "_slime_block";
        DeferredBlock<Block> deferred = registerBlock(name,
                () -> new OozingBaseSlimeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK)));

        SLIME_MAP.put(id, deferred);
    }

    public static void register(IEventBus eventBus){
        readThis();
        BLOCKS.register(eventBus);
        NON_ITEM_BLOCKS.register(eventBus);
    }
}
