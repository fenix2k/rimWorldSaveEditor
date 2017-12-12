package RimSaveEditor.RimObjects;

public class RimSkill {
    public String def = "";
    public int level = 0;
    public double xpSinceLastLevel = 2000;
    public RimSkillPassion passion = RimSkillPassion.None;

    @Override
    public String toString() {
        return def;
    }

    public void setPassion(String passion) {
        if(!passion.isEmpty()) this.passion = RimSkillPassion.valueOf(passion);
        else this.passion = RimSkillPassion.None;
    }

    public String getPassion() {
        return this.passion.name();
    }
}
