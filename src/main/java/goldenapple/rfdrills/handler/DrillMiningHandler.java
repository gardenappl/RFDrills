package goldenapple.rfdrills.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.rfdrills.item.ItemDrill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.BlockEvent;

public class DrillMiningHandler {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event){
        /* if(event.getPlayer().getCurrentEquippedItem() != null){
            if(event.getPlayer().getCurrentEquippedItem().getItem() instanceof ItemDrill){
                ItemStack itemStack = event.getPlayer().getCurrentEquippedItem();
                ItemDrill drill = (ItemDrill)itemStack.getItem();

                if(!event.getPlayer().capabilities.isCreativeMode) {
                    event.getPlayer().setCurrentItemOrArmor(0, drill.drainEnergy(itemStack, drill.getEnergyPerBlock()));

                    if (drill.getEnergyStored(itemStack) == 0) {
                        itemStack.damageItem(1000000, event.getPlayer());
                    }
                }
            }
        } */
    }

    @SubscribeEvent
    public void onEntityHit(LivingAttackEvent event){
        if(event.source.getEntity() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.source.getEntity();
            if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemDrill){
                if(!player.capabilities.isCreativeMode) {
                    ItemStack itemStack = player.getCurrentEquippedItem();
                    ItemDrill drill = (ItemDrill) player.getCurrentEquippedItem().getItem();
                    player.setCurrentItemOrArmor(0, drill.drainEnergy(itemStack, drill.getEnergyPerBlock()));

                    if (drill.getEnergyStored(itemStack) < drill.getEnergyPerBlock() * 2) {
                        itemStack.damageItem(1000000, player);
                    }
                }
            }
        }
    }
}
