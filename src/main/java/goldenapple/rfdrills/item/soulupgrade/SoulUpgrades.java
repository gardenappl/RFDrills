package goldenapple.rfdrills.item.soulupgrade;

import java.util.ArrayList;

public class SoulUpgrades {
    public static ArrayList<AbstractSoulUpgrade> registry = new ArrayList<AbstractSoulUpgrade>();
    public static UpgradeBeastMode upgradeBeastMode = new UpgradeBeastMode();
    public static UpgradeEmpowered upgradeEmpowered = new UpgradeEmpowered();
    public static UpgradeFork upgradeFork= new UpgradeFork();

    static{
        registry.add(upgradeBeastMode);
        registry.add(upgradeEmpowered);
        registry.add(upgradeFork);
    }
}
