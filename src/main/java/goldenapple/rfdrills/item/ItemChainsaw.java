package goldenapple.rfdrills.item;

import cofh.api.energy.IEnergyContainerItem;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.DrillTier;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.MiscUtil;
import goldenapple.rfdrills.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

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

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.isCreativeMode) {
            entity.setCurrentItemOrArmor(0, drainEnergy(itemStack, tier.energyPerBlock));

            if (this.getEnergyStored(itemStack) == 0 && tier.canBreak) {
                itemStack.damageItem(1000000, entity);
            }
            return true; //See DrillMiningHandler
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
            return true; //See DrillMiningHandler
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
                    list.add(StatCollector.translateToLocal("rfdrills.tooltip.can_break"));
                }
                if (toolMaterial.getEnchantability() > 0) {
                    list.add(StatCollector.translateToLocal("rfdrills.tooltip.enchantable"));
                }
            } else {
                list.add(StatCollector.translateToLocal("info.cofh.hold") + " §e§o" + StatCollector.translateToLocal("info.cofh.shift") + " §r§7" + StatCollector.translateToLocal("info.cofh.forDetails"));
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
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        int energy = getEnergyStored(itemStack);
        int energyReceived = Math.min(tier.maxEnergy - energy, Math.min(tier.rechargeRate, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            itemStack.stackTagCompound.setInteger("Energy", energy);
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
