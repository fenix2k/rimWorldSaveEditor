package RimSaveEditor.RimObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RimColonist {
    public String id = "";
    public String def = "";

    public String firstName = "";
    public String lastName = "";
    public String nickName = "";
    public RimGender sex = RimGender.Male;

    public double ageBiologicalTicks = 0;
    public double birthAbsTicks = 0;

    public String faction = "";
    public String childhood = "";
    public String adulthood = "";

    public List<RimTrait> traits = new ArrayList<>();
    public List<RimWeapon> weapons = new ArrayList<>();
    public List<RimApparel> apparels = new ArrayList<>();
    public Map<String, RimSkill> skills = new HashMap<>();

    public RimColonist() {
    }

    @Override
    public String toString() {
        return this.nickName + " (" + this.id + ")";
    }

    public void setSex(String sex) {
        if(!sex.isEmpty()) this.sex = RimGender.valueOf(sex);
        else this.sex = RimGender.Male;
    }

    public String getSex() {
        return this.sex.name();
    }

    public void setAgeBiologicalTicks(String ageBiologicalTicks) {
        this.ageBiologicalTicks = Double.parseDouble(ageBiologicalTicks);
        this.birthAbsTicks = (this.ageBiologicalTicks - 1703676)*-1;
    }

    public void setAge(String age)  {
        double ageD = Double.parseDouble(age);
        this.ageBiologicalTicks = (double)(ageD*3600000.00);
        this.birthAbsTicks = (this.ageBiologicalTicks - 1703676)*-1;
    }

    public double getAge() {
        return this.ageBiologicalTicks/3600000.00;
    }

    public RimApparel getApparelItemByDef(String def) {
        int i = 0;
        for(RimApparel obj: this.apparels) {
            if(obj.def.equals(def))
                return obj;
            i++;
        }
        return null;
    }

    public RimWeapon getWeaponItemByDef(String def) {
        int i = 0;
        for(RimWeapon obj: this.weapons) {
            if(obj.def.equals(def))
                return obj;
            i++;
        }
        return null;
    }
}
