package com.grodomir.oozingfactory.registry;

import com.grodomir.oozingfactory.common.block.ModBlocks;
import com.grodomir.oozingfactory.common.item.ModItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface OozingCauldronInteractions extends CauldronInteraction {
    CauldronInteraction FILL_SLIME = (state, level, pos, player, hand, stack) ->
            CauldronInteraction.emptyBucket(level, pos, player, hand, stack, ModBlocks.SLIME_CAULDRON.get().defaultBlockState(), SoundEvents.BUCKET_EMPTY);

    CauldronInteraction EMPTY_SLIME = (state, level, pos, player, hand, stack) ->
            CauldronInteraction.fillBucket(state, level, pos, player, hand, stack, new ItemStack(ModItems.SLIME_BUCKET.get()), blockstate -> true, SoundEvents.BUCKET_FILL);

    CauldronInteraction.InteractionMap SLIME = CauldronInteraction.newInteractionMap("slime");

    static void register(){
        EMPTY.map().put(ModItems.SLIME_BUCKET.get(), FILL_SLIME);
        WATER.map().put(ModItems.SLIME_BUCKET.get(), FILL_SLIME);
        LAVA.map().put(ModItems.SLIME_BUCKET.get(), FILL_SLIME);
        POWDER_SNOW.map().put(ModItems.SLIME_BUCKET.get(), FILL_SLIME);
        SLIME.map().put(ModItems.SLIME_BUCKET.get(), FILL_SLIME);

        SLIME.map().put(Items.BUCKET, EMPTY_SLIME);

        CauldronInteraction.addDefaultInteractions(SLIME.map());
    }
}
