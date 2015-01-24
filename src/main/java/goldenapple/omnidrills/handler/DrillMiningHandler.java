package goldenapple.omnidrills.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.omnidrills.item.ItemDrill;
import goldenapple.omnidrills.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.world.BlockEvent;

public class DrillMiningHandler {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event){
        if(event.getPlayer().getCurrentEquippedItem() != null){
            if(event.getPlayer().getCurrentEquippedItem().getItem() instanceof ItemDrill){
                ItemStack itemStack = event.getPlayer().getCurrentEquippedItem();
                ItemDrill drill = (ItemDrill)itemStack.getItem();

                if(!event.getPlayer().capabilities.isCreativeMode) {
                    event.getPlayer().setCurrentItemOrArmor(0, drill.setEnergy(itemStack, drill.getEnergyStored(itemStack) - drill.getEnergyPerBlock()));

                    if (drill.getEnergyStored(itemStack) <= drill.getEnergyPerBlock()) {
                        itemStack.damageItem(1000000, event.getPlayer());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityHit(LivingAttackEvent event){
        if(event.source.getEntity() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.source.getEntity();
            if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemDrill){
                if(!player.capabilities.isCreativeMode) {
                    ItemStack itemStack = player.getCurrentEquippedItem();
                    ItemDrill drill = (ItemDrill) player.getCurrentEquippedItem().getItem();
                    player.setCurrentItemOrArmor(0, drill.setEnergy(itemStack, drill.getEnergyStored(itemStack) - (drill.getEnergyPerBlock() * 3)));

                    if (drill.getEnergyStored(itemStack) < drill.getEnergyPerBlock() * 3) {
                        itemStack.damageItem(1000000, player);
                    }
                }
            }
        }
    }
}
