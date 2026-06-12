package com.grodomir.oozingfactory.screen.upgrade_station;

import com.grodomir.oozingfactory.common.block.ModBlocks;
import com.grodomir.oozingfactory.common.block.entity.upgrade_station.UpgradeStationBlockEntity;
import com.grodomir.oozingfactory.screen.OozingMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class UpgradeStationMenu extends AbstractContainerMenu {
    public final UpgradeStationBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public UpgradeStationMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public UpgradeStationMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data){
        super(OozingMenuTypes.UPGRADE_STATION_MENU.get(), containerId);
        this.blockEntity = ((UpgradeStationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 0, 54, 17));
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 1, 104, 34));

        addDataSlots(data);
    }

    public boolean isCrafting(){
        return this.data.get(0) > 0;
    }

    public int getScaledArrowProgres(){
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    public int getOverworldProgress(){
        int energy  = blockEntity.getOverworld_energy();
        int height = 35;

        return energy * height / 1000;
    }

    private void addPlayerHotbar(Inventory inv) {
        for(int i = 0; i < 9; ++i){
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }

    private void addPlayerInventory(Inventory inv) {
        for(int i = 0; i < 3; ++i){
            for(int l = 0; l < 9; ++l){
                this.addSlot(new Slot(inv, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.UPGRADE_STATION.get());
    }
}
