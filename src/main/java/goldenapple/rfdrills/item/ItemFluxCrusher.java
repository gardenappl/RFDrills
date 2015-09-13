package goldenapple.rfdrills.item;

import cofh.api.item.IEmpowerableItem;
import cofh.core.item.IEqualityOverrideItem;
import cofh.core.util.KeyBindingEmpower;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.reference.Names;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import goldenapple.rfdrills.util.MiscUtil;
import goldenapple.rfdrills.util.StringHelper;
import goldenapple.rfdrills.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ItemFluxCrusher extends ItemTool implements IEnergyTool, IEqualityOverrideItem, IEmpowerableItem{
    private static final Set<Material> effectiveMaterials = Sets.newHashSet(Material.anvil, Material.clay, Material.craftedSnow, Material.glass, Material.dragonEgg, Material.grass, Material.ground, Material.ice, Material.snow, Material.iron, Material.rock, Material.sand, Material.coral, Material.wood, Material.leaves, Material.plants, Material.vine, Material.cloth, Material.gourd);

    private static ToolTier tier = ToolTier.FLUX_CRUSHER;
    private IIcon iconEmpty;
    private IIcon iconActive;
    public ItemFluxCrusher(){
        super(1.0F, tier.material, null);
        this.setHarvestLevel("pickaxe", tier.material.getHarvestLevel());
        this.setHarvestLevel("shovel", tier.material.getHarvestLevel());
        this.setHarvestLevel("axe", tier.material.getHarvestLevel());
        this.setHarvestLevel("sickle", tier.material.getHarvestLevel()); //don't know what mod would use that but why not
        this.setCreativeTab(RFDrills.RFDrillsTab);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe", "shovel", "axe", "sickle");
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return effectiveMaterials.contains(block.getMaterial());
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if(getToolClasses(stack).contains(toolClass) && getEnergyStored(stack) >= tier.energyPerBlock)
            return super.getHarvestLevel(stack, toolClass);
        else
            return -1;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if(getEnergyStored(stack) >= getEnergyPerUse(stack, block, meta) && ToolHelper.isToolEffective(stack, block, meta, effectiveMaterials)){
            switch (getMode(stack)){
                case 0: return efficiencyOnProperMaterial;
                case 1: return efficiencyOnProperMaterial / 3;
                case 2: return efficiencyOnProperMaterial / 6;
                default:
                    LogHelper.warn("Illegal drill mode!");
                    return efficiencyOnProperMaterial;
            }
        }else
            return 1.0F;
    }

    private int getEnergyPerUse(ItemStack stack){
        return Math.round(tier.energyPerBlock / (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) + 1)); //Vanilla formula: a 100% / (unbreaking level + 1) chance to not take damage
    }

    private int getEnergyPerUseWithMode(ItemStack stack){
        int energy = getEnergyPerUse(stack);
        switch (getMode(stack)){
            case 0: break;
            case 1: energy = energy * 5; break;
            case 2: energy = energy * 10; break;
            default: LogHelper.warn("Illegal drill mode!"); break;
        }
        return energy;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        list.add(setEnergy(new ItemStack(item, 1, 0), 0));
        list.add(setEnergy(new ItemStack(item, 1, 0), tier.maxEnergy));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return tier.rarity;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return Math.max(1.0 - (double)getEnergyStored(stack) / (double)tier.maxEnergy, 0);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return getEnergyStored(stack) < getMaxEnergyStored(stack);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        World world = player.worldObj;

        if(!world.isRemote && getEnergyStored(stack) > 0){
            int radius = 0;

            switch (getMode(stack)){
                case 1: radius = 1; break;
                case 2: radius = 2; break;
            }

            for (int a = x - radius; a <= x + radius; a++) {
                for (int b = y - radius; b <= y + radius; b++) {
                    for(int c = z - radius; c <= z + radius; c++) {
                        if (world.blockExists(a, b, c) && !world.isAirBlock(a, b, c)) {
                            if (!(a == x && b == y && c == z)) //don't harvest the same block twice silly!
                                ToolHelper.harvestBlock(world, a, b, c, player, effectiveMaterials);
                        }
                    }
                }
            }
        }

        ToolHelper.drainEnergy(stack, player, getEnergyPerUseWithMode(stack));
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entityAttacked, EntityLivingBase entityAttacker) {
        if(entityAttacker instanceof EntityPlayer)
            ToolHelper.drainEnergy(stack, (EntityPlayer) entityAttacker, getEnergyPerUse(stack) * 2);
        return true;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean what) {
        list.add(StringHelper.writeEnergyInfo(getEnergyStored(stack), tier.maxEnergy));

        if (MiscUtil.isShiftPressed()) {
            list.add(StringHelper.writeEnergyPerBlockInfo(getEnergyPerUseWithMode(stack)));
            if(tier.hasModes)
                list.add(writeModeInfo(stack));
            list.add(StatCollector.translateToLocal("rfdrills.crusher.tooltip"));
            if(tier.material.getEnchantability() > 0)
                list.add(StatCollector.translateToLocal("rfdrills.enchantable.tooltip"));
            if (tier.hasModes)
                list.add(StringHelper.writeModeSwitchInfo("rfdrills.crusher_has_modes.tooltip", KeyBindingEmpower.instance));
        } else
            list.add(cofh.lib.util.helpers.StringHelper.shiftForDetails());
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass) {
        if(getEnergyStored(stack) == 0)
            return iconEmpty;
        else if(getMode(stack) == 1 || getMode(stack) == 2)
            return iconActive;
        else
            return itemIcon;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(Reference.MOD_ID.toLowerCase() + ":" + Names.FLUX_CRUSHER);
        iconEmpty = register.registerIcon(Reference.MOD_ID.toLowerCase() + ":" + Names.FLUX_CRUSHER + "_empty");
        iconActive = register.registerIcon(Reference.MOD_ID.toLowerCase() + ":" + Names.FLUX_CRUSHER + "_active");
    }

    @Override
    public String getUnlocalizedName() {
        return "item." +Reference.MOD_ID.toLowerCase() + ":" + Names.FLUX_CRUSHER;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + Reference.MOD_ID.toLowerCase() + ":" + Names.FLUX_CRUSHER;
    }

    public int getMode(ItemStack stack){
        if(!tier.hasModes) return 0;
        if(getEnergyStored(stack) == 0) return 0;
        if(stack.stackTagCompound == null) return 0;

        if(stack.stackTagCompound.hasKey("Mode")) {
            return stack.stackTagCompound.getByte("Mode");
        }else{
            return 0;
        }
    }

    public boolean setMode(ItemStack stack, int mode) {
        if(getEnergyStored(stack) == 0) return false;

        if(stack.stackTagCompound == null){
            stack.stackTagCompound = new NBTTagCompound();
        }

        stack.stackTagCompound.setByte("Mode", (byte)mode);
        return true;
    }

    /* IEnergyTool */

    @Override
    public ToolTier getTier(ItemStack stack) {
        return tier;
    }

    @Override
    public ItemStack setEnergy(ItemStack stack, int energy){
        if(stack.stackTagCompound == null)
            stack.stackTagCompound = new NBTTagCompound();

        stack.stackTagCompound.setInteger("Energy", Math.min(energy, getMaxEnergyStored(stack)));
        return stack;
    }

    @Override
    public ItemStack drainEnergy(ItemStack stack, int energy){
        return setEnergy(stack, Math.max(getEnergyStored(stack) - energy, 0));
    }

    @Override
    public int getEnergyPerUse(ItemStack stack, Block block, int meta) {
        return getEnergyPerUseWithMode(stack);
    }

    @Override
    public String writeModeInfo(ItemStack stack){
        if(!tier.hasModes) return "";
        switch (getMode(stack)) {
            case 0:
                return StatCollector.translateToLocal("rfdrills.1x1x1.mode");
            case 1:
                return StatCollector.translateToLocal("rfdrills.3x3x3.mode");
            case 2:
                return StatCollector.translateToLocal("rfdrills.5x5x5.mode");
            default:
                LogHelper.warn("Illegal drill mode!");
                return StatCollector.translateToLocal("rfdrills.1x1x1.mode");
        }
    }

    @Override
    public boolean canProperlyHarvest(ItemStack stack, Block block, int meta) {
        return ToolHelper.isToolEffective(stack, block, meta, effectiveMaterials);
    }

    /* IEnergyContainerItem */

    @Override
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) { //stolen from ItemEnergyContainer
        if(stack.stackTagCompound == null)
            stack.stackTagCompound = new NBTTagCompound();

        int energy = getEnergyStored(stack);
        int energyReceived = Math.min(tier.maxEnergy - energy, Math.min(tier.rechargeRate, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            stack.stackTagCompound.setInteger("Energy", energy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored(ItemStack stack) {
        if(stack.stackTagCompound == null)
            stack.stackTagCompound = new NBTTagCompound();

        if(stack.stackTagCompound.hasKey("Energy"))
            return stack.stackTagCompound.getInteger("Energy");
        else{
            stack.stackTagCompound.setInteger("Energy", 0);
            return 0;
        }
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
        return tier.maxEnergy;
    }

    /* IEqualityOverrideItem */

    @Override
    public boolean isLastHeldItemEqual(ItemStack current, ItemStack previous) {
        return current.getItem() == previous.getItem();
    }

    /* IEmpowerableItem */

    @Override
    public boolean isEmpowered(ItemStack stack) {
        return getMode(stack) == 2;
    }

    @Override
    public boolean setEmpoweredState(ItemStack stack, boolean b) {
        if(!tier.hasModes) return false;

        if(getMode(stack) == 2)
            setMode(stack, 0);
        else
            setMode(stack, getMode(stack) + 1);

        return true;
    }

    @Override
    public void onStateChange(EntityPlayer player, ItemStack stack) {
        if(getMode(stack) == 0)
            player.worldObj.playSoundAtEntity(player, "random.orb", 0.2F, 0.6F);
        else
            player.worldObj.playSoundAtEntity(player, "ambient.weather.thunder", 0.4F, 1.0F);

        player.addChatComponentMessage(new ChatComponentText(writeModeInfo(stack)));
    }
}
