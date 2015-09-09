package goldenapple.rfdrills.util;

import goldenapple.rfdrills.item.IEnergyTool;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

public class ToolHelper {

    public static void drainEnergy(ItemStack stack, EntityPlayer player, int energy){
        if(player.capabilities.isCreativeMode) return;

        IEnergyTool tool = (IEnergyTool)stack.getItem();

        tool.drainEnergy(stack, energy);
        player.setCurrentItemOrArmor(0, stack);

        if (tool.getEnergyStored(stack) <= 0 && tool.getTier(stack).canBreak) {
            player.renderBrokenItemStack(stack);
            player.destroyCurrentEquippedItem();
            player.addStat(StatList.objectBreakStats[Item.getIdFromItem(stack.getItem())], 1);
        }
    }

    public static void harvestBlock(World world, int x, int y, int z, EntityPlayer entityPlayer){
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        if (!(entityPlayer instanceof EntityPlayerMP) || block.getBlockHardness(world, x, y, z) < 0 || !ForgeHooks.canHarvestBlock(block, entityPlayer, meta))
            return;
        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world, player.theItemInWorldManager.getGameType(), player, x, y, z);
        if(event.isCanceled())
            return;

        if(!world.isRemote){ //Server-side: Simulating ItemInWorldManager
            block.onBlockHarvested(world, x, y, z, meta, player);

            if(block.removedByPlayer(world, player, x, y, z, true)) {
                block.onBlockDestroyedByPlayer(world, x, y, z, meta);
                block.harvestBlock(world, player, x, y, z, meta);
                block.dropXpOnBlockBreak(world, x, y, z, event.getExpToDrop());
            }
        }else { //Client-side: Simulating PlayerControllerMP
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + meta << 12);

            if(block.removedByPlayer(world, player, x, y ,z, true))
                block.onBlockDestroyedByPlayer(world, x, y, z, meta);
        }
    }
}
