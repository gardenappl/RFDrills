package goldenapple.rfdrills.item.soulupgrade;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import goldenapple.rfdrills.init.ModItems;
import net.minecraftforge.event.AnvilUpdateEvent;

public class SoulUpgradeRecipeHandler {
    @SubscribeEvent
    public void onAnvilRepair(AnvilUpdateEvent event){
        if(event.left.getItem() == ModItems.soulCrusher){
            for(AbstractSoulUpgrade upgrade : SoulUpgrades.registry){
                int currentLevel = SoulUpgradeHelper.getUpgradeLevel(event.left, upgrade);

                if(upgrade.isUpgradeAvailable(event.left) && upgrade.isRecipeValid(currentLevel + 1, event.right)){
                    event.output = SoulUpgradeHelper.applyUpgrade(event.left, upgrade, (byte)(currentLevel + 1));
                    event.materialCost = upgrade.getItemCost(currentLevel + 1);
                    event.cost = upgrade.getLevelCost(currentLevel + 1);
                }
            }
        }
    }
}
