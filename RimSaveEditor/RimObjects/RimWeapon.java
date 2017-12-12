package RimSaveEditor.RimObjects;

public class RimWeapon {
    public String def = "";
    public String id = "";
    public String label = "";
    public String desc = "";
    public int health = 500;
    public int stackCount = 1;
    public String pos = "";
    public RimItemQuality quality = RimItemQuality.Normal;
    public RimWeapon defLink = null;

    @Override
    public String toString() {
        if(def.length() > 0)
            return def;
        return "none";
    }

    public RimWeapon() {
    }

    public void setQuality(String quality) {
        if(!quality.isEmpty()) this.quality = RimItemQuality.valueOf(quality);
        else this.quality = RimItemQuality.Normal;
    }

    public String getQuality() {
        return this.quality.name();
    }
}
