package goldenapple.rfdrills.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import goldenapple.rfdrills.DrillTier;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import goldenapple.rfdrills.util.MiscUtil;
import goldenapple.rfdrills.util.StringHelper;
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
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import java.util.List;
import java.util.Set;

public class ItemDrill extends ItemTool implements IEnergyTool {
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
            switch (getMode(itemStack)){
                case 0: return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial : 1.0F;
                case 1: return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial / 3 : 1.0F;
                default:
                    LogHelper.warn("Illegal drill mode!");
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
        switch (getMode(itemStack)){
            case 0: break;
            case 1: energy = energy * 3; break;
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
    public EnumRarity getRarity(ItemStack itemStack) {
        return tier.rarity;
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        if(itemStack.hasTagCompound()){
            return !itemStack.stackTagCompound.getBoolean("isCreativeTabIcon");
        }
        return true;
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
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("rfdrills.1x3x1.mode")));
                    break;
                case 1:
                    setMode(itemStack, (byte) 0);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("rfdrills.1x1x1.mode")));
                    break;
                default:
                    setMode(itemStack, (byte) 0);
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("rfdrills.1x1x1.mode")));
                    LogHelper.warn("Illegal drill mode! Resetting to 0");
                    break;
            }
        }
        return itemStack;
    }

    private void harvestBlock(World world, int x, int y, int z, EntityPlayer player){
        Block block = world.getBlock(x, y, z);
        if (block.getBlockHardness(world, x, y, z) < 0) {
            return;
        }
        int meta = world.getBlockMetadata(x, y, z);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x, y, z, world, block, meta, player);

        MinecraftForge.EVENT_BUS.post(event);
        if(!event.isCanceled()) {
            if (block.canHarvestBlock(player, meta)) {
                block.harvestBlock(world, player, x, y, z, meta);
            }
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void setDamage(ItemStack itemStack, int damage) {
        //do nothing, other methods are responsible for energy consumption
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(getMode(itemStack) == 1 && entity instanceof EntityPlayer && !world.isRemote && getEnergyStored(itemStack) >= getEnergyPerUseWithMode(itemStack)){
            for (int b = y - 1; b <= y + 1; b++) {
                if (world.blockExists(x, b, z) && effectiveMaterials.contains(world.getBlock(x, b, z).getMaterial())) {
                    if (b != y) { //don't harvest the same block twice
                        harvestBlock(world, x, b, z, (EntityPlayer) entity);
                    }
                }
            }
        }
        if(entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.isCreativeMode) {
            entity.setCurrentItemOrArmor(0, drainEnergy(itemStack, getEnergyPerUseWithMode(itemStack)));

            if (this.getEnergyStored(itemStack) == 0 && tier.canBreak) {
              //itemStack.damageItem(1000000, entity);
                entity.renderBrokenItemStack(itemStack);
                ((EntityPlayer)entity).destroyCurrentEquippedItem();
                ((EntityPlayer)entity).addStat(StatList.objectBreakStats[Item.getIdFromItem(itemStack.getItem())], 1);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityAttacked, EntityLivingBase entityAttacker) {
        if(entityAttacker instanceof EntityPlayer && !((EntityPlayer)entityAttacker).capabilities.isCreativeMode) {
            entityAttacker.setCurrentItemOrArmor(0, drainEnergy(itemStack, getEnergyPerUse(itemStack) * 2));

            if (this.getEnergyStored(itemStack) == 0 && tier.canBreak) {
                entityAttacker.renderBrokenItemStack(itemStack);
                ((EntityPlayer)entityAttacker).destroyCurrentEquippedItem();
                ((EntityPlayer)entityAttacker).addStat(StatList.objectBreakStats[Item.getIdFromItem(itemStack.getItem())], 1);
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
                list.add(StringHelper.writeEnergyPerBlockInfo(getEnergyPerUseWithMode(itemStack)));
                if(tier.hasModes) {
                    switch (getMode(itemStack)) {
                        case 0:
                            list.add(StatCollector.translateToLocal("rfdrills.1x1x1.mode"));
                            break;
                        case 1:
                            list.add(StatCollector.translateToLocal("rfdrills.1x3x1.mode"));
                            break;
                        default:
                            list.add(StatCollector.translateToLocal("rfdrills.1x1x1.mode"));
                            LogHelper.warn("Illegal drill mode!");
                            break;
                    }
                }
                list.add(StatCollector.translateToLocal("rfdrills.drill.tooltip"));
                if (tier.canBreak) {
                    list.add(StatCollector.translateToLocal("rfdrills.can_break.tooltip"));
                }
                if (toolMaterial.getEnchantability() > 0) {
                    list.add(StatCollector.translateToLocal("rfdrills.enchantable.tooltip"));
                }
                if (tier.hasModes){
                    list.add(StatCollector.translateToLocal("rfdrills.drill_has_modes.tooltip"));
                }
            } else {
              //list.add(StatCollector.translateToLocal("info.cofh.hold") + " §e§o" + StatCollector.translateToLocal("info.cofh.shift") + " §r§7" + StatCollector.translateToLocal("info.cofh.forDetails"));
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

    public static ItemStack setMode(ItemStack itemStack, byte mode){
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        itemStack.stackTagCompound.setByte("Mode", mode);
        return itemStack;
    }

    public byte getMode(ItemStack itemStack){
        if(!tier.hasModes){
            return 0;
        }

        if(itemStack.stackTagCompound == null){
            return 0;
        }

        if(itemStack.stackTagCompound.hasKey("Mode")) {
            return itemStack.stackTagCompound.getByte("Mode");
        }else{
            return 0;
        }
    }

    /* IEnergyTool methods*/

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
