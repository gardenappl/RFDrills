package goldenapple.rfdrills.item;

import cofh.api.item.IEmpowerableItem;
import cofh.core.item.IEqualityOverrideItem;
import cofh.core.util.KeyBindingEmpower;
import cofh.lib.util.helpers.BlockHelper;
import cofh.repack.codechicken.lib.math.MathHelper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import goldenapple.rfdrills.DrillTier;
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
    private static final Set<Block> vanillaBlocks = Sets.newHashSet(Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium);
    private static final Set<Material> effectiveMaterials = Sets.newHashSet(Material.anvil, Material.clay, Material.craftedSnow, Material.glass, Material.dragonEgg, Material.grass, Material.ground, Material.ice, Material.snow, Material.iron, Material.rock, Material.sand, Material.coral);

    private DrillTier tier;
    private final String name;

    public ItemDrill(String name, DrillTier tier){
        super(1.0F, tier.material, vanillaBlocks);
        this.name = name;
        this.tier = tier;
        this.setCreativeTab(RFDrills.OmniDrillsTab);
        this.setHarvestLevel("pickaxe", tier.material.getHarvestLevel());
        this.setHarvestLevel("shovel", tier.material.getHarvestLevel());
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe", "shovel");
    }

    @Override
    public boolean func_150897_b(Block block){
        return effectiveMaterials.contains(block.getMaterial()) && (block.getHarvestLevel(0) <= tier.material.getHarvestLevel());
    }

    @Override
    public float getDigSpeed(ItemStack itemStack, Block block, int meta) {
        if(getEnergyStored(itemStack) >= tier.energyPerBlock && canHarvestBlock(block, itemStack)){
            if(isEmpowered(itemStack)) {
                return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial / 3 : 1.0F;
            }else {
                return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial : 1.0F;
            }
        }else{
            return effectiveMaterials.contains(block.getMaterial()) ? 0.5F : 1.0F;
        }
    }

    @Override
    public int getHarvestLevel(ItemStack itemStack, String toolClass) {
        if((toolClass.equals("shovel") || toolClass.equals("pickaxe")) && getEnergyStored(itemStack) >= tier.energyPerBlock){
            return super.getHarvestLevel(itemStack, toolClass);
        }else{
            return -1;
        }
    }

    private int getEnergyPerUse(ItemStack itemStack){
        return Math.round(tier.energyPerBlock / (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) + 1)); //Vanilla formula: a 100% / (unbreaking level + 1) chance to not take damage
    }

    private int getEnergyPerUseWithMode(ItemStack itemStack){
        int energy = getEnergyPerUse(itemStack);
        if(isEmpowered(itemStack)) energy = energy * 3;

        return energy;
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
        if(itemStack.hasTagCompound())
            return !itemStack.stackTagCompound.getBoolean("isCreativeTabIcon");
        else
            return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return Math.max(1.0 - (double) getEnergyStored(itemStack) / (double) tier.maxEnergy, 0);
    }

    @Override
    public void setDamage(ItemStack itemStack, int damage) {
        //do nothing, other methods are responsible for energy consumption
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(!(entity instanceof EntityPlayer)) return false;
        EntityPlayer player = (EntityPlayer)entity;

        if(!world.isRemote && getEnergyStored(itemStack) > 0){
            int xRadius = 0, yRadius = 0, zRadius = 0;

            if(isEmpowered(itemStack)){
                if (BlockHelper.getCurrentMousedOverSide(player) == 0 || BlockHelper.getCurrentMousedOverSide(player) == 1) {
                    switch(MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3) { //Stolen from MineFactoryReloaded //https://github.com/powercrystals/MineFactoryReloaded/blob/master/src/powercrystals/minefactoryreloaded/block/BlockConveyor.java
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
                        if (world.blockExists(a, b, c) && effectiveMaterials.contains(world.getBlock(a, b, c).getMaterial())) {
                            if (!(a == x && b == y && c == z)) { //don't harvest the same block twice
                                ToolHelper.harvestBlock(world, a, b, c, player);
                            }
                        }
                    }
                }
            }
        }

        ToolHelper.damageTool(itemStack, player, getEnergyPerUse(itemStack, block, world.getBlockMetadata(x, y, z)));
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityAttacked, EntityLivingBase entityAttacker) {
        if(entityAttacker instanceof EntityPlayer)
            ToolHelper.damageTool(itemStack, (EntityPlayer)entityAttacker, getEnergyPerUse(itemStack) * 2);

        return true;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean what) {
        try {
            list.add(StringHelper.writeEnergyInfo(getEnergyStored(itemStack), tier.maxEnergy));

            if (MiscUtil.isShiftPressed()) {
                list.add(StringHelper.writeEnergyPerBlockInfo(getEnergyPerUseWithMode(itemStack)));
                if(tier.hasModes) {
                    list.add(writeModeInfo(itemStack));
                }
                list.add(StatCollector.translateToLocal("rfdrills.drill.tooltip"));
                if (tier.canBreak) {
                    list.add(StatCollector.translateToLocal("rfdrills.can_break.tooltip"));
                }
                if (toolMaterial.getEnchantability() > 0) {
                    list.add(StatCollector.translateToLocal("rfdrills.enchantable.tooltip"));
                }
                if (tier.hasModes){
                    list.add(StringHelper.writeModeSwitchInfo("rfdrills.drill_has_modes.tooltip", KeyBindingEmpower.instance));
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

    public String writeModeInfo(ItemStack itemStack){
        if(!tier.hasModes) return "";

        if(isEmpowered(itemStack))
            return StatCollector.translateToLocal("rfdrills.1x3x1.mode");
        else
            return StatCollector.translateToLocal("rfdrills.1x1x1.mode");
    }

    /* IEnergyTool */

    @Override
    public DrillTier getTier(ItemStack itemStack) {
        return tier;
    }

    @Override
    public ItemStack setEnergy(ItemStack itemStack, int energy){
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        itemStack.stackTagCompound.setInteger("Energy", Math.min(energy, getMaxEnergyStored(itemStack)));
        return itemStack;
    }

    @Override
    public ItemStack drainEnergy(ItemStack itemStack, int energy){
        return setEnergy(itemStack, Math.max(getEnergyStored(itemStack) - energy, 0));
    }

    @Override
    public int getEnergyPerUse(ItemStack itemStack, Block block, int meta) {
        return getEnergyPerUseWithMode(itemStack);
    }

    /* IEnergyContainerItem */

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

    /* IEqualityOverrideItem */

    @Override
    public boolean isLastHeldItemEqual(ItemStack current, ItemStack previous) {
        return previous.getItem() == current.getItem(); //used to prevent not being able to mine while the drill is recharging. Otherwise, the mining progress gets reset every tick because of NBT changes
    }

    /* IEmpowerableItem */

    @Override
    public boolean isEmpowered(ItemStack itemStack) {
        if(!tier.hasModes){
            return false;
        }

        if(itemStack.stackTagCompound == null){
            return false;
        }

        if(itemStack.stackTagCompound.hasKey("Mode")) {
            return itemStack.stackTagCompound.getByte("Mode") == 1;
        }else{
            return false;
        }
    }

    @Override
    public boolean setEmpoweredState(ItemStack itemStack, boolean b) {
        if(!tier.hasModes) return false;

        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        itemStack.stackTagCompound.setByte("Mode", b ? (byte)1 : 0);
        return true;
    }

    @Override
    public void onStateChange(EntityPlayer player, ItemStack itemStack) {
        player.addChatComponentMessage(new ChatComponentText(writeModeInfo(itemStack)));
    }
}
