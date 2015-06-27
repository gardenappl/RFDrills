package goldenapple.rfdrills.item;

import cofh.api.item.IEmpowerableItem;
import cofh.core.item.IEqualityOverrideItem;
import cofh.core.util.KeyBindingEmpower;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.eventhandler.Event;
import goldenapple.rfdrills.DrillTier;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.item.soulupgrade.AbstractSoulUpgrade;
import goldenapple.rfdrills.item.soulupgrade.SoulUpgradeHelper;
import goldenapple.rfdrills.item.soulupgrade.SoulUpgrades;
import goldenapple.rfdrills.reference.Names;
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
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemSoulCrusher extends ItemTool implements IEnergyTool, IEqualityOverrideItem, IEmpowerableItem {
    private static final Set<Block> vanillaBlocks = Sets.newHashSet(Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium, Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin);
    private static final Set<Material> effectiveMaterials = Sets.newHashSet(Material.anvil, Material.clay, Material.craftedSnow, Material.glass, Material.dragonEgg, Material.grass, Material.ground, Material.ice, Material.snow, Material.iron, Material.rock, Material.sand, Material.coral, Material.wood, Material.leaves, Material.plants, Material.vine, Material.cloth, Material.gourd);

    private static DrillTier tier = DrillTier.SOUL_CRUSHER;
    public ItemSoulCrusher() {
        super(1.0F, tier.material, vanillaBlocks);
        this.setHarvestLevel("pickaxe", tier.material.getHarvestLevel());
        this.setHarvestLevel("shovel", tier.material.getHarvestLevel());
        this.setHarvestLevel("axe", tier.material.getHarvestLevel());
        this.setHarvestLevel("sickle", tier.material.getHarvestLevel());
        this.setCreativeTab(RFDrills.OmniDrillsTab);
    }

    @Override
    public boolean func_150897_b(Block block){
        return effectiveMaterials.contains(block.getMaterial()) && (block.getHarvestLevel(0) <= tier.material.getHarvestLevel());
    }

    @Override
    public int getHarvestLevel(ItemStack itemStack, String toolClass) {
        if(getToolClasses(itemStack).contains(toolClass) && getEnergyStored(itemStack) >= tier.energyPerBlock){
            return super.getHarvestLevel(itemStack, toolClass);
        }else{
            return -1;
        }
    }

    @Override
    public float getDigSpeed(ItemStack itemStack, Block block, int meta) {
        if(getEnergyStored(itemStack) >= getEnergyPerUse(itemStack, block, meta) && canHarvestBlock(block, itemStack)){
            switch (getMode(itemStack)){
                case 0: return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial : 1.0F;
                case 1: //same as case 2
                case 2: return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial / 3 : 1.0F;
                case 3: return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial / 6 : 1.0F;
                default:
                    LogHelper.warn("Illegal drill mode!");
                    return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial : 1.0F;
            }
        }else{
            return effectiveMaterials.contains(block.getMaterial()) ? 0.5F : 1.0F;
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
            case 2: energy = energy * 5; break;
            case 3: energy = energy * 10; break;
            default: LogHelper.warn("Illegal drill mode!"); break;
        }
        return energy;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        list.add(setEnergy(new ItemStack(item), 0));

        ItemStack upgradedStack = new ItemStack(item);
        upgradedStack = SoulUpgradeHelper.applyUpgrade(upgradedStack, SoulUpgrades.upgradeEmpowered, (byte)2);
        upgradedStack = SoulUpgradeHelper.applyUpgrade(upgradedStack, SoulUpgrades.upgradeBeastMode, (byte)2);
        upgradedStack = SoulUpgradeHelper.applyUpgrade(upgradedStack, SoulUpgrades.upgradeFork, (byte) 1);
        list.add(setEnergy(upgradedStack, getMaxEnergyStored(upgradedStack)));
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
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return Math.max(1.0 - (double)getEnergyStored(itemStack) / (double)getMaxEnergyStored(itemStack), 0);
    }

    @Override
    public void setDamage(ItemStack itemStack, int damage) {
        //do nothing, other methods are responsible for energy consumption
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
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(entity instanceof EntityPlayer && !world.isRemote){
            int radiusHorizontal = 0;
            int radiusVertical = 0;
            switch (getMode(itemStack)){
                case 1: radiusVertical = 1; break;
                case 2: radiusVertical = 1; radiusHorizontal = 1; break;
                case 3: radiusVertical = 2; radiusHorizontal = 2; break;
            }
            for (int a = x - radiusHorizontal; a <= x + radiusHorizontal; a++) {
                for (int b = y - radiusVertical; b <= y + radiusVertical; b++) {
                    for(int c = z - radiusHorizontal; c <= z + radiusHorizontal; c++) {
                        if (world.blockExists(a, b, c) && effectiveMaterials.contains(world.getBlock(a, b, c).getMaterial())) {
                            if (!(a == x && b == y && c == z)) { //don't harvest the same block twice silly!
                                harvestBlock(world, a, b, c, (EntityPlayer) entity);
                            }
                        }
                    }
                }
            }
        }
        if(entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.isCreativeMode) {
            entity.setCurrentItemOrArmor(0, drainEnergy(itemStack, getEnergyPerUseWithMode(itemStack)));

            if (this.getEnergyStored(itemStack) == 0 && tier.canBreak) {
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

    private boolean tillBlock(ItemStack itemStack, World world, int x, int y, int z, int sideHit, EntityPlayer player){
        Block block = world.getBlock(x, y, z);

        UseHoeEvent event = new UseHoeEvent(player, itemStack, world, x, y, z);
        if(MinecraftForge.EVENT_BUS.post(event)) //if the event got canceled
            return false;

        if (event.getResult() == Event.Result.ALLOW) //if another mod handled this block using the event
            return true;

        if (sideHit != 0 && world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (block == Blocks.grass || block == Blocks.dirt) && player.canPlayerEdit(x, y, z, sideHit, itemStack)) {
            if (!world.isRemote)
                world.setBlock(x, y, z, Blocks.farmland);
            return true;
        }

        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitX, float hitY, float hitZ) {
        if(SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeFork) == 0) return false;
        if(!tillBlock(itemStack, world, x, y, z, sideHit, player)) return false; //if the player right-clicks a block of cobble near a block of dirt we won't till the dirt
        if(getEnergyStored(itemStack) == 0) return false;

        int radius = 0;

        switch (getMode(itemStack)){
            case 2: radius = 1; break;
            case 3: radius = 2; break;
        }

        for(int a = x - radius; a <= x + radius; a++) {
            for(int c = z - radius; c <= z + radius; c++) { //don't care about y levels with a hoe
                if(!(a == x && c == z)) { //we already tilled the block at x, y, z
                    tillBlock(itemStack, world, a, y, c, sideHit, player);
                }
            }
        }

        player.setCurrentItemOrArmor(0, drainEnergy(itemStack, getEnergyPerUseWithMode(itemStack)));
        if (this.getEnergyStored(itemStack) == 0 && tier.canBreak) {
            player.renderBrokenItemStack(itemStack);
            player.destroyCurrentEquippedItem();
            player.addStat(StatList.objectBreakStats[Item.getIdFromItem(itemStack.getItem())], 1);
        }
        world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), Blocks.farmland.stepSound.getStepResourcePath(), (Blocks.farmland.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.farmland.stepSound.getPitch() * 0.8F);

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean wtf) {
        try {
            list.add(StringHelper.writeEnergyInfo(getEnergyStored(itemStack), getMaxEnergyStored(itemStack)));

            if (MiscUtil.isShiftPressed()) {
                for(AbstractSoulUpgrade upgrade : SoulUpgrades.registry){ //installed upgrades
                    if(SoulUpgradeHelper.getUpgradeLevel(itemStack, upgrade) != 0) {
                        list.add(EnumChatFormatting.DARK_AQUA.toString() + StringHelper.writeUpgradeInfo(itemStack, upgrade));
                        upgrade.addDescription(itemStack, list);
                    }
                }

                list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("rfdrills.soul_upgrade.recipe"));

                for(AbstractSoulUpgrade upgrade : SoulUpgrades.registry){ //available recipes
                    if(upgrade.isUpgradeAvailable(itemStack)){
                        list.add(EnumChatFormatting.DARK_AQUA.toString() + StringHelper.writeUpgradeInfo(SoulUpgradeHelper.getUpgradeLevel(itemStack, upgrade) + 1, upgrade));
                        upgrade.addRecipeDescription(itemStack, list);
                    }
                }

                list.add(StringHelper.writeModeSwitchInfo("rfdrills.crusher_has_modes.tooltip", KeyBindingEmpower.instance));
            } else {
                for(Map.Entry<AbstractSoulUpgrade, Byte> entry : SoulUpgradeHelper.getUpgrades(itemStack).entrySet()){
                    list.add(EnumChatFormatting.DARK_AQUA.toString() + StringHelper.writeUpgradeInfo(itemStack, entry.getKey()));
                }
                list.add(cofh.lib.util.helpers.StringHelper.shiftForDetails());
            }
        }catch (Exception e){
            LogHelper.warn("Something went wrong with the tooltips!");
            e.printStackTrace();
        }
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(Reference.MOD_ID.toLowerCase() + ":" + Names.SOUL_CRUSHER);
    }

    @Override
    public String getUnlocalizedName() {
        return "item." + Reference.MOD_ID.toLowerCase() + ":" + Names.SOUL_CRUSHER;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + Reference.MOD_ID.toLowerCase() + ":" + Names.SOUL_CRUSHER;
    }

    public int getMode(ItemStack itemStack){
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

    public boolean setMode(ItemStack itemStack, int mode) {
        if(SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeBeastMode) == 0) return false;
        if(getEnergyStored(itemStack) == 0) return false;

        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        itemStack.stackTagCompound.setByte("Mode", (byte)mode);
        return true;
    }

    private int getNumModes(ItemStack itemStack) {
        return SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeBeastMode) + 2;
    }

    public String writeModeInfo(ItemStack itemStack){
        switch (getMode(itemStack)) {
            case 0:
                return StatCollector.translateToLocal("rfdrills.1x1x1.mode");
            case 1:
                return StatCollector.translateToLocal("rfdrills.1x3x1.mode");
            case 2:
                return StatCollector.translateToLocal("rfdrills.3x3x3.mode");
            case 3:
                return StatCollector.translateToLocal("rfdrills.5x5x5.mode");
            default:
                LogHelper.warn("Illegal drill mode!");
                return StatCollector.translateToLocal("rfdrills.1x1x1.mode");
        }
    }

    /* IEnergyTool */

    @Override
    public DrillTier getTier(ItemStack itemStack) {
        return DrillTier.FLUX_CRUSHER;
    }

    @Override
    public ItemStack setEnergy(ItemStack itemStack, int energy) {
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        itemStack.stackTagCompound.setInteger("Energy", Math.min(energy, getMaxEnergyStored(itemStack)));
        return itemStack;
    }

    @Override
    public ItemStack drainEnergy(ItemStack itemStack, int energy) {
        return setEnergy(itemStack, Math.max(0, getEnergyStored(itemStack) - energy));
    }

    @Override
    public int getEnergyPerUse(ItemStack itemStack, Block block, int meta) {
        return getEnergyPerUseWithMode(itemStack);
    }

    @Override
    public int receiveEnergy(ItemStack itemStack, int maxReceive, boolean simulate) {
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        int rechargeRate = tier.rechargeRate;

        switch (SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeEmpowered)){
            case 1: rechargeRate = 750; break;
            case 2: rechargeRate = 1500; break;
            case 3: rechargeRate = 5000; break;
        }

        int energy = getEnergyStored(itemStack);
        int energyReceived = Math.min(getMaxEnergyStored(itemStack) - energy, Math.min(rechargeRate, maxReceive));

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
        switch (SoulUpgradeHelper.getUpgradeLevel(itemStack, SoulUpgrades.upgradeEmpowered)){
            case 0: return tier.maxEnergy; //200 000
            case 1: return 500000;
            case 2: return 1000000;
            case 3: return 5000000;
            default: return tier.maxEnergy;
        }
    }

    /* IEqaulityOverrideItem */

    @Override
    public boolean isLastHeldItemEqual(ItemStack current, ItemStack previous) {
        return current.getItem() == previous.getItem();
    }

    /* IEmpowerableItem */

    @Override
    public boolean isEmpowered(ItemStack itemStack) {
        return getMode(itemStack) == getNumModes(itemStack);
    }

    @Override
    public boolean setEmpoweredState(ItemStack itemStack, boolean b) {
        if(!tier.hasModes) return false;

        if(getMode(itemStack) == getNumModes(itemStack)){
            setMode(itemStack, 0);
        }else{
            setMode(itemStack, getMode(itemStack) + 1);
        }

        return true;
    }

    @Override
    public void onStateChange(EntityPlayer player, ItemStack itemStack) {
        if(getMode(itemStack) == 0){
            player.worldObj.playSoundAtEntity(player, "random.orb", 0.2F, 0.6F);
        }else {
            player.worldObj.playSoundAtEntity(player, "ambient.weather.thunder", 0.4F, 1.0F);
        }
        player.addChatComponentMessage(new ChatComponentText(writeModeInfo(itemStack)));
    }
}
