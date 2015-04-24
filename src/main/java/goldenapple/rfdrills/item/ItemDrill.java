package goldenapple.rfdrills.item;

import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import goldenapple.rfdrills.RFDrillsCreativeTab;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.MiscUtil;
import goldenapple.rfdrills.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ItemDrill extends ItemTool implements IEnergyContainerItem {
    private static final Set<Block> pickaxeVanillaBlocks = Sets.newHashSet(Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium);
    private static final Set<Block> shovelVanillaBlocks = Sets.newHashSet(Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium);
    private static final Set<Material> effectiveMaterials = Sets.newHashSet(Material.anvil, Material.clay, Material.craftedSnow, Material.glass, Material.dragonEgg, Material.grass, Material.ground, Material.ice, Material.snow, Material.iron, Material.rock, Material.sand, Material.coral);
    private static Set<Block> effectiveVanillaBlocks = Sets.newHashSet();

    private final String name;
    private final EnumRarity rarity;
    private final int maxEnergy;
    private final int rechargeRate;
    private final int energyPerBlock;
    private final boolean canBreak;

    static {
        effectiveVanillaBlocks.addAll(pickaxeVanillaBlocks);
        effectiveVanillaBlocks.addAll(shovelVanillaBlocks);
    }

    public ItemDrill(String name, ToolMaterial material, int maxEnergy, int rechargeRate, int energyPerBlock, EnumRarity rarity, boolean canBreak){
        super(1.0F, material, effectiveVanillaBlocks);
        this.name = name;
        this.maxEnergy = maxEnergy;
        this.rechargeRate = rechargeRate;
        this.rarity = rarity;
        this.energyPerBlock = energyPerBlock;
        this.canBreak = canBreak;
        this.setCreativeTab(RFDrillsCreativeTab.OmniDrillsTab);
        this.setHarvestLevel("pickaxe", material.getHarvestLevel());
        this.setHarvestLevel("shovel", material.getHarvestLevel());
    }

    public int getEnergyPerBlock() {
        return energyPerBlock;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe", "shovel");
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public boolean func_150897_b(Block p_150897_1_){ //stolen from ItemPickaxe to avoid vanilla block harvesting weirdness (e.g. enchanting tables not dropping)
        return p_150897_1_ == Blocks.obsidian ? this.toolMaterial.getHarvestLevel() == 3 : (p_150897_1_ != Blocks.diamond_block && p_150897_1_ != Blocks.diamond_ore ? (p_150897_1_ != Blocks.emerald_ore && p_150897_1_ != Blocks.emerald_block ? (p_150897_1_ != Blocks.gold_block && p_150897_1_ != Blocks.gold_ore ? (p_150897_1_ != Blocks.iron_block && p_150897_1_ != Blocks.iron_ore ? (p_150897_1_ != Blocks.lapis_block && p_150897_1_ != Blocks.lapis_ore ? (p_150897_1_ != Blocks.redstone_ore && p_150897_1_ != Blocks.lit_redstone_ore ? (p_150897_1_.getMaterial() == Material.rock ? true : (p_150897_1_.getMaterial() == Material.iron ? true : p_150897_1_.getMaterial() == Material.anvil)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2);
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block) { //should be called "getEfficiencyOnBlock" or something I dunno
        if(getEnergyStored(itemStack) > energyPerBlock) {
            return effectiveMaterials.contains(block.getMaterial()) ? this.efficiencyOnProperMaterial : 1.0F;
        }else return 1.0F;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if(toolClass.equals("shovel") || toolClass.equals("pickaxe")){
            return toolMaterial.getHarvestLevel();
        }else{
            return -1;
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        list.add(setEnergy(new ItemStack(item, 1, 0), 0));
        list.add(setEnergy(new ItemStack(item, 1, 0), maxEnergy));
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return rarity;
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        if(itemStack.stackTagCompound != null){
            return !itemStack.stackTagCompound.getBoolean("isCreativeTabIcon");
        }
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if (this.getEnergyStored(itemStack) <= energyPerBlock) {
            itemStack.damageItem(1000000, entity);
            entity.renderBrokenItemStack(itemStack);
            if(entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addStat(StatList.objectBreakStats[Item.getIdFromItem(this)], 1);
            }
        }
        return true; //See DrillMiningHandler
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityAttacked, EntityLivingBase entityAttacker) {
        if (this.getEnergyStored(itemStack) <= energyPerBlock) {
            itemStack.damageItem(1000000, entityAttacker);
            entityAttacker.renderBrokenItemStack(itemStack);
            if(entityAttacker instanceof EntityPlayer) {
                ((EntityPlayer) entityAttacker).addStat(StatList.objectBreakStats[Item.getIdFromItem(this)], 1);
            }
        }
        return true; //See DrillMiningHandler
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1.0F - (double)this.getEnergyStored(itemStack) / (double)maxEnergy;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean what) {
        try {
            list.add(StringHelper.writeEnergyInfo(getEnergyStored(itemStack), maxEnergy));

            if (MiscUtil.isShiftPressed()) {
                list.add(StringHelper.writeEnergyPerBlockInfo(energyPerBlock));
                if (canBreak) {
                    list.add(StatCollector.translateToLocal("rfdrills.tooltip.can_break"));
                }
                if (toolMaterial.getHarvestLevel() >= 3) {
                    list.add(StatCollector.translateToLocal("rfdrills.tooltip.can_mine_obsidian"));
                }
                if (toolMaterial.getEnchantability() > 0) {
                    list.add(StatCollector.translateToLocal("rfdrills.tooltip.enchantable"));
                }
            } else {
                list.add(StatCollector.translateToLocal("rfdrills.tooltip.press_shift"));
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

    public int drainEnergy(ItemStack itemStack, int drain){
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        int energy = getEnergyStored(itemStack);
        int energyDrained = Math.min(energy, drain);

        energy -= energyDrained;
        itemStack.stackTagCompound.setInteger("Energy", energy);
        return energyDrained;
    }

    /* IEnergyContainerItem stuff */

    @Override
    public int receiveEnergy(ItemStack itemStack, int maxReceive, boolean simulate) { //stolen from ItemEnergyContainer
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        int energy = getEnergyStored(itemStack);
        int energyReceived = Math.min(maxEnergy - energy, Math.min(this.rechargeRate, maxReceive));

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
        return maxEnergy;
    }
}
