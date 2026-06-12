package com.grodomir.oozingfactory.common.block.entity.upgrade_station;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.registry.OozingEntities;
import com.grodomir.oozingfactory.screen.upgrade_station.UpgradeStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class UpgradeStationBlockEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_SLOT = 0;
    public static final int PROCESSING_SLOT = 1;

    private int overworld_energy = 0;
    private int nether_energy = 0;
    private int end_energy = 0;
    private int progress = 0;
    private int SPEED = 10;
    private int MAX_PROGRESS = SPEED;

    public static final int MAX_ENERGY = 1000;
    protected ContainerData data;

    public UpgradeStationBlockEntity(BlockPos pos, BlockState state) {
        super(OozingEntities.UPGRADE_STATION_BE.get(), pos, state);

        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> UpgradeStationBlockEntity.this.progress;
                    case 1 -> UpgradeStationBlockEntity.this.MAX_PROGRESS;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0: UpgradeStationBlockEntity.this.progress = value; break;
                    case 1: UpgradeStationBlockEntity.this.MAX_PROGRESS = value; break;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public final ItemStackHandler itemHandler = new ItemStackHandler(2){
        @Override
        public int getSlotLimit(int slot) {
            return super.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public final EnergyStorage feEnergyStorage = new EnergyStorage(
            1000,
            100,
            100
    );

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.oozingfactory.upgrade_station");
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++){
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert  this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState){
        if(hasRecipe()){
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);
            if(hasCraftingFinished()){
                craft();
                resetProgress();
            }
        }else{
            resetProgress();
        }
    }

    private void craft() {
        //addOverworldEnergy(100);
        feEnergyStorage.receiveEnergy(20, false);
        itemHandler.extractItem(INPUT_SLOT, 1, false);
    }

    private void resetProgress() {
        progress = 0;
        MAX_PROGRESS = SPEED;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.MAX_PROGRESS;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        ItemStack input = new ItemStack(Blocks.NETHERRACK);
        return itemHandler.getStackInSlot(INPUT_SLOT).is(input.getItem()) && canPutMoreEnergy();
    }

    private boolean canPutMoreEnergy() {
        return overworld_energy <= 900;
    }

    public void addOverworldEnergy(int amount){
        overworld_energy = addEnergy(overworld_energy, amount);
    }

    private int addEnergy(int current, int added){
        //return (current + added) >= MAX_ENERGY ? MAX_ENERGY : (current + added);
        return Math.min((current + added), MAX_ENERGY);
    }

    public int getOverworld_energy() {
        return overworld_energy;
    }

    public int getNether_energy() {
        return nether_energy;
    }

    public int getEnd_energy() {
        return end_energy;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("overworld_energy", overworld_energy);
        tag.putInt("nether_energy", nether_energy);
        tag.putInt("end_energy", end_energy);

        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("upgrade_station.progress", progress);
        tag.putInt("upgrade_station.max_progress", MAX_PROGRESS);

        tag.put("fe_energy", feEnergyStorage.serializeNBT(registries));

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        overworld_energy = tag.getInt("overworld_energy");
        nether_energy = tag.getInt("nether_energy");
        end_energy = tag.getInt("end_energy");

        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("upgrade_station.progress");
        MAX_PROGRESS = tag.getInt("upgrade_station.max_progress");

        feEnergyStorage.deserializeNBT(registries, tag.get("fe_energy"));

        super.loadAdditional(tag, registries);
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
        return new UpgradeStationMenu(containerId, playerInventory, this, this.data);
    }

    public IEnergyStorage getEnergyHandler(@Nullable Direction direction) {
        return feEnergyStorage;
    }
}
