package goldenapple.rfdrills.item.soulupgrade;

import java.util.ArrayList;

public class SoulUpgrades {
    public static ArrayList<AbstractSoulUpgrade> registry = new ArrayList<AbstractSoulUpgrade>();
    public static UpgradeBeastMode upgradeBeastMode = new UpgradeBeastMode();

    static{
        registry.add(upgradeBeastMode);
    }
}
