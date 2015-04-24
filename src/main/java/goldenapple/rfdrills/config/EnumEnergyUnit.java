package goldenapple.rfdrills.config;

public enum EnumEnergyUnit { //i don't want energy conversion so this is pretty much useless
    RF(1.0F),
    EU(0.25F), //4 RF = 1 EU
    J(2.5F), //1 RF = 2.5 J
    gJ(1.6F); //1 RF = 1.6 GalactiCraft J

    private float conversionRateFromRF;
    private float conversionRateToRF;

    private EnumEnergyUnit(float concersionRateFromRF){
        this.conversionRateFromRF = concersionRateFromRF;
        this.conversionRateToRF = 1 / concersionRateFromRF;
    }

    public static float convert(float energy, EnumEnergyUnit energyUnitFrom, EnumEnergyUnit energyUnitTo){
        float energyRF = energy * energyUnitFrom.conversionRateToRF;
        return energyRF * energyUnitTo.conversionRateFromRF;
    }
}
