package com.grodomir.oozingfactory.common.block.entity;

import com.grodomir.oozingfactory.common.block.custom.BasicSieve;
import com.grodomir.oozingfactory.registry.OozingEntities;
import com.grodomir.oozingfactory.screen.BasicSieveMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class BasicSieveEntity extends BlockEntity implements MenuProvider {
    private static final int OUTPUT_SLOT = 0;

    protected final ContainerData data;

    //private final int SPEED = 60;
    private int progress = 0;
    private int max_progress = 60;

    public BasicSieveEntity(BlockPos pos, BlockState blockState) {
        super(OozingEntities.BASIC_SIEVE_BE.get(), pos, blockState);

        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index){
                    case 0 -> BasicSieveEntity.this.progress;
                    case 1 -> BasicSieveEntity.this.max_progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index){
                    case 0: BasicSieveEntity.this.progress = value; break;
                    case 1: BasicSieveEntity.this.max_progress = value; break;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public final ItemStackHandler itemHandler = new ItemStackHandler(1){
        @Override
        public int getSlotLimit(int slot) {
            return super.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.oozingfactory.basic_sieve");
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++){
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState){
        boolean slimeAbove = BasicSieve.getBlockAbove(level, blockPos).getBlock() instanceof SlimeBlock;
        if(!slimeAbove){
            resetProgress();
            return;
        }

        if(hasRecipe(level, blockPos)){
            increaseCraftingProgress();
            modifySpeedBasedOnLevel(level, blockPos);
            setChanged(level, blockPos, blockState);

            if(hasCraftingFinished()){
                craftItem();
                resetProgress();
            }
        }/*else {
            resetProgress();
        }*/
    }

    private void modifySpeedBasedOnLevel(Level level, BlockPos blockPos) {
        BlockPos blockAbove = blockPos.above();
        if(level.getBlockEntity(blockAbove) instanceof OozingBaseSlimeBlockEntity be){
            max_progress = switch (be.getCurrentLevel()){
                case 2 -> 50;
                case 3 -> 40;
                case 4 -> 30;
                case 5 -> 20;
                default-> 60;
            };
        }
    }

    private void craftItem() {
        ItemStack output = new ItemStack(Items.IRON_NUGGET);

        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    private void resetProgress() {
        progress = 0;
        //max_progress = SPEED;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.max_progress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe(Level level, BlockPos blockPos) {
        ItemStack output = new ItemStack(Items.IRON_NUGGET);
        BlockState block = BasicSieve.getBlockAbove(level, blockPos);
        return block.getBlock() instanceof SlimeBlock &&
                canInsertAmountIntoOutputslot(output.getCount()) &&
                canInsertItemIntoOutputSlot(output) &&
                BasicSieve.isCauldronEmpty(level, blockPos);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputslot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("basic_sieve.progress", progress);
        tag.putInt("basic_sieve.max_progress", max_progress);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("basic_sieve.progress");
        max_progress = tag.getInt("basic_sieve.max_progress");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BasicSieveMenu(containerId, playerInventory, this, this.data);
    }
}
