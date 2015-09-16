package goldenapple.rfdrills.compat.waila;

import goldenapple.rfdrills.item.*;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.StringHelper;
import goldenapple.rfdrills.util.ToolHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class WailaCompat implements IWailaDataProvider {
    //Singleton
    private static WailaCompat instance;

    private WailaCompat(){}

    public static WailaCompat getInstance(){
        if(instance == null){
            instance = new WailaCompat();
        }
        return instance;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        MovingObjectPosition pos = accessor.getPosition();
        int meta = accessor.getMetadata();

        if(accessor.getPlayer().getCurrentEquippedItem() == null || !(accessor.getPlayer().getCurrentEquippedItem().getItem() instanceof IEnergyTool))
            return tooltip;

        ItemStack equipStack = accessor.getPlayer().getCurrentEquippedItem();
        IEnergyTool energyTool = (IEnergyTool) equipStack.getItem();

        //for disguised blocks
        if(stack.getItem() instanceof ItemBlock){
            block = Block.getBlockFromItem(stack.getItem());
            meta = stack.getItemDamage();
        }

        if(equipStack.getItem() instanceof IEnergyTool){
            if(config.getConfig("rfdrills.waila_rf")) {
                boolean harvest = ToolHelper.isToolEffective(equipStack, block, meta) || block.getBlockHardness(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ) == 0;
                tooltip.add(StringHelper.writeEnergyPerBlockInfo(energyTool.getEnergyPerUse(equipStack, block, meta), harvest));

            }
            if(config.getConfig("rfdrills.waila_mode") && (accessor.getPlayer().isSneaking() || !(config.getConfig("rfdrills.waila_mode.sneakingonly")))) {
                if (!energyTool.writeModeInfo(equipStack).isEmpty()) {
                    tooltip.add(energyTool.writeModeInfo(equipStack));
                }
            }
        }

        return tooltip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return tooltip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        return tag;
    }

    public static void init(IWailaRegistrar registrar){
        registrar.addConfig(Reference.MOD_NAME, "rfdrills.waila_rf", "option.rfdrills.waila_rf");
        registrar.addConfig(Reference.MOD_NAME, "rfdrills.waila_mode", "option.rfdrills.waila_mode");
        registrar.addConfig(Reference.MOD_NAME, "rfdrills.waila_mode.sneakingonly", "option.rfdrills.waila_mode.sneakingonly");

        registrar.registerBodyProvider(getInstance(), Block.class);
    }
}
