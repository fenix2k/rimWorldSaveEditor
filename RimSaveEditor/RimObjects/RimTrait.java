package RimSaveEditor.RimObjects;

import RimSaveEditor.MultiLineTooltips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RimTrait {
    public String label = "";
    public String def = "";

    public int degree = 0;
    public String description = "";
    public StringBuilder toolTip = new StringBuilder();

    public Map<String, String> statOffsets = new HashMap<>();
    public Map<String, Integer> skillGains = new HashMap<>();
    public List<String> conflictingTraits = new ArrayList<>();
    public List<String> requiredWorkTags = new ArrayList<>();
    public List<String> disabledWorkTags = new ArrayList<>();

    public List<RimTraitDegreeData> degreeDatas = new ArrayList<>();

    @Override
    public String toString() {
        if(!def.isEmpty())
            return def + " [" + degree + "] (" + label + ")";
        else return "none";
    }

    public void setToolTip() {
        String desc = description.replaceFirst("NAME", "Colonist");
        String tab = "&nbsp;&nbsp;";

        toolTip.append("<html>");
        toolTip.append("<b>Select one of many traits. Trait must be unique.</b><hr/>");
        toolTip.append(MultiLineTooltips.splitToolTip(desc) + "<br/>");
        if(skillGains.size() > 0) {
            toolTip.append("<br/><b>Skill Gains: </b><br/>");
            for(Map.Entry<String, Integer> pair: skillGains.entrySet()) {
                toolTip.append(tab + pair.getKey() + ": " + pair.getValue() + "<br/>");
            }
        }
        if(statOffsets.size() > 0) {
            toolTip.append("<b>Stat Offsets: </b><br/>");
            for(Map.Entry<String, String> pair: statOffsets.entrySet()) {
                toolTip.append(tab + pair.getKey() + ": " + (int)(Double.parseDouble(pair.getValue())*100) + "%<br/>");
            }
        }
        if(conflictingTraits.size() > 0) {
            toolTip.append("<b>Conflicting Traits: </b><br/>");
            for(String str: conflictingTraits) {
                toolTip.append(tab + str + "<br/>");
            }
        }
        if(requiredWorkTags.size() > 0) {
            toolTip.append("<b>Required Work Tags: </b><br/>");
            for(String str: requiredWorkTags) {
                toolTip.append(tab + str + "<br/>");
            }
        }
        if(disabledWorkTags.size() > 0) {
            toolTip.append("<b>Disabled Work Tags: </b><br/>");
            for(String str: disabledWorkTags) {
                toolTip.append(tab + str + "<br/>");
            }
        }
        toolTip.append("</html>");
    }
}
