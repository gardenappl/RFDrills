package goldenapple.omnidrills.item;

import cofh.api.energy.IEnergyContainerItem;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import goldenapple.omnidrills.reference.Names;
import goldenapple.omnidrills.reference.Reference;
import goldenapple.omnidrills.util.LogHelper;
import goldenapple.omnidrills.util.MiscUtil;
import goldenapple.omnidrills.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Set;

public class ItemDrill extends ItemTool implements IEnergyContainerItem {
    private static Set<Material> effectiveMaterials = Sets.newHashSet();
    private final String name;
    private final EnumRarity rarity;
    private final int maxEnergy;
    private final int maxEnergyReceive;
    private final int energyPerBlock;
    private final boolean canBreak;

    static {
        effectiveMaterials.add(Material.anvil);
        effectiveMaterials.add(Material.clay);
        effectiveMaterials.add(Material.craftedSnow);
        effectiveMaterials.add(Material.glass);
        effectiveMaterials.add(Material.dragonEgg);
        effectiveMaterials.add(Material.grass);
        effectiveMaterials.add(Material.ground);
        effectiveMaterials.add(Material.ice);
        effectiveMaterials.add(Material.snow);
        effectiveMaterials.add(Material.iron);
        effectiveMaterials.add(Material.packedIce);
        effectiveMaterials.add(Material.piston);
        effectiveMaterials.add(Material.rock);
        effectiveMaterials.add(Material.sand);
    }

    public ItemDrill(String name, ToolMaterial material, int maxEnergy, int maxEnergyReceive, int energyPerBlock, EnumRarity rarity, boolean canBreak){
        super(1.0F, material, effectiveMaterials);
        this.name = name;
        this.maxEnergy = maxEnergy;
        this.maxEnergyReceive = maxEnergyReceive;
        this.rarity = rarity;
        this.energyPerBlock = energyPerBlock;
        this.canBreak = canBreak;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe", "shovel");
    }

    @Override
    public float func_150893_a(ItemStack itemStack, Block block) { //should be called "getEfficiencyOnBlock" or something I dunno
        if(getEnergyStored(itemStack) != 0) {
            return effectiveMaterials.contains(block.getMaterial()) ? this.efficiencyOnProperMaterial : 1.0F;
        }else return 1.0F;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(block.getBlockHardness(world, x, y, z) != 0){
            drainEnergy(itemStack, energyPerBlock);
            if(canBreak && getEnergyStored(itemStack) == 0){
                entity.renderBrokenItemStack(itemStack);
                itemStack.stackSize--;
            }
        }
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entity1, EntityLivingBase entity2) {
        drainEnergy(itemStack, energyPerBlock * 3);
        if(canBreak && getEnergyStored(itemStack) == 0){
            entity1.renderBrokenItemStack(itemStack);
            itemStack.stackSize--;
        }
        return true;
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
    public void getSubItems(Item item, CreativeTabs creeativTab, List list) {
        list.add(setEnergy(new ItemStack(item, 1, 0), 0));
        list.add(setEnergy(new ItemStack(item, 1, 0), maxEnergy));
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return rarity;
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1.0F - (double)this.getEnergyStored(itemStack) / (double)maxEnergy;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean what) {
        if(MiscUtil.isShiftPressed()) {
            list.add(StringHelper.writeEnergyInfo(getEnergyStored(itemStack), maxEnergy));
            list.add(StringHelper.writeEnergyPerBlockInfo(energyPerBlock));
        }else{
            list.add(StatCollector.translateToLocal("omnidrills.tooltip.press_shift"));
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

    private ItemStack setEnergy(ItemStack itemStack, int energy){
        if(itemStack.stackTagCompound == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        itemStack.stackTagCompound.setInteger("Energy", energy);
        return itemStack;
    }

    private int drainEnergy(ItemStack itemStack, int drain){
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
        int energyReceived = Math.min(maxEnergy - energy, Math.min(this.maxEnergyReceive, maxReceive));

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
