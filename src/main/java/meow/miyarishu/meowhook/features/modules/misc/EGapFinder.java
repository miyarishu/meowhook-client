package meow.miyarishu.meowhook.features.modules.misc;

import meow.miyarishu.meowhook.features.modules.Module;
import meow.miyarishu.meowhook.features.setting.Setting;
import meow.miyarishu.meowhook.util.Util;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.world.World;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EGapFinder extends Module {
    public Setting<Boolean> enchGap = register(new Setting("EGap", true));
    public Setting<Boolean> mendingBook = register(new Setting("Mending", false));
    public EGapFinder(){
        super("EGapFinder", "", Category.MISC, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (Util.mc.world.isRemote)
            return;

        World world = Util.mc.world;

        EntityPlayer player = null;

        if (!world.playerEntities.isEmpty()) {
            player = (EntityPlayer)world.playerEntities.get(0);



            for (TileEntity tile : world.loadedTileEntityList) {
                if (tile instanceof TileEntityLockableLoot) {
                    TileEntityLockableLoot lockable = (TileEntityLockableLoot)tile;
                    if (lockable.getLootTable() != null) {
                        lockable.fillWithLoot(player);
                        for (int i = 0; i < lockable.getSizeInventory(); i++) {
                            ItemStack stack = lockable.getStackInSlot(i);
                            if (stack.getItem() == Items.GOLDEN_APPLE && stack.getItemDamage() == 1) {
                                if (enchGap.getValue()) {
                                    writeToFile("Dungeon Chest with ench gapple at: " + lockable.getPos().getX() + " " + lockable.getPos().getY() + " " + lockable.getPos().getZ());
                                }
                            }
                            if (stack.getItem() == Items.ENCHANTED_BOOK &&
                                    EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0) {
                                if (mendingBook.getValue()) {
                                    writeToFile("Dungeon Chest with Mending Book: " + lockable.getPos().getX() + " " + lockable.getPos().getY() + " " + lockable.getPos().getZ());
                                }
                            }
                        }
                    }
                }
            }

            for (Entity entity : world.loadedEntityList) {
                if (entity instanceof EntityMinecartContainer) {
                    EntityMinecartContainer cart = (EntityMinecartContainer)entity;
                    if (cart.getLootTable() != null) {
                        cart.addLoot(player);
                        for (int i = 0; i < cart.itemHandler.getSlots(); i++) {
                            ItemStack stack = cart.itemHandler.getStackInSlot(i);
                            if (stack.getItem() == Items.GOLDEN_APPLE && stack.getItemDamage() == 1) {
                                if (enchGap.getValue()) {
                                    writeToFile("Minecart with ench gapple at: " + cart.posX + " " + cart.posY + " " + cart.posZ);
                                }
                            }
                            if (stack.getItem() == Items.ENCHANTED_BOOK &&
                                    EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0) {
                                if (mendingBook.getValue()) {
                                    writeToFile("Minecart with Mending at: " + cart.posX + " " + cart.posY + " " + cart.posZ);
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    protected static void writeToFile(String coords) {
        try(FileWriter fw = new FileWriter("FoundCoords.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println(coords);
        } catch (IOException iOException) {}
    }

}