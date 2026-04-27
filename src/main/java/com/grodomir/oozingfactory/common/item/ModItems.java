package com.grodomir.oozingfactory.common.item;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.common.fluid.ModFluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OozingFactoryMod.MODID);

    public static final DeferredItem<BucketItem> SLIME_BUCKET = ITEMS.register("slime_bucket",
            () -> new BucketItem(ModFluids.SOURCE_SLIME.get(), new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
