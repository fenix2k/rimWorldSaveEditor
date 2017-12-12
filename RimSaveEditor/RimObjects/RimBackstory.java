package RimSaveEditor.RimObjects;

import RimSaveEditor.MultiLineTooltips;

public class RimBackstory {
    public String def = "";
    public String title = "";
    public String titleShort = "";
    public String desc = "";
    public StringBuilder toolTip = new StringBuilder();

    @Override
    public String toString() {
        if(def.length() > 0)
         return title + " (" + def + ")";
        else return "none";
    }

    public void setToolTip() {
        String dsc = desc.replaceFirst("NAME", "Colonist");
        toolTip.append("<html>");
        toolTip.append("<b>Select one of many backstory. Backstory must be unique.</b><hr/>");
        toolTip.append(MultiLineTooltips.splitToolTip(dsc) + "<br/>");
        toolTip.append("</html>");
    }
}
