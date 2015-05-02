package goldenapple.rfdrills.item;

import cofh.api.energy.IEnergyContainerItem;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.DrillTier;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import goldenapple.rfdrills.util.MiscUtil;
import goldenapple.rfdrills.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.List;

public class ItemChainsaw extends ItemAxe implements IEnergyContainerItem{
    private final String name;
    private final DrillTier tier;

    public ItemChainsaw(String name, DrillTier tier){
        super(tier.material);
        this.name = name;
        this.tier = tier;
        this.setCreativeTab(RFDrills.OmniDrillsTab);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        list.add(setEnergy(new ItemStack(item, 1, 0), 0));
        list.add(setEnergy(new ItemStack(item, 1, 0), tier.maxEnergy));
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return tier.rarity;
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        return true;
    }

    public boolean func_150897_b(Block p_150897_1_){ //stolen from ItemShears
        return p_150897_1_ == Blocks.web || p_150897_1_ == Blocks.redstone_wire || p_150897_1_ == Blocks.tripwire;
    }

    @Override
    public float getDigSpeed(ItemStack itemStack, Block block, int meta) {
        itemStack.setItemDamage(0);
        if(getEnergyStored(itemStack) >= tier.energyPerBlock){
            if(block instanceof IShearable && getMode(itemStack) == 1) {
                return 100.0F;
            }else {
                return super.getDigSpeed(itemStack, block, meta);
            }
        }else{
            return 0.5F;
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return Math.max(1.0 - (double)getEnergyStored(itemStack) / (double)tier.maxEnergy, 0);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(!world.isRemote && player.isSneaking() && tier.hasModes) {
            switch (getMode(itemStack)) {
                case 0:
                    setMode(itemStack, (byte) 1);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("rfdrills.shears_on.mode")));
                    break;
                case 1:
                    setMode(itemStack, (byte) 0);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("rfdrills.shears_off.mode")));
                    break;
                default:
                    setMode(itemStack, (byte) 1);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("rfdrills.shears_on.mode")));
                    LogHelper.warn("Illegal chainsaw mode! Resetting to 1");
                    break;
            }
        }
        return itemStack;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity) {
        if(getMode(itemStack) == 1) {
            if ((getEnergyStored(itemStack) >= tier.energyPerBlock || tier.canBreak) && Items.shears.itemInteractionForEntity(itemStack, player, entity)) {
                itemStack.setItemDamage(0);
                if(!player.capabilities.isCreativeMode) {
                    player.setCurrentItemOrArmor(0, drainEnergy(itemStack, tier.energyPerBlock));

                    if (this.getEnergyStored(itemStack) == 0 && tier.canBreak) {
                        itemStack.damageItem(1000000, entity);
                    }
                }
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, int x, int y, int z, EntityPlayer player) {
        if(getMode(itemStack) == 1 && getEnergyStored(itemStack) >= tier.energyPerBlock / 5) {
            if (Items.shears.onBlockStartBreak(itemStack, x, y, z, player)) {
                itemStack.setItemDamage(0);
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.isCreativeMode) {
            entity.setCurrentItemOrArmor(0, drainEnergy(itemStack, (block instanceof IShearable && getMode(itemStack) == 1) ? tier.energyPerBlock / 5 : tier.energyPerBlock));

            if (this.getEnergyStored(itemStack) == 0 && tier.canBreak) {
                itemStack.damageItem(1000000, entity);
            }
            return !(block instanceof IShearable && getMode(itemStack) == 1);
        }

        return false;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityAttacked, EntityLivingBase entityAttacker) {
        if(entityAttacker instanceof EntityPlayer && !((EntityPlayer)entityAttacker).capabilities.isCreativeMode) {
            entityAttacker.setCurrentItemOrArmor(0, drainEnergy(itemStack, tier.energyPerBlock * 2));

            if (this.getEnergyStored(itemStack) == 0 && tier.canBreak) {
                itemStack.damageItem(1000000, entityAttacker);
            }
            return true;
        }

        return false;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean what) {
        try {
            list.add(StringHelper.writeEnergyInfo(getEnergyStored(itemStack), tier.maxEnergy));

            if (MiscUtil.isShiftPressed()) {
                list.add(StringHelper.writeEnergyPerBlockInfo(tier.energyPerBlock));
                if (tier.canBreak) {
                    list.add(StatCollector.translateToLocal("rfdrills.can_break.tooltip"));
                }
                if (toolMaterial.getEnchantability() > 0) {
                    list.add(StatCollector.translateToLocal("rfdrills.enchantable.tooltip"));
                }
                if (tier.hasModes) {
                    list.add(StatCollector.translateToLocal("rfdrills.chainsaw_has_modes.tooltip"));
                }
            } else {
              //list.add(StatCollector.translateToLocal("info.cofh.hold") + " §e§o" + StatCollector.translateToLocal("info.cofh.shift") + " §r§7" + StatCollector.translateToLocal("info.cofh.forDetails"));
                list.add(cofh.lib.util.helpers.StringHelper.shiftForDetails());
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(Reference.MOD_ID.toLowerCase() + ":" + name);
    }

    @Override
    public String getUnlocalizedName() {
        return "item." +Reference.MOD_ID.toLowerCase() + ":" + name;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + Reference.MOD_ID.toLowerCase() + ":" + name;
    }

    public ItemStack setMode(ItemStack itemStack, byte mode){
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        itemStack.stackTagCompound.setByte("Mode", mode);
        return itemStack;
    }

    public byte getMode(ItemStack itemStack){
        if(!tier.hasModes){
            return ConfigHandler.shearsDefault ? (byte)1 : 0;
        }

        if(itemStack.stackTagCompound == null){
            return ConfigHandler.shearsDefault ? (byte)1 : 0;
        }

        if(itemStack.stackTagCompound.hasKey("Mode")) {
            return itemStack.stackTagCompound.getByte("Mode");
        }else{
            return ConfigHandler.shearsDefault ? (byte)1 : 0;
        }
    }

    public ItemStack setEnergy(ItemStack itemStack, int energy){
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        itemStack.stackTagCompound.setInteger("Energy", energy);
        return itemStack;
    }

    public ItemStack drainEnergy(ItemStack itemStack, int energy){
        return setEnergy(itemStack, Math.max(getEnergyStored(itemStack) - energy, 0));
    }

    /* IEnergyContainerItem stuff */

    @Override
    public int receiveEnergy(ItemStack itemStack, int maxReceive, boolean simulate) { //stolen from ItemEnergyContainer
        int energy = getEnergyStored(itemStack);
        int energyReceived = Math.min(tier.maxEnergy - energy, Math.min(tier.rechargeRate, maxReceive));

        if (!simulate) {
            setEnergy(itemStack, energy += energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack itemStack, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored(ItemStack itemStack) {
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        if(itemStack.stackTagCompound.hasKey("Energy")) {
            return itemStack.stackTagCompound.getInteger("Energy");
        }else{
            itemStack.stackTagCompound.setInteger("Energy", 0);
            return 0;
        }
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return tier.maxEnergy;
    }
}
