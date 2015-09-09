package goldenapple.rfdrills.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.List;
import java.util.Set;

public class ItemFluxHoe extends ItemTool implements IEnergyTool {
    public static final Set<Material> effectiveMaterials = Sets.newHashSet(Material.leaves, Material.plants, Material.vine, Material.web);

    private static DrillTier tier = DrillTier.HOE;

    public ItemFluxHoe() {
        super(2.0F, tier.material, Sets.newHashSet());
        this.setCreativeTab(RFDrills.RFDrillsTab);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return block == Blocks.web || block == Blocks.vine;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("sickle");
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block){ //Returns efficiency of mining given block
        return effectiveMaterials.contains(block.getMaterial()) ? efficiencyOnProperMaterial : 1.0F;
    }

    @Override
    public boolean isItemTool(ItemStack stack){
        return true;
    }

    private int getEnergyPerUse(ItemStack itemStack){
        return Math.round(tier.energyPerBlock / (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) + 1)); //Vanilla formula: a 100% / (unbreaking level + 1) chance to not take damage
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
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return Math.max(1.0 - (double) getEnergyStored(itemStack) / (double) tier.maxEnergy, 0);
    }

    @Override
    public void setDamage(ItemStack itemStack, int damage) {
        //do nothing, other methods are responsible for energy consumption
    }

    private void harvestBlock(World world, int x, int y, int z, EntityPlayer player){
        Block block = world.getBlock(x, y, z);
        if (block.getBlockHardness(world, x, y, z) < 0 || block.equals(Blocks.waterlily)) {
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

        if (!world.isRemote && block.equals(Blocks.vine))
            block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 1);

        world.setBlockToAir(x, y, z);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(!(entity instanceof EntityPlayer)) return false;
        if(getEnergyStored(itemStack) == 0) return false;
        EntityPlayer player = (EntityPlayer)entity;

        if(block.getMaterial().equals(Material.leaves)) { //Harvesting leaves in a 3x3x3 area
            for (int a = x - 1; a <= x + 1; a++) {
                for(int b = y - 1; b <= y + 1; b++) {
                    for (int c = z - 1; c <= z + 1; c++) {
                        if (world.blockExists(x, b, z) && effectiveMaterials.contains(world.getBlock(a, b, c).getMaterial())) {
                            if (!(a == x && b == y && c == z)) { //don't harvest the same block twice
                                harvestBlock(world, a, b, c, player);
                            }
                        }
                    }
                }
            }
        }else{ //Harvesting everything else in a 3x1x3 area
            for (int a = x - 1; a <= x + 1; a++) {
                for (int c = z - 1; c <= z + 1; c++) {
                    if (world.blockExists(a, y, c) && effectiveMaterials.contains(world.getBlock(a, y, c).getMaterial())) {
                        if(!(a == x && c == z))
                        harvestBlock(world, a, y, c, player);
                    }
                }
            }
        }

        ToolHelper.drainEnergy(itemStack, player, getEnergyPerUse(itemStack, block, world.getBlockMetadata(x, y, z)));
        return true;
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
        if(getEnergyStored(itemStack) == 0) return false;
        if(!tillBlock(itemStack, world, x, y, z, sideHit, player)) return false; //if the player right-clicks a block of cobble near a block of dirt we won't till the dirt

        for(int a = x - 1; a <= x + 1; a++) {
            for(int c = z - 1; c <= z + 1; c++) { //don't care about y levels with a hoe
                if(!(a == x && c == z)) { //we already tilled the block at x, y, z
                    tillBlock(itemStack, world, a, y, c, sideHit, player);
                }
            }
        }

        ToolHelper.drainEnergy(itemStack, player, getEnergyPerUse(itemStack));
        world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), Blocks.farmland.stepSound.getStepResourcePath(), (Blocks.farmland.stepSound.getVolume() + 1.0F) / 2.0F, Blocks.farmland.stepSound.getPitch() * 0.8F);

        return true;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityAttacked, EntityLivingBase entityAttacker) {
        if(entityAttacker instanceof EntityPlayer)
            ToolHelper.drainEnergy(itemStack, (EntityPlayer) entityAttacker, getEnergyPerUse(itemStack) * 2);

        return true;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean what) {
        try {
            list.add(StringHelper.writeEnergyInfo(getEnergyStored(itemStack), tier.maxEnergy));

            if (MiscUtil.isShiftPressed()) {
                list.add(StringHelper.writeEnergyPerBlockInfo(getEnergyPerUse(itemStack)));
                list.add(StatCollector.translateToLocal("rfdrills.hoe.tooltip"));
                if (tier.canBreak) {
                    list.add(StatCollector.translateToLocal("rfdrills.can_break.tooltip"));
                }
                if (toolMaterial.getEnchantability() > 0) {
                    list.add(StatCollector.translateToLocal("rfdrills.enchantable.tooltip"));
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
    public String getUnlocalizedName(){
        return "item." + Reference.MOD_ID.toLowerCase() + ":" + "flux_hoe";
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack){
        return getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister){
        itemIcon = iconRegister.registerIcon(Reference.MOD_ID.toLowerCase() + ":" + "flux_hoe");
    }

    /* IEnergyTool */

    @Override
    public DrillTier getTier(ItemStack itemStack) {
        return tier;
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
        return setEnergy(itemStack, Math.max(getEnergyStored(itemStack) - energy, 0));
    }

    @Override
    public int getEnergyPerUse(ItemStack itemStack, Block block, int meta) {
        return getEnergyPerUse(itemStack);
    }

    @Override
    public int receiveEnergy(ItemStack itemStack, int maxReceive, boolean simulate) {
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
    public int extractEnergy(ItemStack itemStack, int i, boolean b) {
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
