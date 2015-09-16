package goldenapple.rfdrills.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goldenapple.rfdrills.RFDrills;
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
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ItemFluxHoe extends ItemTool implements IEnergyTool {
    public static final Set<Material> effectiveMaterials = Sets.newHashSet(Material.leaves, Material.plants, Material.vine, Material.web);

    private static final ToolTier tier = ToolTier.HOE;
    public ItemFluxHoe() {
        super(2.0F, tier.material, null);
        this.setCreativeTab(RFDrills.RFDrillsTab);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return getEnergyStored(stack) >= getEnergyPerUse(stack) && (block == Blocks.web || block == Blocks.vine);
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        return getEnergyStored(stack) >= getEnergyPerUse(stack) && effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial : 1.0F;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("sickle");
    }

    @Override
    public boolean isItemTool(ItemStack stack){
        return true;
    }

    private int getEnergyPerUse(ItemStack stack){
        return Math.round(tier.energyPerBlock / (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) + 1)); //Vanilla formula: a 100% / (unbreaking level + 1) chance to not take damage
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        list.add(setEnergy(new ItemStack(item, 1, 0), 0));
        list.add(setEnergy(new ItemStack(item, 1, 0), tier.maxEnergy));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
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

        if(world.getBlock(x, y, z).getMaterial().equals(Material.leaves)) { //Harvesting leaves in a 3x3x3 area
            for (int a = x - 1; a <= x + 1; a++) {
                for(int b = y - 1; b <= y + 1; b++) {
                    for (int c = z - 1; c <= z + 1; c++) {
                        if (world.blockExists(a, b, c) && !world.isAirBlock(a, b, c)) {
                            if (!(a == x && b == y && c == z)) { //don't harvest the same block twice
                                ToolHelper.harvestBlock(world, a, b, c, player);
                            }
                        }
                    }
                }
            }
        }else{ //Harvesting everything else in a 3x1x3 area
            for (int a = x - 1; a <= x + 1; a++) {
                for (int c = z - 1; c <= z + 1; c++) {
                    if (world.blockExists(a, y, c) && !world.isAirBlock(a, y, c)) {
                        if(!(a == x && c == z))
                            ToolHelper.harvestBlock(world, a, y, c, player);
                    }
                }
            }
        }

        ToolHelper.drainEnergy(stack, player, getEnergyPerUse(stack, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitX, float hitY, float hitZ) {
        if(getEnergyStored(stack) == 0) return false;
        if(!ToolHelper.hoeBlock(stack, world, x, y, z, sideHit, player)) return false; //if the player right-clicks a block of cobble near a block of dirt we won't till the dirt

        if(!player.isSneaking()) {
            for (int a = x - 1; a <= x + 1; a++) {
                for (int c = z - 1; c <= z + 1; c++) { //don't care about y levels with a hoe
                    if (!(a == x && c == z)) { //we already tilled the block at x, y, z
                        ToolHelper.hoeBlock(stack, world, a, y, c, sideHit, player);
                    }
                }
            }
        }

        ToolHelper.drainEnergy(stack, player, getEnergyPerUse(stack));
        world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), Blocks.farmland.stepSound.getStepResourcePath(), (Blocks.farmland.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.farmland.stepSound.getPitch() * 0.8F);

        return true;
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
        try {
            list.add(StringHelper.writeEnergyInfo(getEnergyStored(stack), tier.maxEnergy));

            if (MiscUtil.isShiftPressed()) {
                list.add(StringHelper.writeEnergyPerBlockInfo(getEnergyPerUse(stack)));
                list.add(StatCollector.translateToLocal("rfdrills.hoe.tooltip"));
                if (tier.canBreak) {
                    list.add(StatCollector.translateToLocal("rfdrills.can_break.tooltip"));
                }
                if (toolMaterial.getEnchantability() > 0) {
                    list.add(StatCollector.translateToLocal("rfdrills.enchantable.tooltip"));
                }
            } else {
                list.add(cofh.lib.util.helpers.StringHelper.shiftForDetails());
            }
        }catch (Exception e){
            LogHelper.warn("Something went wrong with the tooltips!");
            e.printStackTrace();
        }
    }

    @Override
    public String getUnlocalizedName(){
        return "item." + Reference.MOD_ID.toLowerCase() + ":" + "flux_hoe";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister){
        itemIcon = iconRegister.registerIcon(Reference.MOD_ID.toLowerCase() + ":" + "flux_hoe");
    }

    /* IEnergyTool */

    @Override
    public ToolTier getTier(ItemStack stack) {
        return tier;
    }

    @Override
    public ItemStack setEnergy(ItemStack stack, int energy) {
        if(stack.stackTagCompound == null){
            stack.stackTagCompound = new NBTTagCompound();
        }

        stack.stackTagCompound.setInteger("Energy", Math.min(energy, getMaxEnergyStored(stack)));
        return stack;
    }

    @Override
    public ItemStack drainEnergy(ItemStack stack, int energy) {
        return setEnergy(stack, Math.max(getEnergyStored(stack) - energy, 0));
    }

    @Override
    public int getEnergyPerUse(ItemStack stack, Block block, int meta) {
        return getEnergyPerUse(stack);
    }

    @Override
    public String writeModeInfo(ItemStack stack) {
        return "";
    }

    @Override
    public EnumModIntegration getModType() {
        return EnumModIntegration.OTHER;
    }

    /* IEnergyContainerItem */

    @Override
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
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
    public int extractEnergy(ItemStack stack, int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored(ItemStack stack) {
        if(stack.stackTagCompound == null)
            stack.stackTagCompound = new NBTTagCompound();

        if(stack.stackTagCompound.hasKey("Energy"))
            return stack.stackTagCompound.getInteger("Energy");
        else {
            stack.stackTagCompound.setInteger("Energy", 0);
            return 0;
        }
    }

    @Override
    public int getMaxEnergyStored(ItemStack stack) {
        return tier.maxEnergy;
    }
}
