package net.blay09.mods.cookingforblockheads.tile;

import net.blay09.mods.cookingforblockheads.api.kitchen.IKitchenStorageProvider;
import net.minecraft.inventory.IInventory;

public class TileCounter extends BaseKitchenTileWithInventory implements IInventory, IKitchenStorageProvider {
    protected static final String name = "counter";
    public TileCounter() {
        super(name);
    }


    @Override
    public boolean receiveClientEvent(int id, int value) {
        return super.receiveClientEvent(id, value);
    }

}