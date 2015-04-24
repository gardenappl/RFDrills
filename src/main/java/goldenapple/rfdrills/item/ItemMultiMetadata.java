package goldenapple.rfdrills.item;

import goldenapple.rfdrills.RFDrillsCreativeTab;
import goldenapple.rfdrills.reference.Reference;
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
        this.setCreativeTab(RFDrillsCreativeTab.OmniDrillsTab);
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
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        for(int i = 0; i < names.length; i++){
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int getMetadata(int meta) {
        return meta > names.length ? 0 : meta;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return stack.getItemDamage() < icons.length ? icons[stack.getItemDamage()] : icons[0];
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
