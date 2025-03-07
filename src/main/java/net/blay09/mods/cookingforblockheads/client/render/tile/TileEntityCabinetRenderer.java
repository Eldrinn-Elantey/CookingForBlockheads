package net.blay09.mods.cookingforblockheads.client.render.tile;

import net.blay09.mods.cookingforblockheads.CookingConfig;
import net.blay09.mods.cookingforblockheads.client.model.ModelCabinet;
import net.blay09.mods.cookingforblockheads.client.render.RenderUtils;
import net.blay09.mods.cookingforblockheads.tile.TileCabinet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntityCabinetRenderer extends TileEntityRendererBase {

    private static final ResourceLocation textureSmall =
            new ResourceLocation("cookingforblockheads", "textures/entity/ModelCabinet.png");

    private ModelCabinet model = new ModelCabinet();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float delta) {
        int metadata = 0;
        TileCabinet tileCabinet = (TileCabinet) tileEntity;
        final boolean isDoorFlipped = tileCabinet.isFlipped();
        final int dye = tileCabinet.getColor();
        if (tileEntity.hasWorldObj()) {
            metadata = tileEntity.getBlockMetadata();
        }

        GL11.glPushMatrix();
        boolean oldRescaleNormal = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
        if (oldRescaleNormal) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glTranslatef((float) x, (float) y + 1f, (float) z);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        float angle = RenderUtils.getAngle(metadata);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glRotatef(angle, 0f, 1f, 0f);
        GL11.glRotatef(180f, 0f, 0f, 1f);
        float doorAngle =
                tileCabinet.getPrevDoorAngle() + (tileCabinet.getDoorAngle() - tileCabinet.getPrevDoorAngle()) * delta;
        doorAngle = 1.0f - doorAngle;
        doorAngle = 1.0f - doorAngle * doorAngle * doorAngle;

        bindTexture(textureSmall);

        model.setFlipped(isDoorFlipped);
        if (isDoorFlipped) {
            model.DoorFlipped.rotateAngleY = -(float) ((Math.PI / 2f) * doorAngle);
            model.DoorHandleFlipped.rotateAngleY = -(float) ((Math.PI / 2f) * doorAngle);
        } else {
            model.Door.rotateAngleY = (float) ((Math.PI / 2f) * doorAngle);
            model.DoorHandle.rotateAngleY = (float) ((Math.PI / 2f) * doorAngle);
        }

        model.renderUncolored();
        GL11.glColor4f(colorTable[dye][0], colorTable[dye][1], colorTable[dye][2], 1f);
        model.renderColored();

        if (doorAngle > 0f) {
            GL11.glColor4f(1f, 1f, 1f, 1f);
            model.renderInterior();
            GL11.glRotatef(180f, 0f, 0f, -1f);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            if (!CookingConfig.disableItemRender && Minecraft.getMinecraft().gameSettings.fancyGraphics) {
                RenderItem.renderInFrame = true;
                for (int i = 0; i < tileCabinet.getSizeInventory(); i++) {
                    final ItemStack itemStack = tileCabinet.getStackInSlot(i);
                    int relSlot;
                    if (itemStack != null) {
                        float itemX, itemY, itemZ, partialTickTime;
                        relSlot = i;
                        if (i > tileCabinet.getSizeInventory() / 2) {
                            relSlot -= tileCabinet.getSizeInventory() / 2;
                        }
                        itemX = (relSlot > 8)
                                ? Math.min(4f / 5f, (relSlot - 9) / 5f)
                                : Math.min(8f / 9f, (float) relSlot / 9f);
                        itemY = -2f + ((i > tileCabinet.getSizeInventory() / 2) ? -0.7f : 0.01f);
                        itemZ = (relSlot > 8) ? -0.8f : -0.1f;
                        partialTickTime = 0f;

                        if (relSlot % 2 == 0) {
                            itemZ -= 0.1f;
                        }

                        tileCabinet.getRenderItem().setEntityItemStack(itemStack);
                        RenderManager.instance.renderEntityWithPosYaw(
                                tileCabinet.getRenderItem(), 0.45f - itemX, itemY, 0.5f + itemZ, 0f, partialTickTime);
                    }
                }
                RenderItem.renderInFrame = false;
            }
        }

        if (!oldRescaleNormal) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }
}
