package goldenapple.rfdrills.util;

import cpw.mods.fml.common.eventhandler.Event;
import goldenapple.rfdrills.item.IEnergyTool;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;

public class ToolHelper {
    public static boolean isToolEffective(ItemStack stack, Block block, int meta){
        if(block == null)
            return false;

        if(stack != null) {
            for (String toolClass : stack.getItem().getToolClasses(stack)) {
                if (toolClass.equals(block.getHarvestTool(meta)))
                    return stack.getItem().getHarvestLevel(stack, toolClass) >= block.getHarvestLevel(meta);
            }

            return stack.getItem().canHarvestBlock(block, stack);
        }
        return false;
    }

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

        if (!(entityPlayer instanceof EntityPlayerMP))
            return;
        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        if(!((isToolEffective(player.getCurrentEquippedItem(), block, meta) && block.getBlockHardness(world, x, y, z) >= 0) || player.capabilities.isCreativeMode))
            return;

        BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world, player.theItemInWorldManager.getGameType(), player, x, y, z);
        if(event.isCanceled())
            return;

        if(!world.isRemote){ //Server-side: Simulating ItemInWorldManager
            block.onBlockHarvested(world, x, y, z, meta, player);

            if(block.removedByPlayer(world, player, x, y, z, true)) {
                block.onBlockDestroyedByPlayer(world, x, y, z, meta);
                if(!player.capabilities.isCreativeMode && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
                    block.harvestBlock(world, player, x, y, z, meta);
                    block.dropXpOnBlockBreak(world, x, y, z, event.getExpToDrop());
                }
                world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + meta << 12);
            }
        }else { //Client-side: Simulating PlayerControllerMP

            if(block.removedByPlayer(world, player, x, y ,z, true))
                block.onBlockDestroyedByPlayer(world, x, y, z, meta);
        }
    }

    public static boolean hoeBlock(ItemStack stack, World world, int x, int y, int z, int sideHit, EntityPlayer player){
        Block block = world.getBlock(x, y, z);

        UseHoeEvent event = new UseHoeEvent(player, stack, world, x, y, z);
        if(MinecraftForge.EVENT_BUS.post(event)) //if the event got canceled
            return false;

        if (event.getResult() == Event.Result.ALLOW) //if another mod handled this block using the event
            return true;

        //vanilla hoe behaviour
        if (sideHit != 0 && world.isAirBlock(x, y + 1, z) && (block == Blocks.grass || block == Blocks.dirt) && player.canPlayerEdit(x, y, z, sideHit, stack)) {
            if (!world.isRemote)
                world.setBlock(x, y, z, Blocks.farmland);
            return true;
        }

        return false;
    }
}
