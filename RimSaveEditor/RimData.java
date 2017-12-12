package RimSaveEditor;

import RimSaveEditor.RimObjects.*;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RimData {
    public String filename = "";
    public String gameVersion = "";
    public String playerColonyName = "";
    public String playerFaction = "";

    public List<RimColonist> colonists = new ArrayList<>();
    public List<RimTrait> traits = new ArrayList<>();
    public List<RimBackstory> backstories = new ArrayList<>();
    public List<RimApparel> apparels = new ArrayList<>();
    public List<RimWeapon> weapons = new ArrayList<>();

    public List<String> clothStuffTypes = Arrays.asList(
            "Cloth", "WoolMegasloth", "WoolMuffalo", "WoolCamel",
            "WoolAlpaca", "Synthread", "DevilstrandCloth", "Hyperweave"
    );
    public List<String> metalStuffTypes = Arrays.asList(
            "Silver", "Gold", "Steel", "Plasteel", "Uranium", "Jade"
    );

    public boolean healAll = false;
    public boolean removeNeeds = false;

    public void load(String filename) {
        this.loadSaveGameInfo(filename);

        this.loadBackstoriesDef();
        this.loadTraitsDef();
        this.loadApparelsDef();
        this.loadWeaponsDef();

        this.loadColonists();
    }

    public void clearColonistData() {
        colonists.clear();
    }

    public boolean loadSaveGameInfo(String filename) {
        try {
            Logger.log(0, "Try load savegame info...");
            this.filename = filename;
            String queryGameversion = "/savegame/meta/gameVersion[1]";
            String queryPlayerFactionAndColony = "/savegame/game/world/worldObjects/worldObjects/li[@Class='FactionBase'][last()]";

            VTDGen vg = new VTDGen();
            if (!vg.parseFile(this.filename, false))
                return false;

            VTDNav vn = vg.getNav();
            AutoPilot ap = new AutoPilot(vn);
            ap.selectXPath(queryGameversion);

            if(ap.evalXPath() != -1) {
                int k = vn.getText();
                if(k != -1) {
                    this.gameVersion = vn.toNormalizedString(k);
                    Logger.log(0, "(gameInfo) GameVersion data loaded");
                }
                else {
                    Logger.log(2, "(gameInfo) GameVersion load failed. Exit..");
                    return false;
                }
            }
            else {
                Logger.log(2, "(gameInfo) GameVersion not found. Wrong file format. Exit..");
                return false;
            }

            ap.selectXPath(queryPlayerFactionAndColony);
            if(ap.evalXPath() != -1) {
                if (vn.toElement(VTDNav.FIRST_CHILD, "faction")) {
                    int k = vn.getText();
                    if(k != -1) {
                        this.playerFaction = vn.toNormalizedString(k);
                        Logger.log(0, "(gameInfo) Faction data loaded");
                    }
                    else {
                        Logger.log(2, "(gameInfo) Faction load failed. Exit..");
                        return false;
                    }
                    vn.toElement(VTDNav.PARENT);
                }
                if (vn.toElement(VTDNav.FIRST_CHILD, "nameInt")) {
                    int k = vn.getText();
                    if(k != -1) {
                        this.playerColonyName = vn.toNormalizedString(k);
                        Logger.log(0, "(gameInfo) PlayerColonyName data loaded");
                    }
                    else {
                        Logger.log(0, "(gameInfo) PlayerColonyName load failed");
                    }
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(0, "(gameInfo) PlayerColonyName not found");
                }
            }
            else {
                Logger.log(2, "(gameInfo) Faction not found. Exit..");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(2, "(gameInfo) Failed to load savegame info. Exit..");
            return false;
        }
        Logger.log(0, "(gameInfo) Savegame info loaded successful");
        return true;
    }

    public boolean loadColonists() {
        try {
            Logger.log(0, "Try to load colonist data...");
            String query = "/savegame/game/maps/li/things/thing[@Class='Pawn'][def='Human']" +
                    "[faction='" + this.playerFaction + "']";

            VTDGen vg = new VTDGen();
            if (!vg.parseFile(this.filename, false))
                return false;

            VTDNav vn = vg.getNav();

            AutoPilot ap = new AutoPilot(vn);
            ap.selectXPath(query);

            this.backstories.add(new RimBackstory());

            while (ap.evalXPath() != -1) {
                RimColonist colonist = new RimColonist();
                if (vn.toElement(VTDNav.FIRST_CHILD, "def")) {
                    int k = vn.getText();
                    if(k == -1) {
                        Logger.log(2, "(load data) Colonist param 'def' is empty. Skip colonist");
                        continue;
                    }
                    colonist.def = vn.toNormalizedString(k);
                    Logger.log(0, "(load data) Colonist param 'def' found");
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist param 'def' not found. Skip colonist");
                    continue;
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "id")) {
                    int k = vn.getText();
                    if(k == -1) {
                        Logger.log(2, "(load data) Colonist param 'id' is empty. Skip colonist");
                        continue;
                    }
                    colonist.id = vn.toNormalizedString(k);
                    Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'id' found");
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist param 'id' not found. Skip colonist");
                    continue;
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "faction")) {
                    int k = vn.getText();
                    if(k == -1) {
                        Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'faction' is empty. Skip colonist");
                        continue;
                    }
                    colonist.faction = vn.toNormalizedString(k);
                    Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'faction' found");
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'faction' not found. Skip colonist");
                    continue;
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "name")) {
                    if(vn.toElement(VTDNav.FIRST_CHILD, "first")) {
                        int k = vn.getText();
                        if(k == -1) continue;
                        colonist.firstName = vn.toNormalizedString(k);
                        Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'first' found");
                        vn.toElement(VTDNav.PARENT);
                    }
                    if(vn.toElement(VTDNav.FIRST_CHILD, "nick")) {
                        int k = vn.getText();
                        if(k == -1) continue;
                        colonist.nickName = vn.toNormalizedString(k);
                        Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'nick' found");
                        vn.toElement(VTDNav.PARENT);
                    }
                    if(vn.toElement(VTDNav.FIRST_CHILD, "last")) {
                        int k = vn.getText();
                        if(k == -1) continue;
                        colonist.lastName = vn.toNormalizedString(k);
                        Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'last' found");
                        vn.toElement(VTDNav.PARENT);
                    }
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'name' not found. Skip colonist");
                    continue;
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "gender")) {
                    int k = vn.getText();
                    if(k != -1) {
                        colonist.setSex(vn.toNormalizedString(k));
                        Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'gender' found");
                    }
                    vn.toElement(VTDNav.PARENT);
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "ageTracker")) {
                    if(vn.toElement(VTDNav.FIRST_CHILD, "ageBiologicalTicks")) {
                        int k = vn.getText();
                        if(k == -1) {
                            Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'ageTracker' is empty. Skip colonist");
                            continue;
                        }
                        colonist.setAgeBiologicalTicks(vn.toNormalizedString(k));
                        Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'ageTracker' found");
                        vn.toElement(VTDNav.PARENT);
                    }
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'ageTracker' not found. Skip colonist");
                    continue;
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "story")) {
                    if (vn.toElement(VTDNav.FIRST_CHILD, "childhood")) {
                        int k = vn.getText();
                        if (k != -1) {
                            colonist.childhood = vn.toNormalizedString(k);
                            Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'childhood' found");
                        }
                        vn.toElement(VTDNav.PARENT);
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "adulthood")) {
                        int k = vn.getText();
                        if (k != -1) {
                            colonist.adulthood = vn.toNormalizedString(k);
                            Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'adulthood' found");
                        }
                        vn.toElement(VTDNav.PARENT);
                    }

                    if (vn.toElement(VTDNav.FIRST_CHILD, "traits")) {
                        VTDNav vn1 = vn.cloneNav();
                        AutoPilot ap1 = new AutoPilot(vn1);
                        ap1.selectXPath("allTraits/li");

                        while (ap1.evalXPath() != -1) {
                            RimTrait trait = new RimTrait();
                            if (vn1.toElement(VTDNav.FIRST_CHILD, "def")) {
                                int m = vn1.getText();
                                if (m == -1) continue;
                                trait.def = vn1.toNormalizedString(m);
                                Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'trait def' found");
                                vn1.toElement(VTDNav.PARENT);
                            }
                            if (vn1.toElement(VTDNav.FIRST_CHILD, "degree")) {
                                int m = vn1.getText();
                                if (m != -1)
                                    trait.degree = Integer.parseInt(vn1.toNormalizedString(m));
                                else trait.degree = 0;
                                vn1.toElement(VTDNav.PARENT);
                            }
                            colonist.traits.add(trait);
                        }
                        vn.toElement(VTDNav.PARENT);
                    }
                    else {
                        Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'traits' not found. Skip colonist");
                        continue;
                    }

                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'story' not found. Skip colonist");
                    continue;
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "skills")) {
                    VTDNav vn1 = vn.cloneNav();
                    AutoPilot ap1 = new AutoPilot(vn1);
                    ap1.selectXPath("skills/li");

                    while (ap1.evalXPath() != -1) {
                        RimSkill skill = new RimSkill();
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "def")) {
                            int m = vn1.getText();
                            if (m == -1) continue;
                            skill.def = vn1.toNormalizedString(m);
                            Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'skill "+skill.def+"' found");
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "level")) {
                            int m = vn1.getText();
                            if (m != -1)
                                skill.level = Integer.parseInt(vn1.toNormalizedString(m));
                            else skill.level = 0;
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "xpSinceLastLevel")) {
                            int m = vn1.getText();
                            if (m != -1)
                                skill.xpSinceLastLevel = Double.parseDouble(vn1.toNormalizedString(m));
                            else skill.xpSinceLastLevel = 2000;
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "passion")) {
                            int m = vn1.getText();
                            if (m != -1)
                                skill.setPassion(vn1.toNormalizedString(m));
                            else skill.setPassion("None");
                            vn1.toElement(VTDNav.PARENT);
                        }
                        colonist.skills.put(skill.def, skill);
                    }
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'skills' not found. Skip colonist");
                    continue;
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "apparel")) {
                    VTDNav vn1 = vn.cloneNav();
                    AutoPilot ap1 = new AutoPilot(vn1);
                    ap1.selectXPath("wornApparel/innerList/li");

                    while (ap1.evalXPath() != -1) {
                        RimApparel apparel = new RimApparel();
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "def")) {
                            int m = vn1.getText();
                            if (m == -1) continue;
                            apparel.def = vn1.toNormalizedString(m);
                            Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'skill "+apparel.def+"' found");
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "id")) {
                            int m = vn1.getText();
                            if (m == -1) continue;
                            apparel.id = vn1.toNormalizedString(m);
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "health")) {
                            int m = vn1.getText();
                            if (m != -1)
                                apparel.health = Integer.parseInt(vn1.toNormalizedString(m));
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "stuff")) {
                            int m = vn1.getText();
                            if (m != -1)
                                apparel.stuff = vn1.toNormalizedString(m);
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "color")) {
                            int m = vn1.getText();
                            if (m != -1)
                                apparel.color = vn1.toNormalizedString(m);
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "colorActive")) {
                            int m = vn1.getText();
                            if (m != -1)
                                apparel.colorActive = vn1.toNormalizedString(m);
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "quality")) {
                            int m = vn1.getText();
                            if (m != -1)
                                apparel.setQuality(vn1.toNormalizedString(m));
                            vn1.toElement(VTDNav.PARENT);
                        }

                        int j = this.getApparelIndexByDef(apparel.def);
                        if(j != -1) apparel.defLink = this.apparels.get(j);
                        else apparel.defLink = apparel;

                        colonist.apparels.add(apparel);
                    }
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'apparel' not found. Skip colonist");
                    continue;
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "equipment")) {
                    VTDNav vn1 = vn.cloneNav();
                    AutoPilot ap1 = new AutoPilot(vn1);
                    ap1.selectXPath("equipment/innerList/li");

                    while (ap1.evalXPath() != -1) {
                        RimWeapon weapon = new RimWeapon();
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "def")) {
                            int m = vn1.getText();
                            if (m == -1) continue;
                            weapon.def = vn1.toNormalizedString(m);
                            Logger.log(0, "(load data) Colonist ("+colonist.id+") param 'skill "+weapon.def+"' found");
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "id")) {
                            int m = vn1.getText();
                            if (m == -1) continue;
                            weapon.id = vn1.toNormalizedString(m);
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "health")) {
                            int m = vn1.getText();
                            if (m != -1)
                                weapon.health = Integer.parseInt(vn1.toNormalizedString(m));
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "stackCount")) {
                            int m = vn1.getText();
                            if (m != -1)
                                weapon.stackCount = Integer.parseInt(vn1.toNormalizedString(m));
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "pos")) {
                            int m = vn1.getText();
                            if (m != -1)
                                weapon.pos = vn1.toNormalizedString(m);
                            vn1.toElement(VTDNav.PARENT);
                        }
                        if(vn1.toElement(VTDNav.FIRST_CHILD, "quality")) {
                            int m = vn1.getText();
                            if (m != -1)
                                weapon.setQuality(vn1.toNormalizedString(m));
                            vn1.toElement(VTDNav.PARENT);
                        }

                        int j = this.getWeaponIndexByDef(weapon.def);
                        if(j != -1) weapon.defLink = this.weapons.get(j);
                        else weapon.defLink = weapon;

                        colonist.weapons.add(weapon);
                    }
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    Logger.log(2, "(load data) Colonist ("+colonist.id+") param 'equipment' not found. Skip colonist");
                    continue;
                }

                this.colonists.add(colonist);
            }
            vn.toElement(VTDNav.PARENT);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(2, "Load data Error");
            return false;
        }

        if(this.colonists.size() > 0)
            Logger.log(0, "Colonist data load successful");
        else Logger.log(0, "Colonist data count is 0");

        return true;
    }

    public boolean saveColonists() {
        try {
            Logger.log(0, "Try to save colonist data...");
            String query = "/savegame/game/maps/li/things/thing[@Class='Pawn'][def='Human']" +
                    "[faction='" + this.playerFaction + "']";

            VTDGen vg = new VTDGen();
            if (!vg.parseFile(this.filename, false))
                return false;

            VTDNav vn = vg.getNav();

            AutoPilot ap = new AutoPilot(vn);
            XMLModifier xm = new XMLModifier(vn);
            ap.selectXPath(query);
            RimColonist colonist = new RimColonist();

            int i = 0;
            while ((i = ap.evalXPath()) != -1) {

                if (vn.toElement(VTDNav.FIRST_CHILD, "id")) {
                    int k = vn.getText();
                    if(k == -1) continue;
                    colonist = null;
                    for(RimColonist col: this.colonists) {
                        String id = vn.toNormalizedString(k);
                        if (id.equals(col.id)) {
                            colonist = col;
                            break;
                        }
                    }
                    if(colonist == null) return false;
                    vn.toElement(VTDNav.PARENT);
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "name")) {
                    if(vn.toElement(VTDNav.FIRST_CHILD, "first")) {
                        int k = vn.getText();
                        if(k != -1)
                            xm.updateToken(k, colonist.firstName);
                        vn.toElement(VTDNav.PARENT);
                    }
                    if(vn.toElement(VTDNav.FIRST_CHILD, "nick")) {
                        int k = vn.getText();
                        if(k != -1)
                            xm.updateToken(k, colonist.nickName);
                        vn.toElement(VTDNav.PARENT);
                    }
                    if(vn.toElement(VTDNav.FIRST_CHILD, "last")) {
                        int k = vn.getText();
                        if(k != -1)
                            xm.updateToken(k, colonist.lastName);
                        vn.toElement(VTDNav.PARENT);
                    }
                    vn.toElement(VTDNav.PARENT);
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "gender")) {
                    int k = vn.getText();
                    if(k != -1)
                        xm.updateToken(k, colonist.getSex());
                    vn.toElement(VTDNav.PARENT);
                }
                else {
                    if (vn.toElement(VTDNav.FIRST_CHILD, "name")) {
                        xm.insertAfterElement("\n<gender>" + colonist.getSex() + "</gender>");
                        vn.toElement(VTDNav.PARENT);
                    }
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "ageTracker")) {
                    if(vn.toElement(VTDNav.FIRST_CHILD, "ageBiologicalTicks")) {
                        int k = vn.getText();
                        if(k != -1)
                            xm.updateToken(k, String.valueOf((int)colonist.ageBiologicalTicks));
                            vn.toElement(VTDNav.PARENT);
                    }
                    if(vn.toElement(VTDNav.FIRST_CHILD, "birthAbsTicks")) {
                        int k = vn.getText();
                        if(k != -1)
                            xm.updateToken(k, String.valueOf((int)colonist.birthAbsTicks));
                            vn.toElement(VTDNav.PARENT);
                    }
                    vn.toElement(VTDNav.PARENT);
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "story")) {
                    String backstory = "";
                    if (vn.toElement(VTDNav.FIRST_CHILD, "childhood")) {
                        int k = vn.getText();
                        if (k != -1)
                            xm.updateToken(k, colonist.childhood);
                        vn.toElement(VTDNav.PARENT);
                    }
                    else {
                        backstory += "\n<childhood>" + colonist.childhood + "</childhood>";
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "adulthood")) {
                        int k = vn.getText();
                        if (k != -1)
                            xm.updateToken(k, colonist.adulthood);
                        vn.toElement(VTDNav.PARENT);
                    }
                    else {
                        backstory += "\n<adulthood>" + colonist.adulthood + "</adulthood>";
                    }
                    if(!backstory.isEmpty())
                        xm.insertAfterHead(backstory);

                    if (vn.toElement(VTDNav.FIRST_CHILD, "traits")) {
                        xm.remove(vn.getContentFragment());
                        String xmlData = "\n<allTraits>";
                        for(RimTrait trait: colonist.traits) {
                            xmlData += "<li>\n";
                            xmlData += "\t<def>" + trait.def + "</def>\n";
                            xmlData += "\t<degree>" + trait.degree + "</degree>\n";
                            xmlData += "</li>\n";
                        }
                        xmlData += "</allTraits>\n";
                        xm.insertAfterHead(xmlData);

                        vn.toElement(VTDNav.PARENT);
                    }

                    vn.toElement(VTDNav.PARENT);
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "skills")) {
                    xm.remove(vn.getContentFragment());
                    String xmlData = "\n<skills>";
                    for(Map.Entry<String, RimSkill> skill: colonist.skills.entrySet()) {
                        xmlData += "<li>\n";
                        xmlData += "\t<def>" + skill.getValue().def + "</def>\n";
                        xmlData += "\t<level>" + skill.getValue().level + "</level>\n";
                        xmlData += "\t<xpSinceLastLevel>" + skill.getValue().xpSinceLastLevel + "</xpSinceLastLevel>\n";
                        xmlData += "\t<passion>" + skill.getValue().passion.name() + "</passion>\n";
                        xmlData += "</li>\n";
                    }
                    xmlData += "</skills>\n";
                    xm.insertAfterHead(xmlData);

                    vn.toElement(VTDNav.PARENT);
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "apparel")) {
                    if (vn.toElement(VTDNav.FIRST_CHILD, "wornApparel")) {
                        xm.remove(vn.getContentFragment());
                        String xmlData = "\n<innerList>";
                        for(RimApparel apparel: colonist.apparels) {
                            xmlData += "<li>\n";
                            xmlData += "\t<def>" + apparel.def + "</def>\n";
                            xmlData += "\t<id>" + apparel.id + "</id>\n";
                            xmlData += "\t<health>" + apparel.health + "</health>\n";
                            xmlData += "\t<stackCount>" + apparel.stackCount + "</stackCount>\n";
                            xmlData += "\t<stuff>" + apparel.stuff + "</stuff>\n";
                            xmlData += "\t<color>" + apparel.color + "</color>\n";
                            xmlData += "\t<colorActive>" + apparel.colorActive + "</colorActive>\n";
                            xmlData += "\t<quality>" + apparel.quality.name() + "</quality>\n";
                            xmlData += "</li>\n";
                        }
                        xmlData += "</innerList>\n";
                        xm.insertAfterHead(xmlData);
                        vn.toElement(VTDNav.PARENT);
                    }
                    vn.toElement(VTDNav.PARENT);
                }

                if (vn.toElement(VTDNav.FIRST_CHILD, "equipment")) {
                    if (vn.toElement(VTDNav.FIRST_CHILD, "equipment")) {
                        xm.remove(vn.getContentFragment());
                        String xmlData = "\n<innerList>";
                        for(RimWeapon weapon: colonist.weapons) {
                            xmlData += "<li>\n";
                            xmlData += "\t<def>" + weapon.def + "</def>\n";
                            xmlData += "\t<id>" + weapon.id + "</id>\n";
                            xmlData += "\t<health>" + weapon.health + "</health>\n";
                            xmlData += "\t<stackCount>" + weapon.stackCount + "</stackCount>\n";
                            xmlData += "\t<pos>" + weapon.pos + "</pos>\n";
                            xmlData += "\t<quality>" + weapon.quality.name() + "</quality>\n";
                            xmlData += "\t<verbTracker>\n" +
                                    "\t\t<verbs>\n" +
                                    "\t\t</verbs>\n" +
                                    "\t</verbTracker>\n";
                            xmlData += "</li>\n";
                        }
                        xmlData += "</innerList>\n";
                        xm.insertAfterHead(xmlData);

                        vn.toElement(VTDNav.PARENT);
                    }
                    vn.toElement(VTDNav.PARENT);
                }

                if(this.removeNeeds) {
                    if (vn.toElement(VTDNav.FIRST_CHILD, "needs")) {
                        xm.remove(vn.getContentFragment());
                        xm.insertAfterHead("<needs></needs>");
                        vn.toElement(VTDNav.PARENT);
                    }
                }

                if(this.healAll) {
                    if (vn.toElement(VTDNav.FIRST_CHILD, "healthTracker")) {
                        if (vn.toElement(VTDNav.FIRST_CHILD, "healthState")) {
                            xm.remove(vn.getElementFragment());
                            vn.toElement(VTDNav.PARENT);
                        }
                        if (vn.toElement(VTDNav.FIRST_CHILD, "hediffSet")) {
                            xm.remove(vn.getContentFragment());
                            xm.insertAfterHead("<hediffs></hediffs>");
                            vn.toElement(VTDNav.PARENT);
                        }
                        vn.toElement(VTDNav.PARENT);
                    }
                }
            }
            vn.toElement(VTDNav.PARENT);

            xm.output(filename);

            this.healAll = false;
            this.removeNeeds = false;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(2, "Write file Error");
            return false;
        }
        Logger.log(0, "Write successfully");
        return true;
    }

    public boolean loadBackstoriesDef() {
        try {
            Logger.log(0, "Try to load backstory defs...");
            List<String> files = new ArrayList<>();
            files.add("defs/Backstories.xml");

            String query = "/BackstoryTranslations/*";

            VTDGen vg = new VTDGen();
            for(String file: files) {
                if (!vg.parseFile(file, false))
                    return false;

                VTDNav vn = vg.getNav();
                AutoPilot ap = new AutoPilot(vn);
                ap.selectXPath(query);
                int i = 0;
                while ((i=ap.evalXPath()) != -1) {
                    RimBackstory backstory = new RimBackstory();
                    backstory.def = vn.toNormalizedString(i);

                    if (vn.toElement(VTDNav.FIRST_CHILD, "title")) {
                        int k = vn.getText();
                        if (k != -1)
                            backstory.title = vn.toNormalizedString(k);
                        else return false;
                        vn.toElement(VTDNav.PARENT);
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "titleShort")) {
                        int k = vn.getText();
                        if (k != -1)
                            backstory.titleShort = vn.toNormalizedString(k);
                        vn.toElement(VTDNav.PARENT);
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "desc")) {
                        int k = vn.getText();
                        if (k != -1)
                            backstory.desc = vn.toNormalizedString(k);
                        vn.toElement(VTDNav.PARENT);
                    }
                    backstory.setToolTip();
                    this.backstories.add(backstory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(2, "Failed to load backstory defs...");
            return false;
        }

        if(this.backstories.size() > 0)
            Logger.log(0, "Backstory defs load successful");
        else Logger.log(1, "Backstory defs count is 0");

        return true;
    }

    public boolean loadTraitsDef() {
        try {
            Logger.log(0, "Try to load trait defs...");
            List<String> files = new ArrayList<>();
            files.add("defs/Traits_Spectrum.xml");
            files.add("defs/Traits_Singular.xml");

            String query = "/Defs/TraitDef";
            List<RimTrait> traitsDef = new ArrayList<>();

            VTDGen vg = new VTDGen();
            for(String file: files) {
                if (!vg.parseFile(file, false))
                    return false;

                VTDNav vn = vg.getNav();
                AutoPilot ap = new AutoPilot(vn);
                ap.selectXPath(query);

                while (ap.evalXPath() != -1) {
                    RimTrait trait = new RimTrait();
                    if (vn.toElement(VTDNav.FIRST_CHILD, "defName")) {
                        int k = vn.getText();
                        if (k != -1)
                            trait.def = vn.toNormalizedString(k);
                        else continue;
                        vn.toElement(VTDNav.PARENT);
                    }

                    if (vn.toElement(VTDNav.FIRST_CHILD, "disabledWorkTags")) {
                        int k = vn.getText();
                        if (k != -1)
                            trait.disabledWorkTags.add(vn.toNormalizedString(k));
                        else continue;
                        vn.toElement(VTDNav.PARENT);
                    }

                    if (vn.toElement(VTDNav.FIRST_CHILD, "conflictingTraits")) {
                        VTDNav vn1 = vn.cloneNav();
                        AutoPilot ap1 = new AutoPilot(vn1);
                        ap1.selectXPath("*");
                        while (ap1.evalXPath() != -1) {
                            int k = vn1.getText();
                            if (k != -1)
                                trait.conflictingTraits.add(vn1.toNormalizedString(k));
                        }
                        vn.toElement(VTDNav.PARENT);
                    }

                    if (vn.toElement(VTDNav.FIRST_CHILD, "requiredWorkTags")) {
                        VTDNav vn1 = vn.cloneNav();
                        AutoPilot ap1 = new AutoPilot(vn1);
                        ap1.selectXPath("*");
                        while (ap1.evalXPath() != -1) {
                            int k = vn1.getText();
                            if (k != -1)
                                trait.requiredWorkTags.add(vn1.toNormalizedString(k));
                        }
                        vn.toElement(VTDNav.PARENT);
                    }

                    if (vn.toElement(VTDNav.FIRST_CHILD, "degreeDatas")) {
                        VTDNav vn1 = vn.cloneNav();
                        AutoPilot ap1 = new AutoPilot(vn1);
                        ap1.selectXPath("li");

                        while (ap1.evalXPath() != -1) {
                            RimTraitDegreeData degreeData = new RimTraitDegreeData();
                            if (vn1.toElement(VTDNav.FIRST_CHILD, "label")) {
                                int m = vn1.getText();
                                if (m == -1) continue;
                                    degreeData.label = vn1.toNormalizedString(m);
                                vn1.toElement(VTDNav.PARENT);
                            }
                            if (vn1.toElement(VTDNav.FIRST_CHILD, "description")) {
                                int m = vn1.getText();
                                if (m != -1)
                                    degreeData.description = vn1.toNormalizedString(m);
                                vn1.toElement(VTDNav.PARENT);
                            }
                            if (vn1.toElement(VTDNav.FIRST_CHILD, "degree")) {
                                int m = vn1.getText();
                                if (m != -1)
                                    degreeData.degree = Integer.parseInt(vn1.toNormalizedString(m));
                                vn1.toElement(VTDNav.PARENT);
                            }
                            if (vn1.toElement(VTDNav.FIRST_CHILD, "statOffsets")) {
                                VTDNav vn2 = vn1.cloneNav();
                                AutoPilot ap2 = new AutoPilot(vn2);
                                ap2.selectXPath("*");
                                while (ap2.evalXPath() != -1) {
                                    int m = vn2.getText();
                                    if (m != -1)
                                        degreeData.statOffsets.put(vn2.toNormalizedString(m - 1), vn2.toNormalizedString(m));
                                }
                                vn1.toElement(VTDNav.PARENT);
                            }
                            if (vn1.toElement(VTDNav.FIRST_CHILD, "skillGains")) {
                                VTDNav vn2 = vn1.cloneNav();
                                AutoPilot ap2 = new AutoPilot(vn2);
                                ap2.selectXPath("li");
                                while (ap2.evalXPath() != -1) {
                                    String key = "";
                                    if(vn2.toElement(VTDNav.FIRST_CHILD, "key")) {
                                        int m = vn2.getText();
                                        if (m != -1)
                                            key = vn2.toNormalizedString(m);
                                        else continue;
                                        vn2.toElement(VTDNav.PARENT);
                                    }
                                    if(vn2.toElement(VTDNav.FIRST_CHILD, "value")) {
                                        int m = vn2.getText();
                                        if (m != -1)
                                            degreeData.skillGains.put(key, Integer.parseInt(vn2.toNormalizedString(m)));
                                        vn2.toElement(VTDNav.PARENT);
                                    }
                                }
                                vn1.toElement(VTDNav.PARENT);
                            }
                            trait.degreeDatas.add(degreeData);
                        }
                        vn.toElement(VTDNav.PARENT);
                    }

                    traitsDef.add(trait);
                }
            }
            this.traitConversion(traitsDef);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(2, "Failed to load trait defs");
            return false;
        }

        if(this.traits.size() > 0)
            Logger.log(0, "Trait defs load successful");
        else Logger.log(1, "Trait defs count is 0");

        return true;
    }

    public void traitConversion(List<RimTrait> traitsDef) {
        traits.add(new RimTrait());

        for(RimTrait traitDef: traitsDef) {
            for(RimTraitDegreeData degree: traitDef.degreeDatas) {
                RimTrait trait = new RimTrait();

                trait.label = degree.label;
                trait.def = traitDef.def;
                trait.degree = degree.degree;
                trait.description = degree.description;
                trait.conflictingTraits = traitDef.conflictingTraits;
                trait.disabledWorkTags = traitDef.disabledWorkTags;
                trait.requiredWorkTags = traitDef.requiredWorkTags;
                trait.skillGains = degree.skillGains;
                trait.statOffsets = degree.statOffsets;
                trait.setToolTip();
                //trait.degreeDatas = traitDef.degreeDatas;

                this.traits.add(trait);
            }
        }
    }

    public boolean loadApparelsDef() {
        try {
            Logger.log(0, "Try to load apparel defs...");
            List<String> files = new ArrayList<>();
            files.add("defs/Apparel_Various.xml");
            files.add("defs/Apparel_Hats.xml");
            files.add("defs/Apparel_Belts.xml");

            String query = "/Defs/ThingDef";

            VTDGen vg = new VTDGen();
            for(String file: files) {
                if (!vg.parseFile(file, false))
                    return false;

                VTDNav vn = vg.getNav();
                AutoPilot ap = new AutoPilot(vn);
                ap.selectXPath(query);

                while (ap.evalXPath() != -1) {
                    int e = vn.getAttrVal("Abstract");
                    if(e != -1) {
                        if(vn.toNormalizedString(e).equals("True")) continue;
                    }

                    RimApparel apparel = new RimApparel();

                    if (vn.toElement(VTDNav.FIRST_CHILD, "defName")) {
                        int k = vn.getText();
                        if (k != -1)
                            apparel.def = vn.toNormalizedString(k);
                        else return false;
                        vn.toElement(VTDNav.PARENT);
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "label")) {
                        int k = vn.getText();
                        if (k != -1)
                            apparel.label = vn.toNormalizedString(k);
                        else return false;
                        vn.toElement(VTDNav.PARENT);
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "description")) {
                        int k = vn.getText();
                        if (k != -1)
                            apparel.description = vn.toNormalizedString(k);
                        else return false;
                        vn.toElement(VTDNav.PARENT);
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "apparel")) {
                        VTDNav vn1 = vn.cloneNav();
                        AutoPilot ap1 = new AutoPilot(vn1);
                        ap1.selectXPath("bodyPartGroups/li");
                        while (ap1.evalXPath() != -1) {
                            int k = vn1.getText();
                            if (k != -1)
                                apparel.addBodyPart(vn1.toNormalizedString(k).toUpperCase());
                        }

                        vn1 = vn.cloneNav();
                        ap1 = new AutoPilot(vn1);
                        ap1.selectXPath("layers/li");
                        while (ap1.evalXPath() != -1) {
                            int k = vn1.getText();
                            if (k != -1)
                                apparel.addLayer(vn1.toNormalizedString(k).toUpperCase());
                        }

                        vn.toElement(VTDNav.PARENT);
                    }
                    this.apparels.add(apparel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(2, "Failed to load apparels defs");
            return false;
        }

        if(this.apparels.size() > 0)
            Logger.log(0, "Apparels defs load successful");
        else Logger.log(1, "Apparels defs count is 0");

        return true;
    }

    public boolean loadWeaponsDef() {
        try {
            Logger.log(0, "Try to load equipment defs...");
            List<String> files = new ArrayList<>();
            files.add("defs/Weapons_Melee.xml");
            files.add("defs/Weapons_RangedNeolithic.xml");
            files.add("defs/Weapons_Guns.xml");
            files.add("defs/Weapons_Grenades.xml");

            String query = "/Defs/ThingDef";

            VTDGen vg = new VTDGen();
            for(String file: files) {
                if (!vg.parseFile(file, false))
                    return false;

                VTDNav vn = vg.getNav();
                AutoPilot ap = new AutoPilot(vn);
                ap.selectXPath(query);

                while (ap.evalXPath() != -1) {
                    int e = vn.getAttrVal("Abstract");
                    if(e != -1) {
                        if(vn.toNormalizedString(e).equals("True")) continue;
                    }

                    RimWeapon weapon = new RimWeapon();

                    if (vn.toElement(VTDNav.FIRST_CHILD, "defName")) {
                        int k = vn.getText();
                        if (k != -1)
                            weapon.def = vn.toNormalizedString(k);
                        else return false;
                        vn.toElement(VTDNav.PARENT);
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "label")) {
                        int k = vn.getText();
                        if (k != -1)
                            weapon.label = vn.toNormalizedString(k);
                        else return false;
                        vn.toElement(VTDNav.PARENT);
                    }
                    if (vn.toElement(VTDNav.FIRST_CHILD, "description")) {
                        int k = vn.getText();
                        if (k != -1)
                            weapon.desc = vn.toNormalizedString(k);
                        else return false;
                        vn.toElement(VTDNav.PARENT);
                    }

                    this.weapons.add(weapon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(2, "Failed to load equipment defs");
            return false;
        }

        if(this.apparels.size() > 0)
            Logger.log(0, "Equipment defs load successful");
        else Logger.log(1, "Equipment defs count is 0");

        return true;
    }

    public int getBackstoryIndexByDef(String def) {
        int i = 0;
        for(RimBackstory obj: this.backstories) {
            if(obj.def.equals(def))
                return i;
            i++;
        }
        return -1;
    }

    public int getTraitIndexByDef(RimTrait trait) {
        int i = 0;
        for(RimTrait obj: this.traits) {
            if(obj.def.equals(trait.def) && obj.degree == trait.degree)
                return i;
            i++;
        }
        return -1;
    }

    public RimTrait getTraitItemByDef(String def) {
        int i = 0;
        for(RimTrait obj: this.traits) {
            if(obj.def.equals(def))
                return obj;
            i++;
        }
        return null;
    }

    public int getApparelIndexByDef(String def) {
        int i = 0;
        for(RimApparel obj: this.apparels) {
            if(obj.def.equals(def))
                return i;
            i++;
        }
        return -1;
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

    public int getWeaponIndexByDef(String def) {
        int i = 0;
        for(RimWeapon obj: this.weapons) {
            if(obj.def.equals(def))
                return i;
            i++;
        }
        return -1;
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
