package goldenapple.rfdrills.item;

import cofh.api.item.IEmpowerableItem;
import cofh.core.item.IEqualityOverrideItem;
import cofh.core.util.KeyBindingEmpower;
import cofh.lib.util.helpers.BlockHelper;
import cofh.repack.codechicken.lib.math.MathHelper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.config.ConfigHandler;
import goldenapple.rfdrills.item.soulupgrade.AbstractSoulUpgrade;
import goldenapple.rfdrills.item.soulupgrade.SoulUpgradeHelper;
import goldenapple.rfdrills.item.soulupgrade.SoulUpgrades;
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
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemSoulCrusher extends ItemTool implements IEnergyTool, IEqualityOverrideItem, IEmpowerableItem {
    private static final Set<Material> effectiveMaterials = Sets.newHashSet(Material.anvil, Material.clay, Material.craftedSnow, Material.glass, Material.dragonEgg, Material.grass, Material.ground, Material.ice, Material.snow, Material.iron, Material.rock, Material.sand, Material.coral, Material.wood, Material.cloth, Material.gourd);

    private static ToolTier tier = ToolTier.SOUL_CRUSHER;
    public ItemSoulCrusher() {
        super(1.0F, tier.material, null);
        this.setHarvestLevel("pickaxe", tier.material.getHarvestLevel());
        this.setHarvestLevel("shovel", tier.material.getHarvestLevel());
        this.setHarvestLevel("axe", tier.material.getHarvestLevel());
        this.setCreativeTab(RFDrills.RFDrillsTab);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe", "shovel", "axe");
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
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if(getToolClasses(stack).contains(toolClass) && getEnergyStored(stack) >= getEnergyPerUseWithMode(stack))
            return super.getHarvestLevel(stack, toolClass);
        else
            return -1;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if(getEnergyStored(stack) >= getEnergyPerUse(stack, block, meta) && ToolHelper.isToolEffective(stack, block, meta)){
            switch (getMode(stack)){
                case 0: return efficiencyOnProperMaterial;
                case 1: //same as case 2
                case 2: return efficiencyOnProperMaterial / 3;
                case 3: return efficiencyOnProperMaterial / 6;
                default:
                    LogHelper.warn("Illegal drill mode!");
                    return efficiencyOnProperMaterial;
            }
        }else{
            lol: LogHelper.info("lol");

            return 1.0F;
        }
    }

    private int getEnergyPerUse(ItemStack stack){
        return Math.round(tier.energyPerBlock / (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) + 1)); //Vanilla formula: a 100% / (unbreaking level + 1) chance to not take damage
    }

    private int getEnergyPerUseWithMode(ItemStack stack){
        int energy = getEnergyPerUse(stack);
        switch (getMode(stack)){
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
    public EnumRarity getRarity(ItemStack stack) {
        return tier.rarity;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return Math.max(1.0 - (double)getEnergyStored(stack) / (double)getMaxEnergyStored(stack), 0);
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

            switch (getMode(stack)){
                case 1: if (BlockHelper.getCurrentMousedOverSide(player) == 0 || BlockHelper.getCurrentMousedOverSide(player) == 1) { //if looking at the top or bottom of a block
                    switch(MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3) { //Stolen from MineFactoryReloaded https://github.com/powercrystals/MineFactoryReloaded/blob/master/src/powercrystals/minefactoryreloaded/block/BlockConveyor.java
                        case 0: zRadius = 1; break;
                        case 1: xRadius = 1; break;
                        case 2: zRadius = 1; break;
                        case 3: xRadius = 1; break;
                    }
                } else
                    yRadius = 1; break;
                case 2: xRadius = 1; yRadius = 1; zRadius = 1; break;
                case 3: xRadius = 2; yRadius = 2; zRadius = 2; break;
            }

            for (int a = x - xRadius; a <= x + xRadius; a++) {
                for (int b = y - yRadius; b <= y + yRadius; b++) {
                    for(int c = z - zRadius; c <= z + zRadius; c++) {
                        if (world.blockExists(a, b, c) && !world.isAirBlock(a, b, c)) {
                            if (!(a == x && b == y && c == z)) //don't harvest the same block twice silly!
                                ToolHelper.harvestBlock(world, a, b, c, player);
                        }
                    }
                }
            }
        }
        return false;
    }


    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entityAttacked, EntityLivingBase entityAttacker) {
        if(entityAttacker instanceof EntityPlayer)
            ToolHelper.drainEnergy(stack, (EntityPlayer) entityAttacker, getEnergyPerUse(stack) * 2);
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(!world.isRemote && player.isSneaking() && MiscUtil.shouldModeShiftClick(this)){
            setEmpoweredState(stack, !isEmpowered(stack));
            onStateChange(player, stack);
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitX, float hitY, float hitZ) {
        if(getEnergyStored(stack) == 0) return false;
        if(SoulUpgradeHelper.getUpgradeLevel(stack, SoulUpgrades.upgradeFork) == 0) return false;
        if(!ToolHelper.hoeBlock(stack, world, x, y, z, sideHit, player)) return false; //if the player right-clicks a block of cobble near a block of dirt we won't till the dirt

        int xRadius = 0, zRadius = 0;
        switch (getMode(stack)){
            case 1:
                switch(MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3) { //Stolen from MineFactoryReloaded https://github.com/powercrystals/MineFactoryReloaded/blob/master/src/powercrystals/minefactoryreloaded/block/BlockConveyor.java
                    case 0: zRadius = 1; break;
                    case 1: xRadius = 1; break;
                    case 2: zRadius = 1; break;
                    case 3: xRadius = 1; break;
                } break;
            case 2: xRadius = 1; zRadius = 1; break;
            case 3: xRadius = 2; zRadius = 2; break;
        }

        for(int a = x - xRadius; a <= x + xRadius; a++) {
            for(int c = z - zRadius; c <= z + zRadius; c++) { //don't care about y levels with a hoe
                if(!(a == x && c == z)) { //we already tilled the block at x, y, z
                    ToolHelper.hoeBlock(stack, world, a, y, c, sideHit, player);
                }
            }
        }

        ToolHelper.drainEnergy(stack, player, getEnergyPerUseWithMode(stack));
        world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), Blocks.farmland.stepSound.getStepResourcePath(), (Blocks.farmland.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.farmland.stepSound.getPitch() * 0.8F);

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean wtf) {
        list.add(StringHelper.writeEnergyInfo(getEnergyStored(stack), getMaxEnergyStored(stack)));

        if (MiscUtil.isShiftPressed()) {
            for(Map.Entry<AbstractSoulUpgrade, Byte> entry : SoulUpgradeHelper.getUpgrades(stack).entrySet()){ //installed upgrades
                list.add(EnumChatFormatting.DARK_AQUA.toString() + StringHelper.writeUpgradeInfo(entry.getValue(), entry.getKey()));
                entry.getKey().addDescription(stack, list);
            }

            boolean printedOneRecipeMessage = false;
            for(AbstractSoulUpgrade upgrade : SoulUpgrades.registry){ //available recipes
                if(upgrade.isUpgradeAvailable(stack)){
                    if(!printedOneRecipeMessage) {
                        list.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("rfdrills.soul_upgrade.recipe"));
                        printedOneRecipeMessage = true;
                    }

                    list.add(EnumChatFormatting.DARK_AQUA.toString() + StringHelper.writeUpgradeInfo(SoulUpgradeHelper.getUpgradeLevel(stack, upgrade) + 1, upgrade));
                    upgrade.addRecipeDescription(stack, list);
                }
            }

            if (tier.hasModes) {
                if (ConfigHandler.modeShiftClickEIO)
                    list.add(StatCollector.translateToLocal("rfdrills.drill_has_modes.sneak.tooltip"));
                else
                    list.add(StringHelper.writeModeSwitchInfo("rfdrills.drill_has_modes.tooltip", KeyBindingEmpower.instance));
            }
        } else {
            for(Map.Entry<AbstractSoulUpgrade, Byte> entry : SoulUpgradeHelper.getUpgrades(stack).entrySet())
                list.add(EnumChatFormatting.DARK_AQUA.toString() + StringHelper.writeUpgradeInfo(entry.getValue(), entry.getKey()));
            list.add(cofh.lib.util.helpers.StringHelper.shiftForDetails());
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
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + Reference.MOD_ID.toLowerCase() + ":" + Names.SOUL_CRUSHER;
    }

    public int getMode(ItemStack stack){
        if(!tier.hasModes){
            return 0;
        }

        if(stack.stackTagCompound == null){
            return 0;
        }

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

    private int getNumModes(ItemStack stack) {
        return SoulUpgradeHelper.getUpgradeLevel(stack, SoulUpgrades.upgradeBeastMode) + 1;
    }

    /* IEnergyTool */

    @Override
    public ToolTier getTier(ItemStack stack) {
        return ToolTier.FLUX_CRUSHER;
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
        return setEnergy(stack, Math.max(0, getEnergyStored(stack) - energy));
    }

    @Override
    public int getEnergyPerUse(ItemStack stack, Block block, int meta) {
        return getEnergyPerUseWithMode(stack);
    }

    @Override
    public String writeModeInfo(ItemStack stack){
        switch (getMode(stack)) {
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

    @Override
    public EnumModIntegration getModType() {
        return EnumModIntegration.EIO;
    }

    /* IEmpowerableItem */

    @Override
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        if(stack.stackTagCompound == null)
            stack.stackTagCompound = new NBTTagCompound();

        int rechargeRate = tier.rechargeRate;

        switch (SoulUpgradeHelper.getUpgradeLevel(stack, SoulUpgrades.upgradeEmpowered)){
            case 1: rechargeRate = 750; break;
            case 2: rechargeRate = 1500; break;
            case 3: rechargeRate = 5000; break;
        }

        int energy = getEnergyStored(stack);
        int energyReceived = Math.min(getMaxEnergyStored(stack) - energy, Math.min(rechargeRate, maxReceive));

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
        switch (SoulUpgradeHelper.getUpgradeLevel(stack, SoulUpgrades.upgradeEmpowered)){
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
    public boolean isEmpowered(ItemStack stack) {
        return getMode(stack) == getNumModes(stack);
    }

    @Override
    public boolean setEmpoweredState(ItemStack stack, boolean b) {
        if(!tier.hasModes) return false;

        if(getMode(stack) == getNumModes(stack))
            setMode(stack, 0);
        else
            setMode(stack, getMode(stack) + 1);

        return true;
    }

    @Override
    public void onStateChange(EntityPlayer player, ItemStack stack) {
        if(ConfigHandler.modeSoundEIO) {
            if (getMode(stack) == 0)
                player.worldObj.playSoundAtEntity(player, "random.orb", 0.2F, 0.6F);
            else
                player.worldObj.playSoundAtEntity(player, "ambient.weather.thunder", 0.4F, 1.0F);
        }
        player.addChatComponentMessage(new ChatComponentText(writeModeInfo(stack)));
    }
}
