package goldenapple.rfdrills.item.soulupgrade;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.rfdrills.init.ModItems;
import net.minecraftforge.event.AnvilUpdateEvent;

public class SoulUpgradeRecipeHandler {
    @SubscribeEvent
    public void onAnvilRepair(AnvilUpdateEvent event){
        if(event.left.getItem() == ModItems.soulCrusher){
            for(AbstractSoulUpgrade upgrade : SoulUpgrades.registry){
                if(upgrade.isUpgradeAvailable(event.left) && upgrade.isRecipeValid(event.right)){
                    event.output = SoulUpgradeHelper.applyUpgrade(event.left, upgrade, (byte)(SoulUpgradeHelper.getUpgradeLevel(event.left, upgrade) + 1));
                    event.materialCost = 1;
                    event.cost = upgrade.getLevelCost(event.left);
                }
            }
        }
    }
}
