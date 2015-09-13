package goldenapple.rfdrills.item;

import cofh.api.item.IEmpowerableItem;
import cofh.core.item.IEqualityOverrideItem;
import cofh.core.util.KeyBindingEmpower;
import cofh.lib.util.helpers.BlockHelper;
import cofh.repack.codechicken.lib.math.MathHelper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.reference.Reference;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ItemDrill extends ItemTool implements IEnergyTool, IEqualityOverrideItem, IEmpowerableItem {
    private static final Set<Material> effectiveMaterials = Sets.newHashSet(Material.anvil, Material.clay, Material.craftedSnow, Material.glass, Material.dragonEgg, Material.grass, Material.ground, Material.ice, Material.snow, Material.iron, Material.rock, Material.sand, Material.coral);

    private ToolTier tier;
    private final String name;

    public ItemDrill(String name, ToolTier tier){
        super(1.0F, tier.material, null);
        this.name = name;
        this.tier = tier;
        this.setCreativeTab(RFDrills.RFDrillsTab);
        this.setHarvestLevel("pickaxe", tier.material.getHarvestLevel());
        this.setHarvestLevel("shovel", tier.material.getHarvestLevel());
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe", "shovel");
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return getEnergyStored(stack) >= getEnergyPerUseWithMode(stack) && effectiveMaterials.contains(block.getMaterial());
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        return getEnergyStored(stack) >= getEnergyPerUseWithMode(stack) && effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial : 1.0F;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if(getEnergyStored(stack) >= getEnergyPerUse(stack, block, meta) && ToolHelper.isToolEffective(stack, block, meta)){
            if(isEmpowered(stack))
                return efficiencyOnProperMaterial / 3;
            else
                return efficiencyOnProperMaterial;
        }else
           return 1.0F;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if(getToolClasses(stack).contains(toolClass) && getEnergyStored(stack) >= tier.energyPerBlock){
            return super.getHarvestLevel(stack, toolClass);
        }else{
            return -1;
        }
    }

    private int getEnergyPerUse(ItemStack stack){
        return Math.round(tier.energyPerBlock / (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) + 1)); //Vanilla formula: a 100% / (unbreaking level + 1) chance to not take damage
    }

    private int getEnergyPerUseWithMode(ItemStack stack){
        int energy = getEnergyPerUse(stack);
        if(isEmpowered(stack))
            energy = energy * 3;
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
        if(stack.hasTagCompound())
            return !stack.stackTagCompound.getBoolean("isCreativeTabIcon");
        else
            return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return Math.max(1.0 - (double) getEnergyStored(stack) / (double) tier.maxEnergy, 0);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        World world = player.worldObj;

        if(!world.isRemote && getEnergyStored(stack) > 0){
            int xRadius = 0, yRadius = 0, zRadius = 0;

            if(isEmpowered(stack)){
                if (BlockHelper.getCurrentMousedOverSide(player) == 0 || BlockHelper.getCurrentMousedOverSide(player) == 1) {
                    switch(MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3) { //Stolen from MineFactoryReloaded https://github.com/powercrystals/MineFactoryReloaded/blob/master/src/powercrystals/minefactoryreloaded/block/BlockConveyor.java
                        case 0: zRadius = 1; break;
                        case 1: xRadius = 1; break;
                        case 2: zRadius = 1; break;
                        case 3: xRadius = 1; break;
                    }
                } else {
                    yRadius = 1;
                }
            }
            for(int a = x - xRadius; a <= x + xRadius; a++) {
                for (int b = y - yRadius; b <= y + yRadius; b++) {
                    for(int c = z - zRadius; c <= z + zRadius; c++) {
                        if (world.blockExists(a, b, c) && !world.isAirBlock(x, y, z)) {
                            if (!(a == x && b == y && c == z)) { //don't harvest the same block twice
                                ToolHelper.harvestBlock(world, a, b, c, player);
                            }
                        }
                    }
                }
            }
        }

        ToolHelper.drainEnergy(stack, player, getEnergyPerUse(stack, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));
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
            list.add(StatCollector.translateToLocal("rfdrills.drill.tooltip"));
            if (tier.canBreak)
                list.add(StatCollector.translateToLocal("rfdrills.can_break.tooltip"));
            if (toolMaterial.getEnchantability() > 0)
                list.add(StatCollector.translateToLocal("rfdrills.enchantable.tooltip"));
            if (tier.hasModes)
                list.add(StringHelper.writeModeSwitchInfo("rfdrills.drill_has_modes.tooltip", KeyBindingEmpower.instance));
        } else
            list.add(cofh.lib.util.helpers.StringHelper.shiftForDetails());
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
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + Reference.MOD_ID.toLowerCase() + ":" + name;
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

        if(isEmpowered(stack))
            return StatCollector.translateToLocal("rfdrills.1x3x1.mode");
        else
            return StatCollector.translateToLocal("rfdrills.1x1x1.mode");
    }

    @Override
    public boolean canProperlyHarvest(ItemStack stack, Block block, int meta) {
        return ToolHelper.isToolEffective(stack, block, meta);
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
        return previous.getItem() == current.getItem(); //used to prevent not being able to mine while the drill is recharging. Otherwise, the mining progress gets reset every tick because of NBT changes
    }

    /* IEmpowerableItem */

    @Override
    public boolean isEmpowered(ItemStack stack) {
        if(!tier.hasModes)
            return false;

        if(stack.stackTagCompound == null)
            return false;

        if(stack.stackTagCompound.hasKey("Mode"))
            return stack.stackTagCompound.getByte("Mode") == 1;
        else
            return false;
    }

    @Override
    public boolean setEmpoweredState(ItemStack stack, boolean b) {
        if(!tier.hasModes) return false;

        if(stack.stackTagCompound == null)
            stack.stackTagCompound = new NBTTagCompound();

        stack.stackTagCompound.setByte("Mode", b ? (byte)1 : 0);
        return true;
    }

    @Override
    public void onStateChange(EntityPlayer player, ItemStack stack) {
        player.addChatComponentMessage(new ChatComponentText(writeModeInfo(stack)));
    }
}
