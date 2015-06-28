package goldenapple.rfdrills.util;

import goldenapple.rfdrills.item.IEnergyTool;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public class ToolHelper {
    public static void damageTool(ItemStack itemStack, EntityPlayer player, int energy){
        if(player.capabilities.isCreativeMode) return;
        if(!(itemStack.getItem() instanceof IEnergyTool)) return;

        IEnergyTool tool = (IEnergyTool)itemStack.getItem();

        player.setCurrentItemOrArmor(0, tool.drainEnergy(itemStack, energy));
        if (tool.getEnergyStored(itemStack) == 0 && tool.getTier(itemStack).canBreak) {
            player.renderBrokenItemStack(itemStack);
            player.destroyCurrentEquippedItem();
            player.addStat(StatList.objectBreakStats[Item.getIdFromItem(itemStack.getItem())], 1);
        }

        return;
    }

    public static void harvestBlock(World world, int x, int y, int z, EntityPlayer player){
        Block block = world.getBlock(x, y, z);
        if (block.getBlockHardness(world, x, y, z) < 0) {
            return;
        }
        int meta = world.getBlockMetadata(x, y, z);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x, y, z, world, block, meta, player);

        if(!MinecraftForge.EVENT_BUS.post(event)){ //if the event is canceled
            if (block.canHarvestBlock(player, meta)) {
                block.onBlockHarvested(world, x, y, z, meta, player);
                block.harvestBlock(world, player, x, y, z, meta);
            }
            world.setBlockToAir(x, y, z);
        }
    }
}
