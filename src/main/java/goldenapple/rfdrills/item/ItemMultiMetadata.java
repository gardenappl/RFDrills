package goldenapple.rfdrills.item;

import goldenapple.rfdrills.RFDrills;
import goldenapple.rfdrills.reference.Reference;
import goldenapple.rfdrills.util.LogHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemMultiMetadata extends Item {
    private String[] names;
    private IIcon[] icons;
    private String defaultName;
    private EnumRarity[] rarities;

    public ItemMultiMetadata(String[] names, String defaultName, EnumRarity[] rarities){
        this.names = names;
        this.icons = new IIcon[names.length];
        this.defaultName = defaultName;
        this.setCreativeTab(RFDrills.OmniDrillsTab);
        this.setHasSubtypes(true);
    }

    public ItemMultiMetadata(String[] names, String defaultName){
        this(names, defaultName, null);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        if(rarities == null){
            return EnumRarity.common;
        }else if(itemStack.getItemDamage() >= rarities.length){
            return EnumRarity.common;
        }else{
            return rarities[itemStack.getItemDamage()];
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        for(int i = 0; i < names.length; i++){
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int getMetadata(int meta) {
        return meta < names.length ? meta : 0;
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return meta < icons.length ? icons[meta] : icons[0];
    }

    @Override
    public void registerIcons(IIconRegister register) {
        for(int i = 0; i < names.length; i++){
            icons[i] = register.registerIcon(Reference.MOD_ID + ":" + names[i]);
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "item." + Reference.MOD_ID + ":" + defaultName;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return itemStack.getItemDamage() < names.length ? "item." + Reference.MOD_ID + ":" + names[itemStack.getItemDamage()] : getUnlocalizedName();
    }
}
